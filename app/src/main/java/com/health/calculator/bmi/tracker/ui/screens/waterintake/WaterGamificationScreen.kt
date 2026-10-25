package com.health.calculator.bmi.tracker.ui.screens.waterintake

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
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
import com.health.calculator.bmi.tracker.data.model.*
import com.health.calculator.bmi.tracker.ui.theme.FeatureColors
import com.health.calculator.bmi.tracker.ui.theme.HealthColors
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterGamificationScreen(
    viewModel: WaterGamificationViewModel,
    onNavigateBack: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val earnedBadges by viewModel.earnedBadges.collectAsStateWithLifecycle(initialValue = emptyList())
    val streakData by viewModel.streakData.collectAsStateWithLifecycle(initialValue = null)
    val hydrationScore by viewModel.todayScore.collectAsStateWithLifecycle()
    val newlyEarnedBadge by viewModel.newlyEarnedBadge.collectAsStateWithLifecycle()

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.EmojiEvents,
                            contentDescription = null,
                            tint = HealthColors.Warning,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(stringResource(R.string.txt_achievements), fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onNavigateBack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (AppColors.isDark) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp, top = 8.dp)
            ) {
                // Streak section
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(tween(500)) + scaleIn(tween(500), initialScale = 0.9f)
                    ) {
                        StreakDisplayCard(streakData ?: WaterStreakData())
                    }
                }

                // Hydration Score
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(tween(500, 100)) + slideInVertically(tween(500, 100)) { 40 }
                    ) {
                        HydrationScoreCard(hydrationScore)
                    }
                }

                // Badges header
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(tween(500, 200))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(R.string.txt_badge_collection),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "${earnedBadges.size}/${BadgeType.entries.size} earned",
                                fontSize = 13.sp,
                                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            )
                        }
                    }
                }

                // Badge grid
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(tween(500, 300)) + slideInVertically(tween(500, 300)) { 40 }
                    ) {
                        BadgeGrid(
                            allBadges = BadgeType.entries,
                            earnedBadges = earnedBadges.map { it.badgeType }.toSet(),
                            earnedTimestamps = earnedBadges.associate { it.badgeType to it.earnedTimestamp }
                        )
                    }
                }

                // Milestone progress
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(tween(500, 400)) + slideInVertically(tween(500, 400)) { 40 }
                    ) {
                        MilestoneProgressCard(streakData ?: WaterStreakData())
                    }
                }
            }

            // Badge unlock overlay
            newlyEarnedBadge?.let { badge ->
                BadgeUnlockOverlay(
                    badge = badge,
                    onDismiss = { viewModel.dismissBadgeUnlock() }
                )
            }
        }
    }
}

// ─── Streak Display ──────────────────────────────────────────────────────────

