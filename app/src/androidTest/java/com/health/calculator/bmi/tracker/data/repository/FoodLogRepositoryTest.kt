package com.health.calculator.bmi.tracker.data.repository

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.health.calculator.bmi.tracker.data.model.FoodEntry
import com.health.calculator.bmi.tracker.data.model.FoodPreset
import org.json.JSONArray
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class FoodLogRepositoryTest {
    private val baseContext = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var prefix: String
    private lateinit var isolatedContext: Context

    @Before fun setUp() {
        prefix = "food_repository_test_${System.nanoTime()}_"
        isolatedContext = object : ContextWrapper(baseContext) {
            override fun getSharedPreferences(name: String, mode: Int): SharedPreferences =
                baseContext.getSharedPreferences(prefix + name, mode)
        }
    }

    @After fun tearDown() {
        listOf("food_log", "food_presets").forEach { name ->
            baseContext.getSharedPreferences(prefix + name, Context.MODE_PRIVATE)
                .edit().clear().commit()
        }
    }

    @Test fun malformedTodayLogIsPreservedAndBlocksNewEntries() {
        val prefs = isolatedContext.getSharedPreferences("food_log", Context.MODE_PRIVATE)
        prefs.edit().putString("log_date", LocalDate.now().toString())
            .putString("today_log", "{damaged").commit()

        val repository = FoodLogRepository(isolatedContext)
        assertTrue(repository.isReadOnly.value)
        assertFalse(repository.addEntry(FoodEntry(name = "Meal", calories = 100.0)))
        assertFalse(repository.updateDailyTargets(2100.0))
        assertFalse(repository.saveTargetCalories(2100.0))
        assertEquals("{damaged", prefs.getString("today_log", null))
        assertFalse(prefs.contains("target_calories"))
    }

    @Test fun malformedHistoryCannotBeReplacedDuringDayRollover() {
        val prefs = isolatedContext.getSharedPreferences("food_log", Context.MODE_PRIVATE)
        val priorDate = LocalDate.now().minusDays(1).toString()
        val oldEntry = JSONObject()
            .put("id", 1).put("timestamp", 1).put("name", "Old meal")
            .put("calories", 100.0).put("protein", 0.0).put("carbs", 0.0)
            .put("fat", 0.0).put("mealSlot", "Other").put("serving", "")
            .put("isPreset", false)
        val oldLog = JSONObject()
            .put("date", priorDate).put("entries", JSONArray().put(oldEntry))
            .put("targetCalories", 2000.0).put("targetProtein", 0.0)
            .put("targetCarbs", 0.0).put("targetFat", 0.0).toString()
        prefs.edit().putString("log_date", priorDate)
            .putString("today_log", oldLog)
            .putString("history_logs", "[damaged").commit()

        val repository = FoodLogRepository(isolatedContext)
        assertTrue(repository.isReadOnly.value)
        assertFalse(repository.checkAndResetIfNewDay())
        assertFalse(repository.addEntry(FoodEntry(name = "Meal", calories = 100.0)))
        assertEquals("[damaged", prefs.getString("history_logs", null))
        assertEquals(oldLog, prefs.getString("today_log", null))
        assertEquals(priorDate, prefs.getString("log_date", null))
    }

    @Test fun malformedPresetsArePreservedAndCannotBeReplaced() {
        val prefs = isolatedContext.getSharedPreferences("food_presets", Context.MODE_PRIVATE)
        prefs.edit().putString("custom_presets", "[damaged").commit()

        val repository = FoodLogRepository(isolatedContext)
        assertTrue(repository.isReadOnly.value)
        val preset = FoodPreset("test", "Meal", 100.0, 1.0, 1.0, 1.0, "1 serving", "🍽️")
        assertFalse(repository.addCustomPreset(preset))
        assertEquals("[damaged", prefs.getString("custom_presets", null))
    }

    @Test fun corruptionFoundAfterStartupAlsoBlocksWrites() {
        val repository = FoodLogRepository(isolatedContext)
        val prefs = isolatedContext.getSharedPreferences("food_log", Context.MODE_PRIVATE)
        val savedToday = prefs.getString("today_log", null)
        prefs.edit().putString("history_logs", "[damaged").commit()

        assertTrue(repository.getHistoricalLogs().isEmpty())
        assertTrue(repository.isReadOnly.value)
        assertFalse(repository.addEntry(FoodEntry(name = "Meal", calories = 100.0)))
        assertEquals(savedToday, prefs.getString("today_log", null))
        assertEquals("[damaged", prefs.getString("history_logs", null))
    }

    @Test fun validStorageStillAcceptsAndPersistsFoodEntries() {
        val repository = FoodLogRepository(isolatedContext)
        val prefs = isolatedContext.getSharedPreferences("food_log", Context.MODE_PRIVATE)

        assertFalse(repository.isReadOnly.value)
        assertTrue(repository.addEntry(FoodEntry(name = "Meal", calories = 100.0)))
        assertEquals("Meal", repository.todayLog.value.entries.single().name)
        assertTrue(prefs.getString("today_log", "")!!.contains("Meal"))
    }
}
