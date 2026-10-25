package com.health.calculator.bmi.tracker.ui.screens.calorie

import com.health.calculator.bmi.tracker.ui.theme.AppColors
import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.data.model.*
import com.health.calculator.bmi.tracker.ui.theme.HealthColors

@Composable
fun MealPlanningSection(
    mealPlan: MealPlan,
    ifPlan: IntermittentFastingPlan?,
    workoutNutrition: WorkoutNutrition?,
    selectedMealCount: Int,
    customSplits: List<Float>,
    ifType: String,
    ifWindowStart: Int,
    workoutEnabled: Boolean,
    workoutTime: String,
    onMealCountChanged: (Int) -> Unit,
    onCustomSplitChanged: (List<Float>) -> Unit,
    onIFTypeChanged: (String) -> Unit,
    onIFWindowStartChanged: (Int) -> Unit,
    onWorkoutEnabledChanged: (Boolean) -> Unit,
    onWorkoutTimeChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Section Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Restaurant,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.txt_meal_planning),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Meal Count Selector
        MealCountSelector(
            selectedCount = selectedMealCount,
            onCountChanged = onMealCountChanged
        )

        // Meal Distribution
        MealDistributionCard(
            mealPlan = mealPlan,
            customSplits = customSplits,
            onCustomSplitChanged = onCustomSplitChanged
        )

        // Intermittent Fasting
        IntermittentFastingCard(
            ifType = ifType,
            windowStart = ifWindowStart,
            ifPlan = ifPlan,
            onTypeChanged = onIFTypeChanged,
            onWindowStartChanged = onIFWindowStartChanged
        )

        // IF Visual Timeline
        if (ifType != "none" && ifPlan != null) {
            IFTimelineVisual(ifPlan = ifPlan)
        }

        // Workout Nutrition
        WorkoutNutritionCard(
            enabled = workoutEnabled,
            workoutTime = workoutTime,
            workoutNutrition = workoutNutrition,
            onEnabledChanged = onWorkoutEnabledChanged,
            onTimeChanged = onWorkoutTimeChanged
        )

        // Meal Ideas
        MealIdeasSection(mealPlan = mealPlan)
    }
}

