package com.health.calculator.bmi.tracker.ui.screens.heartrate

import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.ui.theme.FeatureColors
import com.health.calculator.bmi.tracker.ui.theme.HealthColors
import com.health.calculator.bmi.tracker.util.*

private fun categoryIcon(category: ActivityCategory): ImageVector = when (category) {
    ActivityCategory.DAILY -> Icons.Outlined.Home
    ActivityCategory.CARDIO -> Icons.Outlined.DirectionsRun
    ActivityCategory.STRENGTH -> Icons.Outlined.FitnessCenter
    ActivityCategory.SPORTS -> Icons.Outlined.Flag
}

/**
 * ActivityReference keeps its emoji field for persisted/backwards-compatible
 * data, but the shipped UI uses one consistent vector icon family. Matching on
 * the stable activity name means old saved data still renders correctly.
 */
private fun activityIcon(activity: ActivityReference): ImageVector = when {
    activity.name.contains("sleep", ignoreCase = true) -> Icons.Outlined.FavoriteBorder
    activity.name.contains("walk", ignoreCase = true) -> Icons.Outlined.DirectionsWalk
    activity.name.contains("run", ignoreCase = true) ||
            activity.name.contains("jog", ignoreCase = true) ||
            activity.name.contains("sprint", ignoreCase = true) -> Icons.Outlined.DirectionsRun
    activity.name.contains("cycle", ignoreCase = true) -> Icons.Outlined.DirectionsRun
    activity.name.contains("weight", ignoreCase = true) ||
            activity.name.contains("training", ignoreCase = true) ||
            activity.name.contains("yoga", ignoreCase = true) ||
            activity.name.contains("pilates", ignoreCase = true) ||
            activity.name.contains("stretch", ignoreCase = true) ||
            activity.name.contains("elliptical", ignoreCase = true) ||
            activity.name.contains("climber", ignoreCase = true) -> Icons.Outlined.FitnessCenter
    activity.name.contains("heart", ignoreCase = true) -> Icons.Outlined.MonitorHeart
    activity.category == ActivityCategory.SPORTS -> Icons.Outlined.Flag
    else -> Icons.Outlined.Assessment
}

// ============================================================
// MAIN ACTIVITY REFERENCE SECTION
// ============================================================

@Composable
fun ActivityReferenceSection(
    zones: List<HeartRateZone>,
    weightKg: Float,
    restingHR: Int? = null,
    modifier: Modifier = Modifier
) {
    val personalizedActivities = remember(zones, weightKg, restingHR) {
        ActivityHeartRateReferenceEngine.personalizeActivities(
            zones = zones,
            weightKg = weightKg,
            restingHR = restingHR
        )
    }

    var selectedCategory by remember {
        mutableStateOf<ActivityCategory?>(null)
    }
    var searchQuery by remember { mutableStateOf("") }
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.DirectionsRun,
                contentDescription = null,
                tint = FeatureColors.HeartDeep,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = stringResource(R.string.txt_activity_heart_rate_guide),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.txt_personalized_bpm_ranges_for_co),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.txt_search_activities)) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Clear",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
        )

        // Category filter chips
        CategoryFilterChips(
            selectedCategory = selectedCategory,
            onCategorySelect = { cat ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                selectedCategory = if (selectedCategory == cat) null else cat
            }
        )

        // Activity cards by category
        val filteredActivities = personalizedActivities
            .filterKeys { selectedCategory == null || it == selectedCategory }
            .mapValues { (_, activities) ->
                if (searchQuery.isBlank()) activities
                else activities.filter {
                    it.activity.name.contains(searchQuery, ignoreCase = true) ||
                            it.activity.description.contains(searchQuery, ignoreCase = true) ||
                            it.activity.zoneLabel.contains(searchQuery, ignoreCase = true)
                }
            }
            .filterValues { it.isNotEmpty() }

        if (filteredActivities.isEmpty()) {
            EmptySearchResult(query = searchQuery)
        } else {
            filteredActivities.forEach { (category, activities) ->
                CategorySection(
                    category = category,
                    activities = activities,
                    zones = zones
                )
            }
        }

        // Note
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = HealthColors.Info,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.txt_heart_rate_ranges_are_personal) +
                            "Actual heart rate during activities varies based on fitness level, technique, " +
                            "environment, and individual physiology. Calorie estimates are approximate.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    lineHeight = 15.sp
                )
            }
        }
    }
}

// ============================================================
// CATEGORY FILTER CHIPS
// ============================================================

