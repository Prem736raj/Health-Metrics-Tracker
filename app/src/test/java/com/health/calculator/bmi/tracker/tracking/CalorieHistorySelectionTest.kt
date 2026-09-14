package com.health.calculator.bmi.tracker.tracking

import com.health.calculator.bmi.tracker.data.model.DailyFoodLog
import com.health.calculator.bmi.tracker.data.model.FoodEntry
import com.health.calculator.bmi.tracker.domain.usecase.CalorieHistoryAnalyticsUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CalorieHistorySelectionTest {

    private val analytics = CalorieHistoryAnalyticsUseCase()

    @Test
    fun findLogForDate_returnsMatchingDate() {
        val expected = DailyFoodLog(
            date = "2026-09-14",
            entries = listOf(FoodEntry(timestamp = 20L, name = "Lunch", calories = 500.0)),
            targetCalories = 2000.0
        )

        val result = analytics.findLogForDate(
            listOf(
                DailyFoodLog("2026-09-13", emptyList(), 2000.0),
                expected
            ),
            "2026-09-14"
        )

        assertEquals(expected, result)
    }

    @Test
    fun findLogForDate_prefers_populated_latest_snapshot_when_date_is_duplicated() {
        val older = DailyFoodLog(
            date = "2026-09-14",
            entries = listOf(FoodEntry(timestamp = 10L, name = "Breakfast", calories = 400.0)),
            targetCalories = 2000.0
        )
        val latest = DailyFoodLog(
            date = "2026-09-14",
            entries = listOf(FoodEntry(timestamp = 30L, name = "Dinner", calories = 800.0)),
            targetCalories = 2100.0
        )

        assertEquals(latest, analytics.findLogForDate(listOf(older, latest), "2026-09-14"))
    }

    @Test
    fun findLogForDate_returns_null_for_unknown_date() {
        val log = DailyFoodLog("2026-09-14", emptyList(), 2000.0)

        assertNull(analytics.findLogForDate(listOf(log), "2026-09-15"))
    }
}
