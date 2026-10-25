package com.health.calculator.bmi.tracker.ui.screens.heartrate

import com.health.calculator.bmi.tracker.ui.theme.AppColors
import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.ui.components.FitnessLevel
import com.health.calculator.bmi.tracker.ui.theme.HealthColors
import com.health.calculator.bmi.tracker.util.*

/**
 * Recommendation models retain legacy emoji labels for saved results and
 * generated copy. Keep those values backwards-compatible, but render a
 * stable Material icon so this screen is consistent across fonts and themes.
 */
private fun zoneRecommendationIcon(legacyLabel: String): ImageVector = when {
    legacyLabel.contains("⚖") -> Icons.Outlined.MonitorWeight
    legacyLabel.contains("❤️") -> Icons.Outlined.FavoriteBorder
    legacyLabel.contains("🏔") -> Icons.Outlined.Timeline
    legacyLabel.contains("🏆") -> Icons.Outlined.EmojiEvents
    legacyLabel.contains("🌱") -> Icons.Outlined.DirectionsWalk
    legacyLabel.contains("🚶") -> Icons.Outlined.DirectionsWalk
    legacyLabel.contains("🔥") -> Icons.Outlined.Whatshot
    legacyLabel.contains("💪") -> Icons.Outlined.FitnessCenter
    legacyLabel.contains("⚡") || legacyLabel.contains("🚀") -> Icons.Outlined.Speed
    legacyLabel.contains("🎯") -> Icons.Outlined.Flag
    legacyLabel.contains("⏱") -> Icons.Outlined.Schedule
    legacyLabel.contains("📅") -> Icons.Outlined.CalendarMonth
    legacyLabel.contains("📋") -> Icons.Outlined.Assignment
    legacyLabel.contains("💡") -> Icons.Outlined.Lightbulb
    legacyLabel.contains("✅") -> Icons.Outlined.CheckCircle
    legacyLabel.contains("⚠") -> Icons.Outlined.Warning
    else -> Icons.Outlined.FavoriteBorder
}

/** Remove legacy decorative markers from persisted recommendation copy. */
private fun cleanRecommendationCopy(text: String): String {
    val legacyMarkers = listOf(
        "⚖️", "⚖", "❤️", "🏔️", "🏔", "🏆", "🌱", "🚶", "🔥", "💪",
        "⚡", "🚀", "🎯", "⏱️", "⏱", "📅", "📋", "💡", "✅", "⚠️", "⚠"
    )
    return text
        .let { source -> legacyMarkers.fold(source) { cleaned, marker -> cleaned.replace(marker, "") } }
        .lines()
        .joinToString("\n") { it.trimStart() }
        .trim()
}

@Composable
fun ZoneRecommendationsSection(
    recommendations: List<WorkoutRecommendation>,
    calorieBurns: List<ZoneCalorieBurn>,
    fitnessLevel: FitnessLevel,
    onGoalSelected: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedGoalIndex by remember { mutableIntStateOf(-1) }
    val haptic = LocalHapticFeedback.current

    // Auto-select the recommended goal for user's fitness level
    LaunchedEffect(recommendations) {
        val recommended = recommendations.indexOfFirst { it.isRecommendedForLevel }
        if (recommended >= 0) selectedGoalIndex = recommended
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section header
        Text(
            text = stringResource(R.string.txt_training_recommendations),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = zoneRecommendationIcon(fitnessLevel.emoji),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Based on your fitness level: ${fitnessLevel.label}",
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            )
        }

        // Calorie Burn per Zone Card
        CaloriesBurnedCard(calorieBurns = calorieBurns)

        // Goal selector chips
        GoalSelectorChips(
            recommendations = recommendations,
            selectedIndex = selectedGoalIndex,
            onSelect = { index ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                selectedGoalIndex = if (selectedGoalIndex == index) -1 else index
                if (index in recommendations.indices) {
                    onGoalSelected(recommendations[index].goalName)
                }
            }
        )

        // Selected goal detail
        AnimatedVisibility(
            visible = selectedGoalIndex in recommendations.indices,
            enter = expandVertically(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            if (selectedGoalIndex in recommendations.indices) {
                GoalRecommendationCard(
                    recommendation = recommendations[selectedGoalIndex]
                )
            }
        }
    }
}

