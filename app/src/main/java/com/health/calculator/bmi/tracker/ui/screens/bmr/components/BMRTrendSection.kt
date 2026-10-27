// File: com/health/calculator/bmi/tracker/ui/screens/bmr/components/BMRTrendSection.kt
package com.health.calculator.bmi.tracker.ui.screens.bmr.components

import com.health.calculator.bmi.tracker.ui.components.detail.DetailColors as AppColors
import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.data.model.*
import com.health.calculator.bmi.tracker.ui.theme.CalculatorColors
import com.health.calculator.bmi.tracker.ui.theme.HealthColors
import kotlin.math.abs
import kotlin.math.roundToInt


private fun bmrInsightIcon(insight: BMRChangeInsight): ImageVector = when (insight) {
    BMRChangeInsight.NO_DATA -> Icons.Outlined.ShowChart
    BMRChangeInsight.STABLE -> Icons.Outlined.TrendingFlat
    BMRChangeInsight.INCREASED_WEIGHT_GAIN,
    BMRChangeInsight.INCREASED_MUSCLE_GAIN -> Icons.Outlined.TrendingUp
    BMRChangeInsight.DECREASED_WEIGHT_LOSS,
    BMRChangeInsight.DECREASED_CONCERNING -> Icons.Outlined.TrendingDown
}

@Composable
fun BMRTrendSection(
    historyPoints: List<BMRHistoryPoint>,
    stats: BMRTrendStats,
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400)) + expandVertically(tween(400)),
        exit = fadeOut(tween(200)) + shrinkVertically(tween(200)),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.ShowChart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.txt_bmr_tdee_trends),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${stats.totalReadings} reading${if (stats.totalReadings != 1) "s" else ""} recorded",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    historyPoints.isEmpty() -> EmptyTrendState()
                    historyPoints.size == 1 -> SingleReadingState(stats)
                    else -> {
                        // Previous comparison badge
                        PreviousComparisonBadge(stats = stats)

                        Spacer(modifier = Modifier.height(14.dp))

                        // Dual line graph
                        BMRTDEEGraph(
                            points = historyPoints,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height((240 * androidx.compose.ui.platform.LocalDensity.current.fontScale.coerceAtLeast(1f)).dp)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Graph legend
                        GraphLegend()

                        Spacer(modifier = Modifier.height(16.dp))

                        // Statistics
                        TrendStatisticsGrid(stats = stats)

                        Spacer(modifier = Modifier.height(14.dp))

                        // Insight
                        InsightCard(stats = stats)
                    }
                }
            }
        }
    }
}

// ============================================================
// Empty & Single Reading States
// ============================================================
@Composable
private fun EmptyTrendState() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.ShowChart,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.txt_no_bmr_readings_yet),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.txt_save_your_first_bmr_calculatio),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun SingleReadingState(stats: BMRTrendStats) {
    val restingInk = AppColors.Accent
    val dailyInk = AppColors.Success

    Column {
        // Show current stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SingleStatCard(
                label = "Current BMR",
                value = "${stats.currentBMR.toInt()}",
                unit = "kcal/day",
                color = restingInk,
                modifier = Modifier.weight(1f)
            )
            SingleStatCard(
                label = "Current TDEE",
                value = "${stats.currentTDEE.toInt()}",
                unit = "kcal/day",
                color = dailyInk,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppColors.surface(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f))
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.txt_great_start_save_another_bmr_r),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun SingleStatCard(
    label: String,
    value: String,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.surface(color.copy(alpha = 0.06f))),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, color.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = AppColors.text(color)
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                color = AppColors.text(color.copy(alpha = 0.7f))
            )
        }
    }
}

