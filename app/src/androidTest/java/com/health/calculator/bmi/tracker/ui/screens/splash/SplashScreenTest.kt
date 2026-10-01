package com.health.calculator.bmi.tracker.ui.screens.splash

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.atomic.AtomicInteger

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {
    @get:Rule val composeRule = createComposeRule()

    @Test fun completesOnceAfterSplashSequence() {
        val completions = AtomicInteger(0)
        composeRule.setContent {
            SplashScreen(onSplashComplete = { completions.incrementAndGet() })
        }

        composeRule.waitUntil(timeoutMillis = 10_000) {
            completions.get() == 1
        }
        composeRule.waitForIdle()
        assertEquals(1, completions.get())
    }
}
