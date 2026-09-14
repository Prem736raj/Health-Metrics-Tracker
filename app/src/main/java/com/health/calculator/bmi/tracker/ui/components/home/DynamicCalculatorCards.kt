package com.health.calculator.bmi.tracker.ui.components.home

import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import com.health.calculator.bmi.tracker.ui.theme.CalculatorColors
import com.health.calculator.bmi.tracker.ui.theme.HealthColors
import com.health.calculator.bmi.tracker.ui.theme.WellnessPalette

// ============================================================
// BASE CALCULATOR CARD
// ============================================================

@Composable
fun DynamicCalculatorCard(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hasData: Boolean = false,
    dataContent: @Composable ColumnScope.() -> Unit = {},
    needsAttention: Boolean = false,
    attentionMessage: String? = null,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    progressRing: (@Composable BoxScope.() -> Unit)? = null,
    backgroundImageRes: Int? = null // NEW: Optional background image
) {
    val haptic = LocalHapticFeedback.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp) // Fixed height for premium feel
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
        shape = RoundedCornerShape(24.dp),
        border = if (backgroundImageRes == null) BorderStroke(1.dp, accentColor.copy(alpha = 0.15f)) else null,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = if (backgroundImageRes != null) 6.dp else 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image with Gradient Overlay
            if (backgroundImageRes != null) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = backgroundImageRes),
                    contentDescription = null,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Dark gradient overlay for text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.scrim.copy(alpha = 0.8f)
                                )
                            )
                        )
                )
            } else {
                // Fallback gradient for cards without images
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                )
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top section (icon/progress)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (progressRing != null) {
                            progressRing()
                        } else if (backgroundImageRes == null) {
                            // Keep the vector icon visible on tonal cards.
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                accentColor.copy(alpha = 0.25f),
                                                accentColor.copy(alpha = 0.05f)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = title,
                                    tint = accentColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }

                // Bottom section (Text/Data)
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (backgroundImageRes != null) WellnessPalette.OnHero else MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    if (hasData) {
                        dataContent()
                    } else {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (backgroundImageRes != null) WellnessPalette.OnHero.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}


// ============================================================
// 1. BMI CALCULATOR CARD
// ============================================================

@Composable
fun BMICalculatorCard(
    lastBMI: Float?,
    lastCategory: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasData = lastBMI != null
    val categoryColor = remember(lastBMI) {
        when {
            lastBMI == null -> CalculatorColors.BMI
            lastBMI < 18.5f -> HealthColors.Warning
            lastBMI < 25f -> HealthColors.Healthy
            lastBMI < 30f -> HealthColors.Caution
            else -> HealthColors.Danger
        }
    }
    val needsAttention = lastBMI != null && (lastBMI < 18.5f || lastBMI >= 25f)

    DynamicCalculatorCard(
        icon = Icons.Outlined.Calculate,
        title = "BMI Calculator",
        description = "Calculate your Body Mass Index",
        onClick = onClick,
        modifier = modifier,
        hasData = hasData,
        accentColor = categoryColor,
        needsAttention = needsAttention,
        attentionMessage = if (needsAttention) {
            when {
                lastBMI!! < 18.5f -> "Underweight"
                lastBMI >= 30f -> "Obese range"
                else -> "Overweight range"
            }
        } else null,
        backgroundImageRes = R.drawable.bmi_bg,
        dataContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Color dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(categoryColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                // BMI value
                Text(
                    text = stringResource(R.string.txt_1f).format(lastBMI),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = WellnessPalette.OnHero
                )
                Spacer(modifier = Modifier.width(6.dp))
                // Category
                lastCategory?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelMedium,
                        color = WellnessPalette.OnHero.copy(alpha = 0.8f)
                    )
                }
            }
        }
    )
}

// ============================================================
// 2. BMR CALCULATOR CARD
// ============================================================

@Composable
fun BMRCalculatorCard(
    lastBMR: Int?,
    lastTDEE: Int?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasData = lastBMR != null

    DynamicCalculatorCard(
        icon = Icons.Outlined.LocalFireDepartment,
        title = "BMR Calculator",
        description = "Calculate your Basal Metabolic Rate",
        onClick = onClick,
        modifier = modifier,
        hasData = hasData,
        accentColor = CalculatorColors.BMR,
        dataContent = {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = stringResource(R.string.txt_d_1).format(lastBMR),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = CalculatorColors.BMR
                )
                Text(
                    text = stringResource(R.string.txt_cal_day),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            if (lastTDEE != null) {
                Text(
                    text = stringResource(R.string.txt_tdee_d_cal).format(lastTDEE),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    fontSize = 10.sp
                )
            }
        }
    )
}

