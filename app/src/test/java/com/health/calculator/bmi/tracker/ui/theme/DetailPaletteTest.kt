package com.health.calculator.bmi.tracker.ui.theme

import com.health.calculator.bmi.tracker.ui.components.detail.DetailFeature
import com.health.calculator.bmi.tracker.ui.components.detail.detailContrastRatio
import com.health.calculator.bmi.tracker.ui.components.detail.detailPalette
import org.junit.Assert.*
import org.junit.Test

class DetailPaletteTest {
    @Test fun everyFeatureHasReadableTextSurfacesInBothModes() {
        DetailFeature.entries.forEach { feature ->
            listOf(false, true).forEach { dark ->
                val p = detailPalette(feature, dark)
                val surfaces = listOf(p.background, p.surface, p.raised, p.accentContainer, p.errorSurface)
                assertNotEquals("Error fill must not alias an ordinary card", p.surface, p.errorSurface)
                val text = mapOf("body" to p.text, "secondary" to p.secondary,
                    "accent" to p.accent, "warning" to p.warning,
                    "success" to p.success, "danger" to p.danger)
                text.forEach { (role, color) ->
                    surfaces.forEachIndexed { index, surface ->
                        val contrast = detailContrastRatio(color, surface)
                        assertTrue("$feature dark=$dark $role/surface$index = $contrast", contrast >= 4.5)
                    }
                }
                assertTrue("$feature dark=$dark button", detailContrastRatio(p.onAccent, p.accent) >= 4.5)
                surfaces.take(3).forEach { surface ->
                    assertTrue("$feature dark=$dark functional outline", detailContrastRatio(p.outline, surface) >= 3.0)
                }
            }
        }
    }

    @Test fun dayAndNightAreDistinctForEveryFeature() {
        DetailFeature.entries.forEach { feature ->
            val day = detailPalette(feature, false)
            val night = detailPalette(feature, true)
            assertNotEquals(day.background, night.background)
            assertNotEquals(day.surface, night.surface)
            assertNotEquals(day.text, night.text)
            assertNotEquals(day.accent, night.accent)
            assertFalse(day.dark)
            assertTrue(night.dark)
        }
        assertNotEquals(DetailFeature.BMI.dayAccent, DetailFeature.BMR.dayAccent)
        assertNotEquals(DetailFeature.BMI.nightAccent, DetailFeature.BMR.nightAccent)
    }
}
