package com.health.calculator.bmi.tracker.ui.components.detail

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Presentation only: do not substitute these labels for stored timestamps. */
fun trendAxisLabels(
    timestamps: List<Long>,
    zone: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.getDefault()
): List<String> {
    val dates = timestamps.map { Instant.ofEpochMilli(it).atZone(zone) }
    val days = dates.map { it.toLocalDate() }
    val repeatedDay = days.distinct().size < days.size
    val repeatedMinute = dates.map { it.withSecond(0).withNano(0) }.distinct().size < dates.size
    val pattern = when {
        days.distinct().size == 1 -> if (repeatedMinute) "HH:mm:ss" else "HH:mm"
        repeatedDay -> if (repeatedMinute) "MM/dd HH:mm:ss" else "MM/dd HH:mm"
        dates.map { it.year }.distinct().size > 1 -> "MM/dd/yy"
        else -> "MM/dd"
    }
    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return dates.map { it.format(formatter) }
}

/** Always include both ends; keep dates sparse enough to read on a phone. */
fun trendTickIndices(count: Int, maxTicks: Int = 3): List<Int> {
    if (count <= 0) return emptyList()
    if (count == 1) return listOf(0)
    val ticks = maxTicks.coerceAtLeast(2).coerceAtMost(count)
    return (0 until ticks).map { (it.toDouble() * (count - 1) / (ticks - 1)).toInt() }.distinct()
}