@Composable
private fun StreakDisplayCard(streakData: WaterStreakData) {
    val isActive = streakData.currentStreak > 0
    val contentColor = if (isActive) Color.White else MaterialTheme.colorScheme.onSurface

    val infiniteTransition = rememberInfiniteTransition(label = "flame")
    val flameScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isActive) 1.15f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_scale"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = if (isActive) listOf(HealthColors.Caution, FeatureColors.CalorieDeep)
                        else listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surface)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = if (isActive) Icons.Outlined.Whatshot else Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier
                        .size(48.dp)
                        .scale(flameScale)
                )

                if (isActive) {
                    Text(
                        text = "${streakData.currentStreak}",
                        color = AppColors.text(contentColor),
                        fontSize = 52.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = stringResource(R.string.txt_day_streak),
                        color = AppColors.text(contentColor.copy(alpha = 0.9f)),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )

                    // Next milestone
                    val nextMilestone = listOf(3, 7, 14, 30, 60, 90, 365)
                        .firstOrNull { it > streakData.currentStreak }

                    nextMilestone?.let { milestone ->
                        val daysRemaining = milestone - streakData.currentStreak
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = AppColors.surface(contentColor.copy(alpha = 0.2f))
                            )
                        ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Outlined.Flag, contentDescription = null, tint = contentColor, modifier = Modifier.size(16.dp))
                        Text(
                            text = "$daysRemaining days to $milestone-day milestone!",
                            color = AppColors.text(contentColor),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                        }
                    }
                } else {
                    Text(
                        text = stringResource(R.string.txt_no_active_streak),
                        color = AppColors.text(contentColor),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AppColors.surface(contentColor.copy(alpha = 0.2f))
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.txt_start_a_new_streak_today_meet_),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            color = AppColors.text(contentColor),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Best streak
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.EmojiEvents, contentDescription = null, tint = contentColor, modifier = Modifier.size(16.dp))
                    Text(
                        text = "Best: ${streakData.longestStreak} days",
                        color = AppColors.text(contentColor.copy(alpha = 0.7f)),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

// ─── Hydration Score ─────────────────────────────────────────────────────────

@Composable
private fun HydrationScoreCard(score: HydrationScore?) {
    val displayScore = score ?: HydrationScore(
        0, "F", "😟", 0, 0, 0, 0, emptyList()
    )

    val animatedScore by animateIntAsState(
        targetValue = displayScore.totalScore,
        animationSpec = tween(1000, easing = EaseOutCubic),
        label = "score_anim"
    )

    val animatedSweep by animateFloatAsState(
        targetValue = displayScore.totalScore / 100f * 360f,
        animationSpec = tween(1200, easing = EaseOutCubic),
        label = "sweep_anim"
    )

    val scoreColor = when {
        displayScore.totalScore >= 90 -> HealthColors.Healthy
        displayScore.totalScore >= 70 -> HealthColors.Good
        displayScore.totalScore >= 50 -> HealthColors.Warning
        displayScore.totalScore >= 30 -> HealthColors.Caution
        else -> HealthColors.Danger
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                stringResource(R.string.txt_today_s_hydration_score),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Score ring
                Box(
                    modifier = Modifier.size(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val ringTrackColor = MaterialTheme.colorScheme.outline
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 12f
                        val radius = (size.minDimension - strokeWidth) / 2

                        drawCircle(
                            color = ringTrackColor.copy(alpha = 0.18f),
                            radius = radius,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )

                        drawArc(
                            color = scoreColor,
                            startAngle = -90f,
                            sweepAngle = animatedSweep,
                            useCenter = false,
                            topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                            size = Size(size.width - strokeWidth, size.height - strokeWidth),
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$animatedScore",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AppColors.text(scoreColor)
                        )
                        Text(
                            text = displayScore.grade,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.text(scoreColor)
                        )
                    }
                }

                // Breakdown
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    displayScore.breakdown.forEach { item ->
                        ScoreBreakdownRow(item)
                    }
                }
            }

            // Grade message
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.surface(scoreColor.copy(alpha = 0.1f))
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = hydrationGradeIcon(displayScore.grade),
                        contentDescription = null,
                        tint = scoreColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = getGradeMessage(displayScore.grade),
                        fontSize = 13.sp,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                    )
                }
            }
        }
    }
}

