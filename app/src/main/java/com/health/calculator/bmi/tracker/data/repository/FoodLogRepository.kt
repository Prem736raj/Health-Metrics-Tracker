package com.health.calculator.bmi.tracker.data.repository

import dagger.hilt.android.qualifiers.ApplicationContext

import android.content.Context
import com.health.calculator.bmi.tracker.data.model.DailyFoodLog
import com.health.calculator.bmi.tracker.data.model.FoodEntry
import com.health.calculator.bmi.tracker.data.model.FoodPreset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class FoodLogRepository(@ApplicationContext private val context: Context) {

    private val prefs by lazy {
        context.getSharedPreferences("food_log", Context.MODE_PRIVATE)
    }
    private val presetPrefs by lazy {
        context.getSharedPreferences("food_presets", Context.MODE_PRIVATE)
    }

    /** ISO dates sort chronologically and the formatter is safe across threads. */
    private val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE
    private val todayKey: String
        get() = LocalDate.now(ZoneId.systemDefault()).format(dateFormat)

    private val _isReadOnly = MutableStateFlow(false)
    /** A malformed stored record must not be replaced with an apparently empty log. */
    val isReadOnly: StateFlow<Boolean> = _isReadOnly.asStateFlow()

    private val _todayLog = MutableStateFlow(emptyDailyLog())
    /**
     * The food log is day-scoped. The repository refreshes at the next local
     * midnight (and periodically while waiting), so a process that stays alive
     * across midnight cannot keep showing yesterday's entries indefinitely.
     */
    val todayLog: StateFlow<DailyFoodLog> = _todayLog.asStateFlow()

    private val dateRefreshScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _customPresets = MutableStateFlow<List<FoodPreset>>(emptyList())
    val customPresets: StateFlow<List<FoodPreset>> = _customPresets.asStateFlow()

    val defaultPresets = listOf(
        FoodPreset("rice_1cup", "Rice (1 cup, cooked)", 206.0, 4.3, 44.5, 0.4, "1 cup (186g)", "🍚"),
        FoodPreset("chicken_100g", "Chicken Breast (100g)", 165.0, 31.0, 0.0, 3.6, "100g", "🍗"),
        FoodPreset("egg_1", "Egg (1 large)", 72.0, 6.3, 0.4, 4.8, "1 large (50g)", "🥚"),
        FoodPreset("banana_1", "Banana (1 medium)", 105.0, 1.3, 27.0, 0.4, "1 medium (118g)", "🍌"),
        FoodPreset("bread_1slice", "Bread (1 slice)", 79.0, 3.0, 15.0, 1.0, "1 slice (30g)", "🍞"),
        FoodPreset("milk_1cup", "Milk (1 cup)", 149.0, 8.0, 12.0, 8.0, "1 cup (244ml)", "🥛"),
        FoodPreset("apple_1", "Apple (1 medium)", 95.0, 0.5, 25.0, 0.3, "1 medium (182g)", "🍎"),
        FoodPreset("oats_1cup", "Oatmeal (1 cup, cooked)", 166.0, 5.9, 28.1, 3.6, "1 cup (234g)", "🥣"),
        FoodPreset("salmon_100g", "Salmon (100g)", 208.0, 20.0, 0.0, 13.0, "100g", "🐟"),
        FoodPreset("broccoli_1cup", "Broccoli (1 cup)", 55.0, 3.7, 11.0, 0.6, "1 cup (91g)", "🥦"),
        FoodPreset("peanut_butter_1tbsp", "Peanut Butter (1 tbsp)", 95.0, 4.0, 3.0, 8.0, "1 tbsp (16g)", "🥜"),
        FoodPreset("greek_yogurt_100g", "Greek Yogurt (100g)", 59.0, 10.0, 3.6, 0.4, "100g", "🥛"),
        FoodPreset("sweet_potato_1", "Sweet Potato (medium)", 103.0, 2.3, 24.0, 0.1, "1 medium (130g)", "🍠"),
        FoodPreset("almonds_10", "Almonds (10 nuts)", 69.0, 2.6, 2.4, 6.0, "10 nuts (14g)", "🥜"),
        FoodPreset("pasta_1cup", "Pasta (1 cup, cooked)", 220.0, 8.0, 43.0, 1.3, "1 cup (140g)", "🍝")
    )

    init {
        if (!_isReadOnly.value && verifyStoredData()) {
            loadTodayLog()
            loadCustomPresets()
        }
        scheduleDateRefresh()
    }

    private fun verifyStoredData(): Boolean = try {
        prefs.getString("log_date", null)
        prefs.getString("today_log", null)?.let { fromJson(JSONObject(it)) }
        presetPrefs.getString("custom_presets", null)?.let { parsePresets(it) }
        true
    } catch (_: Exception) {
        _isReadOnly.value = true
        false
    }

    private fun scheduleDateRefresh() {
        dateRefreshScope.launch {
            while (isActive) {
                val now = ZonedDateTime.now(ZoneId.systemDefault())
                val untilMidnight = FoodLogDayPolicy.nextMidnightDelayMillis(now)

                // Re-check hourly at most so a timezone or clock adjustment
                // is reflected promptly even when the process remains alive.
                delay(untilMidnight.coerceAtMost(MAX_DATE_REFRESH_DELAY_MS))
                checkAndResetIfNewDay()
            }
        }
    }

    @Synchronized
    fun addEntry(entry: FoodEntry): Boolean {
        if (!checkAndResetIfNewDay()) return false
        val current = _todayLog.value
        val updatedEntries = FoodHistoryPolicy.append(current.entries, entry)
        val updated = current.copy(entries = updatedEntries)
        _todayLog.value = updated
        saveTodayLog(updated)
        return true
    }

    @Synchronized
    fun removeEntry(entryId: Long): Boolean {
        if (!checkAndResetIfNewDay()) return false
        val current = _todayLog.value
        val updatedEntries = current.entries.filter { it.id != entryId }
        val updated = current.copy(entries = updatedEntries)
        _todayLog.value = updated
        saveTodayLog(updated)
        return true
    }

    @Synchronized
    fun updateDailyTargets(
        targetCalories: Double,
        targetProtein: Double = 0.0,
        targetCarbs: Double = 0.0,
        targetFat: Double = 0.0
    ): Boolean {
        if (!checkAndResetIfNewDay()) return false
        val updated = _todayLog.value.copy(
            targetCalories = targetCalories,
            targetProteinGrams = targetProtein,
            targetCarbGrams = targetCarbs,
            targetFatGrams = targetFat
        )
        _todayLog.value = updated
        saveTodayLog(updated)
        return true
    }

    fun addCustomPreset(preset: FoodPreset): Boolean {
        if (_isReadOnly.value) return false
        val current = _customPresets.value.toMutableList()
        current.add(preset)
        _customPresets.value = current
        saveCustomPresets(current)
        return true
    }

    fun removeCustomPreset(presetId: String): Boolean {
        if (_isReadOnly.value) return false
        val current = _customPresets.value.toMutableList()
        current.removeAll { it.id == presetId }
        _customPresets.value = current
        saveCustomPresets(current)
        return true
    }

    @Synchronized
    fun checkAndResetIfNewDay(): Boolean {
        if (_isReadOnly.value) return false
        val savedDate = prefs.getString("log_date", "") ?: ""
        val today = todayKey
        if (FoodLogDayPolicy.needsReset(savedDate, today)) {
            // Save yesterday's log to history
            val yesterdayLog = _todayLog.value
            if (savedDate.isNotBlank() && yesterdayLog.entries.isNotEmpty()) {
                if (!saveToHistory(yesterdayLog, savedDate)) return false
            }
            // Reset for today
            val fresh = emptyDailyLog()
            _todayLog.value = fresh
            saveTodayLog(fresh)
        }
        return true
    }

    @Synchronized
    fun getHistoricalLogs(): List<DailyFoodLog> {
        if (_isReadOnly.value) return emptyList()
        return try {
            prefs.getString("history_logs", null)?.let { parseHistory(it) } ?: emptyList()
        } catch (_: Exception) {
            _isReadOnly.value = true
            emptyList()
        }
    }

    private fun parseHistory(json: String): List<DailyFoodLog> {
        val arr = JSONArray(json)
        return (0 until arr.length()).map { fromJson(arr.getJSONObject(it)) }
    }

    private fun loadTodayLog() {
        val savedDate = prefs.getString("log_date", "") ?: ""
        val today = todayKey
        if (savedDate == today) {
            val json = prefs.getString("today_log", null)
            if (json != null) {
                try {
                    _todayLog.value = fromJson(JSONObject(json))
                    return
                } catch (_: Exception) {
                    _isReadOnly.value = true
                    return
                }
            }
        }
        if (savedDate.isNotBlank() && savedDate != today) {
            // A cold start after midnight must archive the previous day before
            // replacing it. This also covers users who never reopen the food
            // screen while the app is running.
            prefs.getString("today_log", null)?.let { json ->
                val oldLog = try { fromJson(JSONObject(json)) } catch (_: Exception) {
                    _isReadOnly.value = true
                    return
                }
                if (oldLog.entries.isNotEmpty() && !saveToHistory(oldLog, savedDate)) return
            }
        }
        val fresh = emptyDailyLog()
        _todayLog.value = fresh
        if (savedDate != today) saveTodayLog(fresh)
    }

    private fun loadCustomPresets() {
        val json = presetPrefs.getString("custom_presets", null) ?: return
        try {
            _customPresets.value = parsePresets(json)
        } catch (_: Exception) {
            _isReadOnly.value = true
        }
    }

    private fun parsePresets(json: String): List<FoodPreset> {
        val arr = JSONArray(json)
        return (0 until arr.length()).map { i ->
            val o = arr.getJSONObject(i)
            FoodPreset(
                id = o.getString("id"), name = o.getString("name"),
                calories = o.getDouble("calories"), proteinGrams = o.getDouble("protein"),
                carbGrams = o.getDouble("carbs"), fatGrams = o.getDouble("fat"),
                servingSize = o.getString("serving"), emoji = o.getString("emoji"),
                isCustom = true
            )
        }
    }

    private fun saveTodayLog(log: DailyFoodLog) {
        prefs.edit()
            .putString("log_date", todayKey)
            .putString("today_log", toJson(log).toString())
            .apply()
    }

    private fun saveCustomPresets(presets: List<FoodPreset>) {
        val arr = JSONArray()
        presets.forEach { p ->
            arr.put(JSONObject().apply {
                put("id", p.id); put("name", p.name); put("calories", p.calories)
                put("protein", p.proteinGrams); put("carbs", p.carbGrams); put("fat", p.fatGrams)
                put("serving", p.servingSize); put("emoji", p.emoji)
            })
        }
        presetPrefs.edit().putString("custom_presets", arr.toString()).apply()
    }

    private fun saveToHistory(log: DailyFoodLog, date: String): Boolean {
        val current = FoodHistoryPolicy.archive(getHistoricalLogs(), log.copy(date = date))
        if (_isReadOnly.value) return false
        val arr = JSONArray()
        // History is user data, not a rolling cache. Never silently discard old days.
        current.forEach { arr.put(toJson(it)) }
        prefs.edit().putString("history_logs", arr.toString()).apply()
        return true
    }

    private fun emptyDailyLog(): DailyFoodLog {
        val targetCals = try {
            prefs.getFloat("target_calories", 2000f).toDouble()
        } catch (_: ClassCastException) {
            _isReadOnly.value = true
            2000.0
        }
        return DailyFoodLog(
            date = todayKey,
            entries = emptyList(),
            targetCalories = targetCals
        )
    }

    private fun toJson(log: DailyFoodLog): JSONObject {
        val entriesArr = JSONArray()
        log.entries.forEach { e ->
            entriesArr.put(JSONObject().apply {
                put("id", e.id); put("timestamp", e.timestamp)
                put("name", e.name); put("calories", e.calories)
                put("protein", e.proteinGrams); put("carbs", e.carbGrams); put("fat", e.fatGrams)
                put("mealSlot", e.mealSlot); put("serving", e.servingSize); put("isPreset", e.isPreset)
            })
        }
        return JSONObject().apply {
            put("date", log.date); put("entries", entriesArr)
            put("targetCalories", log.targetCalories)
            put("targetProtein", log.targetProteinGrams)
            put("targetCarbs", log.targetCarbGrams)
            put("targetFat", log.targetFatGrams)
        }
    }

    private fun fromJson(o: JSONObject): DailyFoodLog {
        val entriesArr = o.getJSONArray("entries")
        val entries = (0 until entriesArr.length()).map { i ->
            val e = entriesArr.getJSONObject(i)
            FoodEntry(
                id = e.getLong("id"), timestamp = e.getLong("timestamp"),
                name = e.getString("name"), calories = e.getDouble("calories"),
                proteinGrams = e.getDouble("protein"), carbGrams = e.getDouble("carbs"),
                fatGrams = e.getDouble("fat"), mealSlot = e.getString("mealSlot"),
                servingSize = e.getString("serving"), isPreset = e.getBoolean("isPreset")
            )
        }
        return DailyFoodLog(
            date = o.getString("date"),
            entries = entries,
            targetCalories = o.getDouble("targetCalories"),
            targetProteinGrams = o.getDouble("targetProtein"),
            targetCarbGrams = o.getDouble("targetCarbs"),
            targetFatGrams = o.getDouble("targetFat")
        )
    }

    fun saveTargetCalories(calories: Double): Boolean {
        if (_isReadOnly.value) return false
        prefs.edit().putFloat("target_calories", calories.toFloat()).apply()
        return true
    }

    fun getWeeklyAdherence(): kotlinx.coroutines.flow.Flow<Float> = todayLog.map { today ->
        FoodHistoryPolicy.weeklyAdherence(getHistoricalLogs() + today, LocalDate.now(ZoneId.systemDefault()))
    }

    fun getDailyCalorieGoal(): kotlinx.coroutines.flow.Flow<Int> = kotlinx.coroutines.flow.flow {
        emit(prefs.getFloat("target_calories", 2000f).toInt())
    }

    suspend fun getAverageCaloriesInRange(startMillis: Long, endMillis: Long): Int {
        val logs = getLogsInTimeRange(startMillis, endMillis)
        if (logs.isEmpty()) return 0
        val total = logs.sumOf { it.entries.sumOf { entry -> entry.calories } }
        val days = ((endMillis - startMillis) / (24 * 60 * 60 * 1000)).toInt() + 1
        return (total / days.coerceAtLeast(1)).toInt()
    }

    suspend fun getDaysLoggedInRange(startMillis: Long, endMillis: Long): Int {
        val logs = getLogsInTimeRange(startMillis, endMillis)
        return logs.count { it.entries.isNotEmpty() }
    }

    suspend fun getDaysOnTargetInRange(startMillis: Long, endMillis: Long): Int {
        val logs = getLogsInTimeRange(startMillis, endMillis)
        return logs.count { log ->
            val consumed = log.entries.sumOf { it.calories }
            val target = log.targetCalories
            consumed <= target && consumed >= target - 500 // Within a reasonable range
        }
    }

    private fun getLogsInTimeRange(startMillis: Long, endMillis: Long): List<DailyFoodLog> {
        val allHistory = getHistoricalLogs()
        val today = _todayLog.value
        
        val zone = ZoneId.systemDefault()
        val startStr = java.time.Instant.ofEpochMilli(startMillis)
            .atZone(zone)
            .toLocalDate()
            .format(dateFormat)
        val endStr = java.time.Instant.ofEpochMilli(endMillis)
            .atZone(zone)
            .toLocalDate()
            .format(dateFormat)
        
        return (allHistory + today).filter { log ->
            log.date >= startStr && log.date <= endStr
        }
    }
}

internal object FoodLogDayPolicy {
    fun needsReset(savedDate: String, today: String): Boolean = savedDate != today

    fun nextMidnightDelayMillis(now: ZonedDateTime): Long =
        Duration.between(now, now.toLocalDate().plusDays(1).atStartOfDay(now.zone))
            .toMillis()
            .coerceAtLeast(MIN_DATE_REFRESH_DELAY_MS)
}

private const val MIN_DATE_REFRESH_DELAY_MS = 1_000L
private const val MAX_DATE_REFRESH_DELAY_MS = 60 * 60 * 1_000L
