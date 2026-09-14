package com.health.calculator.bmi.tracker.calculator

import com.health.calculator.bmi.tracker.ui.screens.calculators.bmi.components.BMIEdgeCaseHandler
import com.health.calculator.bmi.tracker.ui.screens.calculators.bmi.components.EdgeCaseSeverity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class BMIEdgeCaseHandlerTest {
    @Test
    fun edgeCaseThresholdsRemainInformational() {
        assertEquals(EdgeCaseSeverity.CONCERN, BMIEdgeCaseHandler.getEdgeCaseMessage(9.9f)?.severity)
        assertEquals(EdgeCaseSeverity.CONCERN, BMIEdgeCaseHandler.getEdgeCaseMessage(50f)?.severity)
        assertNull(BMIEdgeCaseHandler.getEdgeCaseMessage(20f))
    }

    @Test
    fun nonFiniteBmiAndInputsAreNotSilentlyAccepted() {
        assertNotNull(BMIEdgeCaseHandler.getEdgeCaseMessage(Float.NaN))
        assertNotNull(BMIEdgeCaseHandler.getEdgeCaseMessage(Float.POSITIVE_INFINITY))
        assertNotNull(BMIEdgeCaseHandler.validateWeight(Float.NaN))
        assertNotNull(BMIEdgeCaseHandler.validateHeight(Float.POSITIVE_INFINITY))

        val result = BMIEdgeCaseHandler.validateInputs(Float.NaN, 170f, 30)
        assertFalse(result.isValid)
        assertNotNull(result.weightError)
    }
}
