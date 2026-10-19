package com.health.calculator.bmi.tracker

import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.health.calculator.bmi.tracker.core.navigation.NavGraph
import com.health.calculator.bmi.tracker.core.navigation.Screen
import com.health.calculator.bmi.tracker.data.model.ThemeMode
import com.health.calculator.bmi.tracker.ui.theme.HealthCalculatorTheme
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/** Real Hilt/Room/navigation rendering smoke; does not assert clinical correctness. */
@RunWith(AndroidJUnit4::class)
class ReleaseNavigationSmokeTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()
    private lateinit var controller: NavHostController

    private val routes = listOf(
        "home", "track", "calculators", "insights", "profile", "settings", "history",
        "calculator/bmi", "calculator/bmr", "calculator/blood_pressure",
        "calculator/waist_hip", "calculator/water_intake", "calculator/metabolic_syndrome",
        "calculator/bsa", "ideal_body_weight", "calculator/daily_calorie", "calculator/heart_rate_zone",
        "weight_tracking", "water_tracker", "food_log", "calorie_history",
        "blood_pressure_log", "blood_pressure_trends", "blood_pressure_reminders",
        "blood_pressure_export", "blood_pressure_education", "whr_education",
        "health_articles", "achievements", "export_data", "data_management",
        "health_connections", "reminders", "weekly_report", "water_history",
        "water_reminder_settings", "water_gamification", "water_hydration_tools",
        "water_education", "electrolyte_info"
    )

    @Test fun everyPrimaryDestinationRendersInLightTheme() = exerciseRoutes(ThemeMode.LIGHT, 1f)

    @Test fun everyPrimaryDestinationRendersWithLargeTextInDarkTheme() = exerciseRoutes(ThemeMode.DARK, 2f)

    @Test fun loggedWaterInsightOpensTheWaterJournal() {
        val app = composeRule.activity.application as HealthCalculatorApp
        val dao = com.health.calculator.bmi.tracker.data.local.AppDatabase
            .getDatabase(composeRule.activity).waterIntakeDao()
        val log = com.health.calculator.bmi.tracker.data.model.WaterIntakeLog(amountMl = 250, note = "Navigation QA fixture")
        val id = runBlocking {
            app.settingsDataStore.setOnboardingCompleted()
            dao.insertWaterLog(log)
        }
        val start = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        val end = (start.clone() as java.util.Calendar).apply { add(java.util.Calendar.DAY_OF_YEAR, 1) }
        val total = runBlocking { dao.getTotalWaterForDaySync(start.timeInMillis, end.timeInMillis) } ?: 0
        val waterText = "${java.text.NumberFormat.getIntegerInstance().format(total)} ml"
        try {
            composeRule.activityRule.scenario.onActivity { activity ->
                activity.setContent {
                    HealthCalculatorTheme(themeMode = ThemeMode.LIGHT) {
                        controller = rememberNavController()
                        NavGraph(controller)
                    }
                }
            }
            composeRule.waitUntil(30_000) {
                ::controller.isInitialized && controller.currentDestination?.route == Screen.Home.route &&
                    composeRule.onAllNodes(hasText(waterText)).fetchSemanticsNodes().isNotEmpty()
            }
            composeRule.onNode(hasScrollAction()).performScrollToNode(hasText("Hydration consistency"))
            composeRule.onNodeWithText("Log water").performScrollTo().performClick()
            composeRule.runOnIdle { assertEquals(Screen.WaterTracker.route, controller.currentDestination?.route) }
        } finally {
            runBlocking { dao.deleteWaterLog(log.copy(id = id)) }
        }
    }

    private fun exerciseRoutes(theme: ThemeMode, fontScale: Float) {
        val app = composeRule.activity.application as HealthCalculatorApp
        runBlocking { app.settingsDataStore.setOnboardingCompleted() }
        composeRule.activityRule.scenario.onActivity { activity ->
            activity.setContent {
                CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, fontScale)) {
                    HealthCalculatorTheme(themeMode = theme) {
                        controller = rememberNavController()
                        NavGraph(controller)
                    }
                }
            }
        }
        composeRule.waitUntil(30_000) {
            ::controller.isInitialized && controller.currentDestination?.route == Screen.Home.route
        }
        val screenshots = File(composeRule.activity.getExternalFilesDir(null), "release-qa/${theme.name}-${fontScale}")
            .apply { mkdirs() }
        for (route in routes) {
            android.util.Log.i("ReleaseQA", "Rendering $route in $theme at font scale $fontScale")
            composeRule.runOnIdle {
                controller.navigate(route) {
                    popUpTo(Screen.Home.route) { inclusive = false }
                    launchSingleTop = true
                }
            }
            composeRule.waitForIdle()
            composeRule.runOnIdle { assertEquals("Destination failed: $route", route, controller.currentDestination?.route?.substringBefore('?')) }
            assertTrue("No visible screen text for $route", composeRule.onAllNodes(hasText("", substring = true)).fetchSemanticsNodes().isNotEmpty())
            captureScreenshot(File(screenshots, route.replace('/', '_') + ".png"))
            if (route == "profile") {
                composeRule.onNodeWithText("Personal Details").performScrollTo().assertIsDisplayed()
                composeRule.onNodeWithText("Ethnicity / Region").performScrollTo().assertIsDisplayed()
                captureScreenshot(File(screenshots, "profile-edit.png"))
            }
        }
    }
    private fun captureScreenshot(file: File) {
        composeRule.waitForIdle()
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.waitForIdleSync()
        composeRule.waitUntil(5_000) {
            instrumentation.uiAutomation.rootInActiveWindow?.packageName?.toString() == composeRule.activity.packageName
        }
        assertEquals(
            "An external/system window obscures ${file.name}",
            composeRule.activity.packageName,
            instrumentation.uiAutomation.rootInActiveWindow?.packageName?.toString()
        )
        // Capture the actual display without PixelCopy's short render timeout,
        // which can fail on a headless emulator under concurrent compiler load.
        val bitmap = requireNotNull(instrumentation.uiAutomation.takeScreenshot()) {
            "No display screenshot available for ${file.name}"
        }
        file.outputStream().use {
            check(bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, it))
        }
        bitmap.recycle()
    }

}
