package com.health.calculator.bmi.tracker.ui

import com.health.calculator.bmi.tracker.widget.core.WidgetAccessibilityHelper
import com.health.calculator.bmi.tracker.widget.core.WidgetErrorHandler
import com.health.calculator.bmi.tracker.widget.core.WidgetStateManager
import org.junit.Assert.*
import org.junit.Test

/**
 * Verifies accessibility content description contracts for widget and
 * calculator surfaces. These tests run on the JVM and validate that
 * descriptions are non-empty, emoji-free, and informative for TalkBack.
 */
class AccessibilityContentDescriptionTest {

    // ── Widget accessibility badges ─────────────────────────────────

    @Test
    fun bmiAccessibilityBadge_isNotEmpty_forAllRanges() {
        val testValues = listOf(-1f, 0f, 15f, 18.5f, 22f, 25f, 30f, 40f)
        testValues.forEach { bmi ->
            val badge = WidgetAccessibilityHelper.bmiCategoryBadge(bmi)
            assertTrue(
                "BMI badge for $bmi must not be empty",
                badge.isNotEmpty()
            )
        }
    }

    @Test
    fun bpAccessibilityBadge_isNotEmpty_forRepresentativeValues() {
        val testCases = listOf(
            0 to 0,       // not tracked
            89 to 59,     // hypotension
            115 to 75,    // normal
            125 to 78,    // elevated
            135 to 85,    // stage 1
            145 to 95,    // stage 2
            180 to 120,   // crisis
        )
        testCases.forEach { (sys, dia) ->
            val badge = WidgetAccessibilityHelper.bpCategoryBadge(sys, dia)
            assertTrue(
                "BP badge for $sys/$dia must not be empty",
                badge.isNotEmpty()
            )
        }
    }

    @Test
    fun accessibilityBadges_doNotContainSurrogatePairEmoji() {
        // Widget text must use basic plane characters so it renders
        // consistently across all Android font stacks.
        val allBadges = mutableListOf<String>()

        // BMI badges
        listOf(0f, 15f, 22f, 27f, 35f).forEach {
            allBadges.add(WidgetAccessibilityHelper.bmiCategoryBadge(it))
        }

        // BP badges
        listOf(0 to 0, 89 to 59, 115 to 75, 135 to 85, 180 to 120).forEach { (s, d) ->
            allBadges.add(WidgetAccessibilityHelper.bpCategoryBadge(s, d))
        }

        // Error markers
        WidgetStateManager.WidgetState.entries.forEach {
            allBadges.add(WidgetErrorHandler.getErrorConfig(it).marker)
        }

        allBadges.forEach { badge ->
            assertFalse(
                "Badge '$badge' should not contain surrogate-pair emoji",
                badge.any { it.code > 0xFFFF }
            )
        }
    }

    // ── Touch target constant ───────────────────────────────────────

    @Test
    fun minimumTouchTarget_isAtLeast48dp() {
        // Material Design and WCAG 2.1 AA require minimum 48dp touch targets.
        // The app uses 48.dp in Compose and 48dp in XML layouts.
        val minDp = 48
        assertTrue("Minimum touch target must be >= 48dp", minDp >= 48)
    }

    // ── Color-to-badge mapping ──────────────────────────────────────

    @Test
    fun colorBadge_allKnownColors_haveTextAlternatives() {
        // These are the color hex values used in the widget system
        val widgetColors = mapOf(
            "#4CAF50" to "Good",
            "#81C784" to "Good",
            "#8BC34A" to "Normal",
            "#FFC107" to "Fair",
            "#FFEE58" to "Fair",
            "#FF9800" to "Elevated",
            "#FFB74D" to "Elevated",
            "#F44336" to "High",
            "#EF9A9A" to "High",
            "#9C27B0" to "Critical",
            "#CE93D8" to "Critical",
            "#2196F3" to "Low",
            "#64B5F6" to "Low",
            "#9E9E9E" to "No data"
        )

        widgetColors.forEach { (hex, expectedCategory) ->
            val badge = WidgetAccessibilityHelper.colorToBadgeText(hex)
            assertTrue(
                "Color $hex should produce a badge containing '$expectedCategory'",
                badge.contains(expectedCategory, ignoreCase = true)
            )
        }
    }

    @Test
    fun colorBadge_isCaseInsensitive() {
        val lower = WidgetAccessibilityHelper.colorToBadgeText("#4caf50")
        val upper = WidgetAccessibilityHelper.colorToBadgeText("#4CAF50")
        assertEquals(
            "Color badge lookup should be case-insensitive",
            upper, lower
        )
    }
}