@Composable
private fun CaloriesBurnedCard(calorieBurns: List<ZoneCalorieBurn>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Whatshot,
                    contentDescription = null,
                    tint = HealthColors.Caution,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.txt_estimated_calories_burned_per_),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Horizontal bar chart for each zone
            calorieBurns.forEach { burn ->
                CalorieBurnBar(burn = burn, maxCalories = calorieBurns.maxOf { it.caloriesPer30Min })
                if (burn.zoneNumber < 5) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.txt_estimates_based_on_your_body_w),
                style = MaterialTheme.typography.labelSmall,
                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
            )
        }
    }
}

@Composable
private fun CalorieBurnBar(burn: ZoneCalorieBurn, maxCalories: Int) {
    val fraction = if (maxCalories > 0) burn.caloriesPer30Min.toFloat() / maxCalories else 0f
    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = burn.zoneNumber * 80,
            easing = FastOutSlowInEasing
        ),
        label = "calorie_bar_${burn.zoneNumber}"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Zone label
        Row(
            modifier = Modifier.width(72.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = zoneRecommendationIcon(burn.icon),
                contentDescription = null,
                tint = burn.color,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Z${burn.zoneNumber}",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = AppColors.text(burn.color)
            )
        }

        // Bar
        Box(
            modifier = Modifier
                .weight(1f)
                .height(22.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(6.dp))
                    .background(burn.color.copy(alpha = 0.08f))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedFraction)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(6.dp))
                    .background(burn.color.copy(alpha = 0.6f)),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (animatedFraction > 0.25f) {
                    Text(
                        text = "${burn.caloriesPer30Min} cal",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.text(Color.White),
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
            }
            if (animatedFraction <= 0.25f) {
                Text(
                    text = "${burn.caloriesPer30Min} cal",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.text(burn.color),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = (animatedFraction * 200 + 8).dp)
                )
            }
        }
    }
}

