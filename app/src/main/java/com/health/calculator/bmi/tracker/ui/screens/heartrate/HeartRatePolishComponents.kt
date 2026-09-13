package com.health.calculator.bmi.tracker.ui.screens.heartrate

import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R
import com.health.calculator.bmi.tracker.data.export.ExportDisclosurePolicy


import dagger.hilt.android.qualifiers.ApplicationContext

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.health.calculator.bmi.tracker.util.HeartRateZoneCalculator
import com.health.calculator.bmi.tracker.util.HeartRateZoneResult
import com.health.calculator.bmi.tracker.ui.components.HeartRateFormula
import com.health.calculator.bmi.tracker.ui.theme.FeatureColors
import com.health.calculator.bmi.tracker.ui.theme.HealthColors
import com.health.calculator.bmi.tracker.ui.theme.OnSurfaceLight
import com.health.calculator.bmi.tracker.ui.theme.OnSurfaceVariantLight
import com.health.calculator.bmi.tracker.ui.theme.SurfaceLight
import java.io.File
import java.io.FileOutputStream

private fun edgeWarningIcon(legacyIcon: String): ImageVector = when {
    legacyIcon.contains("👶") -> Icons.Outlined.ChildCare
    legacyIcon.contains("👴") -> Icons.Outlined.Person
    legacyIcon.contains("⚠") -> Icons.Outlined.Warning
    legacyIcon.contains("🏆") -> Icons.Outlined.EmojiEvents
    legacyIcon.contains("📉") -> Icons.Outlined.TrendingDown
    legacyIcon.contains("📊") -> Icons.Outlined.Analytics
    else -> Icons.Outlined.Info
}

// ============================================================
// FORMULA COMPARISON CARD
// ============================================================

@Composable
fun FormulaComparisonCard(
    age: Int,
    selectedFormula: HeartRateFormula,
    gender: String?,
    customMaxHR: Int?,
    modifier: Modifier = Modifier
) {
    val allResults = remember(age, gender, customMaxHR) {
        HeartRateZoneCalculator.calculateAllFormulaMHR(age, gender, customMaxHR)
    }

    val bestForUser = remember(age, gender) {
        when {
            gender?.lowercase() == "female" -> HeartRateFormula.GULATI
            age >= 40 -> HeartRateFormula.TANAKA
            else -> HeartRateFormula.STANDARD
        }
    }

    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Calculate,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.txt_formula_comparison),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.txt_mhr_results_from_all_formulas),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Always show selected formula result
            val selectedMHR = allResults[selectedFormula] ?: (220 - age)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = HealthColors.Healthy,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Selected: ${selectedFormula.label}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "$selectedMHR BPM",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Expanded comparison
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(spring(dampingRatio = 0.8f, stiffness = 300f)) + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    allResults.entries
                        .filter { it.key != HeartRateFormula.CUSTOM || customMaxHR != null }
                        .sortedByDescending { it.value }
                        .forEach { (formula, mhr) ->
                            val isSelected = formula == selectedFormula
                            val isBest = formula == bestForUser
                            val maxMHR = allResults.values.maxOrNull() ?: mhr

                            FormulaComparisonRow(
                                formula = formula,
                                mhr = mhr,
                                maxMHR = maxMHR,
                                isSelected = isSelected,
                                isBestForUser = isBest
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Recommendation note
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = HealthColors.Good.copy(alpha = 0.06f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = null,
                                tint = HealthColors.Good,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = buildString {
                                    append("A practical starting option based on your inputs: ${bestForUser.label}; no formula is universally best and sensor accuracy varies. ")
                                    when (bestForUser) {
                                        HeartRateFormula.GULATI -> append("Gulati was developed using women-specific data; that does not guarantee an individual estimate.")
                                        HeartRateFormula.TANAKA -> append("Tanaka is an age-adjusted alternative; estimates can differ from a measured maximum.")
                                        HeartRateFormula.KARVONEN -> append("Karvonen uses your resting heart rate to adjust zones; the result depends on that reading.")
                                        else -> append("The standard formula is a commonly used starting estimate, not a measurement.")
                                    }
                                    append("\n\nAll formulas are estimates. A supervised exercise test is a separate way to assess maximum heart rate when clinically appropriate.")
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FormulaComparisonRow(
    formula: HeartRateFormula,
    mhr: Int,
    maxMHR: Int,
    isSelected: Boolean,
    isBestForUser: Boolean
) {
    val fraction = if (maxMHR > 0) mhr.toFloat() / maxMHR else 0f
    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "formula_bar_${formula.name}"
    )

    val barColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isBestForUser -> HealthColors.Healthy
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Formula name
        Column(modifier = Modifier.width(80.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = formula.label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                )
                if (isBestForUser && !isSelected) {
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "Suggested",
                        tint = HealthColors.Healthy,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
            formula.badge?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }

        // Bar
        Box(
            modifier = Modifier
                .weight(1f)
                .height(20.dp)
                .padding(horizontal = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp))
                    .background(barColor.copy(alpha = 0.08f))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedFraction)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(barColor.copy(alpha = if (isSelected) 0.7f else 0.3f))
            )
        }

        // MHR value
        Text(
            text = "$mhr",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.ExtraBold,
            color = barColor,
            modifier = Modifier.width(36.dp),
            textAlign = TextAlign.End
        )
    }
}