// ============================================================
// 3. BLOOD PRESSURE CARD
// ============================================================

@Composable
fun BloodPressureCard(
    lastSystolic: Int?,
    lastDiastolic: Int?,
    lastCategory: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasData = lastSystolic != null && lastDiastolic != null
    val categoryColor = remember(lastSystolic, lastDiastolic) {
        when {
            lastSystolic == null || lastDiastolic == null -> CalculatorColors.BloodPressure
            lastSystolic < 120 && lastDiastolic < 80 -> HealthColors.Healthy
            lastSystolic < 130 && lastDiastolic < 85 -> HealthColors.Good
            lastSystolic < 140 && lastDiastolic < 90 -> HealthColors.Warning
            lastSystolic < 160 && lastDiastolic < 100 -> HealthColors.Caution
            else -> HealthColors.Danger
        }
    }
    val needsAttention = lastSystolic != null && lastDiastolic != null && 
            (lastSystolic >= 140 || lastDiastolic >= 90)

    DynamicCalculatorCard(
        icon = Icons.Outlined.MonitorHeart,
        title = "Blood Pressure",
        description = "Check your blood pressure category",
        onClick = onClick,
        modifier = modifier,
        hasData = hasData,
        accentColor = categoryColor,
        needsAttention = needsAttention,
        attentionMessage = if (needsAttention) "Elevated reading" else null,
        backgroundImageRes = R.drawable.bp_bg,
        dataContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(categoryColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$lastSystolic/$lastDiastolic",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = WellnessPalette.OnHero
                )
                Text(
                    text = stringResource(R.string.txt_mmhg),
                    style = MaterialTheme.typography.labelMedium,
                    color = WellnessPalette.OnHero.copy(alpha = 0.8f)
                )
            }
            lastCategory?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = WellnessPalette.OnHero.copy(alpha = 0.9f),
                    fontSize = 11.sp
                )
            }
        }
    )
}

// ============================================================
// 4. WHR CARD
// ============================================================

@Composable
fun WHRCalculatorCard(
    lastWHR: Float?,
    lastCategory: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasData = lastWHR != null
    val riskColor = remember(lastWHR, lastCategory) {
        when {
            lastCategory.isNullOrBlank() -> CalculatorColors.WaistToHip
            lastCategory.contains("low", ignoreCase = true) ||
                lastCategory.contains("normal", ignoreCase = true) -> HealthColors.Healthy
            lastCategory.contains("high", ignoreCase = true) ||
                lastCategory.contains("very", ignoreCase = true) -> HealthColors.Danger
            else -> HealthColors.Warning
        }
    }
    val needsAttention = lastCategory?.let {
        it.contains("high", ignoreCase = true) || it.contains("very", ignoreCase = true)
    } == true

    DynamicCalculatorCard(
        icon = Icons.Outlined.Straighten,
        title = "Waist-to-Hip Ratio",
        description = "Assess your body fat distribution",
        onClick = onClick,
        modifier = modifier,
        hasData = hasData,
        accentColor = riskColor,
        needsAttention = needsAttention,
        attentionMessage = if (needsAttention) "High risk range" else null,
        dataContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(riskColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.txt_2f).format(lastWHR),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = riskColor
                )
            }
            lastCategory?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = riskColor.copy(alpha = 0.8f),
                    fontSize = 10.sp
                )
            }
        }
    )
}

// ============================================================
// 5. WATER INTAKE CARD (with progress ring)
// ============================================================

@Composable
fun WaterIntakeCard(
    currentIntake: Int,
    goalIntake: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasData = goalIntake > 0
    val progress = if (goalIntake > 0) (currentIntake.toFloat() / goalIntake).coerceIn(0f, 1f) else 0f
    val percentage = (progress * 100).toInt()
    
    val progressColor = when {
        progress >= 1f -> HealthColors.Healthy
        progress >= 0.7f -> HealthColors.Good
        progress >= 0.4f -> HealthColors.BelowNormal
        else -> HealthColors.BelowNormal.copy(alpha = 0.7f)
    }
    
    val needsAttention = hasData && progress < 0.5f && 
            java.time.LocalTime.now().hour >= 14 // After 2 PM

    DynamicCalculatorCard(
        icon = Icons.Outlined.WaterDrop,
        title = "Water Intake",
        description = "Track your daily hydration",
        onClick = onClick,
        modifier = modifier,
        hasData = hasData,
        accentColor = progressColor,
        needsAttention = needsAttention,
        attentionMessage = if (needsAttention) "Behind on hydration" else null,
        progressRing = {
            WaterProgressRing(
                progress = progress,
                color = progressColor
            )
        },
        backgroundImageRes = R.drawable.water_bg,
        dataContent = {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = WellnessPalette.OnHero
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.txt_of_goal),
                    style = MaterialTheme.typography.labelMedium,
                    color = WellnessPalette.OnHero.copy(alpha = 0.8f)
                )
            }
            Text(
                text = "${currentIntake}ml / ${goalIntake}ml",
                style = MaterialTheme.typography.labelSmall,
                color = WellnessPalette.OnHero.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
    )
}

