package com.health.calculator.bmi.tracker.ui.screens.waterintake

import java.time.LocalDate
import java.time.ZoneId

/** Presentation keeps an unlogged day distinct from a recorded zero. */
data class WaterTrendReading(val date: LocalDate, val totalMl: Int?) {
    val timestamp: Long get() = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun waterTrendReadings(
    data: Map<String, DailyWaterData>,
    days: Int,
    today: LocalDate = LocalDate.now()
): List<WaterTrendReading> = (days.coerceIn(1, 90) - 1 downTo 0).map { offset ->
    val date = today.minusDays(offset.toLong())
    WaterTrendReading(date, data[date.toString()]?.totalMl)
}
