package com.health.calculator.bmi.tracker.ui.screens.heartrate

import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import com.health.calculator.bmi.tracker.util.*
import com.health.calculator.bmi.tracker.ui.components.FitnessLevel
import com.health.calculator.bmi.tracker.ui.theme.HealthColors

/**
 * Zone and goal models still carry legacy emoji labels for persistence and
 * generated copy. Render a stable vector icon instead so this surface keeps
 * the same visual language as the rest of the app.
 */
private fun heartRateGuidelineIcon(legacyLabel: String): ImageVector = when {
    legacyLabel.contains("🟢") || legacyLabel.contains("🌱") -> Icons.Outlined.CheckCircle
    legacyLabel.contains("🔴") || legacyLabel.contains("🏃") -> Icons.Outlined.FavoriteBorder
    legacyLabel.contains("⏱") -> Icons.Outlined.Schedule
    legacyLabel.contains("😴") -> Icons.Outlined.Schedule
    legacyLabel.contains("📊") -> Icons.Outlined.Analytics
    legacyLabel.contains("📅") -> Icons.Outlined.CalendarMonth
    else -> Icons.Outlined.FavoriteBorder
}

// ============================================================
// MAIN WHO GUIDELINES SECTION
// ============================================================

@Composable
fun WHOGuidelinesSection(
    age: Int,
    fitnessLevel: FitnessLevel,
    zones: List<HeartRateZone>,
    selectedGoalName: String,
    modifier: Modifier = Modifier
) {
    val guideline = remember(age) { WHOExerciseGuidelinesEngine.getGuidelineForAge(age) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = stringResource(R.string.txt_who_exercise_guidelines),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.txt_world_health_organization_phys),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        // Age-specific guideline card
        WHOGuidelineCard(guideline = guideline)

        // Zone-to-intensity mapping
        ZoneIntensityMappingCard(zones = zones)

        // Weekly exercise tracker
        WeeklyExerciseTracker(
            guideline = guideline,
            zones = zones
        )
    }
}

// ============================================================
// WHO GUIDELINE CARD
// ============================================================

@Composable
private fun WHOGuidelineCard(guideline: WHOGuideline) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = HealthColors.Good.copy(alpha = 0.06f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Age group badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = HealthColors.Good.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = null,
                            tint = HealthColors.Good,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${guideline.ageGroup} (${guideline.ageRange})",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = HealthColors.Good
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Key message
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = guideline.keyMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Targets
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TargetCard(
                    emoji = "🟢",
                    label = "Moderate",
                    value = "${guideline.moderateMinPerWeek.first}-${guideline.moderateMinPerWeek.last}",
                    unit = "min/week",
                    color = HealthColors.Healthy,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.txt_or),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                TargetCard(
                    emoji = "🔴",
                    label = "Vigorous",
                    value = "${guideline.vigorousMinPerWeek.first}-${guideline.vigorousMinPerWeek.last}",
                    unit = "min/week",
                    color = HealthColors.Danger,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Additional recommendations
            var showAllRecs by remember { mutableStateOf(false) }

            Column {
                val displayRecs = if (showAllRecs) guideline.additionalRecommendations
                else guideline.additionalRecommendations.take(3)

                displayRecs.forEach { rec ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = stringResource(R.string.txt_text_placeholder_3),
                            color = HealthColors.Good,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 8.dp, top = 1.dp)
                        )
                        Text(
                            text = rec,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            lineHeight = 17.sp
                        )
                    }
                }

                if (guideline.additionalRecommendations.size > 3) {
                    TextButton(
                        onClick = { showAllRecs = !showAllRecs },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = if (showAllRecs) "Show less" else "Show all recommendations",
                            style = MaterialTheme.typography.labelSmall,
                            color = HealthColors.Good
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TargetCard(
    emoji: String,
    label: String,
    value: String,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.08f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = heartRateGuidelineIcon(emoji),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = color
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall,
                color = color.copy(alpha = 0.7f)
            )
        }
    }
}

// ============================================================
// ZONE INTENSITY MAPPING
// ============================================================

