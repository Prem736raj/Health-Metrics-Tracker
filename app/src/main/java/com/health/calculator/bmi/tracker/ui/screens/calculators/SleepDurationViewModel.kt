package com.health.calculator.bmi.tracker.ui.screens.calculators

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.calculator.bmi.tracker.data.calculator.SleepEstimate
import com.health.calculator.bmi.tracker.data.model.CalculatorType
import com.health.calculator.bmi.tracker.data.model.HistoryEntry
import com.health.calculator.bmi.tracker.data.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

internal data class SleepSaveState(
    val saving: Boolean = false,
    val savedEstimate: SleepEstimate? = null,
    val failedEstimate: SleepEstimate? = null
)

@HiltViewModel
internal class SleepDurationViewModel @Inject constructor(
    private val history: HistoryRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SleepSaveState())
    val state = _state.asStateFlow()

    fun save(estimate: SleepEstimate) {
        if (_state.value.saving || _state.value.savedEstimate == estimate) return
        _state.value = SleepSaveState(saving = true)
        viewModelScope.launch {
            try {
                history.addEntryOnce(HistoryEntry(
                    calculatorKey = CalculatorType.SLEEP.key,
                    resultValue = estimate.sleepMinutes.toString(),
                    resultLabel = "min",
                    category = "Self-reported estimate",
                    timestamp = estimate.wake.toEpochMilli(),
                    detailsJson = JSONObject().apply {
                        put("Start time (UTC)", estimate.start.toString())
                        put("Time window (min)", estimate.intervalMinutes)
                        put("Time to fall asleep (min)", estimate.settlingMinutes)
                        put("Time awake after falling asleep (min)", estimate.awakeMinutes)
                    }.toString()
                ))
                _state.value = SleepSaveState(savedEstimate = estimate)
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _state.value = SleepSaveState(failedEstimate = estimate)
            }
        }
    }
}
