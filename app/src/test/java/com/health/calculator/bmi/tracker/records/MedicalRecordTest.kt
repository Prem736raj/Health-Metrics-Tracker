package com.health.calculator.bmi.tracker.records

import com.health.calculator.bmi.tracker.data.records.MedicalRecord
import com.health.calculator.bmi.tracker.data.records.MedicalRecordKind
import com.health.calculator.bmi.tracker.data.records.normalize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class MedicalRecordTest {
    @Test
    fun readingsPreserveEnteredPrecisionAndUnits() {
        val glucose = MedicalRecordKind.GLUCOSE
        val reading = requireNotNull(glucose.normalize(record(glucose, "5,40", "mmol/L", "After meal"), 2))
        assertEquals("5.40 mmol/L", glucose.reportResult(reading))
        assertEquals("After meal", glucose.reportContext(reading))
        val temperature = MedicalRecordKind.TEMPERATURE
        val thermometer = requireNotNull(temperature.normalize(record(temperature, "98.60", "°F", "Oral"), 2))
        assertEquals("98.60 °F", temperature.reportResult(thermometer))
        assertEquals("Oral", temperature.reportContext(thermometer))
    }

    @Test
    fun validatesPercentPulseUnitsAndMeasurementTime() {
        val oxygen = MedicalRecordKind.OXYGEN
        assertNull(oxygen.normalize(record(oxygen, "101", "%"), 2))
        assertNull(oxygen.normalize(record(oxygen, "96", "%", detail = "30.5"), 2))
        assertNull(oxygen.normalize(record(oxygen, "96", "bpm"), 2))
        assertNull(oxygen.normalize(record(oxygen, "96", "%", timestamp = 3), 2))
        val checked = requireNotNull(oxygen.normalize(record(oxygen, "96", "%", detail = "071"), 2))
        assertEquals("96%", oxygen.reportResult(checked))
        assertEquals("Pulse 71 bpm", oxygen.reportContext(checked))
    }

    @Test
    fun reportsOmitPrivateMedicationInstructionsAndClinicianDetails() {
        val meds = MedicalRecordKind.MEDICATION
        val medication = requireNotNull(meds.normalize(record(meds, "Sample medication", "", "Stopped", "Private instructions"), 2))
        assertEquals("Sample medication", meds.reportResult(medication))
        assertEquals("Stopped", meds.reportContext(medication))
        val visits = MedicalRecordKind.VISIT
        val visit = record(visits, "Follow-up", "", detail = "Private clinician", timestamp = 3)
        assertNotNull(visits.normalize(visit, 2))
        assertEquals("Follow-up", visits.reportResult(visit))
        assertEquals("", visits.reportContext(visit))
    }

    private fun record(kind: MedicalRecordKind, value: String, unit: String, context: String = "",
        detail: String = "", timestamp: Long = 1
    ) = MedicalRecord("fixture", kind.name, timestamp, value, unit, context, detail, "Private note")
}