@Composable
private fun CategoryFilterChips(
    selectedCategory: ActivityCategory?,
    onCategorySelect: (ActivityCategory) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActivityCategory.entries.forEach { category ->
            val isSelected = selectedCategory == category

            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelect(category) },
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = categoryIcon(category),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = category.label.split(" ").first(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}

// ============================================================
// CATEGORY SECTION
// ============================================================

@Composable
private fun CategorySection(
    category: ActivityCategory,
    activities: List<PersonalizedActivity>,
    zones: List<HeartRateZone>
) {
    var isExpanded by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Category header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = categoryIcon(category),
                        contentDescription = null,
                        tint = FeatureColors.HeartDeep,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = category.label,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${activities.size} activities",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess
                    else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }

            // Activities list
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
                Column(
                    modifier = Modifier.padding(
                        start = 12.dp,
                        end = 12.dp,
                        bottom = 12.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    activities.forEachIndexed { index, personalizedActivity ->
                        ActivityCard(
                            personalizedActivity = personalizedActivity,
                            index = index
                        )
                    }
                }
            }
        }
    }
}

// ============================================================
// ACTIVITY CARD
// ============================================================

@Composable
private fun ActivityCard(
    personalizedActivity: PersonalizedActivity,
    index: Int
) {
    val activity = personalizedActivity.activity
    var showDetails by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDetails = !showDetails },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = personalizedActivity.zoneColor.copy(alpha = 0.04f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Activity emoji
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    personalizedActivity.zoneColor.copy(alpha = 0.15f),
                                    personalizedActivity.zoneColorEnd.copy(alpha = 0.15f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = activityIcon(activity),
                        contentDescription = activity.name,
                        tint = personalizedActivity.zoneColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Activity info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = activity.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Zone badge
                        ZoneBadge(
                            text = activity.zoneLabel,
                            color = personalizedActivity.zoneColor
                        )
                    }
                }

                // BPM and calories column
                Column(horizontalAlignment = Alignment.End) {
                    // BPM range
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.MonitorHeart,
                            contentDescription = null,
                            tint = personalizedActivity.zoneColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (personalizedActivity.bpmLow == personalizedActivity.bpmHigh)
                                "~${personalizedActivity.bpmLow}"
                            else "${personalizedActivity.bpmLow}-${personalizedActivity.bpmHigh}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = personalizedActivity.zoneColor
                        )
                    }

                    Text(
                        text = stringResource(R.string.txt_bpm_1),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Calories
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.LocalFireDepartment,
                            contentDescription = null,
                            tint = FeatureColors.CalorieDeep,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = personalizedActivity.caloriesPer30Min.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = FeatureColors.CalorieDeep
                        )
                    }
                    Text(
                        text = stringResource(R.string.txt_cal_30m),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
                    )
                }
            }

            // Expanded details
            AnimatedVisibility(
                visible = showDetails,
                enter = expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    HorizontalDivider(
                        color = personalizedActivity.zoneColor.copy(alpha = 0.15f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = activity.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Detail chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DetailChip(
                            icon = Icons.Outlined.MonitorHeart,
                            label = "Heart Rate",
                            value = "${personalizedActivity.bpmLow}-${personalizedActivity.bpmHigh} BPM",
                            color = personalizedActivity.zoneColor
                        )
                        DetailChip(
                            icon = Icons.Outlined.LocalFireDepartment,
                            label = "Calories",
                            value = "${personalizedActivity.caloriesPer30Min} cal/30min",
                            color = FeatureColors.CalorieDeep
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Duration calorie table
                    CalorieDurationRow(
                        caloriesPer30 = personalizedActivity.caloriesPer30Min,
                        zoneColor = personalizedActivity.zoneColor
                    )
                }
            }
        }
    }
}

@Composable
private fun ZoneBadge(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.12f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = color,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            fontSize = 10.sp
        )
    }
}

@Composable
private fun DetailChip(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.06f)
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CalorieDurationRow(
    caloriesPer30: Int,
    zoneColor: Color
) {
    val durations = listOf(15, 30, 45, 60)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        durations.forEach { minutes ->
            val cal = (caloriesPer30.toFloat() / 30 * minutes).toInt()
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${minutes}m",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
                Text(
                    text = "$cal",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = zoneColor
                )
                Text(
                    text = stringResource(R.string.txt_cal),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
                )
            }
        }
    }
}

// ============================================================
// EMPTY SEARCH
// ============================================================

@Composable
private fun EmptySearchResult(query: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.SearchOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No activities found for \"$query\"",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.txt_try_a_different_search_term),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}
