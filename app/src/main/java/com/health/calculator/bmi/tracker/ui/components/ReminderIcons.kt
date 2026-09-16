package com.health.calculator.bmi.tracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector
import com.health.calculator.bmi.tracker.data.models.ReminderCategory

/**
 * Stable semantic icons for reminder categories.
 *
 * ReminderCategory retains its legacy emoji marker for persisted/exported data,
 * but Compose surfaces should use platform-independent vector icons so they
 * remain consistent across fonts, themes, and accessibility settings.
 */
internal fun reminderCategoryIcon(category: ReminderCategory): ImageVector = when (category) {
    ReminderCategory.WATER_INTAKE -> Icons.Outlined.WaterDrop
    ReminderCategory.BLOOD_PRESSURE -> Icons.Outlined.FavoriteBorder
    ReminderCategory.WEIGHT_CHECK -> Icons.Outlined.MonitorWeight
    ReminderCategory.MEDICATION -> Icons.Outlined.Medication
    ReminderCategory.EXERCISE -> Icons.Outlined.DirectionsRun
    ReminderCategory.CALORIE_LOGGING -> Icons.Outlined.Restaurant
    ReminderCategory.CUSTOM -> Icons.Outlined.Notifications
}
