package com.health.calculator.bmi.tracker.ui.screens.whr

import dagger.hilt.android.qualifiers.ApplicationContext

import android.content.Context
import android.content.Intent
import com.health.calculator.bmi.tracker.data.model.*
import com.health.calculator.bmi.tracker.data.export.ExportDisclosurePolicy
import java.text.SimpleDateFormat
import java.util.*

object WhrShareUtils {

    fun buildShareText(result: WhrResult): String {
        return buildString {
            appendLine("Waist-to-Hip Ratio Result")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("WHR: ${String.format("%.2f", result.whr)} — ${result.whrCategory.label} (reference context)")
            appendLine("Waist: ${String.format("%.1f", result.waistCm)} cm")
            appendLine("Hip: ${String.format("%.1f", result.hipCm)} cm")
            appendLine("Body Shape: ${result.bodyShape.label}")
            appendLine("Waist Risk: ${result.waistRiskLevel.label}")
            result.whtr?.let {
                appendLine("WHtR: ${String.format("%.2f", it)} — ${if (result.whtrAtRisk == true) "At Risk" else "Normal"}")
            }
            appendLine("━━━━━━━━━━━━━━━━━━━━━━")
            append(ExportDisclosurePolicy.shareFooter())
        }
    }

    fun buildCompactShareText(result: WhrResult): String {
        val summary = "WHR: ${String.format("%.2f", result.whr)} - ${result.whrCategory.label} (reference context) | Waist: ${String.format("%.0f", result.waistCm)}cm | Hip: ${String.format("%.0f", result.hipCm)}cm"
        return ExportDisclosurePolicy.appendToShareText(summary)
    }

    fun buildDetailedShareText(result: WhrResult): String {
        return buildString {
            appendLine("WHR Report")
            appendLine("══════════════════════════════")
            appendLine()
            appendLine("Waist-to-Hip Ratio")
            appendLine("   Value: ${String.format("%.2f", result.whr)}")
            appendLine("   Category: ${result.whrCategory.label} (reference context)")
            appendLine("   Gender: ${if (result.gender == Gender.FEMALE) "Female" else "Male"}")
            appendLine("   Age: ${result.age} years")
            appendLine()
            appendLine("Measurements")
            appendLine("   Waist: ${String.format("%.1f", result.waistCm)} cm")
            appendLine("   Hip: ${String.format("%.1f", result.hipCm)} cm")
            appendLine()
            appendLine("Waist Circumference Reference")
            appendLine("   Status: ${result.waistRiskLevel.label}")
            appendLine("   Population reference: ${String.format("%.0f", result.waistThresholdIncreased)} cm")
            appendLine()
            result.whtr?.let {
                appendLine("Waist-to-Height Ratio")
                appendLine("   Value: ${String.format("%.2f", it)}")
                appendLine("   Reference flag: ${if (result.whtrAtRisk == true) "At or above 0.5" else "Below 0.5"}")
                appendLine()
            }
            appendLine("Body Shape: ${result.bodyShape.label}")
            appendLine()
            appendLine("══════════════════════════════")
            appendLine("Calculated on ${SimpleDateFormat("MMM d, yyyy 'at' h:mm a", Locale.getDefault()).format(Date())}")
            append(ExportDisclosurePolicy.shareFooter())
        }
    }

    fun shareResult(@ApplicationContext context: Context, text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_SUBJECT, "My WHR Result — Health Metrics Tracker")
        }
        context.startActivity(Intent.createChooser(intent, "Share WHR Result"))
    }
}
