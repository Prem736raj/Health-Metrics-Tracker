package com.health.calculator.bmi.tracker.data.calculator

import java.text.NumberFormat
import java.text.ParsePosition
import java.util.Locale

internal enum class MeasurementUnits(
    val title: String,
    val metric: String,
    val customary: String,
    private val metricPerCustomary: Double
) {
    WEIGHT("Weight", "kg", "lb", 0.45359237),
    LENGTH("Length", "cm", "in", 2.54),
    VOLUME("Volume", "mL", "US fl oz", 29.5735295625);

    fun convert(value: Double, fromMetric: Boolean): Double =
        if (fromMetric) value / metricPerCustomary else value * metricPerCustomary
}

internal fun parseMeasurement(text: String, locale: Locale): Double? {
    val input = text.trim()
    val parser = NumberFormat.getNumberInstance(locale).apply { isGroupingUsed = false }
    val position = ParsePosition(0)
    val parsed = parser.parse(input, position)?.toDouble()?.takeIf { position.index == input.length }
    return (parsed ?: input.toDoubleOrNull())
        ?.takeIf { it.isFinite() && it >= 0 }
}