@Composable
private fun ScoreBreakdownRow(item: ScoreBreakdownItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = if (item.earned) Icons.Outlined.CheckCircle else Icons.Outlined.Info,
                contentDescription = null,
                tint = if (item.earned) HealthColors.Healthy else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = item.label,
                fontSize = 12.sp,
                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(
                    alpha = if (item.earned) 0.8f else 0.4f
                ))
            )
        }
        Text(
            text = "${item.points}/${item.maxPoints}",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.secondaryText(if (item.earned) HealthColors.Healthy else MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}

private fun getGradeMessage(grade: String): String = when (grade) {
    "A" -> "Outstanding! You're a hydration superstar today!"
    "B+" -> "Great job! Almost perfect hydration today."
    "B" -> "Good effort! You're doing well, keep it up."
    "C+" -> "Not bad! Try drinking more consistently."
    "C" -> "Average day. Aim higher tomorrow!"
    "D" -> "Below average. Set reminders to drink more."
    else -> "Room for improvement. Every sip counts!"
}

// ─── Badge Grid ──────────────────────────────────────────────────────────────

@Composable
private fun BadgeGrid(
    allBadges: List<BadgeType>,
    earnedBadges: Set<String>,
    earnedTimestamps: Map<String, Long>
) {
    val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

    // Group by tier
    val tiers = BadgeTier.entries

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        tiers.forEach { tier ->
            val tierBadges = allBadges.filter { it.tier == tier }
            if (tierBadges.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(badgeTierColor(tier), CircleShape)
                        )
                        Text(
                            tier.label,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = AppColors.text(badgeTierColor(tier))
                        )
                        val tierEarned = tierBadges.count { it.name in earnedBadges }
                        Text(
                            "($tierEarned/${tierBadges.size})",
                            fontSize = 12.sp,
                            color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                        )
                    }

                    // 3-column grid for badges
                    val rows = (tierBadges.size + 2) / 3
                    for (row in 0 until rows) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (col in 0 until 3) {
                                val idx = row * 3 + col
                                if (idx < tierBadges.size) {
                                    val badge = tierBadges[idx]
                                    val isEarned = badge.name in earnedBadges
                                    val timestamp = earnedTimestamps[badge.name]

                                    BadgeCard(
                                        badge = badge,
                                        isEarned = isEarned,
                                        earnedDate = timestamp?.let { dateFormat.format(Date(it)) },
                                        modifier = Modifier.weight(1f)
                                    )
                                } else {
                                    Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BadgeCard(
    badge: BadgeType,
    isEarned: Boolean,
    earnedDate: String?,
    modifier: Modifier = Modifier
) {
    var showDetail by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isEarned) 1f else 0.95f,
        label = "badge_scale"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(14.dp))
            .clickable { showDetail = !showDetail },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(if (isEarned)
                badgeTierColor(badge.tier).copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ),
        elevation = CardDefaults.cardElevation(if (isEarned) 2.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = badgeIcon(badge),
                        contentDescription = null,
                        tint = badgeTierColor(badge.tier),
                        modifier = Modifier
                            .size(32.dp)
                            .alpha(if (isEarned) 1f else 0.3f)
                    )
                if (!isEarned) {
                    Icon(
                        Icons.Default.Lock,
                        null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = badge.displayName,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                color = AppColors.text(if (isEarned)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
            )

            AnimatedVisibility(
                visible = showDetail,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 2.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                    )
                    Text(
                        text = badge.requirement,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    )
                    if (isEarned && earnedDate != null) {
                        Text(
                            text = "Earned: $earnedDate",
                            fontSize = 12.sp,
                            color = AppColors.text(badgeTierColor(badge.tier))
                        )
                    }
                }
            }
        }
    }
}

// ─── Milestone Progress ──────────────────────────────────────────────────────