// ============================================================
// EDGE CASE WARNINGS
// ============================================================

@Composable
fun HeartRateEdgeCaseWarnings(
    age: Int,
    restingHR: Int?,
    maxHR: Int,
    modifier: Modifier = Modifier
) {
    val warnings = remember(age, restingHR, maxHR) {
        buildList {
            // Very young
            if (age < 15) {
                add(
                    EdgeWarning(
                        emoji = "👶",
                        message = "This calculator is intended for adults. Children and teens need age-appropriate " +
                                "exercise guidance; ask a pediatric clinician if you are planning structured training.",
                        severity = WarningSeverity.INFO
                    )
                )
            }

            // Very old
            if (age > 75) {
                add(
                    EdgeWarning(
                        emoji = "👴",
                        message = "For adults over 75, age-based maximum-heart-rate estimates may be less precise. " +
                                "Use comfortable effort and consider professional guidance before changing intensity.",
                        severity = WarningSeverity.WARNING
                    )
                )
            }

            // Very high resting HR
            if (restingHR != null && restingHR > 100) {
                add(
                    EdgeWarning(
                        emoji = "⚠️",
                        message = "A resting heart rate above 100 BPM can have many explanations, including stress, " +
                                "illness or measurement conditions. If it is persistent or you feel unwell, consider " +
                                "speaking with a healthcare professional.",
                        severity = WarningSeverity.DANGER
                    )
                )
            }

            // Athletic resting HR
            if (restingHR != null && restingHR < 40) {
                add(
                    EdgeWarning(
                        emoji = "🏆",
                        message = "A resting heart rate below 40 BPM is exceptionally low. If you're a trained athlete, " +
                                "this can occur in some people. If you are not regularly trained, or have dizziness, " +
                                "fatigue or fainting, consider speaking with a healthcare professional.",
                        severity = WarningSeverity.INFO
                    )
                )
            }

            // MHR seems very low
            if (maxHR < 140) {
                add(
                    EdgeWarning(
                        emoji = "📉",
                        message = "Your estimated max HR of $maxHR BPM is on the lower side for this age-based input. " +
                                "This can make zone values look lower; treat the result as an estimate.",
                        severity = WarningSeverity.INFO
                    )
                )
            }

            // Resting HR close to calculated zone boundaries
            if (restingHR != null && restingHR > maxHR * 0.5) {
                add(
                    EdgeWarning(
                        emoji = "📊",
                        message = "Your resting HR is above 50% of your estimated max HR. Zone 1 may be very narrow. " +
                                "Recheck the resting value under similar conditions and use the talk test when exercising.",
                        severity = WarningSeverity.INFO
                    )
                )
            }
        }
    }

    if (warnings.isNotEmpty()) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            warnings.forEach { warning ->
                EdgeWarningCard(warning = warning)
            }
        }
    }
}

