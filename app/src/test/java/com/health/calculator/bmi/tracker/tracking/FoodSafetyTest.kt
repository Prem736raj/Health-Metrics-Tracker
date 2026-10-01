package com.health.calculator.bmi.tracker.tracking

import com.health.calculator.bmi.tracker.data.model.DailyFoodLog
import com.health.calculator.bmi.tracker.data.model.FoodEntry
import com.health.calculator.bmi.tracker.data.repository.FoodHistoryPolicy
import com.health.calculator.bmi.tracker.ui.screens.calorie.FoodInputPolicy
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class FoodSafetyTest {
    @Test fun rapidEntriesKeepDistinctIdsWithoutDroppingEitherMeasurement() {
        val first = FoodEntry(id = 42, name = "First", calories = 100.0)
        val second = first.copy(name = "Second")
        val entries = FoodHistoryPolicy.append(listOf(first), second)
        assertEquals(2, entries.size)
        assertNotEquals(entries[0].id, entries[1].id)
        assertEquals(listOf("First", "Second"), entries.map { it.name })
    }

    @Test fun weeklySummaryIncludesTodayAndExcludesStaleOrMalformedDays() {
        val today = LocalDate.of(2026, 9, 20)
        val history = (1L..20L).map { DailyFoodLog(today.minusDays(it).toString(), emptyList(), 2000.0) }
        val loggedToday = DailyFoodLog(today.toString(), listOf(FoodEntry(name = "Meal", calories = 2000.0)), 2000.0)
        val invalidDate = loggedToday.copy(date = "not-a-date")
        val future = loggedToday.copy(date = today.plusDays(1).toString())
        assertEquals(1f / 7f, FoodHistoryPolicy.weeklyAdherence(history + invalidDate + future + loggedToday, today), 0.0001f)
        assertEquals(0f, FoodHistoryPolicy.weeklyAdherence(emptyList(), today))
    }

    @Test fun archiveRetainsMoreThanNinetyDays() {
        val start = LocalDate.of(2025, 1, 1)
        val history = (0L..119L).map { DailyFoodLog(start.plusDays(it).toString(), emptyList(), 2000.0) }
        val next = DailyFoodLog(start.plusDays(120).toString(), emptyList(), 2000.0)
        assertEquals(121, FoodHistoryPolicy.archive(history, next).size)
        assertTrue(FoodHistoryPolicy.archive(history, next).contains(history.first()))
    }

    @Test fun repeatedArchiveUpdatesOneDayWithoutDuplicatingIt() {
        val day = DailyFoodLog("2026-09-19", emptyList(), 2000.0)
        val updated = day.copy(targetCalories = 2100.0)
        assertEquals(listOf(updated), FoodHistoryPolicy.archive(listOf(day), updated))
    }

    @Test fun malformedNegativeAndNonFiniteMacrosAreRejected() {
        listOf("NaN", "Infinity", "1e999", "-1", "abc").forEach {
            assertNull(FoodInputPolicy.parseMacro(it))
        }
        assertEquals(0.0, FoodInputPolicy.parseMacro(" "))
        assertEquals(12.5, FoodInputPolicy.parseMacro("12.5"))
    }

    @Test fun invalidCalorieGoalDoesNotProduceNaNProgress() {
        listOf(0.0, -1.0, Double.NaN, Double.POSITIVE_INFINITY).forEach {
            assertEquals(0f, DailyFoodLog("2026-09-19", emptyList(), it).calorieProgress)
        }
    }
}
