package com.health.calculator.bmi.tracker.data.export

/** Escapes spreadsheet-bound text without allowing user notes to become formulas. */
internal object CsvCellEncoder {
    fun encode(value: String): String {
        // Some spreadsheet applications ignore leading whitespace before formulas.
        val first = value.trimStart().firstOrNull()
        val safe = if (first in listOf('=', '+', '-', '@') ||
            value.firstOrNull() in listOf('\t', '\r', '\n')) "'$value" else value
        return if (safe.any { it == ',' || it == '"' || it == '\r' || it == '\n' }) {
            "\"${safe.replace("\"", "\"\"")}\""
        } else safe
    }
}
