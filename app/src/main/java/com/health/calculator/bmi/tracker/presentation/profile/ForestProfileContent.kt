package com.health.calculator.bmi.tracker.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.health.calculator.bmi.tracker.R
import com.health.calculator.bmi.tracker.data.model.WeightStatistics
import com.health.calculator.bmi.tracker.data.models.HealthJourneySummary
import com.health.calculator.bmi.tracker.domain.model.UserProfile
import com.health.calculator.bmi.tracker.domain.usecases.ProfileCompletionResult
import com.health.calculator.bmi.tracker.ui.components.forest.*
import java.text.DateFormat
import java.util.Date
import java.util.Locale

/** Both botanical profiles have one scroll owner; editors remain in ProfileScreen. */
@Composable
internal fun ForestProfileContent(
    uiState: ProfileUiState,
    journeySummary: HealthJourneySummary,
    weightStatistics: WeightStatistics?,
    onSave: () -> Unit,
    onProfilePictureClick: () -> Unit,
    onTabSelected: (ProfileTab) -> Unit,
    onNavigateToConnections: () -> Unit,
    onNavigateToReminders: () -> Unit,
    onNavigateToMilestones: () -> Unit,
    onLogWeight: () -> Unit,
    onViewWeightTrends: () -> Unit,
    onNavigateToMetric: (String) -> Unit,
    myInfoContent: @Composable () -> Unit
) {
    ForestBackdrop(botanicalArtwork(R.drawable.bg_forest_profile, R.drawable.bg_botanical_day_profile), Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            ForestProfileHero(
                profile = uiState.profile,
                isSaving = uiState.isSaving,
                showSave = uiState.selectedTab == ProfileTab.MY_INFO,
                onSave = onSave,
                onEditPhoto = onProfilePictureClick,
                onConnections = onNavigateToConnections,
                onReminders = onNavigateToReminders
            )
            ForestCompletionCard(uiState.completion)
            Spacer(Modifier.height(10.dp))
            ForestMilestonesRow(journeySummary, onNavigateToMilestones)
            Spacer(Modifier.height(12.dp))
            ForestProfileTabs(uiState.selectedTab, onTabSelected)
            Spacer(Modifier.height(16.dp))
            when (uiState.selectedTab) {
                ProfileTab.MY_INFO -> myInfoContent()
                ProfileTab.HEALTH_OVERVIEW -> ForestHealthOverview(
                    uiState, weightStatistics, onLogWeight, onViewWeightTrends, onNavigateToMetric
                )
            }
        }
    }
}

@Composable
private fun ForestProfileHero(
    profile: UserProfile,
    isSaving: Boolean,
    showSave: Boolean,
    onSave: () -> Unit,
    onEditPhoto: () -> Unit,
    onConnections: () -> Unit,
    onReminders: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val editPhotoLabel = stringResource(R.string.forest_profile_edit_photo)
    val light = BotanicalPalette.isLight
    Box(Modifier.fillMaxWidth().heightIn(min = if (light) 190.dp else 220.dp)) {
        Row(Modifier.align(Alignment.TopEnd).padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Box {
                IconButton(onClick = { expanded = true }, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Default.MoreVert, stringResource(R.string.forest_profile_more), tint = BotanicalPalette.Text)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.txt_health_connections)) },
                        leadingIcon = { Icon(Icons.Default.Share, null) },
                        onClick = { expanded = false; onConnections() }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.txt_reminders)) },
                        leadingIcon = { Icon(Icons.Default.Notifications, null) },
                        onClick = { expanded = false; onReminders() }
                    )
                }
            }
            if (showSave) {
                OutlinedButton(
                    onClick = onSave,
                    enabled = !isSaving,
                    modifier = Modifier.heightIn(min = 48.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, BotanicalPalette.Primary.copy(alpha = 0.45f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BotanicalPalette.Primary),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    if (isSaving) CircularProgressIndicator(Modifier.size(20.dp), color = BotanicalPalette.Primary, strokeWidth = 2.dp)
                    else ForestText(stringResource(R.string.txt_save), 14f, BotanicalPalette.Primary, bold = true)
                }
            }
        }
        Column(
            Modifier.align(Alignment.TopEnd).padding(top = if (light) 60.dp else 78.dp, end = if (light) 75.dp else 58.dp).widthIn(min = 100.dp, max = 160.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier.size(100.dp).clip(CircleShape)
                    .background(Brush.radialGradient(if (light) listOf(BotanicalDayRoles.Hero, BotanicalDayRoles.Selected) else listOf(BotanicalPalette.Primary.copy(alpha = 0.16f), BotanicalPalette.Background)))
                    .border(1.dp, if (light) Color.White else BotanicalPalette.Primary.copy(alpha = 0.38f), CircleShape)
                    .clickable(role = Role.Button, onClickLabel = editPhotoLabel, onClick = onEditPhoto)
                    .semantics { contentDescription = editPhotoLabel },
                contentAlignment = Alignment.Center
            ) {
                if (!profile.profilePictureUri.isNullOrBlank()) {
                    AsyncImage(profile.profilePictureUri, null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                } else if (profile.name.isNotBlank()) {
                    ForestText(profile.name.trim().take(1).uppercase(Locale.getDefault()), 36f, bold = true)
                } else {
                    Icon(Icons.Default.Person, null, Modifier.size(48.dp), tint = if (light) BotanicalPalette.Ink else BotanicalPalette.Text)
                }
            }
            Spacer(Modifier.height(6.dp))
            ForestText(profile.name.ifBlank { stringResource(R.string.forest_profile_your_profile) }, 14f, bold = true)
            Spacer(Modifier.height(if (light) 6.dp else 16.dp))
        }
    }
}

