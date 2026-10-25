package com.health.calculator.bmi.tracker.ui.screens.heartrate

import com.health.calculator.bmi.tracker.ui.theme.AppColors
import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.ui.theme.HealthColors

/** Convert legacy guide markers to stable icons without changing saved copy. */
private fun restingHeartRateIcon(legacyLabel: String): ImageVector = when {
    legacyLabel.contains("❤️") -> Icons.Outlined.FavoriteBorder
    legacyLabel.contains("🧘") -> Icons.Outlined.FavoriteBorder
    legacyLabel.contains("✋") || legacyLabel.contains("👆") -> Icons.Outlined.Info
    legacyLabel.contains("⏱") -> Icons.Outlined.Schedule
    legacyLabel.contains("📝") -> Icons.Outlined.Assignment
    legacyLabel.contains("💡") -> Icons.Outlined.Lightbulb
    else -> Icons.Outlined.MonitorHeart
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestingHRGuideSheet(
    onDismiss: () -> Unit,
    onUseBPReading: () -> Unit,
    lastRestingHR: Int?
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            // Title
            Text(
                text = stringResource(R.string.txt_how_to_measure_resting_heart_r),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Best time note
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.surface(HealthColors.Caution.copy(alpha = 0.08f))
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = HealthColors.Caution,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.txt_best_time_to_measure),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppColors.text(HealthColors.Caution)
                        )
                        Text(
                            text = stringResource(R.string.txt_first_thing_in_the_morning_bef),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Steps
            Text(
                text = stringResource(R.string.txt_step_by_step_guide),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            val steps = listOf(
                Triple("1", "🧘", "Sit still and relax for at least 5 minutes. Take slow, calm breaths."),
                Triple("2", "✋", "Place your index and middle fingers on your wrist (thumb side) or on the side of your neck, just below the jawline."),
                Triple("3", "👆", "Press gently until you feel a steady pulse. Don't press too hard."),
                Triple("4", "⏱️", "Count the number of beats for 60 seconds using a clock or timer. This count is your resting heart rate in BPM."),
                Triple("5", "📝", "For best accuracy, measure on 3 different mornings and use the average.")
            )

            steps.forEach { (number, legacyIcon, text) ->
                GuideStepItem(
                    stepNumber = number,
                    legacyIcon = legacyIcon,
                    text = text
                )
                if (number != "5") {
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Alternative: Shortcut
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.surface(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.txt_quick_alternative),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.txt_count_beats_for_15_seconds_and),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                    )
                }
            }

            Text(
                text = "These are general reference bands, not a diagnosis. Resting heart rate can vary with medicines, illness, stress, sleep and training; compare readings taken under similar conditions.",
                style = MaterialTheme.typography.labelSmall,
                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)),
                lineHeight = 15.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Normal ranges reference
            Text(
                text = stringResource(R.string.txt_normal_resting_heart_rate_rang),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val ranges = listOf(
                Triple("Often seen in trained adults", "40-60 BPM", HealthColors.Healthy),
                Triple("Lower reference band", "60-70 BPM", HealthColors.Healthy),
                Triple("Typical reference band", "70-80 BPM", HealthColors.Warning),
                Triple("Higher reference band", "80-90 BPM", HealthColors.Caution),
                Triple("Above reference band", "90-100 BPM", HealthColors.Caution),
                Triple("Discuss if unexpected", ">100 BPM", HealthColors.Danger)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ranges.forEach { (label, range, color) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = range,
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Use BP checker option
            if (lastRestingHR != null) {
                OutlinedButton(
                    onClick = onUseBPReading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Use pulse from BP Checker ($lastRestingHR BPM)")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(stringResource(R.string.txt_got_it_2), fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun GuideStepItem(
    stepNumber: String,
    legacyIcon: String,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Icon(
                imageVector = restingHeartRateIcon(legacyIcon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)),
                lineHeight = 18.sp
            )
        }
    }
}
