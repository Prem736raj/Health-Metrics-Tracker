package com.health.calculator.bmi.tracker.data.export

import com.health.calculator.bmi.tracker.data.local.entity.BloodPressureEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

internal enum class BpVisitPeriod(val days: Int) {
    LAST_7_DAYS(7),
    LAST_30_DAYS(30)
}

internal class BpVisitReport(
    val period: BpVisitPeriod,
    now: Instant,
    val zone: ZoneId,
    readings: List<BloodPressureEntity>
) {
    val endDate: LocalDate = now.atZone(zone).toLocalDate()
    val startDate: LocalDate = endDate.minusDays(period.days.toLong() - 1)
    private val startTime = startDate.atStartOfDay(zone).toInstant().toEpochMilli()
    private val endTime = now.toEpochMilli()

    val readings: List<BloodPressureEntity> = readings
        .filter { !it.isPartOfAverage && it.measurementTimestamp in startTime..endTime }
        .sortedWith(compareByDescending<BloodPressureEntity> { it.measurementTimestamp }.thenByDescending { it.id })
        .map { it.copy(note = "", medicationName = "", onMedication = false) }

    val daysWithReadings: Int = this.readings
        .map { Instant.ofEpochMilli(it.measurementTimestamp).atZone(zone).toLocalDate() }
        .distinct()
        .size

    val dateRange: String
        get() {
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            return "${startDate.format(formatter)} – ${endDate.format(formatter)}"
        }
}
