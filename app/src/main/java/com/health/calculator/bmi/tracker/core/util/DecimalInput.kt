package com.health.calculator.bmi.tracker.core.util

import java.util.Locale

/** Editable numbers use a stable decimal point and accept comma decimal keyboards. */
internal object DecimalInput {
    fun parse(text: String): Float? =
        text.trim().replace(',', '.').toFloatOrNull()?.takeIf { it.isFinite() }

    fun format(value: Float, decimals: Int = 1): String {
        require(decimals in 0..6)
        return if (value.isFinite()) String.format(Locale.ROOT, "%.$decimals" + "f", value) else ""
    }
}
