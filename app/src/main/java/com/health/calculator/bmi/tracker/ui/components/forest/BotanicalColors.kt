package com.health.calculator.bmi.tracker.ui.components.forest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.health.calculator.bmi.tracker.ui.theme.AppColors

/** Scoped day roles for the four botanical reference screens. */
object BotanicalDayRoles {
    val Background = Color(0xFFFDFCF5)
    val Surface = Color(0xFFFFFEFA)
    val Text = Color(0xFF091512)
    val Secondary = Color(0xFF535B60)
    val Primary = Color(0xFF285D30)
    val Outline = Color(0xFFD6E2CB)
    val Ink = Color(0xFF123C1B)
    val Water = Color(0xFF087CBF)
    val Amber = Color(0xFFA86A0B)
    val Coral = Color(0xFFD93650)
    val Purple = Color(0xFF7551CB)
    val Blue = Color(0xFF2878C4)
    val Selected = Color(0xFFDDF2C5)
    val Button = Color(0xFFD0E0B7)
    val Progress = Color(0xFF68AD4D)
    val Badge = Color(0xFFE6F4D9)
    val Hero = Color(0xFFE3EDCF)
}

/** Explicit renderer scope, independent of whether the selected theme is light. */
val LocalBotanicalScreen = staticCompositionLocalOf { false }

/** Night values delegate to the existing palette without changing its literals. */
object BotanicalPalette {
    val isLight: Boolean @Composable get() = !AppColors.isDark
    val Background: Color @Composable get() = if (isLight) BotanicalDayRoles.Background else ForestPalette.Background
    val Surface: Color @Composable get() = if (isLight) BotanicalDayRoles.Surface else ForestPalette.Surface
    val Text: Color @Composable get() = if (isLight) BotanicalDayRoles.Text else ForestPalette.Text
    val Secondary: Color @Composable get() = if (isLight) BotanicalDayRoles.Secondary else ForestPalette.Secondary
    val Primary: Color @Composable get() = if (isLight) BotanicalDayRoles.Primary else ForestPalette.Primary
    val Outline: Color @Composable get() = if (isLight) BotanicalDayRoles.Outline else ForestPalette.Outline
    val Ink: Color @Composable get() = if (isLight) BotanicalDayRoles.Ink else ForestPalette.Ink
    val Water: Color @Composable get() = if (isLight) BotanicalDayRoles.Water else ForestPalette.Water
    val Amber: Color @Composable get() = if (isLight) BotanicalDayRoles.Amber else ForestPalette.Amber
    val Coral: Color @Composable get() = if (isLight) BotanicalDayRoles.Coral else ForestPalette.Coral
    val Purple: Color @Composable get() = if (isLight) BotanicalDayRoles.Purple else ForestPalette.Purple
    val Blue: Color @Composable get() = if (isLight) BotanicalDayRoles.Blue else ForestPalette.Blue
    val Selected: Color @Composable get() = if (isLight) BotanicalDayRoles.Selected else ForestPalette.Primary
    val Button: Color @Composable get() = if (isLight) BotanicalDayRoles.Button else Color(0xFF345A30)
    val Progress: Color @Composable get() = if (isLight) BotanicalDayRoles.Progress else ForestPalette.Primary
}
