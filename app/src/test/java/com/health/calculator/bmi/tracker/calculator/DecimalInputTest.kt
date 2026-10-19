package com.health.calculator.bmi.tracker.calculator

import com.health.calculator.bmi.tracker.core.util.DecimalInput
import org.junit.Assert.*
import org.junit.Test
import java.util.Locale

class DecimalInputTest {
    @Test fun profileAndUnitConversionRoundTripInDifferentLocales() {
        val original = Locale.getDefault()
        try {
            listOf(Locale.US, Locale.FRANCE, Locale.GERMANY, Locale.forLanguageTag("ar-EG")).forEach {
                Locale.setDefault(it)
                assertEquals(70f, DecimalInput.parse(DecimalInput.format(70f))!!, 0f)
                val pounds = DecimalInput.parse(DecimalInput.format(70f * 2.20462f))!!
                assertEquals(154.3f, pounds, 0.001f)
                assertEquals(70f, DecimalInput.parse(DecimalInput.format(pounds / 2.20462f))!!, 0.1f)
                assertEquals(175f, DecimalInput.parse(DecimalInput.format(175f, 0))!!, 0f)
            }
        } finally {
            Locale.setDefault(original)
        }
    }

    @Test fun decimalKeyboardInputAcceptsCommaAndPoint() {
        assertEquals(70.5f, DecimalInput.parse(" 70,5 ")!!, 0f)
        assertEquals(70.5f, DecimalInput.parse("70.5")!!, 0f)
    }

    @Test fun malformedAndNonFiniteValuesAreRejected() {
        listOf("", "NaN", "Infinity", "-Infinity", "1e999", "70,5.1", "1,234,567").forEach {
            assertNull(it, DecimalInput.parse(it))
        }
        assertEquals("", DecimalInput.format(Float.NaN))
        assertEquals("", DecimalInput.format(Float.POSITIVE_INFINITY))
    }
}
