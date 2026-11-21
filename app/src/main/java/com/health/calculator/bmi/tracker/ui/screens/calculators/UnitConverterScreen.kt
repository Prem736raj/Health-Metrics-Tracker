package com.health.calculator.bmi.tracker.ui.screens.calculators

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.health.calculator.bmi.tracker.data.calculator.MeasurementUnits
import com.health.calculator.bmi.tracker.data.calculator.parseMeasurement
import com.health.calculator.bmi.tracker.ui.components.detail.DetailFeature
import com.health.calculator.bmi.tracker.ui.components.detail.DetailScreenTheme
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun UnitConverterScreen(onBack: () -> Unit) = DetailScreenTheme(DetailFeature.EXPORT) {
    var units by rememberSaveable { mutableStateOf(MeasurementUnits.WEIGHT) }
    var fromMetric by rememberSaveable { mutableStateOf(true) }
    var input by rememberSaveable { mutableStateOf("") }
    val locale = LocalConfiguration.current.locales[0]
    val source = if (fromMetric) units.metric else units.customary
    val target = if (fromMetric) units.customary else units.metric
    val result = parseMeasurement(input, locale)?.let { units.convert(it, fromMetric) }?.takeIf { it.isFinite() }
    Scaffold(topBar = {
        TopAppBar(title = { Text("Unit converter") }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
        })
    }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MeasurementUnits.entries.forEach { type ->
                    FilterChip(selected = units == type, onClick = {
                        units = type
                        input = ""
                    }, label = { Text(type.title) })
                }
            }
            OutlinedTextField(
                value = input,
                onValueChange = { if (it.length <= 32) input = it },
                label = { Text("Measurement ($source)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = input.isNotBlank() && result == null,
                supportingText = if (input.isNotBlank() && result == null) {
                    { Text("Enter a valid nonnegative number.") }
                } else null,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedButton(onClick = { fromMetric = !fromMetric }) {
                Icon(Icons.Outlined.SwapHoriz, null)
                Spacer(Modifier.width(8.dp))
                Text("Swap units")
            }
            if (result != null) {
                val format = NumberFormat.getNumberInstance(locale).apply { maximumFractionDigits = 5 }
                val value = if (result > 0 && result < 0.00001) "< ${format.format(0.00001)}" else format.format(result)
                Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Converted measurement", style = MaterialTheme.typography.labelLarge)
                        Text("$value $target", style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }
        }
    }
}