@Composable
private fun BoxScope.WaterProgressRing(
    progress: Float,
    color: Color
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "water_progress"
    )

    Canvas(modifier = Modifier.size(52.dp)) {
        val strokeWidth = 5.dp.toPx()
        val radius = (size.minDimension - strokeWidth) / 2

        // Background circle
        drawCircle(
            color = color.copy(alpha = 0.12f),
            radius = radius,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Progress arc
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = animatedProgress * 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }

    Icon(
        imageVector = if (progress >= 1f) Icons.Outlined.CheckCircle else Icons.Outlined.WaterDrop,
        contentDescription = if (progress >= 1f) "Water goal reached" else "Water progress",
        tint = color,
        modifier = Modifier
            .size(18.dp)
            .align(Alignment.Center)
    )
}

// ============================================================
// 6. METABOLIC SYNDROME CARD
// ============================================================

@Composable
fun MetabolicSyndromeCard(
    criteriaMet: Int?,
    totalCriteria: Int = 5,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasData = criteriaMet != null
    val riskColor = remember(criteriaMet) {
        when (criteriaMet) {
            null -> CalculatorColors.MetabolicSyndrome
            0 -> HealthColors.Healthy
            1, 2 -> HealthColors.Warning
            else -> HealthColors.Danger
        }
    }
    val needsAttention = criteriaMet != null && criteriaMet >= 3

    DynamicCalculatorCard(
        icon = Icons.Outlined.HealthAndSafety,
        title = "Metabolic Syndrome",
        description = "Review metabolic health markers",
        onClick = onClick,
        modifier = modifier,
        hasData = hasData,
        accentColor = riskColor,
        needsAttention = needsAttention,
        attentionMessage = if (needsAttention) "Several criteria flagged" else null,
        dataContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(riskColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$criteriaMet/$totalCriteria",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = riskColor
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.txt_criteria_met),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            Text(
                text = when (criteriaMet) {
                    0 -> "No criteria flagged"
                    1, 2 -> "Some criteria flagged"
                    else -> "Review flagged criteria"
                },
                style = MaterialTheme.typography.labelSmall,
                color = riskColor.copy(alpha = 0.8f),
                fontSize = 10.sp
            )
        }
    )
}

// ============================================================
// 7. BSA CARD
// ============================================================