@Composable
private fun ZoneIntensityMappingCard(zones: List<HeartRateZone>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Recognizing exercise intensity", style = MaterialTheme.typography.titleSmall)
            Text("For a relative effort check, moderate activity lets you talk but not sing. During vigorous activity, you can say only a few words before pausing for breath.")
            Text("These estimated heart-rate zones do not establish intensity or verify that you met an activity guideline. Medicines, fitness, and health conditions can change your heart-rate response.")
        }
    }
}

@Composable
fun WeeklyExerciseTracker(
    guideline: WHOGuideline,
    zones: List<HeartRateZone>
) {
    var showLogDialog by remember { mutableStateOf(false) }
    var loggedSessions by remember { mutableStateOf(listOf<ExerciseLogEntry>()) }

    val totalModerateMin = loggedSessions
        .filter { it.intensity == ExerciseIntensity.MODERATE }
        .sumOf { it.durationMinutes }
    val totalVigorousMin = loggedSessions
        .filter { it.intensity == ExerciseIntensity.VIGOROUS }
        .sumOf { it.durationMinutes }
    val totalEquivalent = totalModerateMin +
            WHOExerciseGuidelinesEngine.vigorousToModerateEquivalent(totalVigorousMin)
    val targetMin = guideline.moderateMinPerWeek.first
    val progress = if (targetMin > 0) (totalEquivalent.toFloat() / targetMin).coerceIn(0f, 1.5f) else 0f

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.txt_weekly_exercise_tracker),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                FilledTonalButton(
                    onClick = { showLogDialog = true },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.txt_log), style = MaterialTheme.typography.labelMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress ring
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Ring
                WeeklyProgressRing(
                    progress = progress,
                    totalMin = totalEquivalent,
                    targetMin = targetMin,
                    modifier = Modifier.size(100.dp)
                )

                // Stats
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TrackerStat(
                        emoji = "🟢",
                        label = "Moderate",
                        value = "$totalModerateMin min",
                        target = "${guideline.moderateMinPerWeek.first} min"
                    )
                    TrackerStat(
                        emoji = "🔴",
                        label = "Vigorous",
                        value = "$totalVigorousMin min",
                        target = "${guideline.vigorousMinPerWeek.first} min"
                    )
                    TrackerStat(
                        emoji = "📊",
                        label = "Equivalent",
                        value = "$totalEquivalent min",
                        target = "$targetMin min"
                    )
                }
            }

            // WHO target note
            if (totalEquivalent > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (progress >= 1f) HealthColors.Healthy.copy(alpha = 0.1f)
                    else HealthColors.Good.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = if (progress >= 1f)
                            "Your logged intensity and duration reach the guideline reference. This is not a fitness or health assessment."
                        else
                            "${targetMin - totalEquivalent} more moderate-equivalent minutes to reach the WHO target",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = if (progress >= 1f) HealthColors.Healthy else HealthColors.Good,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            // Logged sessions
            if (loggedSessions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(R.string.txt_this_week_s_sessions),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                loggedSessions.forEachIndexed { index, session ->
                    LoggedSessionRow(
                        session = session,
                        onDelete = {
                            loggedSessions = loggedSessions.toMutableList().also {
                                it.removeAt(index)
                            }
                        }
                    )
                    if (index < loggedSessions.lastIndex) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }

    // Log dialog
    if (showLogDialog) {
        ExerciseLogDialog(
            zones = zones,
            onDismiss = { showLogDialog = false },
            onLog = { entry ->
                loggedSessions = loggedSessions + entry
                showLogDialog = false
            }
        )
    }
}

@Composable
private fun WeeklyProgressRing(
    progress: Float,
    totalMin: Int,
    targetMin: Int,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "weekly_progress"
    )

    val ringColor = when {
        progress >= 1f -> HealthColors.Healthy
        progress >= 0.6f -> HealthColors.Good
        progress >= 0.3f -> HealthColors.Warning
        else -> MaterialTheme.colorScheme.outline
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.semantics {
            contentDescription = "Weekly exercise progress: ${(progress * 100).toInt().coerceAtMost(100)} percent, $totalMin of $targetMin minutes"
            progressBarRangeInfo = ProgressBarRangeInfo(progress.coerceIn(0f, 1f), 0f..1f)
        }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 10.dp.toPx()
            drawArc(
                color = ringColor.copy(alpha = 0.12f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = (animatedProgress * 360f).coerceAtMost(360f),
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(progress * 100).toInt().coerceAtMost(100)}%",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = ringColor
            )
            Text(
                text = "$totalMin/$targetMin",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun TrackerStat(emoji: String, label: String, value: String, target: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = heartRateGuidelineIcon(emoji),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.width(64.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = " / $target",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}

@Composable
private fun LoggedSessionRow(
    session: ExerciseLogEntry,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = heartRateGuidelineIcon(session.intensity.emoji),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Zone ${session.zoneNumber} — ${session.zoneName}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
            if (session.activityNote.isNotBlank()) {
                Text(
                    text = session.activityNote,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
        Text(
            text = "${session.durationMinutes} min",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Remove",
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }
    }
}

// ============================================================
// EXERCISE LOG DIALOG
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseLogDialog(
    zones: List<HeartRateZone>,
    onDismiss: () -> Unit,
    onLog: (ExerciseLogEntry) -> Unit
) {
    var selectedZone by remember { mutableIntStateOf(2) } // Default Zone 3 (index 2)
    var duration by remember { mutableStateOf("30") }
    var activityNote by remember { mutableStateOf("") }

    val selectedZoneData = zones.getOrNull(selectedZone)
    var intensity by remember { mutableStateOf<ExerciseIntensity?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val dur = duration.toIntOrNull() ?: 0
                    val selectedIntensity = intensity
                    if (dur > 0 && selectedZoneData != null && selectedIntensity != null) {
                        onLog(
                            ExerciseLogEntry(
                                dayOfWeek = java.time.LocalDate.now().dayOfWeek.value,
                                durationMinutes = dur,
                                zoneNumber = selectedZoneData.zoneNumber,
                                zoneName = selectedZoneData.zoneName,
                                intensity = selectedIntensity,
                                activityNote = activityNote
                            )
                        )
                    }
                },
                enabled = (duration.toIntOrNull() ?: 0) > 0 && intensity != null,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(R.string.txt_log_session))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.txt_cancel)) }
        },
        title = {
            Text(
                text = stringResource(R.string.txt_log_exercise_session),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Choose the intensity you experienced. Estimated heart-rate zones do not establish exercise intensity.")
                Text("Moderate: you can talk but not sing. Vigorous: you can say only a few words before pausing for breath. Light activity does not count as moderate or vigorous time here.")
                ExerciseIntensity.entries.forEach { choice ->
                    FilterChip(
                        selected = intensity == choice,
                        onClick = { intensity = choice },
                        label = { Text(choice.label) }
                    )
                }
                // Zone selector
                Text(
                    text = stringResource(R.string.txt_select_zone),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    zones.forEachIndexed { index, zone ->
                        FilterChip(
                            selected = selectedZone == index,
                            onClick = { selectedZone = index },
                                    label = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = heartRateGuidelineIcon(zone.icon),
                                        contentDescription = null,
                                        tint = zone.color,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Z${zone.zoneNumber}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp
                                    )
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = zone.color.copy(alpha = 0.2f)
                            )
                        )
                    }
                }

                // Selected zone info
                selectedZoneData?.let { zone ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = zone.color.copy(alpha = 0.08f)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = heartRateGuidelineIcon(zone.icon),
                                contentDescription = null,
                                tint = zone.color,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${zone.zoneName} • ${zone.bpmLow}-${zone.bpmHigh} BPM • estimated range",
                                style = MaterialTheme.typography.labelSmall,
                                color = zone.color
                            )
                        }
                    }
                }

                // Duration
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it.filter { c -> c.isDigit() } },
                    label = { Text(stringResource(R.string.txt_duration)) },
                    suffix = { Text(stringResource(R.string.txt_minutes)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Timer,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                // Activity note
                OutlinedTextField(
                    value = activityNote,
                    onValueChange = { activityNote = it },
                    label = { Text(stringResource(R.string.txt_activity_optional)) },
                    placeholder = { Text(stringResource(R.string.txt_e_g_running_cycling)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
