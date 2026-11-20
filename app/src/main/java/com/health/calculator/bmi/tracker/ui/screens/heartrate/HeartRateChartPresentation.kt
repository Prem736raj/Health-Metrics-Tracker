package com.health.calculator.bmi.tracker.ui.screens.heartrate

/** Padded observation bounds, including high readings and flat series. */
fun restingHrChartBounds(values: List<Int>): Pair<Int, Int> {
    val low = values.minOrNull() ?: 60
    val high = values.maxOrNull() ?: low
    return (low - 5) to (high + 5)
}

fun heartRateZoneSegment(low: Int, high: Int, axisLow: Int, axisHigh: Int): Pair<Float, Float> {
    val range = (axisHigh - axisLow).coerceAtLeast(1).toFloat()
    val start = ((low - axisLow) / range).coerceIn(0f, 1f)
    val end = ((high - axisLow) / range).coerceIn(start, 1f)
    return start to end
}
