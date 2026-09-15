package com.health.calculator.bmi.tracker.ui.screens.calculators.bmi.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import com.health.calculator.bmi.tracker.ui.screens.calculators.bmi.BmiCategory
import com.health.calculator.bmi.tracker.ui.screens.calculators.bmi.bmiCategoryUiColor
import com.health.calculator.bmi.tracker.ui.screens.calculators.bmi.bmiValueUiColor
import com.health.calculator.bmi.tracker.ui.theme.HealthColors

class BmiSliderPolicyTest {

    @Test
    fun markerFractionIsClampedAtThePreviewEdges() {
        assertEquals(0f, bmiMarkerFraction(5f), 0.0001f)
        assertEquals(0f, bmiMarkerFraction(12f), 0.0001f)
        assertEquals(0.5f, bmiMarkerFraction(31f), 0.0001f)
        assertEquals(1f, bmiMarkerFraction(50f), 0.0001f)
        assertEquals(1f, bmiMarkerFraction(80f), 0.0001f)
    }

    @Test
    fun markerFractionNeverProducesNonFiniteValues() {
        assertTrue(bmiMarkerFraction(Float.NaN).isFinite())
        assertTrue(bmiMarkerFraction(Float.POSITIVE_INFINITY).isFinite())
        assertTrue(bmiMarkerFraction(Float.NEGATIVE_INFINITY).isFinite())
    }

    @Test
    fun bmiScaleUsesSharedSemanticRolesAtCategoryBoundaries() {
        assertEquals(HealthColors.Severe, bmiCategoryUiColor(BmiCategory.SEVERE_THINNESS))
        assertEquals(HealthColors.Healthy, bmiValueUiColor(18.5f))
        assertEquals(HealthColors.Warning, bmiValueUiColor(25f))
        assertEquals(HealthColors.Severe, bmiValueUiColor(40f))
    }

    @Test
    fun invalidPreviewValueUsesNeutralInformationalRole() {
        assertEquals(HealthColors.Info, bmiValueUiColor(Float.NaN))
        assertEquals(HealthColors.Info, bmiValueUiColor(0f))
    }
}
