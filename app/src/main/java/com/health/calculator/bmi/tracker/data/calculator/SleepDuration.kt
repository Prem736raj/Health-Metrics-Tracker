package com.health.calculator.bmi.tracker.data.calculator

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

internal data class SleepEstimate(
    val start: Instant,
    val wake: Instant,
    val intervalMinutes: Long,
    val settlingMinutes: Long,
    val awakeMinutes: Long
) {
    val sleepMinutes = intervalMinutes - settlingMinutes - awakeMinutes
}

internal sealed interface SleepCalculation {
    data class Valid(val estimate: SleepEstimate) : SleepCalculation
    enum class Error : SleepCalculation {
        INVALID_TIMES, SAME_TIME, INVALID_MINUTES, CLOCK_CHANGE, FUTURE_WAKE, TOO_MANY_AWAKE_MINUTES
    }
}

internal fun calculateSleep(
    startClock: String,
    wakeClock: String,
    wakeDate: LocalDate,
    settling: String,
    awake: String,
    zone: ZoneId,
    now: Instant
): SleepCalculation {
    val startTime = parseClock(startClock) ?: return SleepCalculation.Error.INVALID_TIMES
    val wakeTime = parseClock(wakeClock) ?: return SleepCalculation.Error.INVALID_TIMES
    if (startTime == wakeTime) return SleepCalculation.Error.SAME_TIME
    val settlingMinutes = parseMinutes(settling) ?: return SleepCalculation.Error.INVALID_MINUTES
    val awakeMinutes = parseMinutes(awake) ?: return SleepCalculation.Error.INVALID_MINUTES

    val startDate = if (startTime > wakeTime) wakeDate.minusDays(1) else wakeDate
    val startLocal = startDate.atTime(startTime)
    val wakeLocal = wakeDate.atTime(wakeTime)
    val startOffset = zone.rules.getValidOffsets(startLocal).singleOrNull()
        ?: return SleepCalculation.Error.CLOCK_CHANGE
    val wakeOffset = zone.rules.getValidOffsets(wakeLocal).singleOrNull()
        ?: return SleepCalculation.Error.CLOCK_CHANGE
    val start = startLocal.toInstant(startOffset)
    val wake = wakeLocal.toInstant(wakeOffset)
    if (wake > now) return SleepCalculation.Error.FUTURE_WAKE
    val interval = Duration.between(start, wake).toMinutes()
    if (settlingMinutes + awakeMinutes > interval) return SleepCalculation.Error.TOO_MANY_AWAKE_MINUTES

    return SleepCalculation.Valid(SleepEstimate(start, wake, interval, settlingMinutes, awakeMinutes))
}

private fun parseClock(input: String): LocalTime? {
    val text = input.trim()
    val parts = text.split(':')
    if (parts.size != 2 || parts[0].length !in 1..2 || parts[1].length != 2 ||
        parts.any { part -> part.any { !it.isDigit() } }) return null
    val hour = parts[0].toIntOrNull()?.takeIf { it in 0..23 } ?: return null
    val minute = parts[1].toIntOrNull()?.takeIf { it in 0..59 } ?: return null
    return LocalTime.of(hour, minute)
}

private fun parseMinutes(input: String): Long? =
    if (input.isBlank()) 0 else input.trim().toIntOrNull()?.toLong()?.takeIf { it >= 0 }

internal fun formatSleepDuration(minutes: Long): String =
    if (minutes < 60) "$minutes min" else "${minutes / 60} h ${minutes % 60} min"
