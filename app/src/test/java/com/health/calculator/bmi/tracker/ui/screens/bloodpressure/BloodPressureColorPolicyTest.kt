package com.health.calculator.bmi.tracker.ui.screens.bloodpressure

import com.health.calculator.bmi.tracker.data.model.BpCategory
import com.health.calculator.bmi.tracker.data.model.BpRiskLevel
import com.health.calculator.bmi.tracker.ui.theme.HealthColors
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Keeps blood-pressure presentation aligned with the shared semantic palette.
 * The calculator's clinical thresholds live in the data model; this only
 * protects the visual severity contract used by results, logs and widgets.
 */
class BloodPressureColorPolicyTest {

    @Test
    fun `category colors communicate increasing severity`() {
        assertEquals(HealthColors.Good, getBpCategoryColor(BpCategory.HYPOTENSION))
        assertEquals(HealthColors.Healthy, getBpCategoryColor(BpCategory.OPTIMAL))
        assertEquals(HealthColors.Healthy, getBpCategoryColor(BpCategory.NORMAL))
        assertEquals(HealthColors.Warning, getBpCategoryColor(BpCategory.HIGH_NORMAL))
        assertEquals(HealthColors.Caution, getBpCategoryColor(BpCategory.ISOLATED_SYSTOLIC))
        assertEquals(HealthColors.Caution, getBpCategoryColor(BpCategory.GRADE_1_HYPERTENSION))
        assertEquals(HealthColors.Danger, getBpCategoryColor(BpCategory.GRADE_2_HYPERTENSION))
        assertEquals(HealthColors.Danger, getBpCategoryColor(BpCategory.GRADE_3_HYPERTENSION))
        assertEquals(HealthColors.DangerDark, getBpCategoryColor(BpCategory.HYPERTENSIVE_CRISIS))
    }

    @Test
    fun `risk colors preserve low to emergency ordering`() {
        assertEquals(HealthColors.Healthy, getBpRiskColor(BpRiskLevel.LOW))
        assertEquals(HealthColors.Warning, getBpRiskColor(BpRiskLevel.MODERATE))
        assertEquals(HealthColors.Caution, getBpRiskColor(BpRiskLevel.HIGH))
        assertEquals(HealthColors.Danger, getBpRiskColor(BpRiskLevel.VERY_HIGH))
        assertEquals(HealthColors.DangerDark, getBpRiskColor(BpRiskLevel.EMERGENCY))
    }
}
