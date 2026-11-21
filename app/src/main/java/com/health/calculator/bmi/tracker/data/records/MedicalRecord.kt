package com.health.calculator.bmi.tracker.data.records

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal

internal enum class MedicalRecordKind(
    val title: String,
    val singular: String,
    val description: String,
    val valueLabel: String,
    val units: List<String> = emptyList(),
    val contextLabel: String = "",
    val contexts: List<String> = emptyList(),
    val detailLabel: String = ""
) {
    GLUCOSE("Blood glucose", "glucose reading", "Log a meter reading with its meal context.", "Glucose reading",
        listOf("mg/dL", "mmol/L"), "Meal context", listOf("Fasting", "Before meal", "After meal", "Other")),
    TEMPERATURE("Temperature", "temperature reading", "Record a thermometer reading and measurement site.", "Temperature",
        listOf("°C", "°F"), "Measurement site", listOf("Oral", "Ear", "Forehead", "Armpit", "Rectal", "Other")),
    OXYGEN("Oxygen saturation", "oxygen reading", "Record a pulse oximeter reading and optional pulse.", "SpO₂",
        listOf("%"), detailLabel = "Pulse (bpm, optional)"),
    MEDICATION("Medication list", "medication", "Keep names, prescription instructions and status together.", "Medication name",
        contextLabel = "Status", contexts = listOf("Current", "Stopped"), detailLabel = "Instructions as written"),
    VISIT("Visit journal", "visit", "Keep questions, visit notes and follow-up plans.", "Visit title",
        detailLabel = "Clinician or clinic");

    val isMeasurement: Boolean get() = units.isNotEmpty()
    val dateLabel: String get() = if (this == VISIT) "Visit date" else "Record date"

    fun reportResult(record: MedicalRecord): String =
        if (record.unit == "%") "${record.value}%" else listOf(record.value, record.unit).filter(String::isNotBlank).joinToString(" ")

    fun reportContext(record: MedicalRecord): String = when (this) {
        OXYGEN -> record.detail.takeIf(String::isNotBlank)?.let { "Pulse $it bpm" }.orEmpty()
        VISIT -> ""
        else -> record.context
    }
}

@Entity(tableName = "medical_records", indices = [Index("timestamp")])
internal data class MedicalRecord(
    @PrimaryKey val id: String,
    val kind: String,
    val timestamp: Long,
    val value: String,
    val unit: String,
    val context: String,
    val detail: String,
    val note: String
)

internal fun MedicalRecordKind.normalize(record: MedicalRecord, now: Long): MedicalRecord? {
    val value = record.value.trim()
    val detail = record.detail.trim()
    val note = record.note.trim()
    if (record.kind != name || (isMeasurement && record.timestamp > now)) return null
    if (value.isBlank() || value.length > 100 || detail.length > 250 || note.length > 2_000) return null
    if (record.unit != "" && record.unit !in units) return null
    if (record.context != "" && record.context !in contexts) return null

    if (!isMeasurement) return record.copy(value = value, detail = detail, note = note)
    if (record.unit !in units || value.length > 12) return null
    val decimalText = value.replace(',', '.')
    if (!decimalText.matches(Regex("-?[0-9]+(?:\\.[0-9]+)?"))) return null
    val decimal = decimalText.toBigDecimalOrNull() ?: return null
    when (this) {
        MedicalRecordKind.GLUCOSE -> if (decimal < BigDecimal.ZERO) return null
        MedicalRecordKind.OXYGEN -> {
            if (decimal < BigDecimal.ZERO || decimal > BigDecimal(100)) return null
            if (detail.isNotBlank() && (detail.toIntOrNull() ?: 0) !in 1..999) return null
        }
        else -> Unit
    }
    val canonicalDetail = if (this == MedicalRecordKind.OXYGEN) detail.toIntOrNull()?.toString().orEmpty() else detail
    return record.copy(value = decimalText, detail = canonicalDetail, note = note)
}