// ============================================================
// Previous Comparison Badge
// ============================================================
@Composable
private fun PreviousComparisonBadge(stats: BMRTrendStats) {
    if (!stats.hasPreviousReading) return

    val change = stats.changeFromPrevious
    val isUp = change > 0
    val isStable = abs(change) < 5f
    val color = when {
        isStable -> MaterialTheme.colorScheme.onSurfaceVariant
        isUp -> HealthColors.Info
        else -> HealthColors.Caution
    }
    val icon = when {
        isStable -> Icons.Outlined.TrendingFlat
        isUp -> Icons.Outlined.TrendingUp
        else -> Icons.Outlined.TrendingDown
    }
    val text = when {
        isStable -> "Stable since last reading"
        isUp -> "Up ${abs(change).toInt()} kcal since last reading"
        else -> "Down ${abs(change).toInt()} kcal since last reading"
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.text(color)
            )
        }
    }
}

// ============================================================
// Dual Line Graph (BMR + TDEE)
// ============================================================
@Composable
private fun BMRTDEEGraph(
    points: List<BMRHistoryPoint>,
    modifier: Modifier = Modifier
) {
    val restingInk = AppColors.Accent
    val dailyInk = AppColors.Success

    var selectedPointIndex by remember { mutableIntStateOf(-1) }
    val textMeasurer = rememberTextMeasurer()
    val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val surfaceColor = MaterialTheme.colorScheme.surface

    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(points) {
        animProgress.snapTo(0f)
        animProgress.animateTo(
            1f,
            animationSpec = tween(1200, easing = FastOutSlowInEasing)
        )
    }

    Column {
        // Tooltip
        AnimatedVisibility(
            visible = selectedPointIndex >= 0 && selectedPointIndex < points.size,
            enter = fadeIn(tween(150)) + expandVertically(tween(150)),
            exit = fadeOut(tween(100)) + shrinkVertically(tween(100))
        ) {
            if (selectedPointIndex >= 0 && selectedPointIndex < points.size) {
                val point = points[selectedPointIndex]
                PointTooltip(point = point)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Canvas(
            modifier = modifier.pointerInput(points) {
                detectTapGestures { offset ->
                    val paddingLeft = maxOf(50.dp.toPx(), 14.sp.toPx() * 4.2f)
                    val paddingRight = 16.dp.toPx()
                    val chartWidth = size.width - paddingLeft - paddingRight

                    if (points.size > 1) {
                        val stepX = chartWidth / (points.size - 1).coerceAtLeast(1)
                        val index = ((offset.x - paddingLeft) / stepX).roundToInt()
                            .coerceIn(0, points.size - 1)
                        selectedPointIndex = if (selectedPointIndex == index) -1 else index
                    }
                }
            }
        ) {
            val paddingLeft = maxOf(50.dp.toPx(), 14.sp.toPx() * 4.2f)
            val paddingRight = 16.dp.toPx()
            val paddingTop = 16.dp.toPx()
            val paddingBottom = maxOf(30.dp.toPx(), 14.sp.toPx() * 2.4f)

            val chartWidth = size.width - paddingLeft - paddingRight
            val chartHeight = size.height - paddingTop - paddingBottom

            val allValues = points.flatMap { listOf(it.bmr, it.tdee) }
            val minVal = (allValues.min() - 100f).coerceAtLeast(500f)
            val maxVal = allValues.max() + 100f
            val valueRange = maxVal - minVal

            fun valToY(value: Float): Float =
                paddingTop + chartHeight - ((value - minVal) / valueRange * chartHeight * animProgress.value)

            fun indexToX(index: Int): Float {
                return if (points.size == 1) paddingLeft + chartWidth / 2
                else paddingLeft + (index.toFloat() / (points.size - 1)) * chartWidth
            }

            // Grid
            val gridSteps = 4
            for (i in 0..gridSteps) {
                val value = minVal + (valueRange * i / gridSteps)
                val y = paddingTop + chartHeight - (chartHeight * i / gridSteps)
                drawLine(gridColor, Offset(paddingLeft, y), Offset(size.width - paddingRight, y), 1.dp.toPx())

                val label = textMeasurer.measure(
                    "${value.toInt()}",
                    TextStyle(fontSize = 14.sp, color = labelColor)
                )
                drawText(label, topLeft = Offset(2.dp.toPx(), y - label.size.height / 2))
            }

            // Date labels
            val labelIndices = when {
                points.size <= 5 -> points.indices.toList()
                else -> listOf(0, points.size / 2, points.size - 1)
            }
            labelIndices.forEach { i ->
                if (i < points.size) {
                    val x = indexToX(i)
                    val dateLabel = textMeasurer.measure(
                        points[i].dateLabel,
                        TextStyle(fontSize = 14.sp, color = labelColor)
                    )
                    drawText(
                        dateLabel,
                        topLeft = Offset(
                            (x - dateLabel.size.width / 2).coerceIn(paddingLeft, size.width - paddingRight - dateLabel.size.width.toFloat()),
                            size.height - paddingBottom + 4.dp.toPx()
                        )
                    )
                }
            }

            if (points.size > 1) {
                // TDEE line (draw first so BMR overlays)
                val tdeePath = Path()
                val tdeeFillPath = Path()
                points.forEachIndexed { index, point ->
                    val x = indexToX(index)
                    val y = valToY(point.tdee)
                    if (index == 0) {
                        tdeePath.moveTo(x, y)
                        tdeeFillPath.moveTo(x, paddingTop + chartHeight)
                        tdeeFillPath.lineTo(x, y)
                    } else {
                        val prevX = indexToX(index - 1)
                        val prevY = valToY(points[index - 1].tdee)
                        val cpX = (prevX + x) / 2
                        tdeePath.cubicTo(cpX, prevY, cpX, y, x, y)
                        tdeeFillPath.cubicTo(cpX, prevY, cpX, y, x, y)
                    }
                }
                tdeeFillPath.lineTo(indexToX(points.lastIndex), paddingTop + chartHeight)
                tdeeFillPath.close()

                drawPath(tdeeFillPath, dailyInk.copy(alpha = 0.06f))
                drawPath(tdeePath, dailyInk, style = Stroke(2.dp.toPx(), cap = StrokeCap.Round))

                // BMR line
                val bmrPath = Path()
                val bmrFillPath = Path()
                points.forEachIndexed { index, point ->
                    val x = indexToX(index)
                    val y = valToY(point.bmr)
                    if (index == 0) {
                        bmrPath.moveTo(x, y)
                        bmrFillPath.moveTo(x, paddingTop + chartHeight)
                        bmrFillPath.lineTo(x, y)
                    } else {
                        val prevX = indexToX(index - 1)
                        val prevY = valToY(points[index - 1].bmr)
                        val cpX = (prevX + x) / 2
                        bmrPath.cubicTo(cpX, prevY, cpX, y, x, y)
                        bmrFillPath.cubicTo(cpX, prevY, cpX, y, x, y)
                    }
                }
                bmrFillPath.lineTo(indexToX(points.lastIndex), paddingTop + chartHeight)
                bmrFillPath.close()

                drawPath(bmrFillPath, restingInk.copy(alpha = 0.08f))
                drawPath(bmrPath, restingInk, style = Stroke(2.5.dp.toPx(), cap = StrokeCap.Round))
            }

            // Data points
            points.forEachIndexed { index, point ->
                val x = indexToX(index)
                val bmrY = valToY(point.bmr)
                val tdeeY = valToY(point.tdee)
                val isSelected = index == selectedPointIndex
                val dotSize = if (isSelected) 7.dp.toPx() else 4.dp.toPx()

                // TDEE dot
                drawCircle(dailyInk, dotSize * 0.8f, Offset(x, tdeeY))

                // BMR dot
                if (isSelected) {
                    drawCircle(restingInk.copy(alpha = 0.2f), dotSize * 2f, Offset(x, bmrY))
                }
                drawCircle(surfaceColor, dotSize, Offset(x, bmrY))
                drawCircle(restingInk, dotSize * 0.7f, Offset(x, bmrY))

                // Vertical guide line when selected
                if (isSelected) {
                    drawLine(
                        restingInk.copy(alpha = 0.15f),
                        Offset(x, paddingTop),
                        Offset(x, paddingTop + chartHeight),
                        1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PointTooltip(point: BMRHistoryPoint) {
    val restingInk = AppColors.Accent
    val dailyInk = AppColors.Success

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.SurfaceAlt
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = point.dateLabel,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                color = AppColors.text(AppColors.Secondary)
            )
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TooltipItem("BMR", "${point.bmr.toInt()} kcal", restingInk)
                TooltipItem("TDEE", "${point.tdee.toInt()} kcal", dailyInk)
                TooltipItem("Weight", "${String.format(java.util.Locale.getDefault(), "%.1f", point.weightKg)} kg", AppColors.Text)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${point.formulaName} • ${point.activityLevel}",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                color = AppColors.text(AppColors.Secondary),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TooltipItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
            color = AppColors.text(color.copy(alpha = 0.8f)),
            fontSize = 14.sp
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
            fontWeight = FontWeight.Bold,
            color = AppColors.text(color)
        )
    }
}

@Composable
private fun GraphLegend() {
    val restingInk = AppColors.Accent
    val dailyInk = AppColors.Success

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendDot(color = restingInk, label = "BMR")
        Spacer(modifier = Modifier.width(20.dp))
        LegendDot(color = dailyInk, label = "TDEE")
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}

// ============================================================
// Statistics Grid
// ============================================================
@Composable
private fun TrendStatisticsGrid(stats: BMRTrendStats) {
    val restingInk = AppColors.Accent

    Column {
        Text(
            text = stringResource(R.string.txt_statistics),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TrendStatCard(
                label = "Current",
                value = "${stats.currentBMR.toInt()}",
                unit = "kcal",
                color = restingInk,
                modifier = Modifier.weight(1f)
            )
            TrendStatCard(
                label = "Average",
                value = "${stats.averageBMR.toInt()}",
                unit = "kcal",
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            )
            TrendStatCard(
                label = "Highest",
                value = "${stats.highestBMR.toInt()}",
                unit = "kcal",
                color = HealthColors.Healthy,
                modifier = Modifier.weight(1f)
            )
            TrendStatCard(
                label = "Lowest",
                value = "${stats.lowestBMR.toInt()}",
                unit = "kcal",
                color = HealthColors.Caution,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Change from first reading
        if (stats.totalReadings > 1) {
            val changeColor = when {
                abs(stats.changeFromFirst) < 5f -> MaterialTheme.colorScheme.onSurfaceVariant
                stats.changeFromFirst > 0 -> HealthColors.Info
                else -> HealthColors.Caution
            }
            val sign = if (stats.changeFromFirst >= 0) "+" else ""

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.surface(changeColor.copy(alpha = 0.06f))
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.txt_change_from_first_reading),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$sign${stats.changeFromFirst.toInt()} kcal",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.text(changeColor)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = changeColor.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "$sign${stats.changePercentFromFirst.roundToInt()}%",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                                fontWeight = FontWeight.Bold,
                                color = AppColors.text(changeColor),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TrendStatCard(
    label: String,
    value: String,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.surface(color.copy(alpha = 0.06f))),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, color.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = AppColors.text(color)
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                color = AppColors.text(color.copy(alpha = 0.6f)),
                fontSize = 14.sp
            )
        }
    }
}

// ============================================================
// Insight Card
// ============================================================
@Composable
private fun InsightCard(stats: BMRTrendStats) {
    val insight = stats.getChangeInsight()
    val insightColor = when (insight) {
        BMRChangeInsight.DECREASED_CONCERNING -> HealthColors.Caution
        BMRChangeInsight.STABLE -> MaterialTheme.colorScheme.primary
        else -> HealthColors.Info
    }
    val bgColor = insightColor.copy(alpha = 0.06f)
    val borderColor = insightColor.copy(alpha = 0.15f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.surface(bgColor)),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = bmrInsightIcon(insight),
                    contentDescription = null,
                    tint = insightColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = insight.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = insight.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp
            )

            // Weight context if available
            if (stats.hasPreviousReading && abs(stats.weightChange) > 0.1f) {
                Spacer(modifier = Modifier.height(8.dp))
                val wSign = if (stats.weightChange > 0) "+" else ""
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MonitorWeight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Weight changed ${wSign}${String.format(java.util.Locale.getDefault(), "%.1f", stats.weightChange)} kg since last reading",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
