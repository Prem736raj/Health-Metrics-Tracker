package com.health.calculator.bmi.tracker.core.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/** Consecutive local dates, not 24-hour durations. Multiple logs count once. */
object CalendarStreak {
    fun count(timestamps: Iterable<Long>, today: LocalDate, zone: ZoneId): Int {
        val dates = timestamps.map { Instant.ofEpochMilli(it).atZone(zone).toLocalDate() }
            .filter { !it.isAfter(today) }.toSet()
        var day = if (today in dates) today else today.minusDays(1)
        var streak = 0
        while (day in dates) {
            streak++
            day = day.minusDays(1)
        }
        return streak
    }
}
