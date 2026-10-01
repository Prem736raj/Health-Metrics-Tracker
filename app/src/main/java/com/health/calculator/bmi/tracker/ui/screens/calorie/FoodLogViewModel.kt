package com.health.calculator.bmi.tracker.ui.screens.calorie

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.app.Application
import com.health.calculator.bmi.tracker.R
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.health.calculator.bmi.tracker.data.model.DailyFoodLog
import com.health.calculator.bmi.tracker.data.model.FoodEntry
import com.health.calculator.bmi.tracker.data.model.FoodPreset
import com.health.calculator.bmi.tracker.data.repository.FoodLogRepository
import com.health.calculator.bmi.tracker.domain.usecase.CalorieHistoryAnalyticsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class FoodLogUiState(
    val todayLog: DailyFoodLog? = null,
    val customPresets: List<FoodPreset> = emptyList(),
    val showAddFoodDialog: Boolean = false,
    val showAddPresetDialog: Boolean = false,
    val showQuickAdd: Boolean = false,
    // Add food form
    val newFoodName: String = "",
    val newFoodCalories: String = "",
    val newFoodProtein: String = "",
    val newFoodCarbs: String = "",
    val newFoodFat: String = "",
    val newFoodMealSlot: String = "Other",
    val newFoodServingSize: String = "",
    val showMacroFields: Boolean = false,
    val foodStorageReadOnly: Boolean = false,
    // Custom preset form
    val presetName: String = "",
    val presetCalories: String = "",
    val presetProtein: String = "",
    val presetCarbs: String = "",
    val presetFat: String = "",
    val presetServing: String = "",
    val presetEmoji: String = "🍽️",
    // Error
    val errorMessage: String? = null
)