@Composable
private fun ForestCompletionCard(completion: ProfileCompletionResult) {
    ForestGlass(Modifier.fillMaxWidth(), wave = true) {
        Image(
            painterResource(botanicalArtwork(R.drawable.forest_card_leaves, R.drawable.botanical_day_card_leaves)), null,
            Modifier.align(Alignment.BottomEnd).width(80.dp).height(76.dp),
            contentScale = ContentScale.Fit, alpha = if (BotanicalPalette.isLight) 1f else 0.55f
        )
        Column(Modifier.fillMaxWidth().padding(14.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                ForestText(stringResource(R.string.txt_profile_completion), 16f, modifier = Modifier.weight(1f), bold = true, serif = BotanicalPalette.isLight)
                Spacer(Modifier.width(8.dp))
                ForestText(stringResource(R.string.forest_profile_percentage, completion.percentage), 18f, BotanicalPalette.Primary, bold = true)
            }
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { (completion.percentage / 100f).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(9.dp).clip(CircleShape),
                color = BotanicalPalette.Progress,
                trackColor = if (BotanicalPalette.isLight) BotanicalDayRoles.Hero.copy(alpha = .60f) else BotanicalPalette.Primary.copy(alpha = 0.10f),
                gapSize = 0.dp,
                drawStopIndicator = {}
            )
            if (completion.missingFields.isNotEmpty() && completion.percentage < 100) {
                Spacer(Modifier.height(10.dp))
                ForestText(stringResource(R.string.txt_complete_your_profile_for_more), 12f, BotanicalPalette.Secondary)
                Spacer(Modifier.height(8.dp))
                completion.suggestions.take(2).forEach { suggestion ->
                    Row(Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.Top) {
                        Box(Modifier.padding(top = 6.dp, end = 10.dp).size(4.dp).background(BotanicalPalette.Primary, CircleShape))
                        ForestText(suggestion, 12f, BotanicalPalette.Secondary, Modifier.weight(1f))
                    }
                }
            } else if (completion.percentage == 100) {
                Spacer(Modifier.height(10.dp))
                ForestText(stringResource(R.string.txt_profile_complete_all_calculati), 12f, BotanicalPalette.Secondary)
            }
        }
    }
}

@Composable
private fun ForestMilestonesRow(summary: HealthJourneySummary, onClick: () -> Unit) {
    val days = pluralStringResource(R.plurals.forest_profile_days, summary.daysSinceFirstUse, summary.daysSinceFirstUse)
    val milestones = pluralStringResource(R.plurals.forest_profile_milestones, summary.milestonesEarned, summary.milestonesEarned)
    val records = pluralStringResource(R.plurals.forest_profile_records, summary.personalRecordsSet, summary.personalRecordsSet)
    ForestGlass(Modifier.fillMaxWidth(), onClick = onClick) {
        Row(Modifier.fillMaxWidth().heightIn(min = 54.dp).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            ForestBadge(Icons.Default.EmojiEvents, modifier = Modifier.size(38.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                ForestText(stringResource(R.string.txt_milestones_records), 14f, bold = true)
                Spacer(Modifier.height(3.dp))
                ForestText(stringResource(R.string.forest_profile_count_separator, days, milestones, records), 12f, BotanicalPalette.Secondary)
            }
            Spacer(Modifier.width(8.dp))
            ForestText(stringResource(R.string.txt_view_all), 12f, BotanicalPalette.Primary, bold = true)
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, Modifier.size(20.dp), tint = BotanicalPalette.Secondary)
        }
    }
}

