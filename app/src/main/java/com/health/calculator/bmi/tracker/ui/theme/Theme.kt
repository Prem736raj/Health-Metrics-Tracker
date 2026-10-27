package com.health.calculator.bmi.tracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.health.calculator.bmi.tracker.data.model.ThemeMode
import com.health.calculator.bmi.tracker.ui.components.forest.BotanicalDayRoles
import com.health.calculator.bmi.tracker.ui.components.forest.LocalBotanicalScreen

// ============================================================================
// LIGHT COLOR SCHEME
// ============================================================================

private val LightColorScheme = lightColorScheme(
    // Primary
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,

    // Secondary
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,

    // Tertiary
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,

    // Error
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,

    // Background
    background = BackgroundLight,
    onBackground = OnBackgroundLight,

    // Surface
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    surfaceTint = SurfaceTintLight,
    inverseSurface = InverseSurfaceLight,
    inverseOnSurface = InverseOnSurfaceLight,
    inversePrimary = InversePrimaryLight,

    // Outline
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,

    // Scrim
    scrim = ScrimLight,
)

// ============================================================================
// DARK COLOR SCHEME
// ============================================================================

private val DarkColorScheme = darkColorScheme(
    surfaceDim = BackgroundDark,
    surfaceBright = DarkRoles.Level3,
    surfaceContainerLowest = BackgroundDark,
    surfaceContainerLow = DarkRoles.Level1,
    surfaceContainer = DarkRoles.Level2,
    surfaceContainerHigh = DarkRoles.Level3,
    surfaceContainerHighest = DarkRoles.Level3,
    // Primary
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,

    // Secondary
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,

    // Tertiary
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,

    // Error
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,

    // Background
    background = BackgroundDark,
    onBackground = OnBackgroundDark,

    // Surface
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    surfaceTint = SurfaceTintDark,
    inverseSurface = InverseSurfaceDark,
    inverseOnSurface = InverseOnSurfaceDark,
    inversePrimary = InversePrimaryDark,

    // Outline
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,

    // Scrim
    scrim = ScrimDark,
)

// ============================================================================
// THEME COMPOSABLE
// ============================================================================

/** Day defaults are scoped to the botanical screens; the global light scheme is unchanged. */
private val BotanicalLightColorScheme = LightColorScheme.copy(
    primary = BotanicalDayRoles.Primary,
    onPrimary = Color.White,
    primaryContainer = BotanicalDayRoles.Selected,
    onPrimaryContainer = BotanicalDayRoles.Ink,
    secondary = BotanicalDayRoles.Primary,
    onSecondary = Color.White,
    secondaryContainer = BotanicalDayRoles.Button,
    onSecondaryContainer = BotanicalDayRoles.Ink,
    background = BotanicalDayRoles.Background,
    onBackground = BotanicalDayRoles.Text,
    surface = BotanicalDayRoles.Surface,
    onSurface = BotanicalDayRoles.Text,
    surfaceVariant = BotanicalDayRoles.Selected,
    onSurfaceVariant = BotanicalDayRoles.Secondary,
    surfaceTint = BotanicalDayRoles.Primary,
    surfaceContainerLowest = BotanicalDayRoles.Surface,
    surfaceContainerLow = BotanicalDayRoles.Background,
    surfaceContainer = BotanicalDayRoles.Surface,
    surfaceContainerHigh = BotanicalDayRoles.Background,
    surfaceContainerHighest = BotanicalDayRoles.Selected,
    outline = BotanicalDayRoles.Primary,
    outlineVariant = BotanicalDayRoles.Outline
)

/** Local defaults follow app brightness without changing its preference or window flags. */
@Composable
fun ForestScreenTheme(content: @Composable () -> Unit) {
    val scheme = if (AppColors.isDark) DarkColorScheme else BotanicalLightColorScheme
    CompositionLocalProvider(LocalBotanicalScreen provides true) {
        MaterialTheme(
            colorScheme = scheme,
            typography = MaterialTheme.typography,
            shapes = MaterialTheme.shapes,
            content = content
        )
    }
}

@Composable
fun HealthCalculatorTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    // Dynamic color available on Android 12+ - disabled by default
    // to preserve our carefully chosen medical color palette
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Update system bars to match theme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Make status bar transparent so our content can draw behind it
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()

            val insetsController = WindowCompat.getInsetsController(window, view)
            // In light theme, use dark icons on status bar (and vice versa)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = HealthTypography,
        shapes = HealthShapes,
        content = content
    )
}
