package com.health.calculator.bmi.tracker.tracking

import com.health.calculator.bmi.tracker.domain.tracking.WeightInputPolicy
import org.junit.Assert.*
import org.junit.Test

class WeightInputPolicyTest {
    @Test fun decimalCommaAndPointHaveTheSameSaveValue() {
        assertEquals(70.53, WeightInputPolicy.kilograms("70,53", true)!!, 0.0)
        assertEquals(70.53, WeightInputPolicy.kilograms(" 70.53 ", true)!!, 0.0)
    }

    @Test fun editingDoesNotRoundTheStoredMeasurementInEitherUnit() {
        listOf(true, false).forEach { metric ->
            val text = WeightInputPolicy.editableValue(70.53123, metric)
            assertEquals(70.53123, WeightInputPolicy.kilograms(text, metric)!!, 1e-10)
        }
    }

    @Test fun invalidAndNonFiniteInputsNeverEnableSave() {
        listOf("", "0", "-5", "NaN", "Infinity", "1e999", "70,5.1").forEach {
            assertNull(WeightInputPolicy.kilograms(it, true))
        }
    }
}