@Composable
private fun ForestProfileTabs(selected: ProfileTab, onSelected: (ProfileTab) -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
            .background(BotanicalPalette.Surface.copy(alpha = 0.60f))
            .border(1.dp, BotanicalPalette.Outline, RoundedCornerShape(14.dp)).padding(2.dp).selectableGroup()
    ) {
        ProfileTab.entries.forEach { tab ->
            val isSelected = tab == selected
            Box(
                Modifier.weight(1f).heightIn(min = 48.dp).clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) BotanicalPalette.Selected else Color.Transparent)
                    .selectable(selected = isSelected, role = Role.Tab, onClick = { onSelected(tab) })
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                ForestText(
                    stringResource(if (tab == ProfileTab.MY_INFO) R.string.forest_profile_my_info else R.string.forest_profile_health_overview),
                    14f, if (isSelected) BotanicalPalette.Ink else BotanicalPalette.Secondary, bold = isSelected
                )
            }
        }
    }
}

/** No verticalScroll here: ForestProfileContent owns scrolling for both tabs. */
@Composable
internal fun ForestMyInfoSection(
    profile: UserProfile,
    useMetricSystem: Boolean,
    onNameClick: () -> Unit,
    onDateOfBirthClick: () -> Unit,
    onGenderClick: () -> Unit,
    onHeightClick: () -> Unit,
    onWeightClick: () -> Unit,
    onGoalWeightClick: () -> Unit,
    onActivityLevelClick: () -> Unit,
    onHealthGoalsClick: () -> Unit,
    onFrameSizeClick: () -> Unit,
    onEthnicityClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val notSet = stringResource(R.string.forest_profile_not_set)
    fun number(value: Float) = String.format(Locale.getDefault(), "%.1f", value)
    val height = profile.heightCm?.takeIf { it.isFinite() && it > 0f }?.let {
        if (useMetricSystem) "${number(it)} cm" else "${number(it / 2.54f)} in"
    } ?: notSet
    fun weight(value: Float?) = value?.takeIf { it.isFinite() && it > 0f }?.let {
        if (useMetricSystem) "${number(it)} kg" else "${number(it * 2.20462f)} lb"
    } ?: notSet
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ForestText(stringResource(R.string.txt_personal_details), 23f, bold = true, serif = true, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
        ForestProfileField(stringResource(R.string.forest_profile_name), profile.name.ifBlank { notSet }, Icons.Default.Person, onNameClick)
        ForestProfileField(stringResource(R.string.forest_profile_birthdate), profile.dateOfBirthMillis?.let {
            DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(Date(it))
        } ?: notSet, Icons.Default.Cake, onDateOfBirthClick)
        ForestProfileField(stringResource(R.string.txt_gender), profile.gender.displayName, Icons.Default.Wc, onGenderClick)
        ForestText(stringResource(R.string.txt_physical_parameters), 23f, bold = true, serif = true, modifier = Modifier.padding(top = 14.dp, bottom = 2.dp))
        ForestProfileField(stringResource(R.string.txt_height), height, Icons.Default.Height, onHeightClick)
        ForestProfileField(stringResource(R.string.txt_weight), weight(profile.weightKg), Icons.Default.MonitorWeight, onWeightClick)
        ForestProfileField(stringResource(R.string.forest_profile_goal_weight), weight(profile.goalWeightKg), Icons.Default.Flag, onGoalWeightClick)
        ForestProfileField(stringResource(R.string.txt_frame_size), profile.frameSize.displayName, Icons.Default.Boy, onFrameSizeClick)
        ForestText(stringResource(R.string.txt_lifestyle_goals), 23f, bold = true, serif = true, modifier = Modifier.padding(top = 14.dp, bottom = 2.dp))
        ForestProfileField(stringResource(R.string.forest_profile_activity_level), profile.activityLevel.displayName, Icons.AutoMirrored.Filled.DirectionsRun, onActivityLevelClick)
        ForestProfileField(stringResource(R.string.forest_profile_health_goals), profile.healthGoals.takeIf { it.isNotEmpty() }?.joinToString(", ") { it.displayName } ?: notSet, Icons.Default.Favorite, onHealthGoalsClick)
        ForestProfileField(stringResource(R.string.forest_profile_ethnicity), profile.ethnicityRegion.displayName, Icons.Default.Public, onEthnicityClick)
    }
}

