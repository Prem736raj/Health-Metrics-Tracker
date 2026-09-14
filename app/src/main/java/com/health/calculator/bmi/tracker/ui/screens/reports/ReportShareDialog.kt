// ui/screens/reports/ReportShareDialog.kt
package com.health.calculator.bmi.tracker.ui.screens.reports

import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ReportShareDialog(
    includeWeight: Boolean,
    includeBmi: Boolean,
    includeBp: Boolean,
    includeWater: Boolean,
    includeCalories: Boolean,
    includeExercise: Boolean,
    includeScore: Boolean,
    canShare: Boolean,
    onToggleSection: (String, Boolean) -> Unit,
    onShare: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Share, null, modifier = Modifier.size(24.dp)) },
        title = { Text(stringResource(R.string.txt_share_report), fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(R.string.txt_choose_what_to_include), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    "Only selected sections are included. Sharing is optional.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                ShareToggle(Icons.Outlined.Assessment, stringResource(R.string.txt_overall_health_score), includeScore) { onToggleSection("score", it) }
                ShareToggle(Icons.Outlined.MonitorWeight, stringResource(R.string.txt_weight), includeWeight) { onToggleSection("weight", it) }
                ShareToggle(Icons.Outlined.Assessment, stringResource(R.string.txt_bmi), includeBmi) { onToggleSection("bmi", it) }
                ShareToggle(Icons.Outlined.MonitorHeart, stringResource(R.string.txt_blood_pressure), includeBp) { onToggleSection("bp", it) }
                ShareToggle(Icons.Outlined.WaterDrop, stringResource(R.string.txt_daily_water_intake), includeWater) { onToggleSection("water", it) }
                ShareToggle(Icons.Outlined.Assessment, stringResource(R.string.txt_calories), includeCalories) { onToggleSection("calories", it) }
                ShareToggle(Icons.Outlined.DirectionsWalk, stringResource(R.string.txt_exercise), includeExercise) { onToggleSection("exercise", it) }
            }
        },
        confirmButton = {
            Button(onClick = onShare, enabled = canShare) {
                Icon(Icons.Default.Share, null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(stringResource(R.string.txt_share))
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.txt_cancel)) } }
    )
}

@Composable
private fun ShareToggle(icon: ImageVector, label: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(label, style = MaterialTheme.typography.bodyMedium)
        }
        Switch(checked = checked, onCheckedChange = onToggle)
    }
}
