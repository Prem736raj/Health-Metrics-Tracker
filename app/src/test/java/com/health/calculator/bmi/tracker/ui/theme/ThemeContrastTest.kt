package com.health.calculator.bmi.tracker.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemeContrastTest {
    @Test fun opaqueMaterialTextPairsMeetNormalTextContrast() {
        val pairs = listOf(
            PrimaryLight to OnPrimaryLight, SecondaryLight to OnSecondaryLight,
            TertiaryLight to OnTertiaryLight, ErrorLight to OnErrorLight,
            SurfaceLight to OnSurfaceLight, BackgroundLight to OnBackgroundLight,
            SurfaceVariantLight to OnSurfaceVariantLight,
            PrimaryDark to OnPrimaryDark, SecondaryDark to OnSecondaryDark,
            TertiaryDark to OnTertiaryDark, ErrorDark to OnErrorDark,
            SurfaceDark to OnSurfaceDark, BackgroundDark to OnBackgroundDark,
            SurfaceVariantDark to OnSurfaceVariantDark
        )
        pairs.forEach { (background, foreground) ->
            assertTrue("Insufficient contrast: $foreground on $background", contrast(background, foreground) >= 4.5f)
        }
    }

    private fun contrast(a: Color, b: Color): Float =
        (maxOf(a.luminance(), b.luminance()) + 0.05f) / (minOf(a.luminance(), b.luminance()) + 0.05f)
}
