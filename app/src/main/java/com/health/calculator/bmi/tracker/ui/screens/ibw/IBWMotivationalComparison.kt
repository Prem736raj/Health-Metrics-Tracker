package com.health.calculator.bmi.tracker.ui.screens.ibw

import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.data.model.IBWResult
import com.health.calculator.bmi.tracker.ui.theme.HealthColors
import kotlin.math.abs

private data class MotivationalContent(
    val icon: ImageVector,
    val title: String,
    val message: String,
    val color: Color
)

@Composable
fun IBWMotivationalComparison(
    result: IBWResult,
    showInKg: Boolean
) {
    val factor = if (showInKg) 1.0 else 2.20462
    val unit = if (showInKg) "kg" else "lbs"
    val currentWeight = (result.currentWeightKg ?: return) * factor
    val idealWeight = result.frameAdjustedDevineKg * factor
    val bmiLower = result.bmiLowerKg * factor
    val bmiUpper = result.bmiUpperKg * factor
    val diff = currentWeight - idealWeight
    val absDiff = abs(diff)
    val isAbove = diff > 0.5
    val isBelow = diff < -0.5
    val isInRange = currentWeight in bmiLower..bmiUpper

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.txt_your_weight_journey),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Animated visual bar with 3 markers
            WeightRangeVisual(
                currentWeight = currentWeight,
                idealWeight = idealWeight,
                bmiLower = bmiLower,
                bmiUpper = bmiUpper,
                unit = unit
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Three weight circles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeightBubble(
                    label = "Current",
                    weight = "${"%.1f".format(currentWeight)}",
                    unit = unit,
                    color = MaterialTheme.colorScheme.primary,
                    icon = Icons.Default.Person
                )
                WeightBubble(
                    label = "Ideal",
                    weight = "${"%.1f".format(idealWeight)}",
                    unit = unit,
                    color = HealthColors.Healthy,
                    icon = Icons.Default.Star
                )
                WeightBubble(
                    label = "Range",
                    weight = "${"%.0f".format(bmiLower)}-${"%.0f".format(bmiUpper)}",
                    unit = unit,
                    color = HealthColors.Good,
                    icon = Icons.Default.FitnessCenter
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Motivational message
            MotivationalMessageCard(
                isInRange = isInRange,
                isAbove = isAbove,
                isBelow = isBelow,
                absDiff = absDiff,
                unit = unit
            )
        }
    }
}

@Composable
private fun WeightRangeVisual(
    currentWeight: Double,
    idealWeight: Double,
    bmiLower: Double,
    bmiUpper: Double,
    unit: String
) {
    val allValues = listOf(currentWeight, idealWeight, bmiLower, bmiUpper)
    val minVal = (allValues.minOrNull()!! * 0.9).toFloat()
    val maxVal = (allValues.maxOrNull()!! * 1.1).toFloat()
    val range = maxVal - minVal

    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1500, 300, easing = FastOutSlowInEasing),
        label = "rangeProgress"
    )

    val healthyColor = HealthColors.Healthy
    val currentColor = MaterialTheme.colorScheme.primary
    val idealColor = HealthColors.Caution
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val markerRingColor = MaterialTheme.colorScheme.surface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val barY = size.height * 0.45f
            val barHeight = 14.dp.toPx()
            val markerRadius = 10.dp.toPx()
            val padding = 24.dp.toPx()
            val barWidth = size.width - padding * 2

            // Track background
            drawRoundRect(
                color = trackColor,
                topLeft = Offset(padding, barY - barHeight / 2),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barHeight / 2)
            )

            // Healthy range zone
            val lowerX = padding + ((bmiLower.toFloat() - minVal) / range * barWidth)
            val upperX = padding + ((bmiUpper.toFloat() - minVal) / range * barWidth)
            val healthyWidth = (upperX - lowerX).coerceAtLeast(0f)

            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        healthyColor.copy(alpha = 0.2f),
                        healthyColor.copy(alpha = 0.35f),
                        healthyColor.copy(alpha = 0.2f)
                    ),
                    startX = lowerX,
                    endX = upperX
                ),
                topLeft = Offset(lowerX, barY - barHeight / 2),
                size = Size(healthyWidth * animatedProgress, barHeight),
                cornerRadius = CornerRadius(barHeight / 2)
            )

            // Ideal weight marker (diamond)
            val idealX = padding + ((idealWeight.toFloat() - minVal) / range * barWidth) * animatedProgress
            val diamondSize = 8.dp.toPx()
            val diamondPath = Path().apply {
                moveTo(idealX, barY - diamondSize)
                lineTo(idealX + diamondSize, barY)
                lineTo(idealX, barY + diamondSize)
                lineTo(idealX - diamondSize, barY)
                close()
            }
            drawPath(diamondPath, color = idealColor)
            drawPath(diamondPath, color = markerRingColor, style = Stroke(width = 2.dp.toPx()))

            // Current weight marker (circle with ring)
            val currentX = padding + ((currentWeight.toFloat() - minVal) / range * barWidth) * animatedProgress
            drawCircle(
                color = markerRingColor,
                radius = markerRadius + 2.dp.toPx(),
                center = Offset(currentX, barY)
            )
            drawCircle(
                color = currentColor,
                radius = markerRadius,
                center = Offset(currentX, barY)
            )
            drawCircle(
                color = markerRingColor,
                radius = markerRadius * 0.45f,
                center = Offset(currentX, barY)
            )
        }
    }

    // Labels below
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "${"%.0f".format(minVal)} $unit",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            fontSize = 9.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(healthyColor.copy(alpha = 0.4f))
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                stringResource(R.string.txt_healthy_range),
                style = MaterialTheme.typography.labelSmall,
                color = healthyColor,
                fontSize = 9.sp
            )
        }
        Text(
            "${"%.0f".format(maxVal)} $unit",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            fontSize = 9.sp
        )
    }
}

