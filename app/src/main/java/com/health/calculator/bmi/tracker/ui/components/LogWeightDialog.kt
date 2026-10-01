package com.health.calculator.bmi.tracker.ui.components

import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R
import com.health.calculator.bmi.tracker.domain.tracking.WeightInputPolicy

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogWeightDialog(
    weightInput: String,
    noteInput: String,
    dateMillis: Long,
    useMetric: Boolean,
    isSaving: Boolean,
    onWeightChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onDateClick: () -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    isEditing: Boolean = false
) {
    val unit = if (useMetric) "kg" else "lbs"
    val dateFmt = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
    val isToday = remember(dateMillis) {
        val cal1 = Calendar.getInstance().apply { timeInMillis = dateMillis }
        val cal2 = Calendar.getInstance()
        cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    val isValid = WeightInputPolicy.kilograms(weightInput, useMetric) != null
    val keyboard = LocalSoftwareKeyboardController.current
    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val submit = {
        if (isValid && !isSaving) {
            keyboard?.hide()
            onSave()
        }
    }

    AlertDialog(
        modifier = Modifier.imePadding(),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEditing) stringResource(R.string.weight_edit_title) else stringResource(R.string.txt_log_weight),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = if (isKeyboardVisible) 160.dp else 440.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Weight input
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = onWeightChange,
                    label = { Text(stringResource(R.string.weight_input_label, unit)) },
                    leadingIcon = {
                        Icon(Icons.Outlined.MonitorWeight, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { submit() }),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    isError = weightInput.isNotEmpty() && !isValid,
                    supportingText = {
                        if (weightInput.isNotEmpty() && !isValid) {
                            Text(stringResource(R.string.txt_enter_a_valid_weight_20_350))
                        }
                    }
                )

                // Date selector
                OutlinedCard(
                    onClick = onDateClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.CalendarMonth,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isToday) "Today" else dateFmt.format(Date(dateMillis)),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            if (isToday) {
                                Text(
                                    text = dateFmt.format(Date(dateMillis)),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Note (optional)
                OutlinedTextField(
                    value = noteInput,
                    onValueChange = onNoteChange,
                    label = { Text(stringResource(R.string.txt_note_optional)) },
                    placeholder = { Text(stringResource(R.string.txt_e_g_morning_weigh_in)) },
                    leadingIcon = {
                        Icon(Icons.Outlined.Notes, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Tips
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = stringResource(R.string.txt_text_placeholder_1), modifier = Modifier.padding(end = 8.dp))
                        Text(
                            text = stringResource(R.string.txt_weigh_yourself_in_the_morning_),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = submit,
                enabled = isValid && !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(stringResource(if (isSaving) R.string.weight_saving else if (isEditing) R.string.weight_update else R.string.txt_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.txt_cancel)) }
        }
    )
}
