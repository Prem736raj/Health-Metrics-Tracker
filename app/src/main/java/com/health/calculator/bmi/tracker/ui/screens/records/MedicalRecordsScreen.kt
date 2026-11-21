package com.health.calculator.bmi.tracker.ui.screens.records

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.DeviceThermostat
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.health.calculator.bmi.tracker.data.records.MedicalRecord
import com.health.calculator.bmi.tracker.data.records.MedicalRecordKind
import com.health.calculator.bmi.tracker.ui.components.TimePickerDialog
import com.health.calculator.bmi.tracker.ui.components.detail.DetailFeature
import com.health.calculator.bmi.tracker.ui.components.detail.DetailScreenTheme
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MedicalRecordsScreen(onBack: () -> Unit, onOpenReport: () -> Unit,
    viewModel: MedicalRecordsViewModel = hiltViewModel()
) = DetailScreenTheme(DetailFeature.REPORT) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val kind = state.kind
    val draft = state.draft
    BackHandler(enabled = kind != null, onBack = viewModel::back)
    val title = when {
        draft != null && kind != null -> "${if (state.editing) "Edit" else "Add"} ${kind.singular}"
        kind != null -> kind.title
        else -> "Medical records"
    }
    Scaffold(topBar = {
        TopAppBar(title = { Text(title) }, navigationIcon = {
            IconButton(onClick = if (kind == null) onBack else viewModel::back, enabled = !state.busy) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
        })
    }) { padding ->
        val content = Modifier.fillMaxSize().padding(padding)
        when {
            state.loading -> LinearProgressIndicator(Modifier.padding(padding).fillMaxWidth())
            state.loadFailed -> Column(content.padding(16.dp)) {
                Text("Couldn’t load your records.")
                TextButton(onClick = viewModel::load) { Text("Retry") }
            }
            draft != null && kind != null -> MedicalRecordEditor(content, kind, draft, state, viewModel)
            kind != null -> MedicalRecordList(content, kind, state, viewModel)
            else -> MedicalRecordDirectory(content, state.records, viewModel::open, onOpenReport)
        }
    }
}

@Composable
private fun MedicalRecordDirectory(modifier: Modifier, records: List<MedicalRecord>,
    onOpen: (MedicalRecordKind) -> Unit, onOpenReport: () -> Unit
) {
    LazyColumn(modifier, contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Readings, medications and visits in one place.",
                style = MaterialTheme.typography.titleLarge)
        }
        items(MedicalRecordKind.entries, key = { it.name }) { kind ->
            OutlinedCard(onClick = { onOpen(kind) }, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    val icon = when (kind) {
                        MedicalRecordKind.GLUCOSE -> Icons.Outlined.WaterDrop
                        MedicalRecordKind.TEMPERATURE -> Icons.Outlined.DeviceThermostat
                        MedicalRecordKind.OXYGEN -> Icons.Outlined.MonitorHeart
                        MedicalRecordKind.MEDICATION -> Icons.Outlined.Medication
                        MedicalRecordKind.VISIT -> Icons.Outlined.Description
                    }
                    Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(kind.title, style = MaterialTheme.typography.titleMedium)
                        Text(kind.description, style = MaterialTheme.typography.bodyMedium)
                        val count = records.count { it.kind == kind.name }
                        if (count > 0) Text(if (count == 1) "1 record" else "$count records",
                            style = MaterialTheme.typography.labelMedium)
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
                }
            }
        }
        item {
            OutlinedButton(onClick = onOpenReport, modifier = Modifier.fillMaxWidth()) {
                Text("Open health report")
            }
        }
    }
}

