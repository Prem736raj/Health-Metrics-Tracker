package com.health.calculator.bmi.tracker.ui.components.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp

/** Text alternative to a drawn chart. Rows use the same values as its series. */
@Composable
fun ChartReadings(
    title: String,
    rows: List<Pair<String, String>>,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable(title) { mutableStateOf(false) }
    if (rows.isEmpty()) return
    Column(modifier.fillMaxWidth()) {
        TextButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).semantics {
                stateDescription = if (expanded) "Expanded" else "Collapsed"
            },
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.labelLarge,
                color = DetailColors.Accent,
                modifier = Modifier.weight(1f)
            )
            Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null, tint = DetailColors.Accent)
        }
        if (expanded) {
            rows.forEach { (date, value) ->
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
                    .semantics(mergeDescendants = true) {}) {
                    Text(date, color = DetailColors.Secondary, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(4.dp))
                    Text(value, color = DetailColors.Text, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
