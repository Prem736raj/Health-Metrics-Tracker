package com.health.calculator.bmi.tracker.ui.components.detail

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.ui.theme.AppColors
import kotlin.math.pow

/** Independent feature identities. These never change calculator/category data. */
enum class DetailFeature(val dayAccent: Color, val nightAccent: Color) {
    BMI(Color(0xFF285D42), Color(0xFFB9DEA7)),
    BMR(Color(0xFF825023), Color(0xFFF5D08B)),
    WATER(Color(0xFF1B6276), Color(0xFF8BD4E3)),
    CALORIES(Color(0xFF85462E), Color(0xFFF5BB91)),
    HEART_RATE(Color(0xFF934556), Color(0xFFF1B0BE)),
    BLOOD_PRESSURE(Color(0xFF8C3E4F), Color(0xFFEFB1B9)),
    WAIST_HIP(Color(0xFF645084), Color(0xFFD3BFE8)),
    BSA(Color(0xFF2C6864), Color(0xFF9CD9CE)),
    IDEAL_WEIGHT(Color(0xFF536536), Color(0xFFCDDB9C)),
    METABOLIC(Color(0xFF68602E), Color(0xFFE2D292)),
    WEIGHT(Color(0xFF386135), Color(0xFFB8D59D)),
    HISTORY(Color(0xFF516350), Color(0xFFBBCDB2)),
    SETTINGS(Color(0xFF3C6151), Color(0xFFB7D1BA)),
    CONNECTIONS(Color(0xFF2A6465), Color(0xFF9DD9D1)),
    MILESTONES(Color(0xFF78602A), Color(0xFFE4D093)),
    REMINDERS(Color(0xFF65587B), Color(0xFFCEC2E5)),
    REPORT(Color(0xFF4E6551), Color(0xFFBDDBAD)),
    ASSISTANT(Color(0xFF35635C), Color(0xFFA9D8C6)),
    ARTICLES(Color(0xFF65603D), Color(0xFFDAD4A6)),
    EXPORT(Color(0xFF47625A), Color(0xFFB4D1C4)),
    DATA(Color(0xFF4B5E70), Color(0xFFBED0E1)),
    WELCOME(Color(0xFF6C5934), Color(0xFFE3CD9C))
}

@Immutable
data class DetailPalette(
    val feature: DetailFeature,
    val dark: Boolean,
    val background: Color,
    val surface: Color,
    val raised: Color,
    val text: Color,
    val secondary: Color,
    val accent: Color,
    val onAccent: Color,
    val accentContainer: Color,
    val outline: Color,
    val subtleOutline: Color,
    val warning: Color,
    val success: Color,
    val danger: Color
) {
    // Material resolves content colors by surface equality. Keep the error fill
    // distinct so ordinary cards never inherit the error foreground.
    val errorSurface: Color get() = lerp(surface, danger, .08f)
}

/** Opaque foreground/surface pairs keep both daylight and forest views readable. */
fun detailPalette(feature: DetailFeature, dark: Boolean): DetailPalette {
    val surface = if (dark) Color(0xFF16251D) else Color(0xFFFFFEF8)
    val accent = if (dark) feature.nightAccent else feature.dayAccent
    return DetailPalette(
        feature, dark,
        background = if (dark) Color(0xFF0E1813) else Color(0xFFF9FAF2),
        surface = surface,
        raised = if (dark) Color(0xFF22352A) else Color(0xFFE9F0E2),
        text = if (dark) Color(0xFFF0F3E9) else Color(0xFF14261C),
        secondary = if (dark) Color(0xFFB7C8BA) else Color(0xFF4D5F53),
        accent = accent,
        onAccent = if (dark) Color(0xFF122219) else Color.White,
        accentContainer = lerp(surface, accent, if (dark) .12f else .08f),
        outline = if (dark) Color(0xFF789580) else Color(0xFF758571),
        subtleOutline = if (dark) Color(0xFF344C3D) else Color(0xFFCEDBC7),
        warning = if (dark) Color(0xFFF4CB86) else Color(0xFF80520A),
        success = if (dark) Color(0xFFB5DDA1) else Color(0xFF2D6538),
        danger = if (dark) Color(0xFFFFB2B9) else Color(0xFFA7303F)
    )
}

internal val LocalDetailPalette = staticCompositionLocalOf<DetailPalette?> { null }
val LocalDetailFeature = staticCompositionLocalOf { DetailFeature.BMI }

