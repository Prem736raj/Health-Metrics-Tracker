package com.health.calculator.bmi.tracker.ui.screens.bloodpressure

import kotlin.math.abs
import kotlin.math.roundToInt

/** Same geometry for pointer selection and drawing; null means outside the hit radius. */
fun nearestBpChartPoint(x: Float, left: Float, width: Float, count: Int, radius: Float): Int? {
    if (!x.isFinite() || !left.isFinite() || !width.isFinite() || width <= 0f || count < 2) return null
    val step = width / (count - 1)
    val index = ((x - left) / step).roundToInt().coerceIn(0, count - 1)
    return index.takeIf { abs(x - (left + it * step)) <= radius }
}
