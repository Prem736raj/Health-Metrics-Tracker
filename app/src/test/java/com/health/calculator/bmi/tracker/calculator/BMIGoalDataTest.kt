package com.health.calculator.bmi.tracker.calculator

import com.health.calculator.bmi.tracker.data.model.BMIGoalData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BMIGoalDataTest {
    @Test
    fun targetWeightUsesMetricBmiFormula() {
        // 21.7 at 180 cm = 21.7 * 1.8².
        assertEquals(70.308f, BMIGoalData.calculateTargetWeight(21.7f, 180f), 0.001f)
    }

    @Test
    fun bmiFromWeightRejectsNonPositiveAndNonFiniteInputs() {
        assertEquals(23.148f, BMIGoalData.calculateBMIFromWeight(75f, 180f), 0.001f)
        assertEquals(0f, BMIGoalData.calculateBMIFromWeight(0f, 180f), 0f)
        assertEquals(0f, BMIGoalData.calculateBMIFromWeight(Float.NaN, 180f), 0f)
        assertEquals(0f, BMIGoalData.calculateBMIFromWeight(75f, 0f), 0f)
    }

    @Test
    fun targetInputGuardrailsIncludeDocumentedBoundaries() {
        assertTrue(BMIGoalData.isValidTargetBmi(BMIGoalData.MIN_TARGET_BMI))
        assertTrue(BMIGoalData.isValidTargetBmi(BMIGoalData.MAX_TARGET_BMI))
        assertFalse(BMIGoalData.isValidTargetBmi(BMIGoalData.MIN_TARGET_BMI - 0.1f))
        assertFalse(BMIGoalData.isValidTargetBmi(Float.NaN))

        assertTrue(BMIGoalData.isValidTargetWeightKg(BMIGoalData.MIN_TARGET_WEIGHT_KG))
        assertTrue(BMIGoalData.isValidTargetWeightKg(BMIGoalData.MAX_TARGET_WEIGHT_KG))
        assertFalse(BMIGoalData.isValidTargetWeightKg(BMIGoalData.MAX_TARGET_WEIGHT_KG + 0.1f))
        assertFalse(BMIGoalData.isValidTargetWeightKg(Float.POSITIVE_INFINITY))
    }
}