@Composable
fun BSACalculatorCard(
    lastBSA: Float?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasData = lastBSA != null

    DynamicCalculatorCard(
        icon = Icons.Outlined.Person,
        title = "Body Surface Area",
        description = "Calculate your body surface area",
        onClick = onClick,
        modifier = modifier,
        hasData = hasData,
        accentColor = CalculatorColors.BSA,
        dataContent = {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = stringResource(R.string.txt_2f).format(lastBSA),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = CalculatorColors.BSA
                )
                Text(
                    text = stringResource(R.string.txt_m),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    )
}

// ============================================================
// 8. IBW CARD
// ============================================================

@Composable
fun IBWCalculatorCard(
    idealWeight: Float?,
    currentWeight: Float?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasData = idealWeight != null
    val difference = if (idealWeight != null && currentWeight != null) {
        currentWeight - idealWeight
    } else null
    
    val statusColor = remember(difference) {
        when {
            difference == null -> CalculatorColors.IdealWeight
            kotlin.math.abs(difference) <= 2f -> HealthColors.Healthy
            kotlin.math.abs(difference) <= 5f -> HealthColors.Warning
            else -> HealthColors.Caution
        }
    }

    DynamicCalculatorCard(
        icon = Icons.Outlined.MonitorWeight,
        title = "Ideal Body Weight",
        description = "Find your ideal weight range",
        onClick = onClick,
        modifier = modifier,
        hasData = hasData,
        accentColor = statusColor,
        dataContent = {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = stringResource(R.string.txt_1f).format(idealWeight),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = statusColor
                )
                Text(
                    text = stringResource(R.string.txt_kg_ideal),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            if (difference != null && currentWeight != null) {
                val diffText = when {
                    kotlin.math.abs(difference) < 0.5f -> "At ideal weight!"
                    difference > 0 -> "+%.1f kg to lose".format(difference)
                    else -> "%.1f kg to gain".format(kotlin.math.abs(difference))
                }
                Text(
                    text = diffText,
                    style = MaterialTheme.typography.labelSmall,
                    color = statusColor.copy(alpha = 0.8f),
                    fontSize = 10.sp
                )
            }
        }
    )
}

// ============================================================
// 9. CALORIE CARD (with progress ring)
// ============================================================

@Composable
fun CalorieCalculatorCard(
    consumedCalories: Int,
    targetCalories: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasData = targetCalories > 0
    val progress = if (targetCalories > 0) 
        (consumedCalories.toFloat() / targetCalories).coerceIn(0f, 1.5f) else 0f
    val remaining = targetCalories - consumedCalories
    
    val progressColor = when {
        progress >= 1.2f -> HealthColors.Danger // Over by 20%+
        progress >= 1f -> HealthColors.Healthy // At goal
        progress >= 0.7f -> HealthColors.Good // Close
        else -> HealthColors.Caution // Under
    }
    
    val needsAttention = hasData && remaining > 500 && 
            java.time.LocalTime.now().hour >= 18 // After 6 PM

    DynamicCalculatorCard(
        icon = Icons.Outlined.Restaurant,
        title = "Daily Calories",
        description = "Track your daily calorie intake",
        onClick = onClick,
        modifier = modifier,
        hasData = hasData,
        accentColor = progressColor,
        needsAttention = needsAttention,
        attentionMessage = if (needsAttention) "${remaining} cal remaining" else null,
        progressRing = if (hasData) {
            {
                CalorieProgressRing(
                    progress = progress.coerceAtMost(1f),
                    color = progressColor
                )
            }
        } else null,
        dataContent = {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = stringResource(R.string.txt_d_1).format(consumedCalories),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = progressColor
                )
                Text(
                    text = stringResource(R.string.txt_d).format(targetCalories),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            Text(
                text = when {
                    remaining > 0 -> "$remaining cal remaining"
                    remaining == 0 -> "Goal reached!"
                    else -> "${-remaining} cal over"
                },
                style = MaterialTheme.typography.labelSmall,
                color = progressColor.copy(alpha = 0.8f),
                fontSize = 10.sp
            )
        }
    )
}

@Composable
private fun BoxScope.CalorieProgressRing(
    progress: Float,
    color: Color
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "calorie_progress"
    )

    Canvas(modifier = Modifier.size(52.dp)) {
        val strokeWidth = 5.dp.toPx()
        val radius = (size.minDimension - strokeWidth) / 2

        drawCircle(
            color = color.copy(alpha = 0.12f),
            radius = radius,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = animatedProgress * 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }

    Icon(
        imageVector = Icons.Outlined.LocalFireDepartment,
        contentDescription = "Calorie progress",
        tint = color,
        modifier = Modifier
            .size(18.dp)
            .align(Alignment.Center)
    )
}

// ============================================================
// 10. HEART RATE ZONES CARD
// ============================================================

@Composable
fun HeartRateZonesCard(
    maxHR: Int?,
    restingHR: Int?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasData = maxHR != null

    // Pulsing animation
    val infiniteTransition = rememberInfiniteTransition(label = "hr_pulse")
    val heartScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 800
                1f at 0
                1.1f at 100
                1f at 250
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "heart_scale"
    )

    DynamicCalculatorCard(
        icon = Icons.Outlined.FavoriteBorder,
        title = "Heart Rate Zones",
        description = "Optimize your training intensity",
        onClick = onClick,
        modifier = modifier,
        hasData = hasData,
        accentColor = CalculatorColors.HeartRateZone,
        progressRing = if (hasData) {
            {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(CalculatorColors.HeartRateZone.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Heart rate zones",
                        tint = CalculatorColors.HeartRateZone,
                        modifier = Modifier.size((22 * heartScale).dp)
                    )
                }
            }
        } else null,
        dataContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Max: $maxHR",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = CalculatorColors.HeartRateZone
                )
                Text(
                    text = stringResource(R.string.txt_bpm),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            if (restingHR != null) {
                Text(
                    text = "Resting: $restingHR BPM",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    fontSize = 10.sp
                )
            }
        }
    )
}