@Composable
private fun GoalSelectorChips(
    recommendations: List<WorkoutRecommendation>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // First row: 3 chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            recommendations.take(3).forEachIndexed { index, rec ->
                GoalChip(
                    legacyIcon = rec.goalEmoji,
                    label = rec.goalName,
                    isSelected = selectedIndex == index,
                    isRecommended = rec.isRecommendedForLevel,
                    onClick = { onSelect(index) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Second row: 2 chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            recommendations.drop(3).forEachIndexed { index, rec ->
                val actualIndex = index + 3
                GoalChip(
                    legacyIcon = rec.goalEmoji,
                    label = rec.goalName,
                    isSelected = selectedIndex == actualIndex,
                    isRecommended = rec.isRecommendedForLevel,
                    onClick = { onSelect(actualIndex) },
                    modifier = Modifier.weight(1f)
                )
            }
            // Spacer to balance the row if odd number
            if (recommendations.size - 3 < 2) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun GoalChip(
    legacyIcon: String,
    label: String,
    isSelected: Boolean,
    isRecommended: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.primaryContainer
            isRecommended -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        },
        animationSpec = tween(200),
        label = "chip_color_$label"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.primary
            isRecommended -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            else -> Color.Transparent
        },
        animationSpec = tween(200),
        label = "chip_border_$label"
    )

    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.surface(containerColor)),
        border = if (borderColor != Color.Transparent)
            androidx.compose.foundation.BorderStroke(1.5.dp, borderColor) else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = zoneRecommendationIcon(legacyIcon),
                contentDescription = null,
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            if (isRecommended) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.txt_for_you),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = AppColors.text(MaterialTheme.colorScheme.secondary),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun GoalRecommendationCard(
    recommendation: WorkoutRecommendation
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = zoneRecommendationIcon(recommendation.goalEmoji),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = recommendation.goalName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = recommendation.goalDescription,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    )
                }
            }

            // Recommended tag
            if (recommendation.isRecommendedForLevel) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = HealthColors.Healthy.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = HealthColors.Healthy,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.txt_recommended_for_your_fitness_l),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppColors.text(HealthColors.Healthy)
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

            // Zone Distribution Donut
            ZoneDistributionChart(distribution = recommendation.zoneDistribution)

            // Primary Zone
            InfoRow(
                icon = Icons.Outlined.Flag,
                title = "Primary Zone",
                content = recommendation.primaryZone
            )

            // Duration & Frequency
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CompactInfoCard(
                    icon = Icons.Outlined.Schedule,
                    title = "Duration",
                    value = recommendation.durationRange,
                    modifier = Modifier.weight(1f)
                )
                CompactInfoCard(
                    icon = Icons.Outlined.CalendarMonth,
                    title = "Frequency",
                    value = recommendation.frequencyPerWeek,
                    modifier = Modifier.weight(1f)
                )
            }

            // Key Advice
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.surface(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = recommendation.keyAdvice,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)),
                        lineHeight = 18.sp
                    )
                }
            }

            // Sample Workout
            var showWorkout by remember { mutableStateOf(false) }
            ExpandableSection(
                title = "Sample Workout",
                icon = Icons.Outlined.Assignment,
                isExpanded = showWorkout,
                onToggle = { showWorkout = !showWorkout }
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    )
                ) {
                    Text(
                        text = cleanRecommendationCopy(recommendation.sampleWorkout),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(14.dp),
                        lineHeight = 20.sp,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                    )
                }
            }

            // Tips
            var showTips by remember { mutableStateOf(false) }
            ExpandableSection(
                title = "Tips (${recommendation.tips.size})",
                icon = Icons.Outlined.CheckCircle,
                isExpanded = showTips,
                onToggle = { showTips = !showTips }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    recommendation.tips.forEach { tip ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(end = 8.dp, top = 1.dp)
                                    .size(16.dp)
                            )
                            Text(
                                text = cleanRecommendationCopy(tip),
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)),
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ZoneDistributionChart(distribution: List<ZoneDistribution>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Donut chart
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(110.dp)
        ) {
            val totalPercent = distribution.sumOf { it.percentage }.toFloat()

            Canvas(modifier = Modifier.size(110.dp)) {
                val strokeWidth = 18.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2
                val topLeft = Offset(
                    (size.width - 2 * radius) / 2,
                    (size.height - 2 * radius) / 2
                )
                val arcSize = Size(radius * 2, radius * 2)
                val gap = 2f
                var startAngle = -90f

                if (totalPercent > 0f) {
                    distribution.forEach { zone ->
                        val sweep = (zone.percentage / totalPercent) * 360f
                        drawArc(
                            color = zone.color,
                            startAngle = startAngle + gap / 2,
                            sweepAngle = (sweep - gap).coerceAtLeast(0f),
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                        )
                        startAngle += sweep
                    }
                }
            }

            Text(
                text = if (totalPercent > 0f) "Zone\nSplit" else "No zone\ndata",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                lineHeight = 14.sp
            )
        }

        // Legend
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            distribution.forEach { zone ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(zone.color)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = zoneRecommendationIcon(zone.icon),
                        contentDescription = null,
                        tint = zone.color,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = zone.zoneName,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${zone.percentage}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.text(zone.color)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, title: String, content: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun CompactInfoCard(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ExpandableSection(
    title: String,
    icon: ImageVector,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable { onToggle() }
                .heightIn(min = 48.dp)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess
                else Icons.Default.ExpandMore,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.size(22.dp)
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(modifier = Modifier.padding(top = 4.dp)) {
                content()
            }
        }
    }
}
