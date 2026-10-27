package com.health.calculator.bmi.tracker.ui.screens.onboarding

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.performClick
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.text.TextLayoutResult
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.health.calculator.bmi.tracker.R
import com.health.calculator.bmi.tracker.data.model.ThemeMode
import com.health.calculator.bmi.tracker.ui.theme.HealthCalculatorTheme
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForestOnboardingScreenTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun threeReferencePages_areVisible_andProfileRemainsOptional() {
        var completed = 0
        var profiles = 0
        compose.runOnUiThread {
            compose.activity.actionBar?.hide()
            compose.activity.enableEdgeToEdge()
        }
        compose.setContent {
            HealthCalculatorTheme(themeMode = ThemeMode.DARK) {
                OnboardingScreen(
                    onComplete = { completed++ },
                    onSetUpProfile = { profiles++ },
                    onStartAction = {}
                )
            }
        }
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val resources = instrumentation.targetContext
        val next = resources.getString(R.string.txt_next)
        val titles = listOf(R.string.onboarding_welcome_title, R.string.onboarding_calculators_title, R.string.onboarding_start_title)
        titles.forEachIndexed { index, title ->
            val progress = resources.getString(R.string.onboarding_page_progress, index + 1, 3)
            compose.waitUntil(10_000) {
                compose.onAllNodesWithContentDescription(progress).fetchSemanticsNodes().isNotEmpty()
            }
            compose.waitForIdle()
            compose.onNodeWithText(resources.getString(title)).assertIsDisplayed()
            val screenshot = compose.onRoot().captureToImage().asAndroidBitmap()
            val directory = File(resources.getExternalFilesDir(null), "forest-reference").apply { mkdirs() }
            File(directory, "page-${index + 1}.png").outputStream().use { screenshot.compress(Bitmap.CompressFormat.PNG, 100, it) }
            screenshot.recycle()
            val visibleRoot = compose.onRoot().fetchSemanticsNode().boundsInRoot
            compose.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsActions.GetTextLayoutResult), useUnmergedTree = true)
                .fetchSemanticsNodes().filter { it.boundsInRoot.overlaps(visibleRoot) }.forEach { node ->
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config[SemanticsActions.GetTextLayoutResult].action?.invoke(layouts)
                    layouts.forEach { layout ->
                        // Paragraph width includes the alignment box. Check painted line widths,
                        // with one pixel for fractional sp rounding, rather than that empty box.
                        val lineTooWide = (0 until layout.lineCount).any { line ->
                            layout.getLineRight(line) - layout.getLineLeft(line) > layout.size.width + 1f
                        }
                        val clipped = layout.multiParagraph.didExceedMaxLines ||
                            lineTooWide ||
                            layout.multiParagraph.height > layout.size.height + 1f
                        assertFalse("Page ${index + 1} clips: ${layout.layoutInput.text.text}; size=${layout.size}; paragraph=${layout.multiParagraph.width} x ${layout.multiParagraph.height}", clipped)
                    }
                }
            if (index == 0) compose.onNodeWithText(resources.getString(R.string.onboarding_orbit_sleep)).assertIsDisplayed()
            if (index < 2) {
                compose.onNodeWithText(next).performClick()
                compose.mainClock.advanceTimeBy(700)
            }
        }
        val home = resources.getString(R.string.onboarding_explore_home)
        compose.onAllNodesWithText(home).assertCountEquals(1)
        compose.onAllNodesWithText(resources.getString(R.string.onboarding_explore_home_first)).assertCountEquals(0)
        compose.onNodeWithText(resources.getString(R.string.onboarding_set_up_profile_instead)).assertIsDisplayed().performClick()
        compose.runOnIdle {
            assertEquals(1, profiles)
            assertEquals(0, completed)
        }
    }
}
