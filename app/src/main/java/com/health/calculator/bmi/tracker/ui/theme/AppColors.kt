package com.health.calculator.bmi.tracker.ui.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/** Uses the app's selected theme, including a manual override of the system theme.
 * Each light argument is the exact expression the caller used before the dark audit.
 */
object AppColors {
    val isDark: Boolean
        @Composable get() = MaterialTheme.colorScheme.background.luminance() < 0.5f

    @Composable fun text(light: Color): Color = when {
        !isDark -> light
        LocalContentColor.current == MaterialTheme.colorScheme.onPrimary -> MaterialTheme.colorScheme.onPrimary
        else -> DarkRoles.Text
    }
    @Composable fun secondaryText(light: Color): Color = when {
        !isDark -> light
        LocalContentColor.current == MaterialTheme.colorScheme.onPrimary -> MaterialTheme.colorScheme.onPrimary
        else -> DarkRoles.SecondaryText
    }
    @Composable fun accent(light: Color): Color = if (isDark) DarkRoles.Accent else light
    @Composable fun icon(light: Color): Color = if (isDark) DarkRoles.Accent else light
    @Composable fun outline(light: Color): Color = if (isDark) DarkRoles.SubtleOutline else light
    @Composable fun surface(light: Color, level: Int = 1): Color = if (!isDark) light else when (level) {
        3 -> DarkRoles.Level3
        2 -> DarkRoles.Level2
        else -> DarkRoles.Level1
    }
}
