package com.health.calculator.bmi.tracker.calculator

import com.health.calculator.bmi.tracker.data.calculator.MeasurementUnits
import com.health.calculator.bmi.tracker.data.calculator.parseMeasurement
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Locale

class MeasurementUnitsTest {
    @Test
    fun knownFactorsAndRoundTrips() {
        assertEquals(1.0, MeasurementUnits.WEIGHT.convert(0.45359237, fromMetric = true), 1e-10)
        assertEquals(2.54, MeasurementUnits.LENGTH.convert(1.0, fromMetric = false), 1e-10)
        assertEquals(236.5882365, MeasurementUnits.VOLUME.convert(8.0, fromMetric = false), 1e-8)
        MeasurementUnits.entries.forEach { units ->
            assertEquals(175.0, units.convert(units.convert(175.0, true), false), 1e-8)
        }
    }

    @Test
    fun localizedDecimalAndInvalidInput() {
        assertEquals(1.5, requireNotNull(parseMeasurement("1,5", Locale.GERMANY)), 1e-10)
        assertEquals(1.5, requireNotNull(parseMeasurement("1.5", Locale.US)), 1e-10)
        listOf("", "-1", "17 inches", "NaN", "Infinity", "5,7").forEach {
            assertNull(it, parseMeasurement(it, Locale.US))
        }
    }
}
