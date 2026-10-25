// ui/screens/waterintake/WaterReminderSettingsScreen.kt
package com.health.calculator.bmi.tracker.ui.screens.waterintake

import com.health.calculator.bmi.tracker.ui.theme.AppColors
import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.data.model.ReminderFrequency
import com.health.calculator.bmi.tracker.data.model.WaterReminderSettings
import com.health.calculator.bmi.tracker.data.datastore.SettingsDataStore
import com.health.calculator.bmi.tracker.data.preferences.WaterReminderPreferences
import com.health.calculator.bmi.tracker.notification.WaterReminderScheduler
import com.health.calculator.bmi.tracker.ui.theme.FeatureColors
import com.health.calculator.bmi.tracker.ui.theme.HealthColors

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.health.calculator.bmi.tracker.notifications.NotificationPermissionHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterReminderSettingsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val prefs = remember { WaterReminderPreferences(context) }
    val settingsDataStore = remember { SettingsDataStore(context) }
    val scheduler = remember { WaterReminderScheduler(context) }

    var settings by remember { mutableStateOf(prefs.load()) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var showSavedConfirm by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            settings = settings.copy(isEnabled = true)
        } else {
            settings = settings.copy(isEnabled = false)
        }
    }

    LaunchedEffect(Unit) { isVisible = true }

    // Auto-save and schedule when settings change
    LaunchedEffect(settings) {
        prefs.save(settings)
        // Keep the Settings surface and this detailed reminder editor in sync.
        // Enabling a concrete reminder is an explicit opt-in to the master
        // notification switch; disabling it leaves other categories alone.
        settingsDataStore.updateReminderSetting(
            remindersEnabled = if (settings.isEnabled) true else null,
            waterReminder = settings.isEnabled
        )
        if (settings.isEnabled) {
            scheduler.schedule(settings)
        } else {
            scheduler.cancel()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.NotificationsActive,
                            contentDescription = null,
                            tint = FeatureColors.WaterStart,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(stringResource(R.string.txt_water_reminders), fontWeight = FontWeight.Bold)
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(Modifier.height(4.dp))

                // Header Card
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { -40 }
                ) {
                    ReminderHeaderCard(settings.isEnabled)
                }

                // Main Toggle
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500, 100)) + slideInVertically(tween(500, 100)) { 40 }
                ) {
                    EnableToggleCard(
                        enabled = settings.isEnabled,
                        onToggle = { enable ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (enable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                !NotificationPermissionHelper.isNotificationPermissionGranted(context)) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                settings = settings.copy(isEnabled = enable)
                            }
                        }
                    )
                }

                // Schedule Settings
                AnimatedVisibility(
                    visible = isVisible && settings.isEnabled,
                    enter = fadeIn(tween(400)) + expandVertically(tween(400)),
                    exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Time Range
                        ScheduleCard(
                            settings = settings,
                            onStartTimeClick = { showStartTimePicker = true },
                            onEndTimeClick = { showEndTimePicker = true }
                        )

                        // Frequency
                        FrequencyCard(
                            selectedMinutes = settings.frequencyMinutes,
                            onSelect = { minutes ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                settings = settings.copy(frequencyMinutes = minutes)
                            },
                            wakingMinutes = settings.wakingMinutes,
                            goalMl = context.getSharedPreferences("water_intake_prefs", android.content.Context.MODE_PRIVATE)
                                .getInt("daily_goal_ml", 2500)
                        )

                        // Notification Style
                        NotificationStyleCard(
                            enableSound = settings.enableSound,
                            enableVibration = settings.enableVibration,
                            onSoundToggle = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                settings = settings.copy(enableSound = it)
                            },
                            onVibrationToggle = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                settings = settings.copy(enableVibration = it)
                            }
                        )

                        // Smart Features
                        SmartFeaturesCard(
                            smartSkip = settings.smartSkipEnabled,
                            behindNudge = settings.behindScheduleNudge,
                            onSmartSkipToggle = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                settings = settings.copy(smartSkipEnabled = it)
                            },
                            onBehindNudgeToggle = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                settings = settings.copy(behindScheduleNudge = it)
                            }
                        )

                        // Reminder summary
                        ReminderSummaryCard(settings)
                    }
                }
            }

            // Time Pickers
            if (showStartTimePicker) {
                WaterTimePicker(
                    initialHour = settings.startHour,
                    initialMinute = settings.startMinute,
                    title = "Reminder Start Time",
                    onConfirm = { hour, minute ->
                        settings = settings.copy(startHour = hour, startMinute = minute)
                        showStartTimePicker = false
                    },
                    onDismiss = { showStartTimePicker = false }
                )
            }

            if (showEndTimePicker) {
                WaterTimePicker(
                    initialHour = settings.endHour,
                    initialMinute = settings.endMinute,
                    title = "Reminder End Time",
                    onConfirm = { hour, minute ->
                        settings = settings.copy(endHour = hour, endMinute = minute)
                        showEndTimePicker = false
                    },
                    onDismiss = { showEndTimePicker = false }
                )
            }
        }
    }
}

