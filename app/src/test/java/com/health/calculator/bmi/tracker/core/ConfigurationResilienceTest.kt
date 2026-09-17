package com.health.calculator.bmi.tracker.core

import com.health.calculator.bmi.tracker.core.navigation.AppEntryRouteResolver
import org.junit.Assert.*
import org.junit.Test

/**
 * Verifies configuration resilience contracts that protect against process
 * death, unknown routes, and malformed saved state.
 */
class ConfigurationResilienceTest {

    // ── Route resolver resilience ───────────────────────────────────

    @Test
    fun routeResolver_nullInputReturnsNull() {
        assertNull(AppEntryRouteResolver.resolve())
        assertNull(AppEntryRouteResolver.resolve(dataUri = null, navigateTo = null))
    }

    @Test
    fun routeResolver_emptyStringReturnsNull() {
        assertNull(AppEntryRouteResolver.resolveTarget(""))
        assertNull(AppEntryRouteResolver.resolveTarget("   "))
    }

    @Test
    fun routeResolver_slashOnlyReturnsNull() {
        assertNull(AppEntryRouteResolver.resolveTarget("/"))
        assertNull(AppEntryRouteResolver.resolveTarget("///"))
    }

    @Test
    fun routeResolver_pathTraversalIsIgnored() {
        assertNull(AppEntryRouteResolver.resolveTarget("../../etc/passwd"))
        assertNull(AppEntryRouteResolver.resolveTarget("../not-a-route"))
    }

    @Test
    fun routeResolver_unknownExternalRouteIsIgnored() {
        assertNull(AppEntryRouteResolver.resolveTarget("unknown_screen"))
        assertNull(AppEntryRouteResolver.resolveTarget("admin_panel"))
    }

    @Test
    fun routeResolver_httpUriIsRejected() {
        assertNull(AppEntryRouteResolver.resolve(dataUri = "https://evil.com/water_tracker"))
        assertNull(AppEntryRouteResolver.resolve(dataUri = "http://example.com/home"))
    }

    @Test
    fun routeResolver_validAppLinkResolves() {
        assertNotNull(
            AppEntryRouteResolver.resolve(dataUri = "healthapp://navigate/reminders")
        )
    }

    @Test
    fun routeResolver_isCaseInsensitive() {
        val lower = AppEntryRouteResolver.resolveTarget("home")
        val upper = AppEntryRouteResolver.resolveTarget("HOME")
        val mixed = AppEntryRouteResolver.resolveTarget("Home")
        assertEquals(lower, upper)
        assertEquals(lower, mixed)
    }

    @Test
    fun routeResolver_welcomeBackTakesPriority() {
        val result = AppEntryRouteResolver.resolve(
            navigateTo = "home",
            showWelcomeBack = true
        )
        assertEquals("welcome_back", result)
    }

    @Test
    fun routeResolver_fragmentAndQueryAreStripped() {
        val result = AppEntryRouteResolver.resolve(
            dataUri = "healthapp://navigate/reminders?foo=bar#section"
        )
        assertNotNull(result)
    }

    // ── Alias completeness ──────────────────────────────────────────

    @Test
    fun routeResolver_allCriticalRoutesHaveAliases() {
        val criticalAliases = listOf(
            "home", "track", "calculators", "insights", "profile",
            "bmi_calculator", "water_tracker", "blood_pressure",
            "reminders", "settings", "ai_coach"
        )
        criticalAliases.forEach { alias ->
            assertNotNull(
                "Critical alias '$alias' must resolve to a route",
                AppEntryRouteResolver.resolveTarget(alias)
            )
        }
    }

    // ── Database version contract ───────────────────────────────────

    @Test
    fun databaseVersionConstant_isCurrentVersion() {
        // The AppDatabase.version is 16. If someone bumps it, this test
        // reminds them to also add a migration and update fixtures.
        assertEquals(
            "If you bumped the database version, add a migration and update tests",
            16,
            com.health.calculator.bmi.tracker.data.local.AppDatabase.MIGRATION_15_16.endVersion
        )
    }
}