@Composable
private fun WeightBubble(
    label: String,
    weight: String,
    unit: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "bubbleScale"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = CircleShape,
            color = color.copy(alpha = 0.1f),
            modifier = Modifier.size((64 * scale).dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = weight,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = color
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            fontSize = 10.sp
        )
        Text(
            text = unit,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            fontSize = 8.sp
        )
    }
}

@Composable
private fun MotivationalMessageCard(
    isInRange: Boolean,
    isAbove: Boolean,
    isBelow: Boolean,
    absDiff: Double,
    unit: String
) {
    val content = when {
        isInRange && absDiff < 2 -> MotivationalContent(
            icon = Icons.Default.Star,
            title = "Amazing! You're at your ideal weight!",
            message = "You're right where you should be. Keep up your healthy habits and focus on maintaining this great balance!",
            color = HealthColors.Healthy
        )
        isInRange -> MotivationalContent(
            icon = Icons.Default.CheckCircle,
            title = "Great! You're within the healthy range",
            message = "You're within the healthy BMI weight range even if not at the exact formula result. This is a great place to be!",
            color = HealthColors.Healthy
        )
        isAbove && absDiff < 5 -> MotivationalContent(
            icon = Icons.Default.TrendingUp,
            title = "Almost there! Just ${"%.1f".format(absDiff)} $unit to go",
            message = "You're so close to your ideal range! Small, consistent changes in diet and activity can get you there. You've got this!",
            color = HealthColors.Good
        )
        isAbove && absDiff < 15 -> MotivationalContent(
            icon = Icons.Default.Flag,
            title = "A reachable goal ahead",
            message = "${"%.1f".format(absDiff)} $unit may seem like a lot, but at a healthy pace of 0.5 $unit/week, you could reach your ideal in about ${(absDiff / 0.5).toInt()} weeks. One step at a time!",
            color = HealthColors.Warning
        )
        isAbove -> MotivationalContent(
            icon = Icons.Default.TrackChanges,
            title = "Your journey starts here",
            message = "Every journey begins with a first step. Focus on small, sustainable changes. Even losing 5-10% of your current weight can significantly improve health markers.",
            color = HealthColors.Warning
        )
        isBelow && absDiff < 5 -> MotivationalContent(
            icon = Icons.Default.LocalDining,
            title = "Just a little more to go",
            message = "You're close to your ideal weight. Focus on nutritious, calorie-dense foods and strength training to reach your goal safely.",
            color = HealthColors.Good
        )
        else -> MotivationalContent(
            icon = Icons.Default.Restaurant,
            title = "Nourish your body",
            message = "Consider consulting a healthcare provider about a healthy weight gain plan. Focus on nutrient-dense foods and strength training.",
            color = HealthColors.Warning
        )
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = content.color.copy(alpha = 0.08f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = content.icon,
                contentDescription = null,
                tint = content.color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = content.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = content.color
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = content.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 18.sp
                )
            }
        }
    }
}