@Composable
private fun ReminderHeaderCard(isEnabled: Boolean) {
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
                        colors = if (isEnabled) listOf(FeatureColors.WaterStart, FeatureColors.WaterDeep)
                        else listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surface)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = if (isEnabled) Icons.Outlined.NotificationsActive else Icons.Outlined.NotificationsOff,
                    contentDescription = null,
                    tint = if (isEnabled) FeatureColors.OnWater else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(40.dp)
                )
                Column {
                    Text(
                        text = if (isEnabled) "Reminders Active" else "Reminders Off",
                        color = AppColors.text(Color.White),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isEnabled)
                            "We'll help you stay hydrated throughout the day"
                        else
                            "Enable reminders to never forget to drink water",
                        color = AppColors.text(Color.White.copy(alpha = 0.8f)),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun EnableToggleCard(enabled: Boolean, onToggle: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            if (enabled) FeatureColors.WaterStart.copy(alpha = 0.14f)
                            else MaterialTheme.colorScheme.surfaceVariant,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        tint = if (enabled) FeatureColors.WaterStart else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        stringResource(R.string.txt_water_reminders),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        stringResource(R.string.txt_get_notified_to_drink_water),
                        fontSize = 12.sp,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    )
                }
            }
            Switch(
                checked = enabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = FeatureColors.WaterStart,
                    checkedThumbColor = Color.White
                )
            )
        }
    }
}

@Composable
private fun ScheduleCard(
    settings: WaterReminderSettings,
    onStartTimeClick: () -> Unit,
    onEndTimeClick: () -> Unit
) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = FeatureColors.WaterStart,
                    modifier = Modifier.size(20.dp)
                )
                Text(stringResource(R.string.txt_schedule), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Start Time
                TimePickerButton(
                    label = "Start Time",
                    time = settings.startTimeFormatted,
                    icon = Icons.Outlined.WbSunny,
                    onClick = onStartTimeClick,
                    modifier = Modifier.weight(1f)
                )

                // End Time
                TimePickerButton(
                    label = "End Time",
                    time = settings.endTimeFormatted,
                    icon = Icons.Outlined.NightsStay,
                    onClick = onEndTimeClick,
                    modifier = Modifier.weight(1f)
                )
            }

            // Active hours display
            val hours = settings.wakingMinutes / 60
            val mins = settings.wakingMinutes % 60
            Text(
                text = "Active window: ${hours}h ${if (mins > 0) "${mins}m" else ""}",
                fontSize = 12.sp,
                color = AppColors.text(FeatureColors.WaterDeep),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TimePickerButton(
    label: String,
    time: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.surface(FeatureColors.WaterStart.copy(alpha = 0.12f)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = FeatureColors.WaterDeep,
                modifier = Modifier.size(22.dp)
            )
            Text(label, fontSize = 12.sp, color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)))
            Text(time, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AppColors.text(FeatureColors.WaterDeep))
        }
    }
}

