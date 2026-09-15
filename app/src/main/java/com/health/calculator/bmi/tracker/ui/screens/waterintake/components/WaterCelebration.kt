// ui/screens/waterintake/components/WaterCelebration.kt
package com.health.calculator.bmi.tracker.ui.screens.waterintake.components

import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.util.WaterShareHelper
import com.health.calculator.bmi.tracker.ui.theme.ChartColors
import com.health.calculator.bmi.tracker.ui.theme.FeatureColors
import com.health.calculator.bmi.tracker.ui.theme.HealthColors
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun WaterGoalCelebration(
    currentMl: Int,
    goalMl: Int,
    streakDays: Int,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val percentage = if (goalMl > 0) (currentMl.toFloat() / goalMl * 100).coerceAtMost(200f) else 0f

    // Haptic feedback on appear
    LaunchedEffect(Unit) {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        delay(300)
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    // Auto dismiss after 6 seconds
    LaunchedEffect(Unit) {
        delay(6000)
        onDismiss()
    }

    // Card scale animation
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_scale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "celebration")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        // Confetti particles
        ConfettiAnimation()

        // Floating water drops
        FloatingWaterDrops(infiniteTransition)

        // Main celebration card
        Card(
            modifier = Modifier
                .padding(32.dp)
                .scale(scale),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Pulsing trophy
                val trophyScale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(600, easing = EaseInOutSine),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "trophy_pulse"
                )

                Icon(
                    imageVector = Icons.Outlined.EmojiEvents,
                    contentDescription = "Hydration goal achieved",
                    tint = HealthColors.Healthy,
                    modifier = Modifier
                        .size(52.dp)
                        .scale(trophyScale)
                )

                Text(
                    if (percentage >= 150) "INCREDIBLE!" else "GOAL ACHIEVED!",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    color = HealthColors.Healthy
                )

                // Stats
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CelebrationStat(Icons.Outlined.WaterDrop, "${String.format("%.1f", currentMl / 1000f)}L", "Drank", FeatureColors.WaterDeep)
                    CelebrationStat(Icons.Outlined.ShowChart, "${percentage.toInt()}%", "Progress", ChartColors.Primary)
                    if (streakDays > 0) {
                        CelebrationStat(Icons.Outlined.LocalFireDepartment, "$streakDays", "Streak", HealthColors.Warning)
                    }
                }

                // Motivational message
                val message = when {
                    percentage >= 150 -> "Excellent hydration progress."
                    percentage >= 125 -> "You went beyond today's target."
                    streakDays >= 30 -> "A month of consistent check-ins."
                    streakDays >= 7 -> "A full week of consistent check-ins."
                    else -> "A useful check-in for your day."
                }

                Text(
                    message,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                // Action buttons
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(stringResource(R.string.txt_continue), fontSize = 14.sp)
                    }

                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            WaterShareHelper.shareWaterAchievement(
                                context = context,
                                currentMl = currentMl,
                                goalMl = goalMl,
                                streakDays = streakDays,
                                percentage = percentage
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HealthColors.Healthy)
                    ) {
                        Text(stringResource(R.string.txt_share_1), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun CelebrationStat(icon: ImageVector, value: String, label: String, tint: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(
            label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun ConfettiAnimation() {
    val particles = remember {
        (0 until 30).map {
            ConfettiParticle(
                x = Random.nextFloat(),
                startY = Random.nextFloat() * -0.5f,
                speedY = 0.5f + Random.nextFloat() * 0.5f,
                color = listOf(
                    ChartColors.Secondary,
                    ChartColors.Tertiary,
                    ChartColors.Accent1,
                    ChartColors.Accent2,
                    ChartColors.Accent3,
                    FeatureColors.WaterEnd,
                    FeatureColors.AiEnd
                ).random(),
                size = 8f + Random.nextFloat() * 12f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confetti_time"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val currentY = (particle.startY + time * particle.speedY * 1.5f) % 1.5f - 0.25f
            val x = particle.x + kotlin.math.sin(currentY * kotlin.math.PI * 2).toFloat() * 0.05f

            drawCircle(
                color = particle.color,
                radius = particle.size,
                center = Offset(x * size.width, currentY * size.height)
            )
        }
    }
}

private data class ConfettiParticle(
    val x: Float,
    val startY: Float,
    val speedY: Float,
    val color: Color,
    val size: Float
)

@Composable
private fun FloatingWaterDrops(infiniteTransition: InfiniteTransition) {
    val dropIcons = listOf(
        Icons.Outlined.WaterDrop,
        Icons.Outlined.WaterDrop,
        Icons.Outlined.WaterDrop,
        Icons.Outlined.WaterDrop
    )

    dropIcons.forEachIndexed { index, icon ->
        val offsetY by infiniteTransition.animateFloat(
            initialValue = 600f,
            targetValue = -100f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2500 + index * 300,
                    delayMillis = index * 200,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "drop_$index"
        )

        val offsetX = ((index * 80 + 50) % 300) - 150

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = FeatureColors.WaterEnd.copy(alpha = 0.65f),
            modifier = Modifier
                .size(28.dp)
                .offset(x = offsetX.dp, y = offsetY.dp)
        )
    }
}
