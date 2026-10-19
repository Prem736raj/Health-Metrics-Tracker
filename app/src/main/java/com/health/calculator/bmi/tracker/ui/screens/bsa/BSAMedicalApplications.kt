package com.health.calculator.bmi.tracker.ui.screens.bsa

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** General reference only: deliberately accepts no user measurement or dose rate. */
@Composable
fun BSAMedicalApplicationsSection(modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("BSA in Clinical Context", style = MaterialTheme.typography.titleMedium)
                Text("General education only. This app does not calculate medicine doses or assess organ function.", style = MaterialTheme.typography.bodySmall)
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse BSA context" else "Expand BSA context"
                )
            }
        }
        AnimatedVisibility(visible = expanded) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.padding(12.dp)) {
                val topics = listOf(
                    "Medication protocols" to "Some clinical protocols use BSA as one input. The specific medicine, protocol, organ function and other clinical information also matter. Only the treating team can determine a dose. Do not start, stop or change medicine using an app estimate.",
                    "Burn assessment" to "Clinicians may describe the proportion of the body affected by burns. This differs from the height-and-weight BSA estimate shown here. This app cannot assess a burn, determine severity or plan fluids. Seek appropriate medical care for a burn.",
                    "Kidney-function reports" to "Some laboratory reports index kidney-function estimates to a reference body size. Interpretation depends on the test, units and clinical context. BSA alone cannot measure kidney function or diagnose kidney disease.",
                    "Heart-function reports" to "Some clinical measurements are indexed to body size. They require a separately measured value and professional interpretation. A BSA estimate cannot determine cardiac output or show that heart function is normal."
                )
                topics.forEach { (title, explanation) ->
                    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text(explanation, style = MaterialTheme.typography.bodyMedium)
                }
                Text("For a possible medical emergency, contact local emergency services. Do not use this calculator for urgent decisions.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