@Composable
private fun FrequencyCard(
    selectedMinutes: Int,
    onSelect: (Int) -> Unit,
    wakingMinutes: Int,
    goalMl: Int
) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = null,
                    tint = FeatureColors.WaterStart,
                    modifier = Modifier.size(20.dp)
                )
                Text(stringResource(R.string.txt_frequency), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

            // Suggested frequency
            val suggestedMinutes = if (wakingMinutes > 0 && goalMl > 0) {
                val glasses = goalMl / 250
                if (glasses > 0) (wakingMinutes / glasses) else 60
            } else 60

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = FeatureColors.WaterDeep,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Suggested: every ~${suggestedMinutes} min based on your goal",
                    fontSize = 12.sp,
                    color = AppColors.text(FeatureColors.WaterDeep),
                    fontWeight = FontWeight.Medium
                )
            }

            // Frequency options
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ReminderFrequency.entries.forEach { freq ->
                    val isSelected = selectedMinutes == freq.minutes
                    val borderColor by animateColorAsState(
                        if (isSelected) FeatureColors.WaterStart else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        label = "freq_border"
                    )
                    val bgColor by animateColorAsState(
                        if (isSelected) FeatureColors.WaterStart.copy(alpha = 0.12f) else Color.Transparent,
                        label = "freq_bg"
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(bgColor, RoundedCornerShape(12.dp))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelect(freq.minutes) }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            freq.label,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = AppColors.text(if (isSelected) FeatureColors.WaterDeep else MaterialTheme.colorScheme.onSurface)
                        )

                        val remindersPerDay = if (freq.minutes > 0) wakingMinutes / freq.minutes else 0
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "${remindersPerDay}x/day",
                                fontSize = 12.sp,
                                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            )
                            AnimatedVisibility(
                                visible = isSelected,
                                enter = scaleIn() + fadeIn(),
                                exit = scaleOut() + fadeOut()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .background(FeatureColors.WaterStart, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Check,
                                        null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
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
private fun NotificationStyleCard(
    enableSound: Boolean,
    enableVibration: Boolean,
    onSoundToggle: (Boolean) -> Unit,
    onVibrationToggle: (Boolean) -> Unit
) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = null,
                    tint = FeatureColors.WaterStart,
                    modifier = Modifier.size(20.dp)
                )
                Text(stringResource(R.string.txt_notification_style), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

            SettingsToggleRow(
                icon = Icons.Outlined.VolumeUp,
                title = "Sound",
                subtitle = "Play notification sound",
                checked = enableSound,
                onToggle = onSoundToggle
            )

            SettingsToggleRow(
                icon = Icons.Outlined.Vibration,
                title = "Vibration",
                subtitle = "Vibrate on reminder",
                checked = enableVibration,
                onToggle = onVibrationToggle
            )
        }
    }
}

@Composable
private fun SmartFeaturesCard(
    smartSkip: Boolean,
    behindNudge: Boolean,
    onSmartSkipToggle: (Boolean) -> Unit,
    onBehindNudgeToggle: (Boolean) -> Unit
) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = FeatureColors.WaterStart,
                    modifier = Modifier.size(20.dp)
                )
                Text(stringResource(R.string.txt_smart_features), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

            SettingsToggleRow(
                icon = Icons.Outlined.SkipNext,
                title = "Smart Skip",
                subtitle = "Skip reminder if you recently logged water",
                checked = smartSkip,
                onToggle = onSmartSkipToggle
            )

            SettingsToggleRow(
                icon = Icons.Outlined.WarningAmber,
                title = "Behind Schedule Nudge",
                subtitle = "Extra nudge when you're behind on hydration",
                checked = behindNudge,
                onToggle = onBehindNudgeToggle
            )
        }
    }
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onToggle(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (checked) FeatureColors.WaterStart else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(22.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(
                subtitle,
                fontSize = 12.sp,
                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedTrackColor = FeatureColors.WaterStart,
                checkedThumbColor = Color.White
            )
        )
    }
}

@Composable
private fun ReminderSummaryCard(settings: WaterReminderSettings) {
    val remindersPerDay = if (settings.frequencyMinutes > 0) {
        settings.wakingMinutes / settings.frequencyMinutes
    } else 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.surface(FeatureColors.WaterStart.copy(alpha = 0.1f)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.EventNote,
                    contentDescription = null,
                    tint = FeatureColors.WaterDeep,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    stringResource(R.string.txt_reminder_summary),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = AppColors.text(FeatureColors.WaterDeep)
                )
            }

            val summaryItems = listOf(
                Icons.Outlined.Schedule to "Active from ${settings.startTimeFormatted} to ${settings.endTimeFormatted}",
                Icons.Outlined.Repeat to "${settings.frequencyLabel} (~$remindersPerDay reminders/day)",
                Icons.Outlined.VolumeUp to "Sound: ${if (settings.enableSound) "On" else "Off"}",
                Icons.Outlined.Vibration to "Vibration: ${if (settings.enableVibration) "On" else "Off"}",
                Icons.Outlined.SkipNext to "Smart skip: ${if (settings.smartSkipEnabled) "Enabled" else "Disabled"}",
                Icons.Outlined.WarningAmber to "Behind-schedule nudge: ${if (settings.behindScheduleNudge) "Enabled" else "Disabled"}"
            )

            summaryItems.forEach { (icon, item) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = FeatureColors.WaterDeep,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = item,
                        fontSize = 13.sp,
                        color = AppColors.text(FeatureColors.WaterDeep.copy(alpha = 0.88f))
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WaterTimePicker(
    initialHour: Int,
    initialMinute: Int,
    title: String,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = false
    )

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = AppColors.text(FeatureColors.WaterDeep)
                )

                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        selectorColor = FeatureColors.WaterStart,
                        containerColor = FeatureColors.WaterStart.copy(alpha = 0.12f)
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.txt_cancel))
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onConfirm(timePickerState.hour, timePickerState.minute)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FeatureColors.WaterStart),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(stringResource(R.string.txt_set_time))
                    }
                }
            }
        }
    }
}
