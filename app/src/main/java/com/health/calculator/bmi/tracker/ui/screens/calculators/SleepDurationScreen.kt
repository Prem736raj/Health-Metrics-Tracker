package com.health.calculator.bmi.tracker.ui.screens.calculators

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.health.calculator.bmi.tracker.data.calculator.SleepCalculation
import com.health.calculator.bmi.tracker.data.calculator.calculateSleep
import com.health.calculator.bmi.tracker.data.calculator.formatSleepDuration
import com.health.calculator.bmi.tracker.ui.components.detail.DetailFeature
import com.health.calculator.bmi.tracker.ui.components.detail.DetailScreenTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SleepDurationScreen(
    onBack: () -> Unit,
    viewModel: SleepDurationViewModel = hiltViewModel()
) = DetailScreenTheme(DetailFeature.EXPORT) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val zone = ZoneId.systemDefault()
    var wakeDay by rememberSaveable { mutableLongStateOf(LocalDate.now(zone).toEpochDay()) }
    var startClock by rememberSaveable { mutableStateOf("") }
    var wakeClock by rememberSaveable { mutableStateOf("") }
    var settling by rememberSaveable { mutableStateOf("") }
    var awake by rememberSaveable { mutableStateOf("") }
    var showDate by rememberSaveable { mutableStateOf(false) }
    var calculatedAt by rememberSaveable { mutableLongStateOf(0L) }
    val wakeDate = LocalDate.ofEpochDay(wakeDay)
    val dateFormat = remember { DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM) }
    val result = if (calculatedAt != 0L) {
        calculateSleep(startClock, wakeClock, wakeDate, settling, awake, zone, Instant.ofEpochMilli(calculatedAt))
    } else null

    if (showDate) {
        val picker = rememberDatePickerState(initialSelectedDateMillis = wakeDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
        DatePickerDialog(
            onDismissRequest = { showDate = false },
            confirmButton = {
                TextButton(enabled = picker.selectedDateMillis != null, onClick = {
                    picker.selectedDateMillis?.let {
                        wakeDay = Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC).toLocalDate().toEpochDay()
                        calculatedAt = 0L
                    }
                    showDate = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDate = false }) { Text("Cancel") } }
        ) { DatePicker(picker) }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Sleep duration") }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
        })
    }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Estimate how long you slept from the times you remember.", style = MaterialTheme.typography.bodyLarge)
            OutlinedButton(onClick = { showDate = true }, enabled = !state.saving, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.CalendarMonth, null)
                Spacer(Modifier.width(8.dp))
                Text("Wake date  ·  ${wakeDate.format(dateFormat)}")
            }
            SleepInput("Tried to sleep (24-hour time)", startClock, !state.saving, "Example: 22:30") {
                startClock = it
                calculatedAt = 0L
            }
            SleepInput("Final wake-up (24-hour time)", wakeClock, !state.saving, "Example: 06:30") {
                wakeClock = it
                calculatedAt = 0L
            }
            Text("If the start time is later on the clock, it uses the previous date.",
                style = MaterialTheme.typography.bodySmall)
            SleepInput("Minutes to fall asleep", settling, !state.saving, "Optional", KeyboardType.Number) {
                settling = it
                calculatedAt = 0L
            }
            SleepInput("Minutes awake after falling asleep", awake, !state.saving, "Optional", KeyboardType.Number) {
                awake = it
                calculatedAt = 0L
            }
            Button(onClick = { calculatedAt = System.currentTimeMillis() },
                enabled = !state.saving, modifier = Modifier.fillMaxWidth()) { Text("Calculate sleep") }
            when (result) {
                is SleepCalculation.Valid -> {
                    val estimate = result.estimate
                    val saved = state.savedEstimate == estimate
                    val startDate = estimate.start.atZone(zone).toLocalDate()
                    ElevatedCard(Modifier.fillMaxWidth(), colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )) {
                        Column(Modifier.padding(16.dp).semantics { liveRegion = LiveRegionMode.Polite },
                            verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Estimated sleep", style = MaterialTheme.typography.labelLarge)
                            Text(formatSleepDuration(estimate.sleepMinutes), style = MaterialTheme.typography.headlineMedium)
                            Text("Time window  ·  ${formatSleepDuration(estimate.intervalMinutes)}")
                            Text("Start date  ·  ${startDate.format(dateFormat)}")
                            if (settling.isBlank() || awake.isBlank()) Text("Blank minute fields count as zero.",
                                style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    if (state.failedEstimate == estimate) Text("Couldn’t save your sleep record. Try again.",
                        color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.save(estimate) }, enabled = !state.saving && !saved,
                        modifier = Modifier.fillMaxWidth()) {
                        Text(when {
                            saved -> "Saved to history"
                            state.saving -> "Saving…"
                            else -> "Save sleep record"
                        })
                    }
                }
                is SleepCalculation.Error -> Text(sleepErrorMessage(result), color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                null -> Unit
            }
        }
    }
}

@Composable
private fun SleepInput(label: String, value: String, enabled: Boolean, hint: String,
    keyboard: KeyboardType = KeyboardType.Text, onChange: (String) -> Unit
) {
    OutlinedTextField(value = value, onValueChange = { if (it.length <= 10) onChange(it) },
        modifier = Modifier.fillMaxWidth(), label = { Text(label) }, placeholder = { Text(hint) },
        singleLine = true, enabled = enabled, keyboardOptions = KeyboardOptions(keyboardType = keyboard))
}

private fun sleepErrorMessage(error: SleepCalculation.Error) = when (error) {
    SleepCalculation.Error.INVALID_TIMES -> "Enter both times in 24-hour format, such as 22:30 or 06:30."
    SleepCalculation.Error.SAME_TIME -> "Enter different start and wake times."
    SleepCalculation.Error.INVALID_MINUTES -> "Enter whole minutes of zero or more, or leave the field blank."
    SleepCalculation.Error.CLOCK_CHANGE -> "One of these times was repeated or skipped during a clock change. Check the times and wake date."
    SleepCalculation.Error.FUTURE_WAKE -> "The wake time is in the future. Check the wake date and time."
    SleepCalculation.Error.TOO_MANY_AWAKE_MINUTES -> "Minutes spent falling asleep and awake must fit between the two times."
}