@Composable
private fun MealCountSelector(
    selectedCount: Int,
    onCountChanged: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.txt_number_of_meals_1),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(2, 3, 4, 5, 6).forEach { count ->
                    val isSelected = selectedCount == count
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onCountChanged(count) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "$count",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                stringResource(R.string.txt_meals),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MealDistributionCard(
    mealPlan: MealPlan,
    customSplits: List<Float>,
    onCustomSplitChanged: (List<Float>) -> Unit
) {
    var showCustom by remember { mutableStateOf(false) }
    val mealColors = listOf(
        HealthColors.Caution, // Breakfast
        HealthColors.Healthy, // Lunch
        HealthColors.Info, // Dinner
        HealthColors.Severe, // Snack 1
        HealthColors.Danger, // Snack 2
        HealthColors.BelowNormal // Extra
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.txt_calorie_distribution),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                )
                TextButton(onClick = { showCustom = !showCustom }) {
                    Text(
                        if (showCustom) "Use Default" else "Customize",
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Distribution visual bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                mealPlan.meals.forEachIndexed { index, meal ->
                    val color = mealColors.getOrElse(index) { MaterialTheme.colorScheme.outline }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(meal.percentOfDaily.coerceAtLeast(1f))
                            .background(color)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Meal breakdown
            mealPlan.meals.forEachIndexed { index, meal ->
                val color = mealColors.getOrElse(index) { MaterialTheme.colorScheme.outline }
                MealRowItem(
                    meal = meal,
                    color = color,
                    showCustomSlider = showCustom,
                    onPercentChanged = { newPercent ->
                        val newSplits = customSplits.toMutableList()
                        val diff = newPercent - newSplits[index]
                        newSplits[index] = newPercent
                        // Distribute the difference to other meals
                        val othersCount = newSplits.size - 1
                        if (othersCount > 0) {
                            val adjustment = diff / othersCount
                            newSplits.forEachIndexed { i, _ ->
                                if (i != index) {
                                    newSplits[i] = (newSplits[i] - adjustment).coerceIn(5f, 60f)
                                }
                            }
                        }
                        // Normalize to 100%
                        val total = newSplits.sum()
                        val normalized = newSplits.map { it * 100f / total }
                        onCustomSplitChanged(normalized)
                    }
                )
                if (index < mealPlan.meals.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun MealRowItem(
    meal: MealSlot,
    color: Color,
    showCustomSlider: Boolean,
    onPercentChanged: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        meal.label,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        meal.suggestedTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                        fontSize = 12.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${"%.0f".format(meal.calories)} kcal",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = AppColors.text(color)
                )
                Text(
                    "${"%.0f".format(meal.percentOfDaily)}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                    fontSize = 12.sp
                )
            }
        }

        // Macros mini row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MacroMiniChip("P", "${"%.0f".format(meal.proteinGrams)}g", HealthColors.Info)
            MacroMiniChip("C", "${"%.0f".format(meal.carbGrams)}g", HealthColors.Warning)
            MacroMiniChip("F", "${"%.0f".format(meal.fatGrams)}g", HealthColors.Healthy)
        }

        // Custom slider
        AnimatedVisibility(
            visible = showCustomSlider,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Slider(
                value = meal.percentOfDaily,
                onValueChange = { onPercentChanged(it) },
                valueRange = 10f..50f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                colors = SliderDefaults.colors(
                    thumbColor = color,
                    activeTrackColor = color,
                    inactiveTrackColor = color.copy(alpha = 0.2f)
                )
            )
        }
    }
}

@Composable
private fun MacroMiniChip(label: String, value: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = AppColors.text(color),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                value,
                style = MaterialTheme.typography.labelSmall,
                color = AppColors.text(color.copy(alpha = 0.8f)),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun IntermittentFastingCard(
    ifType: String,
    windowStart: Int,
    ifPlan: IntermittentFastingPlan?,
    onTypeChanged: (String) -> Unit,
    onWindowStartChanged: (Int) -> Unit
) {
    val ifOptions = listOf(
        Triple("none", "No Fasting", Icons.Outlined.LocalDining),
        Triple("16:8", "16:8", Icons.Outlined.Schedule),
        Triple("18:6", "18:6", Icons.Outlined.AccessTime),
        Triple("20:4", "20:4", Icons.Outlined.Schedule)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(R.string.txt_intermittent_fasting),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // IF Type selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ifOptions.forEach { (type, label, icon) ->
                    val isSelected = ifType == type
                    FilterChip(
                        selected = isSelected,
                        onClick = { onTypeChanged(type) },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(label, fontSize = 12.sp)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            // Window start time (only if IF is selected)
            AnimatedVisibility(
                visible = ifType != "none",
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        stringResource(R.string.txt_eating_window_starts_at),
                        style = MaterialTheme.typography.labelMedium,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(10, 11, 12, 13, 14).forEach { hour ->
                            val isSelected = windowStart == hour
                            val displayHour = if (hour > 12) "${hour - 12} PM" else "$hour ${if (hour == 12) "PM" else "AM"}"
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onWindowStartChanged(hour) },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ) {
                                Text(
                                    displayHour,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    // IF Info
                    ifPlan?.let {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                IFInfoChip(Icons.Outlined.LocalDining, "Eating", "${it.eatingHours}h", HealthColors.Healthy)
                                IFInfoChip(Icons.Outlined.Schedule, "Fasting", "${it.fastingHours}h", HealthColors.Caution)
                                val endDisplay = if (it.windowEndHour > 12) "${it.windowEndHour - 12} PM"
                                    else "${it.windowEndHour} ${if (it.windowEndHour == 12) "PM" else "AM"}"
                                IFInfoChip(Icons.Outlined.AccessTime, "Ends", endDisplay, HealthColors.Info)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IFInfoChip(icon: ImageVector, label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, fontSize = 12.sp, color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)))
        }
        Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = AppColors.text(color))
    }
}

@Composable
private fun IFTimelineVisual(ifPlan: IntermittentFastingPlan) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.txt_24_hour_timeline),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Timeline
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                val fastingColor = HealthColors.Caution.copy(alpha = 0.3f)
                val eatingColor = HealthColors.Healthy.copy(alpha = 0.5f)
                val mealMarkerColor = HealthColors.Info
                val surfaceColor = MaterialTheme.colorScheme.surface
                val outlineColor = MaterialTheme.colorScheme.outline

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val hourWidth = width / 24f

                    // Draw 24-hour background (fasting)
                    drawRect(color = fastingColor)

                    // Draw eating window
                    val startX = ifPlan.windowStartHour * hourWidth
                    val windowWidth = ifPlan.eatingHours * hourWidth
                    drawRect(
                        color = eatingColor,
                        topLeft = Offset(startX, 0f),
                        size = androidx.compose.ui.geometry.Size(windowWidth, height)
                    )

                    // Draw hour markers
                    for (hour in 0..24 step 6) {
                        val x = hour * hourWidth
                        drawLine(
                            color = outlineColor.copy(alpha = 0.5f),
                            start = Offset(x, 0f),
                            end = Offset(x, height),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
                        )
                    }

                    // Draw meal markers
                    ifPlan.mealsInWindow.forEach { meal ->
                        val mealHour = parseHourFromTime(meal.suggestedTime)
                        val mealX = mealHour * hourWidth
                        drawCircle(
                            color = mealMarkerColor,
                            radius = 8.dp.toPx(),
                            center = Offset(mealX, height / 2)
                        )
                        drawCircle(
                            color = surfaceColor,
                            radius = 4.dp.toPx(),
                            center = Offset(mealX, height / 2)
                        )
                    }
                }
            }

            // Hour labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("12 AM", "6 AM", "12 PM", "6 PM", "12 AM").forEach { label ->
                    Text(
                        label,
                        style = MaterialTheme.typography.labelSmall,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)),
                        fontSize = 12.sp
                    )
                }
            }

            // Legend
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem("Fasting", HealthColors.Caution.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.width(16.dp))
                LegendItem("Eating", HealthColors.Healthy.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.width(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(HealthColors.Info)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.txt_meal), style = MaterialTheme.typography.labelSmall, fontSize = 12.sp)
                }
            }
        }
    }
}