@Composable
private fun MedicalRecordList(modifier: Modifier, kind: MedicalRecordKind, state: MedicalRecordsState,
    viewModel: MedicalRecordsViewModel
) {
    var query by rememberSaveable(kind.name) { mutableStateOf("") }
    val records = state.records.filter { it.kind == kind.name }
    val visible = records.filter { record ->
        listOf(record.value, record.unit, record.context, record.detail, record.note, medicalRecordDate(record.timestamp))
            .any { it.contains(query.trim(), ignoreCase = true) }
    }
    LazyColumn(modifier, contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(kind.description)
                Button(onClick = viewModel::add, modifier = Modifier.fillMaxWidth()) { Text("Add ${kind.singular}") }
                state.notice?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
                if (records.isNotEmpty()) OutlinedTextField(query, { query = it },
                    modifier = Modifier.fillMaxWidth(), label = { Text("Search records") }, singleLine = true)
            }
        }
        if (visible.isEmpty()) {
            item { Text(if (records.isEmpty()) "No records yet." else "No matching records.") }
        }
        items(visible, key = { it.id }) { record ->
            OutlinedCard(onClick = { viewModel.edit(record) }, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(kind.reportResult(record), style = MaterialTheme.typography.titleMedium)
                    Text(medicalRecordDate(record.timestamp), style = MaterialTheme.typography.labelMedium)
                    val context = kind.reportContext(record)
                    if (context.isNotEmpty()) Text(context, style = MaterialTheme.typography.bodyMedium)
                    if (!kind.isMeasurement && record.detail.isNotBlank()) Text(record.detail,
                        maxLines = 2, overflow = TextOverflow.Ellipsis)
                    if (record.note.isNotBlank()) Text(record.note, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MedicalRecordEditor(modifier: Modifier, kind: MedicalRecordKind, draft: MedicalRecord,
    state: MedicalRecordsState, viewModel: MedicalRecordsViewModel
) {
    val focus = LocalFocusManager.current
    var selectDate by rememberSaveable(draft.id) { mutableStateOf(false) }
    var selectTime by rememberSaveable(draft.id) { mutableStateOf(false) }
    var confirmDelete by rememberSaveable(draft.id) { mutableStateOf(false) }
    val zone = ZoneId.systemDefault()
    val dateTime = Instant.ofEpochMilli(draft.timestamp).atZone(zone)
    if (selectDate) {
        val utcDay = dateTime.toLocalDate().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        val picker = rememberDatePickerState(initialSelectedDateMillis = utcDay)
        DatePickerDialog(
            onDismissRequest = { selectDate = false },
            confirmButton = {
                TextButton(enabled = picker.selectedDateMillis != null, onClick = {
                    picker.selectedDateMillis?.let {
                        val date = Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC).toLocalDate()
                        val timestamp = date.atTime(dateTime.toLocalTime()).atZone(zone).toInstant().toEpochMilli()
                        viewModel.change(draft.copy(timestamp = timestamp))
                    }
                    selectDate = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { selectDate = false }) { Text("Cancel") } }
        ) { DatePicker(picker) }
    }
    if (selectTime) {
        val picker = rememberTimePickerState(initialHour = dateTime.hour, initialMinute = dateTime.minute, is24Hour = true)
        TimePickerDialog(state = picker, onConfirm = {
            val timestamp = dateTime.toLocalDate().atTime(LocalTime.of(picker.hour, picker.minute))
                .atZone(zone).toInstant().toEpochMilli()
            viewModel.change(draft.copy(timestamp = timestamp))
        }, onDismiss = { selectTime = false })
    }
    if (confirmDelete) {
        AlertDialog(onDismissRequest = { confirmDelete = false }, title = { Text("Delete this record?") },
            text = { Text("This permanently removes the saved record from your medical records.") },
            confirmButton = {
                TextButton(onClick = { confirmDelete = false; viewModel.delete() }) { Text("Delete") }
            },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Cancel") } })
    }
    Column(modifier.imePadding().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(draft.value, { if (it.length <= 100) viewModel.change(draft.copy(value = it)) },
            modifier = Modifier.fillMaxWidth(), enabled = !state.busy, label = { Text(kind.valueLabel) }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = if (kind.isMeasurement) KeyboardType.Decimal else KeyboardType.Text))
        if (kind.units.size > 1) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                kind.units.forEach { unit ->
                    FilterChip(draft.unit == unit, { viewModel.change(draft.copy(unit = unit)) },
                        enabled = !state.busy, label = { Text(unit) })
                }
            }
        }
        if (kind.contexts.isNotEmpty()) {
            Text(kind.contextLabel, style = MaterialTheme.typography.labelLarge)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                kind.contexts.forEach { entry ->
                    FilterChip(draft.context == entry, {
                        val context = if (draft.context == entry && kind.isMeasurement) "" else entry
                        viewModel.change(draft.copy(context = context))
                    }, enabled = !state.busy, label = { Text(entry) })
                }
            }
        }
        if (kind.detailLabel.isNotBlank()) {
            OutlinedTextField(draft.detail, { if (it.length <= 250) viewModel.change(draft.copy(detail = it)) },
                modifier = Modifier.fillMaxWidth(), enabled = !state.busy, label = { Text(kind.detailLabel) },
                singleLine = kind == MedicalRecordKind.OXYGEN,
                keyboardOptions = KeyboardOptions(keyboardType = if (kind == MedicalRecordKind.OXYGEN) KeyboardType.Number else KeyboardType.Text))
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(enabled = !state.busy, onClick = { focus.clearFocus(); selectDate = true }) {
                Text("${kind.dateLabel} · ${dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))}")
            }
            OutlinedButton(enabled = !state.busy, onClick = { focus.clearFocus(); selectTime = true }) {
                Text("Time · ${dateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))}")
            }
        }
        OutlinedTextField(draft.note, { if (it.length <= 2_000) viewModel.change(draft.copy(note = it)) },
            modifier = Modifier.fillMaxWidth(), enabled = !state.busy, minLines = 3, maxLines = 6,
            label = { Text(if (kind == MedicalRecordKind.VISIT) "Questions, notes and follow-up" else "Private notes (optional)") })
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(onClick = { focus.clearFocus(); viewModel.save() }, enabled = !state.busy, modifier = Modifier.fillMaxWidth()) {
            Text(if (state.busy) "Saving…" else "Save record")
        }
        if (state.editing) {
            TextButton(onClick = { confirmDelete = true }, enabled = !state.busy) { Text("Delete record") }
        }
    }
}

private val medicalRecordDateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
private fun medicalRecordDate(timestamp: Long): String =
    Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).format(medicalRecordDateFormatter)
