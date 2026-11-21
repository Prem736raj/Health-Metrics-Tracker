package com.health.calculator.bmi.tracker.data.export

import com.health.calculator.bmi.tracker.data.calculator.formatSleepDuration
import com.health.calculator.bmi.tracker.data.local.AppDatabase
import com.health.calculator.bmi.tracker.data.repository.FoodLogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.time.LocalDate

internal class HealthReportRepository(
    private val database: AppDatabase,
    private val food: FoodLogRepository
) {
    suspend fun load(window: ReportWindow): HealthReport = withContext(Dispatchers.IO) {
        val start = window.start
        val end = window.end
        val records = mutableMapOf<ReportTool, List<ReportRecord>>()
        val number = NumberFormat.getNumberInstance().apply { maximumFractionDigits = 1 }
        val history = database.historyDao().getReportEntries(start, end).groupBy { it.calculatorKey }
        ReportTool.entries.filter { it.fromCalculationHistory }.forEach { tool ->
            records[tool] = history[tool.calculator?.key].orEmpty().map { entry ->
                val result = listOf(entry.resultValue, entry.resultLabel)
                    .filter { it.isNotBlank() }.joinToString(" ")
                val sleep = entry.resultValue.toLongOrNull()?.takeIf { tool == ReportTool.SLEEP && it >= 0 }
                ReportRecord(entry.timestamp, sleep?.let(::formatSleepDuration) ?: result, entry.category.orEmpty())
            }
        }

        records[ReportTool.BLOOD_PRESSURE] = database.bloodPressureDao().getReportReadings(start, end).map {
            val details = buildList {
                it.pulse?.let { pulse -> add("Pulse $pulse bpm") }
                if (it.isAveragedResult) add("Average of ${it.readingsInAverage} readings")
            }
            ReportRecord(it.measurementTimestamp, "${it.systolic}/${it.diastolic} mmHg", details.joinToString(" · "))
        }
        records[ReportTool.WEIGHT] = database.weightDao().getWeightsInRange(start, end).first().map {
            ReportRecord(it.dateMillis, "${number.format(it.weightKg)} kg", it.source.displayName)
        }
        records[ReportTool.WATER_ESTIMATE] = database.waterIntakeDao().getReportCalculations(start, end).map {
            ReportRecord(it.timestamp, "${number.format(it.recommendedIntakeMl)} mL/day", "Intake estimate")
        }
        records[ReportTool.WATER_LOG] = database.waterIntakeDao().getWaterLogsBetween(start, end).first()
            .groupBy { window.day(it.timestamp) }
            .map { (day, logs) ->
                val amount = logs.sumOf { it.amountMl.toLong() }
                ReportRecord(day.atStartOfDay(window.zone).toInstant().toEpochMilli(), "${number.format(amount)} mL", "Logged daily total")
            }
        records[ReportTool.FOOD_LOG] = food.getSavedLogsForReport().filter { it.entries.isNotEmpty() }
            .mapNotNull { log ->
                val day = runCatching { LocalDate.parse(log.date) }.getOrNull() ?: return@mapNotNull null
                ReportRecord(day.atStartOfDay(window.zone).toInstant().toEpochMilli(),
                    "${number.format(log.totalCalories)} kcal", "Logged daily total")
            }
        records[ReportTool.STEPS] = database.stepHistoryDao().getInRange(start, end)
            .filter { it.steps > 0 }
            .map { ReportRecord(it.dayStartMillis, "${number.format(it.steps)} steps", "Cached daily total") }

        val medicalRecords = database.medicalRecordDao().inRange(start, end).groupBy { it.kind }
        ReportTool.entries.forEach { tool ->
            val kind = tool.medicalRecordKind ?: return@forEach
            records[tool] = medicalRecords[kind.name].orEmpty().map { record ->
                ReportRecord(record.timestamp, kind.reportResult(record), kind.reportContext(record))
            }
        }
        HealthReport(window, records)
    }
}
