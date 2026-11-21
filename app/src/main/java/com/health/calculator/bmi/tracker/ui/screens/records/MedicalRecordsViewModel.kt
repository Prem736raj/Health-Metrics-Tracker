package com.health.calculator.bmi.tracker.ui.screens.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.calculator.bmi.tracker.data.local.AppDatabase
import com.health.calculator.bmi.tracker.data.records.MedicalRecord
import com.health.calculator.bmi.tracker.data.records.MedicalRecordKind
import com.health.calculator.bmi.tracker.data.records.normalize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject

internal data class MedicalRecordsState(
    val records: List<MedicalRecord> = emptyList(),
    val loading: Boolean = true,
    val loadFailed: Boolean = false,
    val kind: MedicalRecordKind? = null,
    val draft: MedicalRecord? = null,
    val editing: Boolean = false,
    val busy: Boolean = false,
    val error: String? = null,
    val notice: String? = null
)

@HiltViewModel
internal class MedicalRecordsViewModel @Inject constructor(database: AppDatabase) : ViewModel() {
    private val dao = database.medicalRecordDao()
    private val _state = MutableStateFlow(MedicalRecordsState())
    val state = _state.asStateFlow()
    private var observer: Job? = null

    init { load() }

    fun load() {
        observer?.cancel()
        _state.update { it.copy(loading = true, loadFailed = false) }
        observer = viewModelScope.launch {
            try {
                dao.observe().collect { records -> _state.update { it.copy(records = records, loading = false) } }
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _state.update { it.copy(loading = false, loadFailed = true) }
            }
        }
    }

    fun open(kind: MedicalRecordKind) {
        _state.update { it.copy(kind = kind, notice = null) }
    }

    fun back() {
        _state.update { current ->
            when {
                current.busy -> current
                current.draft != null -> current.copy(draft = null, editing = false, error = null)
                else -> current.copy(kind = null, notice = null)
            }
        }
    }

    fun add() {
        val kind = _state.value.kind ?: return
        val draft = MedicalRecord(
            id = UUID.randomUUID().toString(),
            kind = kind.name,
            timestamp = Instant.now().truncatedTo(ChronoUnit.MINUTES).toEpochMilli(),
            value = "",
            unit = kind.units.firstOrNull().orEmpty(),
            context = if (kind == MedicalRecordKind.MEDICATION) kind.contexts.first() else "",
            detail = "",
            note = ""
        )
        _state.update { it.copy(draft = draft, editing = false, error = null, notice = null) }
    }

    fun edit(record: MedicalRecord) {
        _state.update { it.copy(draft = record, editing = true, error = null, notice = null) }
    }

    fun change(draft: MedicalRecord) {
        _state.update { if (it.busy) it else it.copy(draft = draft, error = null) }
    }

    fun save() {
        val current = _state.value
        if (current.busy) return
        val draft = current.draft ?: return
        val kind = current.kind ?: return
        val now = System.currentTimeMillis()
        val record = kind.normalize(draft, now)
        if (record == null) {
            val message = when {
                draft.timestamp > now && kind.isMeasurement -> "Choose a measurement time that has already passed."
                draft.value.isBlank() -> "Enter the ${kind.valueLabel.lowercase()}."
                kind == MedicalRecordKind.OXYGEN -> "Enter oxygen saturation from 0 to 100. Pulse must be a positive whole number."
                kind.isMeasurement -> "Check the number and its unit."
                else -> "Check the title and shorten any long text."
            }
            _state.update { it.copy(error = message) }
            return
        }
        perform("Record saved", "Couldn’t save the record. Try again.") {
            if (current.editing) check(dao.update(record) == 1) else dao.insert(record)
        }
    }

    fun delete() {
        val current = _state.value
        if (current.busy || !current.editing) return
        val draft = current.draft ?: return
        perform("Record deleted", "Couldn’t delete the record. Try again.") { dao.delete(draft.id) }
    }

    private fun perform(success: String, failure: String, operation: suspend () -> Unit) {
        _state.update { it.copy(busy = true, error = null) }
        viewModelScope.launch {
            try {
                operation()
                _state.update { it.copy(draft = null, editing = false, busy = false, notice = success) }
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _state.update { it.copy(busy = false, error = failure) }
            }
        }
    }
}
