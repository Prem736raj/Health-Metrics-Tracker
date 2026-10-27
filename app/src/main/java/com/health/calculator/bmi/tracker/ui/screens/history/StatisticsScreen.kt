package com.health.calculator.bmi.tracker.ui.screens.history

import com.health.calculator.bmi.tracker.ui.theme.AppColors
import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R


import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.TrendingFlat
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import java.time.Instant
import java.time.ZoneId
import java.time.LocalDate
import androidx.lifecycle.viewmodel.compose.viewModel
import com.health.calculator.bmi.tracker.data.model.*
import com.health.calculator.bmi.tracker.ui.theme.HealthColors

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OverallStatsCard(uiState.overallStats)
            }

            item {
                StreakCard(
                    currentStreak = uiState.overallStats.currentStreak,
                    longestStreak = uiState.overallStats.longestStreak
                )
            }

            item {
                Text(
                    text = stringResource(R.string.txt_activity_heatmap),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(uiState.heatmapMonths) { month ->
                HeatmapMonthCard(month)
            }

            item {
                Text(
                    text = stringResource(R.string.txt_health_trends),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (uiState.healthTrends.isEmpty()) {
                item {
                    EmptyTrendsCard()
                }
            } else {
                items(uiState.healthTrends) { trend ->
                    TrendSummaryCard(trend)
                }
            }
        }
    }
}

@Composable
fun OverallStatsCard(stats: OverallStatistics) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), 2)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.txt_total_calculations),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stats.totalCalculations.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black
                    )
                }
                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
            }

            Divider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))

            Row(modifier = Modifier.fillMaxWidth()) {
                StatItem(
                    label = "Active Days",
                    value = stats.totalActiveDays.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    label = "Avg/Day",
                    value = String.format("%.1f", stats.averageCalculationsPerDay),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    label = "Since",
                    value = stats.firstUseDateFormatted.split(",")[0],
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StreakCard(currentStreak: Int, longestStreak: Int) {
    val darkGradientSurface = com.health.calculator.bmi.tracker.ui.theme.AppColors.isDark

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = if (darkGradientSurface) listOf(com.health.calculator.bmi.tracker.ui.theme.DarkRoles.Level2, com.health.calculator.bmi.tracker.ui.theme.DarkRoles.Level2) else listOf(HealthColors.Warning, HealthColors.Caution)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Whatshot,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.txt_current_streak),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = streakDaysLabel(currentStreak),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.text(HealthColors.Caution)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(R.string.txt_best_streak),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = streakDaysLabel(longestStreak),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun HeatmapMonthCard(month: HeatmapMonth) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "${month.monthName} ${month.year}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val zone = ZoneId.systemDefault()
            val first = month.days.firstOrNull()?.let { Instant.ofEpochMilli(it.date).atZone(zone).toLocalDate().withDayOfMonth(1) }
            if (first != null) {
                val daysByNumber = month.days.associateBy { it.dayOfMonth }
                val today = LocalDate.now(zone)
                Row(Modifier.fillMaxWidth()) {
                    listOf("M", "T", "W", "T", "F", "S", "S").forEach { label ->
                        Text(label, Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                calendarWeekCells(first).forEach { week ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        week.forEach { cell ->
                            val number = cell ?: 0
                            val day = daysByNumber[number]
                            val date = first.plusDays((number - 1).toLong())
                            val inMonth = number in 1..first.lengthOfMonth()
                            Box(
                                Modifier.weight(1f).aspectRatio(1f).padding(vertical = 2.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (!inMonth) Color.Transparent else if ((day?.count ?: 0) > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                    .then(if (inMonth && date == today) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)) else Modifier)
                                    .semantics { if (inMonth) contentDescription = "$date: ${day?.count ?: 0} calculations${if (date == today) ", today" else ""}" },
                                contentAlignment = Alignment.Center
                            ) {
                                if (inMonth) Text(number.toString(), style = MaterialTheme.typography.bodySmall,
                                    color = if ((day?.count ?: 0) > 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TrendSummaryCard(trend: HealthTrendSummary) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = trend.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                TrendChip(trend.changePercentage, trend.direction)
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = trend.currentStatus,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(12.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = trend.insight,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = trend.recommendation,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun TrendChip(percentage: Float, direction: TrendDirection) {
    val color = when (direction) {
        TrendDirection.IMPROVING -> HealthColors.Healthy
        TrendDirection.DECLINING -> HealthColors.Danger
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val icon = when (numericChangeDirection(percentage)) {
        1 -> Icons.Outlined.TrendingUp
        -1 -> Icons.Outlined.TrendingDown
        else -> Icons.Outlined.TrendingFlat
    }
    val changeDescription = when {
        percentage > 0f -> "Increased"
        percentage < 0f -> "Decreased"
        else -> "Unchanged"
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = changeDescription,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "${String.format("%.1f", Math.abs(percentage))}%",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = AppColors.text(color)
            )
        }
    }
}

@Composable
fun EmptyTrendsCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Timeline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Text(
                text = stringResource(R.string.txt_no_trends_available_yet),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.txt_keep_calculating_to_see_your_p),
                style = MaterialTheme.typography.labelSmall,
                color = AppColors.secondaryText(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
            )
        }
    }
}

@Composable
fun BorderStroke(width: androidx.compose.ui.unit.Dp, color: Color) =
    androidx.compose.foundation.BorderStroke(width, color)