@Composable
private fun ForestProfileField(label: String, value: String, icon: ImageVector, onClick: () -> Unit) {
    ForestGlass(Modifier.fillMaxWidth(), onClick = onClick) {
        Row(Modifier.fillMaxWidth().heightIn(min = 62.dp).padding(horizontal = 12.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            ForestBadge(icon, modifier = Modifier.size(40.dp))
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                ForestText(label, 12f, BotanicalPalette.Secondary)
                Spacer(Modifier.height(3.dp))
                ForestText(value, 14f, bold = true)
            }
            Spacer(Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, Modifier.size(22.dp), tint = BotanicalPalette.Text)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ForestHealthOverview(
    uiState: ProfileUiState,
    weightStatistics: WeightStatistics?,
    onLogWeight: () -> Unit,
    onViewWeightTrends: () -> Unit,
    onMetricClick: (String) -> Unit
) {
    val overview = uiState.healthOverview
    val latestWeight = weightStatistics?.currentWeight ?: uiState.profile.weightKg?.toDouble()
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ForestText(stringResource(R.string.txt_your_health_status), 23f, bold = true, serif = true)
        ForestText(stringResource(R.string.txt_latest_results_from_your_calcu), 12f, BotanicalPalette.Secondary)
        ForestGlass(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ForestText(stringResource(R.string.txt_weight_tracking), 16f, bold = true)
                if (latestWeight != null && latestWeight.isFinite() && latestWeight > 0.0) {
                    val useMetric = uiState.profile.useMetricSystem
                    val displayedWeight = if (useMetric) latestWeight else latestWeight * 2.20462
                    ForestText(String.format(Locale.getDefault(), "%.1f %s", displayedWeight, if (useMetric) "kg" else "lbs"), 26f, bold = true)
                    if (weightStatistics != null && weightStatistics.totalEntries > 0) {
                        ForestText(weightStatistics.trendDirection.label, 12f, BotanicalPalette.Secondary)
                    }
                    weightStatistics?.averageWeeklyChange?.takeIf { it.isFinite() }?.let { weekly ->
                        val displayedWeekly = if (useMetric) weekly else weekly * 2.20462
                        ForestText(
                            stringResource(R.string.forest_profile_weekly_change,
                                String.format(Locale.getDefault(), "%+.2f", displayedWeekly),
                                if (useMetric) "kg" else "lbs"),
                            12f, BotanicalPalette.Secondary
                        )
                    }
                } else ForestText(stringResource(R.string.txt_no_weight_logged_yet), 14f, BotanicalPalette.Secondary)
                androidx.compose.foundation.layout.FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onLogWeight, modifier = Modifier.heightIn(min = 48.dp), colors = ButtonDefaults.buttonColors(containerColor = BotanicalPalette.Selected, contentColor = BotanicalPalette.Ink)) {
                        Text(stringResource(R.string.txt_log_weight))
                    }
                    OutlinedButton(onClick = onViewWeightTrends, modifier = Modifier.heightIn(min = 48.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = BotanicalPalette.Primary)) {
                        Text(stringResource(R.string.txt_view_trends))
                    }
                }
            }
        }
        listOfNotNull(overview.latestBmi, overview.latestBp, overview.latestWhr, overview.latestBmr, overview.metabolicSyndromeStatus).forEach { metric ->
            ForestProfileField(metric.label, "${metric.value} · ${metric.category}", Icons.Default.Assessment) { onMetricClick(metric.navigateRoute) }
        }
        ForestText(stringResource(R.string.txt_activity_nutrition), 20f, bold = true, serif = true)
        ForestGlass(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ForestText(stringResource(R.string.forest_profile_water_streak, pluralStringResource(R.plurals.forest_profile_days, overview.waterStreak, overview.waterStreak)), 14f)
                ForestText(stringResource(R.string.forest_profile_calorie_adherence, (overview.calorieAdherence * 100).toInt()), 14f)
            }
        }
        if (overview.healthScore >= 0) {
            ForestGlass(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ForestText(stringResource(R.string.txt_overall_health_score), 16f, bold = true)
                    ForestText(overview.healthScore.toString(), 28f, BotanicalPalette.Primary, bold = true)
                    ForestText(stringResource(R.string.txt_based_on_your_aggregated_healt), 12f, BotanicalPalette.Secondary)
                }
            }
        }
    }
}
