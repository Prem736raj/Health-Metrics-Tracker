package com.health.calculator.bmi.tracker.data.calculator

import com.health.calculator.bmi.tracker.data.model.BMRFormula
import org.junit.Assert.*
import org.junit.Test

class BMRValidationConsistencyTest {
    @Test fun invalidHeightNeverPassesTheFormForAnyEquation() {
        BMRFormula.entries.forEach { formula ->
            listOf(0f, 99f, 251f, Float.NaN, Float.POSITIVE_INFINITY).forEach { height ->
                val validation = BMRValidation.validateInputs(70f, height, 30, 20f, formula)
                assertFalse("$formula accepted $height", validation.isValid)
                assertNotNull(validation.heightError)
                assertNull(BMRCalculator.calculate(70f, height, 30, true, 20f, formula))
            }
        }
    }

    @Test fun validAdultInputsPassValidationAndCalculation() {
        BMRFormula.entries.forEach { formula ->
            assertTrue(BMRValidation.validateInputs(70f, 175f, 30, 20f, formula).isValid)
            assertNotNull(BMRCalculator.calculate(70f, 175f, 30, true, 20f, formula))
        }
    }
}