private fun parseHourFromTime(time: String): Float {
    // Parse "12:00 PM" format
    val parts = time.replace(":", " ").split(" ")
    if (parts.size < 2) return 12f
    val hour = parts[0].toIntOrNull() ?: 12
    val isPM = time.contains("PM", ignoreCase = true)
    val adjustedHour = when {
        hour == 12 && !isPM -> 0
        hour == 12 && isPM -> 12
        isPM -> hour + 12
        else -> hour
    }
    return adjustedHour.toFloat()
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp, 8.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, fontSize = 12.sp)
    }
}

@Composable
private fun WorkoutNutritionCard(
    enabled: Boolean,
    workoutTime: String,
    workoutNutrition: WorkoutNutrition?,
    onEnabledChanged: (Boolean) -> Unit,
    onTimeChanged: (String) -> Unit
) {
    val workoutTimes = listOf("Morning", "Afternoon", "Evening")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            stringResource(R.string.txt_workout_nutrition),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            stringResource(R.string.txt_pre_post_workout_meal_planning),
                            style = MaterialTheme.typography.labelSmall,
                            color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                            fontSize = 12.sp
                        )
                    }
                }
                Switch(
                    checked = enabled,
                    onCheckedChange = onEnabledChanged
                )
            }

            AnimatedVisibility(
                visible = enabled,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    // Workout time selector
                    Text(
                        stringResource(R.string.txt_when_do_you_work_out),
                        style = MaterialTheme.typography.labelMedium,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        workoutTimes.forEach { time ->
                            val isSelected = workoutTime == time
                            val timeIcon = when (time) {
                                "Morning" -> Icons.Outlined.WbSunny
                                "Afternoon" -> Icons.Outlined.WbSunny
                                else -> Icons.Outlined.NightsStay
                            }
                            FilterChip(
                                selected = isSelected,
                                onClick = { onTimeChanged(time) },
                                label = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(timeIcon, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(time, fontSize = 12.sp)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }

                    // Pre/Post workout meals
                    workoutNutrition?.let { wn ->
                        Spacer(modifier = Modifier.height(12.dp))

                        // Pre-workout
                        wn.preWorkoutMeal?.let { meal ->
                            WorkoutMealCard(
                                label = "Pre-Workout",
                                timing = wn.preWorkoutTiming,
                                meal = meal,
                                color = HealthColors.Caution,
                                icon = Icons.Outlined.Bolt
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Post-workout
                        wn.postWorkoutMeal?.let { meal ->
                            WorkoutMealCard(
                                label = "Post-Workout",
                                timing = wn.postWorkoutTiming,
                                meal = meal,
                                color = HealthColors.Healthy,
                                icon = Icons.Outlined.FitnessCenter
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkoutMealCard(
    label: String,
    timing: String,
    meal: MealSlot,
    color: Color,
    icon: ImageVector
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.08f)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            label,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = AppColors.text(color)
                        )
                        Text(
                            timing,
                            style = MaterialTheme.typography.labelSmall,
                            color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                            fontSize = 12.sp
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "${"%.0f".format(meal.calories)} kcal",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = AppColors.text(color)
                    )
                    Text(
                        "${meal.percentOfDaily.toInt()}% of daily",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Macros
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MacroMiniChip("P", "${"%.0f".format(meal.proteinGrams)}g", HealthColors.Info)
                MacroMiniChip("C", "${"%.0f".format(meal.carbGrams)}g", HealthColors.Warning)
                MacroMiniChip("F", "${"%.0f".format(meal.fatGrams)}g", HealthColors.Healthy)
            }

            // Meal ideas
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(R.string.txt_ideas),
                style = MaterialTheme.typography.labelSmall,
                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                fontSize = 12.sp
            )
            meal.mealIdeas.take(2).forEach { idea ->
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = foodIdeaIcon(idea.emoji),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        idea.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MealIdeasSection(mealPlan: MealPlan) {
    var expandedMealIndex by remember { mutableStateOf(-1) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = HealthColors.Warning,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(R.string.txt_meal_ideas),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            mealPlan.meals.forEachIndexed { index, meal ->
                val isExpanded = expandedMealIndex == index

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedMealIndex = if (isExpanded) -1 else index },
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    meal.label,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        "${"%.0f".format(meal.calories)} kcal",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            Icon(
                                if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Toggle",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }

                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column(modifier = Modifier.padding(top = 8.dp)) {
                                meal.mealIdeas.forEach { idea ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Icon(
                                            imageVector = foodIdeaIcon(idea.emoji),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                idea.description,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                                            )
                                        }
                                        Text(
                                            "~${idea.calories} kcal",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (index < mealPlan.meals.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

/** Keep legacy meal-idea markers in the data model, but use one vector icon
 * family for rendered planning surfaces. */
private fun foodIdeaIcon(marker: String): ImageVector = when (marker) {
    "⚡" -> Icons.Outlined.Bolt
    "🌅", "☀️" -> Icons.Outlined.WbSunny
    "🌙" -> Icons.Outlined.NightsStay
    "⏰", "🕐", "⌛" -> Icons.Outlined.Schedule
    "🍗", "🐟", "🥚", "🥩" -> Icons.Outlined.Restaurant
    else -> Icons.Outlined.LocalDining
}
