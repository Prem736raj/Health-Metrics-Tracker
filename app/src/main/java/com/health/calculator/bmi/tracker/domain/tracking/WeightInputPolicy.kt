package com.health.calculator.bmi.tracker.domain.tracking

/** Shared by the input UI and save path; edit values are never display-rounded. */
internal object WeightInputPolicy {
    private const val POUNDS_PER_KILOGRAM = 2.20462

    fun kilograms(text: String, useMetric: Boolean): Double? {
        val entered = text.trim().replace(',', '.').toDoubleOrNull() ?: return null
        val kilograms = if (useMetric) entered else entered / POUNDS_PER_KILOGRAM
        return kilograms.takeIf { TrackingQualityPolicy.validateWeightKg(it) == null }
    }

    fun editableValue(kilograms: Double, useMetric: Boolean): String =
        (if (useMetric) kilograms else kilograms * POUNDS_PER_KILOGRAM).toString()
}