@Composable
private fun MilestoneProgressCard(streakData: WaterStreakData) {
    val milestones = listOf(
        Triple(3, "Hydration Habit", Icons.Outlined.WaterDrop),
        Triple(7, "7-Day Streak", Icons.Outlined.Whatshot),
        Triple(14, "Two Week Warrior", Icons.Outlined.Bolt),
        Triple(30, "Monthly Master", Icons.Outlined.EmojiEvents),
        Triple(60, "Hydration Expert", Icons.Outlined.Star),
        Triple(90, "Quarterly Champion", Icons.Outlined.EmojiEvents),
        Triple(365, "Diamond Drinker", Icons.Outlined.WaterDrop)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                stringResource(R.string.txt_streak_milestones),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

            milestones.forEach { (days, name, icon) ->
                val isCompleted = streakData.longestStreak >= days
                val progress = (streakData.currentStreak.toFloat() / days).coerceAtMost(1f)
                val animatedProgress by animateFloatAsState(
                    targetValue = if (isCompleted) 1f else progress,
                    animationSpec = tween(800, easing = EaseOutCubic),
                    label = "milestone_$days"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = if (isCompleted) Icons.Outlined.CheckCircle else icon,
                        contentDescription = null,
                        tint = if (isCompleted) HealthColors.Healthy else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                name,
                                fontSize = 13.sp,
                                fontWeight = if (isCompleted) FontWeight.Bold else FontWeight.Normal,
                                color = AppColors.text(if (isCompleted) HealthColors.Healthy
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                            )
                            Text(
                                "${if (isCompleted) days else streakData.currentStreak}/$days",
                                fontSize = 12.sp,
                                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (isCompleted) HealthColors.Healthy else FeatureColors.WaterDeep,
                            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

// ─── Badge Unlock Overlay ────────────────────────────────────────────────────

@Composable
private fun BadgeUnlockOverlay(
    badge: BadgeType,
    onDismiss: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "unlock_scale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "sparkle")
    val sparkleRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sparkle_rotate"
    )

    LaunchedEffect(Unit) {
        delay(4000)
        onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        // Floating vector particles
        val particleIcons = listOf(
            Icons.Outlined.AutoAwesome,
            Icons.Outlined.Star,
            Icons.Outlined.EmojiEvents,
            Icons.Outlined.WaterDrop,
            Icons.Outlined.AutoAwesome,
            Icons.Outlined.Star
        )
        particleIcons.forEachIndexed { index, icon ->
            val offsetY by infiniteTransition.animateFloat(
                initialValue = 300f,
                targetValue = -300f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000 + index * 200, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "particle_y_$index"
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (index % 2 == 0) HealthColors.Warning else FeatureColors.WaterDeep,
                modifier = Modifier
                    .size(24.dp)
                    .offset(
                        x = ((index * 67 + 30) % 250 - 125).dp,
                        y = offsetY.dp
                    )
            )
        }

        Card(
            modifier = Modifier
                .scale(scale)
                .padding(32.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(stringResource(R.string.txt_badge_unlocked), fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            badgeTierColor(badge.tier).copy(alpha = 0.15f),
                            CircleShape
                        )
                        .border(3.dp, badgeTierColor(badge.tier), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = badgeIcon(badge),
                        contentDescription = null,
                        tint = badgeTierColor(badge.tier),
                        modifier = Modifier.size(44.dp)
                    )
                }

                Text(
                    badge.displayName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AppColors.text(badgeTierColor(badge.tier))
                )
                Text(
                    badge.description,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.surface(badgeTierColor(badge.tier).copy(alpha = 0.1f))
                    )
                ) {
                    Text(
                        text = "${badge.tier.label} Tier",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.text(badgeTierColor(badge.tier))
                    )
                }

                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.txt_awesome), fontSize = 14.sp)
                }
            }
        }
    }
}

private fun hydrationGradeIcon(grade: String?): ImageVector = when (grade?.uppercase()) {
    "A", "B", "B+" -> Icons.Outlined.CheckCircle
    "C", "C+" -> Icons.Outlined.Info
    else -> Icons.Outlined.Warning
}

private fun hydrationGradeColor(grade: String?): Color = when (grade?.uppercase()) {
    "A", "B", "B+" -> HealthColors.Healthy
    "C", "C+" -> HealthColors.Warning
    else -> HealthColors.Danger
}

private fun badgeTierColor(tier: BadgeTier): Color = when (tier) {
    BadgeTier.BRONZE -> HealthColors.Caution
    BadgeTier.SILVER -> HealthColors.Info
    BadgeTier.GOLD -> HealthColors.Warning
    BadgeTier.PLATINUM -> HealthColors.Severe
    BadgeTier.DIAMOND -> HealthColors.BelowNormal
}

private fun badgeIcon(badge: BadgeType): ImageVector = when (badge) {
    BadgeType.FIRST_DROP, BadgeType.GLASS_HALF_FULL -> Icons.Outlined.WaterDrop
    BadgeType.DAILY_CHAMPION, BadgeType.NINETY_DAY_STREAK -> Icons.Outlined.EmojiEvents
    BadgeType.THREE_DAY_STREAK -> Icons.Outlined.WaterDrop
    BadgeType.SEVEN_DAY_STREAK -> Icons.Outlined.Whatshot
    BadgeType.FOURTEEN_DAY_STREAK -> Icons.Outlined.Bolt
    BadgeType.MONTHLY_MASTER -> Icons.Outlined.EmojiEvents
    BadgeType.SIXTY_DAY_STREAK -> Icons.Outlined.Star
    BadgeType.HYDRATION_HERO -> Icons.Outlined.HealthAndSafety
    BadgeType.DIAMOND_DRINKER -> Icons.Outlined.Star
    BadgeType.EARLY_BIRD -> Icons.Outlined.WbSunny
    BadgeType.NIGHT_OWL -> Icons.Outlined.NightsStay
    BadgeType.OVERACHIEVER -> Icons.Outlined.TrendingUp
    BadgeType.CONSISTENCY_KING -> Icons.Outlined.Flag
}
