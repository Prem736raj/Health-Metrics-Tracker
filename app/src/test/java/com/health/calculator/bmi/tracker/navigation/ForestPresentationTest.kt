package com.health.calculator.bmi.tracker.navigation

import com.health.calculator.bmi.tracker.core.navigation.Screen
import com.health.calculator.bmi.tracker.core.navigation.usesForestPresentation
import com.health.calculator.bmi.tracker.core.navigation.usesLightSystemBars
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ForestPresentationTest {
    private val forestRoutes = listOf(Screen.Track.route, Screen.Calculators.route, Screen.Insights.route, Screen.Profile.route)

    @Test fun referenceScreensFollowDayAndNightSystemIconContrast() {
        forestRoutes.forEach { route ->
            assertTrue(usesForestPresentation(route))
            assertTrue("day: $route", usesLightSystemBars(darkAppTheme = false))
            assertFalse("night: $route", usesLightSystemBars(darkAppTheme = true))
        }
    }

    @Test fun otherDestinationsRestoreTheSelectedAppTheme() {
        listOf(null, Screen.Home.route, Screen.Settings.route, Screen.BmiCalculator.route, Screen.History.route, Screen.Onboarding.route).forEach { route ->
            assertFalse(usesForestPresentation(route))
            assertTrue("day: $route", usesLightSystemBars(darkAppTheme = false))
            assertFalse("night: $route", usesLightSystemBars(darkAppTheme = true))
        }
    }
}
