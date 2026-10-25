package com.health.calculator.bmi.tracker.ui.screens.history

import java.time.LocalDate

internal fun streakDaysLabel(days: Int): String = "$days ${if (days == 1) "Day" else "Days"}"

/** Calendar rows start on Monday and include only the month's actual days. */
internal fun calendarWeekCells(first: LocalDate): List<List<Int?>> {
    val start = first.withDayOfMonth(1)
    val leading = start.dayOfWeek.value - 1
    val count = (leading + start.lengthOfMonth() + 6) / 7 * 7
    return (0 until count).map { index ->
        (index - leading + 1).takeIf { it in 1..start.lengthOfMonth() }
    }.chunked(7)
}

/** Numeric direction is independent of whether the app interprets a change as good. */
internal fun numericChangeDirection(change: Float): Int = when {
    !change.isFinite() || change == 0f -> 0
    change > 0f -> 1
    else -> -1
}
