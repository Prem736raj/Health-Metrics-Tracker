package com.health.calculator.bmi.tracker.data.repository

import com.health.calculator.bmi.tracker.data.model.DailyFoodLog
import com.health.calculator.bmi.tracker.data.model.FoodEntry
import java.time.LocalDate

internal object FoodHistoryPolicy {
    fun archive(history: List<DailyFoodLog>, day: DailyFoodLog): List<DailyFoodLog> =
        listOf(day) + history.filterNot { it.date == day.date }

    fun append(entries: List<FoodEntry>, entry: FoodEntry): List<FoodEntry> {
        // Millisecond timestamps can collide on rapid taps. Keep both logs,
        // but never let deleting one ID accidentally remove two entries.
        var id = entry.id
        val usedIds = entries.mapTo(hashSetOf()) { it.id }
        while (id in usedIds) id = if (id == Long.MAX_VALUE) 0L else id + 1
        return entries + entry.copy(id = id)
    }

    fun weeklyAdherence(logs: List<DailyFoodLog>, today: LocalDate): Float {
        val start = today.minusDays(6)
        val recent = logs.associateBy { it.date }.values.filter { log ->
            val date = runCatching { LocalDate.parse(log.date) }.getOrNull()
            date != null && !date.isBefore(start) && !date.isAfter(today)
        }
        if (recent.isEmpty()) return 0f
        return recent.map { log ->
            val consumed = log.totalCalories
            if (log.targetCalories.isFinite() && log.targetCalories > 0 && consumed.isFinite())
                (consumed / log.targetCalories).coerceIn(0.0, 1.0) else 0.0
        }.average().toFloat()
    }
}
