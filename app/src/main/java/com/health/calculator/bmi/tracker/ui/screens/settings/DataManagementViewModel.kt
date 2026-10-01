package com.health.calculator.bmi.tracker.ui.screens.settings

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.health.calculator.bmi.tracker.data.local.AppDatabase
import com.health.calculator.bmi.tracker.data.management.*
import com.health.calculator.bmi.tracker.data.model.CalculatorType
import com.health.calculator.bmi.tracker.data.model.HistoryDisplayEntry
import com.health.calculator.bmi.tracker.data.model.toDisplayEntry
import com.health.calculator.bmi.tracker.data.repository.HistoryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException

@HiltViewModel
class DataManagementViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val historyRepository = HistoryRepository(database.historyDao())
    private val storageAnalyzer = StorageAnalyzer.getInstance(application)
    private val integrityChecker = DataIntegrityChecker.getInstance()
    private val cleanupManager = DataCleanupManager.getInstance(
        application,
        historyRepository,
        storageAnalyzer,
        integrityChecker
    )

    private val _uiState = MutableStateFlow(DataManagementState())
    val uiState: StateFlow<DataManagementState> = _uiState.asStateFlow()

    private var allEntries: List<HistoryDisplayEntry> = emptyList()

    init {
        loadStorageInfo()
        loadEntries()
    }

    private fun loadStorageInfo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingStorage = true) }
            try {
                val info = storageAnalyzer.analyzeStorage()
                _uiState.update { it.copy(storageInfo = info) }
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                _uiState.update { it.copy(snackbarMessage = "Storage information is unavailable. Please try again.") }
            } finally {
                _uiState.update { it.copy(isLoadingStorage = false) }
            }
        }
    }

    private fun loadEntries() {
        viewModelScope.launch {
            historyRepository.getAllEntries().collect { rawEntries ->
                allEntries = rawEntries.map { with(historyRepository) { it.toDisplayEntry() } }
            }
        }
    }

    // === CLEANUP BY AGE ===

    fun showCleanupByAge() {
        _uiState.update { it.copy(showCleanupByAge = true, selectedCleanupAge = null, cleanupPreview = null) }
    }

    fun dismissCleanupByAge() {
        _uiState.update { it.copy(showCleanupByAge = false, selectedCleanupAge = null, cleanupPreview = null) }
    }

    fun previewCleanupByAge(age: CleanupAge) {
        _uiState.update { it.copy(selectedCleanupAge = age) }
        viewModelScope.launch {
            val preview = cleanupManager.previewCleanupByAge(allEntries, age)
            _uiState.update { it.copy(cleanupPreview = preview) }
        }
    }

    fun executeCleanupByAge() {
        if (_uiState.value.isClearing) return
        val age = _uiState.value.selectedCleanupAge ?: return
        _uiState.update { it.copy(isClearing = true) }

        viewModelScope.launch {
            try {
                val deleted = cleanupManager.cleanupByAge(allEntries, age)
                _uiState.update {
                    it.copy(
                        showCleanupByAge = false,
                        snackbarMessage = "$deleted entries deleted"
                    )
                }
                loadStorageInfo()
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                _uiState.update { it.copy(snackbarMessage = "Cleanup failed. No history entries were deleted.") }
            } finally {
                _uiState.update { it.copy(isClearing = false) }
            }
        }
    }

    // === CLEANUP BY CALCULATOR ===

    fun showCleanupByCalculator() {
        _uiState.update { it.copy(showCleanupByCalculator = true, selectedCleanupTypes = emptySet()) }
    }

    fun dismissCleanupByCalculator() {
        _uiState.update { it.copy(showCleanupByCalculator = false, selectedCleanupTypes = emptySet()) }
    }

    fun toggleCleanupType(type: CalculatorType) {
        _uiState.update { state ->
            val newTypes = if (type in state.selectedCleanupTypes)
                state.selectedCleanupTypes - type
            else
                state.selectedCleanupTypes + type
            state.copy(selectedCleanupTypes = newTypes)
        }
    }

    fun executeCleanupByCalculator() {
        if (_uiState.value.isClearing) return
        val types = _uiState.value.selectedCleanupTypes
        if (types.isEmpty()) return

        _uiState.update { it.copy(isClearing = true) }

        viewModelScope.launch {
            try {
                val deleted = cleanupManager.cleanupByCalculator(allEntries, types)
                _uiState.update {
                    it.copy(
                        showCleanupByCalculator = false,
                        snackbarMessage = "$deleted entries from ${types.size} calculator(s) deleted"
                    )
                }
                loadStorageInfo()
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                _uiState.update { it.copy(snackbarMessage = "Cleanup failed. No history entries were deleted.") }
            } finally {
                _uiState.update { it.copy(isClearing = false) }
            }
        }
    }

    // === CACHE CLEANUP ===

    fun clearCache() {
        viewModelScope.launch {
            try {
                val freed = storageAnalyzer.clearCache()
                _uiState.update {
                    it.copy(snackbarMessage = "${StorageInfo.formatBytes(freed)} cache cleared")
                }
                loadStorageInfo()
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                _uiState.update { it.copy(snackbarMessage = "Cache cleanup could not finish. Please try again.") }
            }
        }
    }

    fun clearExports() {
        viewModelScope.launch {
            try {
                val freed = storageAnalyzer.clearExports()
                _uiState.update {
                    it.copy(snackbarMessage = "${StorageInfo.formatBytes(freed)} exports cleared")
                }
                loadStorageInfo()
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                _uiState.update { it.copy(snackbarMessage = "Export cleanup could not finish. Please try again.") }
            }
        }
    }

    // === DATA INTEGRITY ===

    fun runIntegrityCheck() {
        _uiState.update {
            it.copy(integrityReport = IntegrityReport(isChecking = true))
        }

        viewModelScope.launch {
            delay(500) // Brief delay for UX
            val report = integrityChecker.checkIntegrity(allEntries)
            _uiState.update { it.copy(integrityReport = report) }
        }
    }

    // === DELETE EVERYTHING ===

    fun showDeleteEverything() {
        _uiState.update {
            it.copy(
                showDeleteEverything = true,
                deleteEverythingStep = 0,
                deleteConfirmText = ""
            )
        }
    }

    fun dismissDeleteEverything() {
        _uiState.update {
            it.copy(
                showDeleteEverything = false,
                deleteEverythingStep = 0,
                deleteConfirmText = ""
            )
        }
    }

    fun nextDeleteStep() {
        _uiState.update { it.copy(deleteEverythingStep = it.deleteEverythingStep + 1) }
    }

    fun updateDeleteConfirmText(text: String) {
        _uiState.update { it.copy(deleteConfirmText = text) }
    }

    fun executeDeleteEverything() {
        if (_uiState.value.deleteConfirmText.uppercase() != "DELETE") return

        _uiState.update { it.copy(isDeleting = true) }

        viewModelScope.launch {
            val requested = cleanupManager.deleteEverything()
            if (!requested) {
                _uiState.update {
                    it.copy(
                        isDeleting = false,
                        snackbarMessage = "Android could not start the full app-data reset. Please try again."
                    )
                }
            }
            // On success Android clears the app's entire private data set and normally
            // terminates the process before further UI state can be persisted.
        }
    }

    // === UNDO DELETE ===

    fun undoDelete() {
        val undoable = _uiState.value.undoableDelete ?: return

        viewModelScope.launch {
            val restored = cleanupManager.restoreEntry(undoable.entryData)
            _uiState.update {
                it.copy(
                    undoableDelete = null,
                    snackbarMessage = if (restored) "Entry restored" else "Failed to restore"
                )
            }
        }
    }

    fun clearUndo() {
        _uiState.update { it.copy(undoableDelete = null) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}