/** Scoped only at detail entry points; no preference, global palette or window mutation. */
@Composable
fun DetailScreenTheme(feature: DetailFeature, content: @Composable () -> Unit) {
    val inheritedDark = AppColors.isDark
    val palette = remember(feature, inheritedDark) { detailPalette(feature, inheritedDark) }
    val inherited = MaterialTheme.colorScheme
    val scheme = inherited.copy(
        primary = palette.accent, onPrimary = palette.onAccent,
        primaryContainer = palette.accentContainer, onPrimaryContainer = palette.text,
        secondary = palette.accent, onSecondary = palette.onAccent,
        secondaryContainer = palette.raised, onSecondaryContainer = palette.text,
        tertiary = palette.accent, onTertiary = palette.onAccent,
        tertiaryContainer = palette.raised, onTertiaryContainer = palette.text,
        background = palette.background, onBackground = palette.text,
        surface = palette.surface, onSurface = palette.text,
        surfaceVariant = palette.raised, onSurfaceVariant = palette.secondary,
        surfaceDim = palette.background, surfaceBright = palette.raised,
        surfaceContainerLowest = palette.background, surfaceContainerLow = palette.surface,
        surfaceContainer = palette.surface, surfaceContainerHigh = palette.raised,
        surfaceContainerHighest = palette.raised, surfaceTint = Color.Transparent,
        outline = palette.outline, outlineVariant = palette.subtleOutline,
        error = palette.danger, onError = palette.onAccent,
        errorContainer = palette.errorSurface, onErrorContainer = palette.text,
        inverseSurface = if (palette.dark) Color(0xFFE9F0E2) else Color(0xFF14261C),
        inverseOnSurface = if (palette.dark) Color(0xFF14261C) else Color(0xFFF0F3E9),
        inversePrimary = if (palette.dark) feature.dayAccent else feature.nightAccent
    )
    val typography = MaterialTheme.typography.let {
        it.copy(
            titleLarge = it.titleLarge.copy(fontFamily = FontFamily.Serif),
            bodySmall = it.bodySmall.copy(fontSize = 12.sp, lineHeight = 18.sp),
            labelSmall = it.labelSmall.copy(fontSize = 12.sp, lineHeight = 16.sp)
        )
    }
    CompositionLocalProvider(LocalDetailPalette provides palette, LocalDetailFeature provides feature) {
        MaterialTheme(scheme, typography = typography,
            shapes = MaterialTheme.shapes.copy(small = RoundedCornerShape(12.dp),
                medium = RoundedCornerShape(18.dp), large = RoundedCornerShape(24.dp)), content = content)
    }
}

/** AppColors-compatible helpers fall back to the original behavior outside detail scope. */
object DetailColors {
    val active: Boolean @Composable get() = LocalDetailPalette.current != null
    val isDark: Boolean @Composable get() = LocalDetailPalette.current?.dark ?: AppColors.isDark
    val Background: Color @Composable get() = LocalDetailPalette.current?.background ?: MaterialTheme.colorScheme.background
    val Surface: Color @Composable get() = LocalDetailPalette.current?.surface ?: MaterialTheme.colorScheme.surface
    val SurfaceAlt: Color @Composable get() = LocalDetailPalette.current?.raised ?: MaterialTheme.colorScheme.surfaceVariant
    val Text: Color @Composable get() = LocalDetailPalette.current?.text ?: MaterialTheme.colorScheme.onSurface
    val Secondary: Color @Composable get() = LocalDetailPalette.current?.secondary ?: MaterialTheme.colorScheme.onSurfaceVariant
    val Accent: Color @Composable get() = LocalDetailPalette.current?.accent ?: MaterialTheme.colorScheme.primary
    val OnAccent: Color @Composable get() = LocalDetailPalette.current?.onAccent ?: MaterialTheme.colorScheme.onPrimary
    val Outline: Color @Composable get() = LocalDetailPalette.current?.outline ?: MaterialTheme.colorScheme.outline
    val Warning: Color @Composable get() = LocalDetailPalette.current?.warning ?: MaterialTheme.colorScheme.tertiary
    val Success: Color @Composable get() = LocalDetailPalette.current?.success ?: MaterialTheme.colorScheme.primary
    val Danger: Color @Composable get() = LocalDetailPalette.current?.danger ?: MaterialTheme.colorScheme.error

    @Composable fun text(light: Color): Color = if (!active) AppColors.text(light)
        else if (LocalContentColor.current == MaterialTheme.colorScheme.onPrimary) OnAccent else Text
    @Composable fun secondaryText(light: Color): Color = if (!active) AppColors.secondaryText(light)
        else if (LocalContentColor.current == MaterialTheme.colorScheme.onPrimary) OnAccent else Secondary
    @Composable fun accent(light: Color): Color = if (active) Accent else AppColors.accent(light)
    @Composable fun icon(light: Color): Color = if (active) Accent else AppColors.icon(light)
    @Composable fun outline(light: Color): Color = if (active) Outline else AppColors.outline(light)
    @Composable fun surface(light: Color, level: Int = 1): Color = if (!active) AppColors.surface(light, level)
        else if (level > 1) SurfaceAlt else Surface
}

/** Pure WCAG math used by executable tests for every feature and brightness. */
fun detailContrastRatio(first: Color, second: Color): Double {
    fun luminance(c: Color): Double {
        fun linear(v: Float): Double = if (v <= .04045f) v / 12.92 else ((v + .055) / 1.055).pow(2.4)
        return .2126 * linear(c.red) + .7152 * linear(c.green) + .0722 * linear(c.blue)
    }
    val a = luminance(first); val b = luminance(second)
    return (maxOf(a, b) + .05) / (minOf(a, b) + .05)
}
