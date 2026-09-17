package com.health.calculator.bmi.tracker.widget

import com.health.calculator.bmi.tracker.widget.core.WidgetAccessibilityHelper
import com.health.calculator.bmi.tracker.widget.core.WidgetDataChangeReceiver
import com.health.calculator.bmi.tracker.widget.core.WidgetErrorHandler
import com.health.calculator.bmi.tracker.widget.core.WidgetStateManager
import com.health.calculator.bmi.tracker.widget.core.WidgetUpdateScheduler
import org.junit.Assert.*
import org.junit.Test

/**
 * Verifies widget lifecycle contracts that protect the user experience after
 * boot, midnight reset, data loss, and error conditions.
 */
class WidgetLifecycleTest {

    // ── Error handler ───────────────────────────────────────────────

    @Test
    fun everyWidgetState_hasNonBlankErrorConfig() {
        WidgetStateManager.WidgetState.entries.forEach { state ->
            val config = WidgetErrorHandler.getErrorConfig(state)
            assertFalse(
                "Error marker for $state must not be blank",
                config.marker.isBlank()
            )
            assertFalse(
                "Error title for $state must not be blank",
                config.title.isBlank()
            )
        }
    }

    @Test
    fun errorMarkers_areFontStable_noSurrogatePairEmoji() {
        WidgetStateManager.WidgetState.entries.forEach { state ->
            val marker = WidgetErrorHandler.getErrorConfig(state).marker
            assertFalse(
                "Error marker '$marker' for $state should not use surrogate-pair emoji",
                marker.any { it.code > 0xFFFF }
            )
        }
    }

    @Test
    fun shouldShowError_isTrueForAllNonHealthyStates() {
        WidgetStateManager.WidgetState.entries.forEach { state ->
            if (state == WidgetStateManager.WidgetState.HEALTHY) {
                assertFalse(WidgetErrorHandler.shouldShowError(state))
            } else {
                assertTrue(
                    "shouldShowError must be true for $state",
                    WidgetErrorHandler.shouldShowError(state)
                )
            }
        }
    }

    @Test
    fun staleBadge_isNullOnlyForHealthyAndError() {
        assertNull(WidgetErrorHandler.getStaleBadge(WidgetStateManager.WidgetState.HEALTHY))
        // Error is a separate display path, so no stale badge
        assertNull(WidgetErrorHandler.getStaleBadge(WidgetStateManager.WidgetState.ERROR))

        assertNotNull(WidgetErrorHandler.getStaleBadge(WidgetStateManager.WidgetState.STALE))
        assertNotNull(WidgetErrorHandler.getStaleBadge(WidgetStateManager.WidgetState.SETUP_REQUIRED))
        assertNotNull(WidgetErrorHandler.getStaleBadge(WidgetStateManager.WidgetState.EMPTY))
    }

    // ── Intent/action constants ─────────────────────────────────────

    @Test
    fun midnightResetAction_matchesManifestDeclaration() {
        // The manifest declares:
        //   <action android:name="com.health.calculator.bmi.tracker.MIDNIGHT_WIDGET_RESET" />
        assertEquals(
            "com.health.calculator.bmi.tracker.MIDNIGHT_WIDGET_RESET",
            WidgetUpdateScheduler.ACTION_MIDNIGHT_RESET
        )
    }

    @Test
    fun scheduledRefreshAction_matchesManifestDeclaration() {
        // The manifest declares:
        //   <action android:name="com.health.calculator.bmi.tracker.SCHEDULED_WIDGET_REFRESH" />
        assertEquals(
            "com.health.calculator.bmi.tracker.SCHEDULED_WIDGET_REFRESH",
            WidgetUpdateScheduler.ACTION_SCHEDULED_REFRESH
        )
    }

    @Test
    fun dataChangedAction_matchesManifestDeclaration() {
        // The manifest declares:
        //   <action android:name="com.health.calculator.bmi.tracker.WIDGET_DATA_CHANGED" />
        assertEquals(
            "com.health.calculator.bmi.tracker.WIDGET_DATA_CHANGED",
            WidgetDataChangeReceiver.ACTION_DATA_CHANGED
        )
    }

    // ── Accessibility badges ────────────────────────────────────────

    @Test
    fun bmiCategoryBadge_handlesAllRanges() {
        assertEquals("– Not tracked", WidgetAccessibilityHelper.bmiCategoryBadge(0f))
        assertEquals("– Not tracked", WidgetAccessibilityHelper.bmiCategoryBadge(-1f))
        assertEquals("▼ Underweight", WidgetAccessibilityHelper.bmiCategoryBadge(16f))
        assertEquals("✓ Normal", WidgetAccessibilityHelper.bmiCategoryBadge(22f))
        assertEquals("⚠ Overweight", WidgetAccessibilityHelper.bmiCategoryBadge(27f))
        assertEquals("✗ Obese", WidgetAccessibilityHelper.bmiCategoryBadge(35f))
    }

    @Test
    fun bmiCategoryBadge_exactBoundaries() {
        // 18.5 boundary: < 18.5 is underweight, >= 18.5 is normal
        assertEquals("▼ Underweight", WidgetAccessibilityHelper.bmiCategoryBadge(18.49f))
        assertEquals("✓ Normal", WidgetAccessibilityHelper.bmiCategoryBadge(18.5f))

        // 25 boundary
        assertEquals("✓ Normal", WidgetAccessibilityHelper.bmiCategoryBadge(24.99f))
        assertEquals("⚠ Overweight", WidgetAccessibilityHelper.bmiCategoryBadge(25f))

        // 30 boundary
        assertEquals("⚠ Overweight", WidgetAccessibilityHelper.bmiCategoryBadge(29.99f))
        assertEquals("✗ Obese", WidgetAccessibilityHelper.bmiCategoryBadge(30f))
    }

    @Test
    fun bpCategoryBadge_handlesNotTracked() {
        assertEquals("– Not tracked", WidgetAccessibilityHelper.bpCategoryBadge(0, 0))
        assertEquals("– Not tracked", WidgetAccessibilityHelper.bpCategoryBadge(-1, 80))
    }

    @Test
    fun colorBadge_unknownColorReturnsEmpty() {
        assertEquals("", WidgetAccessibilityHelper.colorToBadgeText("#000000"))
        assertEquals("", WidgetAccessibilityHelper.colorToBadgeText("#FFFFFF"))
    }

    @Test
    fun colorBadge_knownColorsReturnNonEmpty() {
        val knownColors = listOf(
            "#4CAF50", "#81C784", "#8BC34A", "#FFC107", "#FFEE58",
            "#FF9800", "#FFB74D", "#F44336", "#EF9A9A", "#9C27B0",
            "#CE93D8", "#2196F3", "#64B5F6", "#9E9E9E"
        )
        knownColors.forEach { hex ->
            val badge = WidgetAccessibilityHelper.colorToBadgeText(hex)
            assertTrue("Color $hex should produce a non-empty badge", badge.isNotEmpty())
        }
    }
}
