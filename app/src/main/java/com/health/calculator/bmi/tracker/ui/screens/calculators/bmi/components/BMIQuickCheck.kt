package com.health.calculator.bmi.tracker.ui.screens.calculators.bmi.components

import com.health.calculator.bmi.tracker.ui.components.detail.DetailColors as AppColors
import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BMIQuickCheckCard(
    profileName: String?,
    profileWeightKg: Float,
    profileHeightCm: Float,
    profileAge: Int,
    profileIsMale: Boolean,
    isUnitKg: Boolean,
    isUnitCm: Boolean,
    onQuickCheck: () -> Unit,
    modifier: Modifier = Modifier
) {


    val hasProfileData = profileWeightKg > 0f && profileHeightCm > 0f && profileAge > 0

    AnimatedVisibility(
        visible = hasProfileData,
        enter = fadeIn(tween(400)) + expandVertically(tween(400)),
        exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(AppColors.SurfaceAlt, AppColors.SurfaceAlt)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.txt_quick_check),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.Text
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        val name = profileName?.takeIf { it.isNotBlank() }
                        val greeting = if (name != null) "Hi $name!" else "Profile data ready!"
                        Text(
                            text = greeting,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.Secondary
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        // Show profile values summary
                        val weightText = if (isUnitKg) String.format("%.1f kg", profileWeightKg)
                        else String.format("%.1f lbs", profileWeightKg * 2.20462f)
                        val heightText = if (isUnitCm) "${profileHeightCm.toInt()} cm"
                        else {
                            val totalIn = profileHeightCm / 2.54
                            "${(totalIn / 12).toInt()}ft ${(totalIn % 12).toInt()}in"
                        }
                        Text(
                            text = "$weightText • $heightText • Age ${profileAge}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.Secondary
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Pulsing check button
                    QuickCheckButton(onClick = onQuickCheck)
                }
            }
        }
    }
}

@Composable
private fun QuickCheckButton(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "quickPulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(contentAlignment = Alignment.Center) {
        // Glow ring
        Box(
            modifier = Modifier
                .size((56 * scale).dp)
                .clip(CircleShape)
                .background(AppColors.Accent.copy(alpha = glowAlpha * 0.3f))
        )

        Button(
            onClick = onClick,
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.Accent,
                contentColor = AppColors.OnAccent
            ),
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 1.dp
            )
        ) {
            Icon(
                Icons.Filled.PlayArrow,
                contentDescription = "Check My BMI",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

// ============================================================
// Last Used Input Memory Card
// ============================================================
@Composable
fun LastUsedInputCard(
    weightKg: Float,
    heightCm: Float,
    age: Int,
    isMale: Boolean,
    isUnitKg: Boolean,
    isUnitCm: Boolean,
    timestamp: Long,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timeAgo = getTimeAgoText(timestamp)
    val weightText = if (isUnitKg) String.format("%.1f kg", weightKg)
    else String.format("%.1f lbs", weightKg * 2.20462f)
    val heightText = if (isUnitCm) "${heightCm.toInt()} cm"
    else {
        val totalIn = heightCm / 2.54
        "${(totalIn / 12).toInt()}ft ${(totalIn % 12).toInt()}in"
    }
    val genderText = if (isMale) "Male" else "Female"

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Outlined.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Last used ($timeAgo)",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$weightText  •  $heightText  •  Age $age  •  $genderText",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            FilledTonalButton(onClick = onApply, contentPadding = PaddingValues(horizontal = 12.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    contentColor = AppColors.OnAccent
                )) {
                Text(stringResource(R.string.txt_use))
            }
        }
    }
}

internal fun getTimeAgoText(timestamp: Long, now: Long = System.currentTimeMillis()): String {
    if (timestamp <= 0L) return ""
    val diff = (now - timestamp).coerceAtLeast(0L)
    val minutes = diff / (1000 * 60)
    val hours = minutes / 60
    val days = hours / 24

    return when {
        minutes < 1 -> "just now"
        minutes < 60 -> "${minutes}m ago"
        hours < 24 -> "${hours}h ago"
        days < 7 -> "${days}d ago"
        days < 30 -> "${days / 7}w ago"
        else -> "${days / 30}mo ago"
    }
}