@HiltViewModel
class FoodLogViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val repository = (application as com.health.calculator.bmi.tracker.HealthCalculatorApp).foodLogRepository

    private val _uiState = MutableStateFlow(FoodLogUiState())
    val uiState: StateFlow<FoodLogUiState> = _uiState.asStateFlow()

    val defaultPresets get() = repository.defaultPresets
    private val analytics = CalorieHistoryAnalyticsUseCase()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { repository.getHistoricalLogs() }
            repository.checkAndResetIfNewDay()
            repository.todayLog.collectLatest { log ->
                _uiState.value = _uiState.value.copy(todayLog = log)
            }
        }
        viewModelScope.launch {
            repository.customPresets.collectLatest { presets ->
                _uiState.value = _uiState.value.copy(customPresets = presets)
            }
        }
        viewModelScope.launch {
            repository.isReadOnly.collectLatest { readOnly ->
                _uiState.value = _uiState.value.copy(foodStorageReadOnly = readOnly)
            }
        }
    }

    private fun showStorageError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = getApplication<Application>().getString(R.string.food_storage_read_only)
        )
    }

    fun setTargetCalories(calories: Double, protein: Double = 0.0, carbs: Double = 0.0, fat: Double = 0.0) {
        if (repository.updateDailyTargets(calories, protein, carbs, fat)) {
            repository.saveTargetCalories(calories)
        } else {
            showStorageError()
        }
    }

    fun showAddFoodDialog() {
        if (repository.isReadOnly.value) { showStorageError(); return }
        _uiState.value = _uiState.value.copy(
            showAddFoodDialog = true,
            newFoodName = "", newFoodCalories = "",
            newFoodProtein = "", newFoodCarbs = "", newFoodFat = "",
            newFoodMealSlot = getCurrentMealSlot(),
            newFoodServingSize = "", showMacroFields = false, errorMessage = null
        )
    }

    fun hideAddFoodDialog() {
        _uiState.value = _uiState.value.copy(showAddFoodDialog = false)
    }

    fun showAddPresetDialog() {
        if (repository.isReadOnly.value) { showStorageError(); return }
        _uiState.value = _uiState.value.copy(
            showAddPresetDialog = true,
            presetName = "", presetCalories = "", presetProtein = "",
            presetCarbs = "", presetFat = "", presetServing = "", presetEmoji = "🍽️"
        )
    }

    fun hideAddPresetDialog() {
        _uiState.value = _uiState.value.copy(showAddPresetDialog = false)
    }

    fun toggleQuickAdd() {
        _uiState.value = _uiState.value.copy(showQuickAdd = !_uiState.value.showQuickAdd)
    }

    // Food form updates
    fun updateFoodName(v: String) { _uiState.value = _uiState.value.copy(newFoodName = v, errorMessage = null) }
    fun updateFoodCalories(v: String) { _uiState.value = _uiState.value.copy(newFoodCalories = v, errorMessage = null) }
    fun updateFoodProtein(v: String) { _uiState.value = _uiState.value.copy(newFoodProtein = v) }
    fun updateFoodCarbs(v: String) { _uiState.value = _uiState.value.copy(newFoodCarbs = v) }
    fun updateFoodFat(v: String) { _uiState.value = _uiState.value.copy(newFoodFat = v) }
    fun updateFoodMealSlot(v: String) { _uiState.value = _uiState.value.copy(newFoodMealSlot = v) }
    fun updateFoodServingSize(v: String) { _uiState.value = _uiState.value.copy(newFoodServingSize = v) }
    fun toggleMacroFields() { _uiState.value = _uiState.value.copy(showMacroFields = !_uiState.value.showMacroFields) }

    // Preset form updates
    fun updatePresetName(v: String) { _uiState.value = _uiState.value.copy(presetName = v) }
    fun updatePresetCalories(v: String) { _uiState.value = _uiState.value.copy(presetCalories = v) }
    fun updatePresetProtein(v: String) { _uiState.value = _uiState.value.copy(presetProtein = v) }
    fun updatePresetCarbs(v: String) { _uiState.value = _uiState.value.copy(presetCarbs = v) }
    fun updatePresetFat(v: String) { _uiState.value = _uiState.value.copy(presetFat = v) }
    fun updatePresetServing(v: String) { _uiState.value = _uiState.value.copy(presetServing = v) }
    fun updatePresetEmoji(v: String) { _uiState.value = _uiState.value.copy(presetEmoji = v) }

    fun addFoodFromForm() {
        val s = _uiState.value
        val name = s.newFoodName.trim()
        if (name.isBlank()) { _uiState.value = s.copy(errorMessage = "Please enter a food name"); return }
        val calories = s.newFoodCalories.toDoubleOrNull()
        if (calories == null || !calories.isFinite() || calories <= 0) { _uiState.value = s.copy(errorMessage = "Please enter valid calories"); return }
        val macros = listOf(s.newFoodProtein, s.newFoodCarbs, s.newFoodFat).map { FoodInputPolicy.parseMacro(it) }
        if (macros.any { it == null }) {
            _uiState.value = s.copy(errorMessage = "Enter non-negative numbers for macros, or leave them blank")
            return
        }

        val entry = FoodEntry(
            name = name,
            calories = calories,
            proteinGrams = requireNotNull(macros[0]),
            carbGrams = requireNotNull(macros[1]),
            fatGrams = requireNotNull(macros[2]),
            mealSlot = s.newFoodMealSlot,
            servingSize = s.newFoodServingSize,
            isPreset = false
        )
        if (!repository.addEntry(entry)) { showStorageError(); return }
        hideAddFoodDialog()
    }

    fun addFoodFromPreset(preset: FoodPreset) {
        val entry = FoodEntry(
            name = preset.name,
            calories = preset.calories,
            proteinGrams = preset.proteinGrams,
            carbGrams = preset.carbGrams,
            fatGrams = preset.fatGrams,
            mealSlot = getCurrentMealSlot(),
            servingSize = preset.servingSize,
            isPreset = true
        )
        if (!repository.addEntry(entry)) showStorageError()
    }

    fun removeEntry(entryId: Long) {
        if (!repository.removeEntry(entryId)) showStorageError()
    }

    fun saveCustomPreset() {
        val s = _uiState.value
        val name = s.presetName.trim()
        if (name.isBlank()) return
        val calories = s.presetCalories.toDoubleOrNull() ?: return
        val macros = listOf(s.presetProtein, s.presetCarbs, s.presetFat).map { FoodInputPolicy.parseMacro(it) }
        if (!calories.isFinite() || calories <= 0 || macros.any { it == null }) {
            _uiState.value = s.copy(errorMessage = "Enter positive calories and non-negative macros")
            return
        }

        val preset = FoodPreset(
            id = "custom_${System.currentTimeMillis()}",
            name = name,
            calories = calories,
            proteinGrams = requireNotNull(macros[0]),
            carbGrams = requireNotNull(macros[1]),
            fatGrams = requireNotNull(macros[2]),
            servingSize = s.presetServing,
            emoji = s.presetEmoji,
            isCustom = true
        )
        if (!repository.addCustomPreset(preset)) { showStorageError(); return }
        hideAddPresetDialog()
    }

    fun removeCustomPreset(id: String) {
        if (!repository.removeCustomPreset(id)) showStorageError()
    }

    private fun getCurrentMealSlot(): String {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..10 -> "Breakfast"
            in 11..14 -> "Lunch"
            in 15..17 -> "Snack"
            in 18..22 -> "Dinner"
            else -> "Other"
        }
    }

    fun getHistoricalLogs(): List<DailyFoodLog> = repository.getHistoricalLogs()

    fun getStats(targetCal: Double, targetP: Double = 0.0, targetC: Double = 0.0, targetF: Double = 0.0) =
        analytics.computeStats(
            logs = repository.getHistoricalLogs() + listOfNotNull(_uiState.value.todayLog),
            targetCalories = targetCal,
            targetProtein = targetP,
            targetCarbs = targetC,
            targetFat = targetF
        )

    fun getWeeklySummaries() =
        analytics.getWeeklySummaries(repository.getHistoricalLogs() + listOfNotNull(_uiState.value.todayLog))
}