private data class EdgeWarning(
    val emoji: String,
    val message: String,
    val severity: WarningSeverity
)

private enum class WarningSeverity { INFO, WARNING, DANGER }

@Composable
private fun EdgeWarningCard(warning: EdgeWarning) {
    val (containerColor, borderColor) = when (warning.severity) {
        WarningSeverity.INFO -> HealthColors.Good.copy(alpha = 0.06f) to HealthColors.Good.copy(alpha = 0.2f)
        WarningSeverity.WARNING -> HealthColors.Caution.copy(alpha = 0.06f) to HealthColors.Caution.copy(alpha = 0.2f)
        WarningSeverity.DANGER -> HealthColors.Danger.copy(alpha = 0.06f) to HealthColors.Danger.copy(alpha = 0.3f)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            val iconTint = when (warning.severity) {
                WarningSeverity.INFO -> HealthColors.Good
                WarningSeverity.WARNING -> HealthColors.Caution
                WarningSeverity.DANGER -> HealthColors.Danger
            }
            Icon(
                imageVector = edgeWarningIcon(warning.emoji),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = warning.message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                lineHeight = 17.sp
            )
        }
    }
}

// ============================================================
// RESTING HR TREND TRACKER
// ============================================================

@Composable
fun RestingHRTrendCard(
    restingHRHistory: List<Pair<String, Int>>, // date to resting HR
    modifier: Modifier = Modifier
) {
    if (restingHRHistory.size < 2) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Analytics,
                    contentDescription = null,
                    tint = FeatureColors.HeartDeep,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.txt_track_your_resting_hr_over_tim),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(R.string.txt_calculate_your_zones_regularly) +
                            "Compare readings under similar conditions; a trend alone cannot measure fitness.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
        return
    }

    val minHR = restingHRHistory.minOf { it.second }
    val maxHR = restingHRHistory.maxOf { it.second }
    val avgHR = restingHRHistory.map { it.second }.average().toInt()
    val latestHR = restingHRHistory.last().second
    val firstHR = restingHRHistory.first().second
    val change = latestHR - firstHR
    val isImproving = change < 0

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Analytics,
                    contentDescription = null,
                    tint = FeatureColors.HeartDeep,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.txt_resting_hr_trend),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Mini graph
            RestingHRMiniGraph(
                data = restingHRHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TrendStat(icon = Icons.Outlined.Schedule, label = "Latest", value = "$latestHR BPM")
                TrendStat(icon = Icons.Outlined.Analytics, label = "Average", value = "$avgHR BPM")
                TrendStat(icon = Icons.Outlined.TrendingDown, label = "Lowest", value = "$minHR BPM")
                TrendStat(
                    icon = if (isImproving) Icons.Outlined.CheckCircle else Icons.Outlined.TrendingUp,
                    label = "Change",
                    value = "${if (change > 0) "+" else ""}$change BPM"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Trend message
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = when {
                    isImproving -> HealthColors.Healthy.copy(alpha = 0.08f)
                    change == 0 -> HealthColors.Good.copy(alpha = 0.08f)
                    else -> HealthColors.Caution.copy(alpha = 0.08f)
                }
            ) {
                Text(
                    text = if (isImproving)
                        "Your resting HR has decreased by ${-change} BPM; this is a trend to interpret with context."
                    else if (change == 0) "Your resting HR is stable across these readings."
                    else "Your resting HR has increased by $change BPM. Stress, illness, sleep and measurement conditions can affect it.",
                    style = MaterialTheme.typography.labelSmall,
                    color = when {
                        isImproving -> HealthColors.Healthy
                        change == 0 -> HealthColors.Good
                        else -> HealthColors.Caution
                    },
                    modifier = Modifier.padding(10.dp),
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
private fun RestingHRMiniGraph(
    data: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    val lineColor = FeatureColors.HeartDeep
    val surfaceColor = MaterialTheme.colorScheme.surface
    val minVal = (data.minOf { it.second } - 5).coerceAtLeast(30)
    val maxVal = (data.maxOf { it.second } + 5).coerceAtMost(120)
    val range = (maxVal - minVal).toFloat().coerceAtLeast(1f)

    androidx.compose.foundation.Canvas(modifier = modifier) {
        val stepX = size.width / (data.size - 1).coerceAtLeast(1)

        // Grid lines
        for (i in 0..4) {
            val y = size.height * i / 4f
            drawLine(
                color = lineColor.copy(alpha = 0.06f),
                start = androidx.compose.ui.geometry.Offset(0f, y),
                end = androidx.compose.ui.geometry.Offset(size.width, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Line path
        val points = data.mapIndexed { index, (_, hr) ->
            val x = index * stepX
            val y = size.height - ((hr - minVal) / range * size.height)
            androidx.compose.ui.geometry.Offset(x, y)
        }

        // Draw fill area
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(points.first().x, size.height)
            points.forEach { lineTo(it.x, it.y) }
            lineTo(points.last().x, size.height)
            close()
        }
        drawPath(
            path = path,
            color = lineColor.copy(alpha = 0.08f)
        )

        // Draw line
        for (i in 0 until points.size - 1) {
            drawLine(
                color = lineColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Draw dots
        points.forEach { point ->
            drawCircle(
                color = lineColor,
                radius = 4.dp.toPx(),
                center = point
            )
            drawCircle(
                color = surfaceColor,
                radius = 2.dp.toPx(),
                center = point
            )
        }
    }
}

@Composable
private fun TrendStat(icon: ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            fontSize = 9.sp
        )
    }
}

// ============================================================
// SHARE AS IMAGE
// ============================================================

fun shareHeartRateZonesAsImage(
    context: Context,
    result: HeartRateZoneResult
) {
    val width = 1080
    val height = 1400
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val bgPaint = Paint().apply { color = SurfaceLight.toArgb() }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

    val titlePaint = Paint().apply {
        color = OnSurfaceLight.toArgb()
        textSize = 52f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        isAntiAlias = true
    }

    val subtitlePaint = Paint().apply {
        color = OnSurfaceVariantLight.toArgb()
        textSize = 32f
        isAntiAlias = true
    }

    val mhrPaint = Paint().apply {
        color = FeatureColors.HeartDeep.toArgb()
        textSize = 80f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    val mhrLabelPaint = Paint().apply {
        color = FeatureColors.HeartDeep.toArgb()
        textSize = 28f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    // Title
    canvas.drawText(context.getString(R.string.txt_my_heart_rate_zones), 60f, 80f, titlePaint)
    canvas.drawText("Age: ${result.age} | ${result.formulaUsed.label} Formula", 60f, 130f, subtitlePaint)

    // MHR
    canvas.drawText("${result.maxHeartRate}", width / 2f, 250f, mhrPaint)
    canvas.drawText(context.getString(R.string.txt_max_heart_rate_bpm), width / 2f, 290f, mhrLabelPaint)

    if (result.restingHeartRate != null) {
        canvas.drawText("Resting: ${result.restingHeartRate} BPM | Reserve: ${result.heartRateReserve} BPM",
            width / 2f, 330f, subtitlePaint.apply { textAlign = Paint.Align.CENTER })
    }

    // Zone bars
    val zoneColors = listOf(
        HealthColors.Good.toArgb(), FeatureColors.StepsDeep.toArgb(),
        HealthColors.Healthy.toArgb(), HealthColors.Caution.toArgb(),
        HealthColors.Danger.toArgb()
    )

    var yPos = 400f
    val barHeight = 120f
    val barMargin = 20f
    val barLeft = 60f
    val barRight = width - 60f

    result.zones.forEachIndexed { index, zone ->
        // Zone bar background
        val barPaint = Paint().apply {
            color = zoneColors.getOrElse(index) { HealthColors.Info.toArgb() }
            isAntiAlias = true
        }
        val barPaintLight = Paint().apply {
            color = (zoneColors.getOrElse(index) { HealthColors.Info.toArgb() } and 0x00FFFFFF) or 0x20000000
            isAntiAlias = true
        }

        canvas.drawRoundRect(RectF(barLeft, yPos, barRight, yPos + barHeight), 20f, 20f, barPaintLight)

        val fraction = zone.bpmHigh.toFloat() / result.maxHeartRate
        val filledWidth = barLeft + (barRight - barLeft) * fraction
        canvas.drawRoundRect(RectF(barLeft, yPos, filledWidth, yPos + barHeight), 20f, 20f, barPaint)

        // Zone text
        val zoneTextPaint = Paint().apply {
            color = FeatureColors.OnHeart.toArgb()
            textSize = 36f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        val zoneDetailPaint = Paint().apply {
            color = FeatureColors.OnHeart.toArgb()
            textSize = 24f
            isAntiAlias = true
        }

        canvas.drawText("Zone ${zone.zoneNumber} — ${zone.zoneName}", barLeft + 20f, yPos + 45f, zoneTextPaint)
        canvas.drawText("${zone.bpmLow}-${zone.bpmHigh} BPM (${zone.percentLow}-${zone.percentHigh}%)",
            barLeft + 20f, yPos + 80f, zoneDetailPaint)
        canvas.drawText(zone.purpose.take(50) + if (zone.purpose.length > 50) "..." else "",
            barLeft + 20f, yPos + 105f, zoneDetailPaint.apply { textSize = 20f })

        yPos += barHeight + barMargin
    }

    // Footer
    yPos += 20f
    val footerPaint = Paint().apply {
        color = OnSurfaceVariantLight.toArgb()
        textSize = 24f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText(context.getString(R.string.txt_generated_by_health_calculator), width / 2f, yPos, footerPaint)

    // Save and share
    try {
        val exportDirectory = File(context.cacheDir, "exports").apply { mkdirs() }
        val file = File(exportDirectory, "heart_rate_zones.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "My Heart Rate Zones")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share Heart Rate Zones"))
    } catch (e: Exception) {
        // Fallback to text share
        shareHeartRateZonesText(context, result)
    }
}

fun shareHeartRateZonesText(@ApplicationContext context: Context, result: HeartRateZoneResult) {
    val text = buildString {
        appendLine("My Heart Rate Zones (Age: ${result.age}, MHR: ${result.maxHeartRate} BPM)")
        result.restingHeartRate?.let {
            appendLine("Resting HR: $it BPM | HR Reserve: ${result.heartRateReserve} BPM")
        }
        appendLine("Formula: ${result.formulaUsed.label}")
        appendLine("━━━━━━━━━━━━━━━━━━━━━━━━")
        result.zones.forEach { zone ->
            appendLine("Zone ${zone.zoneNumber} — ${zone.zoneName}: ${zone.bpmLow}-${zone.bpmHigh} BPM (${zone.percentLow}-${zone.percentHigh}%)")
        }
        appendLine("━━━━━━━━━━━━━━━━━━━━━━━━")
        appendLine()
        append(ExportDisclosurePolicy.shareFooter())
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
        putExtra(Intent.EXTRA_SUBJECT, "My Heart Rate Zones")
    }
    context.startActivity(Intent.createChooser(intent, "Share Heart Rate Zones"))
}
