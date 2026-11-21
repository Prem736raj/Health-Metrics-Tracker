package com.health.calculator.bmi.tracker.data.export

import com.health.calculator.bmi.tracker.data.model.CalculatorType
import com.health.calculator.bmi.tracker.data.records.MedicalRecordKind
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

internal enum class ReportTool(
    val title: String,
    val calculator: CalculatorType? = null,
    val medicalRecordKind: MedicalRecordKind? = null,
    val requiresOptIn: Boolean = false
) {
    BLOOD_PRESSURE("Blood pressure", CalculatorType.BLOOD_PRESSURE),
    WEIGHT("Weight"),
    WATER_LOG("Water log"),
    FOOD_LOG("Food log"),
    STEPS("Steps from Health Connect"),
    SLEEP("Sleep duration", CalculatorType.SLEEP),
    GLUCOSE("Blood glucose", medicalRecordKind = MedicalRecordKind.GLUCOSE),
    TEMPERATURE("Temperature", medicalRecordKind = MedicalRecordKind.TEMPERATURE),
    OXYGEN("Oxygen saturation", medicalRecordKind = MedicalRecordKind.OXYGEN),
    BMI("Body mass index", CalculatorType.BMI),
    BMR("Resting energy (BMR)", CalculatorType.BMR),
    WAIST_HIP("Waist-to-hip ratio", CalculatorType.WHR),
    WATER_ESTIMATE("Water intake estimate", CalculatorType.WATER_INTAKE),
    METABOLIC("Metabolic screening estimate", CalculatorType.METABOLIC_SYNDROME),
    BSA("Body surface area", CalculatorType.BSA),
    WEIGHT_ESTIMATE("Height-based weight estimate", CalculatorType.IBW),
    ENERGY_ESTIMATE("Daily energy estimate", CalculatorType.CALORIE),
    HEART_RATE("Heart-rate zones", CalculatorType.HEART_RATE),
    MEDICATIONS("Medication entries", medicalRecordKind = MedicalRecordKind.MEDICATION, requiresOptIn = true),
    VISITS("Visit entries", medicalRecordKind = MedicalRecordKind.VISIT, requiresOptIn = true);

    val fromCalculationHistory: Boolean
        get() = calculator != null && this != BLOOD_PRESSURE && this != WATER_ESTIMATE

    companion object {
        val defaultSelection = entries.filterNot { it.requiresOptIn }.toSet()
    }
}

internal enum class ReportPeriod(val days: Int) {
    WEEK(7), MONTH(30), QUARTER(90)
}

internal class ReportWindow(
    val period: ReportPeriod,
    now: Instant = Instant.now(),
    val zone: ZoneId = ZoneId.systemDefault()
) {
    private val endDate = now.atZone(zone).toLocalDate()
    private val startDate = endDate.minusDays(period.days.toLong() - 1)
    val start = startDate.atStartOfDay(zone).toInstant().toEpochMilli()
    val end = now.toEpochMilli()
    val title: String = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).let {
        "${startDate.format(it)} – ${endDate.format(it)}"
    }

    fun date(timestamp: Long): String = Instant.ofEpochMilli(timestamp).atZone(zone)
        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))

    fun day(timestamp: Long) = Instant.ofEpochMilli(timestamp).atZone(zone).toLocalDate()

    fun time(timestamp: Long): String = Instant.ofEpochMilli(timestamp).atZone(zone)
        .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
}

internal data class ReportRecord(val timestamp: Long, val result: String, val context: String = "")

internal data class ReportSection(val tool: ReportTool, val records: List<ReportRecord>)

internal class HealthReport(
    val window: ReportWindow,
    records: Map<ReportTool, List<ReportRecord>>
) {
    val sections = ReportTool.entries.mapNotNull { tool ->
        val inRange = records[tool].orEmpty()
            .filter { it.timestamp in window.start..window.end && it.result.isNotBlank() }
            .sortedByDescending { it.timestamp }
        inRange.takeIf { it.isNotEmpty() }?.let { ReportSection(tool, it) }
    }

    fun snapshot(selected: Set<ReportTool>) =
        HealthReportSnapshot(window, sections.filter { it.tool in selected })
}

internal data class HealthReportSnapshot(val window: ReportWindow, val sections: List<ReportSection>) {
    val recordCount = sections.sumOf { it.records.size }

    val recordCountLabel = if (recordCount == 1) "1 record" else "$recordCount records"
    val sectionCountLabel = if (sections.size == 1) "1 section" else "${sections.size} sections"

    fun tablePages(): List<List<ReportTable>> = buildList {
        val headerLines = 4
        val pageLines = 31
        var tables = mutableListOf<ReportTable>()
        var freeLines = pageLines
        sections.forEach { section ->
            var offset = 0
            while (offset < section.records.size) {
                if (freeLines <= headerLines) {
                    add(tables)
                    tables = mutableListOf()
                    freeLines = pageLines
                }
                val count = minOf(section.records.size - offset, freeLines - headerLines)
                tables.add(ReportTable(section, section.records.subList(offset, offset + count), continued = offset > 0))
                offset += count
                freeLines -= headerLines + count
            }
        }
        if (tables.isNotEmpty()) add(tables)
    }
}

internal data class ReportTable(val section: ReportSection, val rows: List<ReportRecord>, val continued: Boolean)
