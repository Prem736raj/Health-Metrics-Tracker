package com.health.calculator.bmi.tracker.ui.screens.calorie

import com.health.calculator.bmi.tracker.data.model.DailyFoodLog
import java.time.LocalDate

/** Date/snapshot selection for display only; stored logs and targets are unchanged. */
fun caloriePresentationLogs(logs: List<DailyFoodLog>): List<DailyFoodLog> = logs
    .filter { runCatching { LocalDate.parse(it.date) }.isSuccess && it.totalCalories.isFinite() }
    .groupBy { it.date }
    .values.map { snapshots ->
        snapshots.maxWith(compareBy<DailyFoodLog> { it.entries.isNotEmpty() }
            .thenBy { it.entries.maxOfOrNull { entry -> entry.timestamp } ?: Long.MIN_VALUE })
    }.sortedBy { it.date }

fun caloriePeriodLogs(
    logs: List<DailyFoodLog>,
    days: Int,
    today: LocalDate = LocalDate.now()
): List<DailyFoodLog> {
    val first = today.minusDays(days.coerceIn(1, 365).toLong() - 1)
    return caloriePresentationLogs(logs).filter {
        val date = LocalDate.parse(it.date)
        it.entries.isNotEmpty() && !date.isBefore(first) && !date.isAfter(today)
    }
}
