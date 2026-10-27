package com.health.calculator.bmi.tracker.presentation.navigation

import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.TextLayoutResult
import com.health.calculator.bmi.tracker.core.navigation.Screen
import com.health.calculator.bmi.tracker.data.model.ThemeMode
import com.health.calculator.bmi.tracker.data.models.HealthJourneySummary
import com.health.calculator.bmi.tracker.domain.insights.DeterministicInsightEngine
import com.health.calculator.bmi.tracker.domain.insights.DeterministicInsightInput
import com.health.calculator.bmi.tracker.domain.usecases.ProfileCompletionUseCase
import com.health.calculator.bmi.tracker.presentation.components.BottomNavigationBar
import com.health.calculator.bmi.tracker.presentation.profile.*
import com.health.calculator.bmi.tracker.ui.theme.ForestScreenTheme
import com.health.calculator.bmi.tracker.ui.theme.AppColors
import com.health.calculator.bmi.tracker.ui.theme.HealthCalculatorTheme
import java.time.LocalDate
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ForestMainScreensTest(private val themeMode: ThemeMode) {
    companion object {
        @JvmStatic @Parameterized.Parameters(name = "{0}")
        fun modes() = listOf(arrayOf(ThemeMode.LIGHT), arrayOf(ThemeMode.DARK))
    }
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    private fun show(route: String, content: @Composable () -> Unit) {
        compose.runOnUiThread { compose.activity.actionBar?.hide(); compose.activity.enableEdgeToEdge() }
        compose.setContent {
            HealthCalculatorTheme(themeMode = themeMode) {
                Scaffold(containerColor = com.health.calculator.bmi.tracker.ui.components.forest.BotanicalPalette.Background, contentWindowInsets = WindowInsets(0, 0, 0, 0), bottomBar = { BottomNavigationBar(route, {}) }) { padding ->
                    androidx.compose.foundation.layout.Box(Modifier.padding(padding)) { content() }
                }
            }
        }
    }

    @Test fun track_equalCards_andAllSevenActions() {
        val clicked = mutableListOf<Int>()
        show(Screen.Track.route) { TrackHubScreen({clicked.add(0)}, {clicked.add(1)}, {clicked.add(2)}, {clicked.add(3)}, {clicked.add(4)}, {clicked.add(5)}, {clicked.add(6)}) }
        verifyVisibleText("track")
        val badgeLayouts = mutableListOf<TextLayoutResult>()
        compose.onNodeWithText("Better habits\na brighter you").fetchSemanticsNode()
            .config[SemanticsActions.GetTextLayoutResult].action?.invoke(badgeLayouts)
        assertEquals("The reference header badge must stay on two lines", 2, badgeLayouts.single().lineCount)
        listOf("Weight", "Water", "Blood pressure", "Food and calories", "Steps and\nconnected data", "History", "Reminders").forEachIndexed { index, title ->
            compose.onNode(hasScrollToIndexAction()).performScrollToNode(hasText(title))
            compose.onNodeWithText(title).performClick()
            compose.runOnIdle { assertEquals(index, clicked.last()) }
        }
        compose.onNode(hasScrollToIndexAction()).performScrollToNode(hasText("Weight"))
        val weight = compose.onNodeWithText("Weight").onParent().fetchSemanticsNode().boundsInRoot
        val water = compose.onNodeWithText("Water").onParent().fetchSemanticsNode().boundsInRoot
        assertEquals(weight.height, water.height, 1f)
        assertEquals(weight.width, water.width, 1f)
    }

    @Test fun calculators_allTenDestinations_andQualitySheet() {
        var opened: CalculatorDestination? = null
        show(Screen.Calculators.route) { CalculatorsHubScreen { opened = it } }
        verifyVisibleText("calculators")
        compose.onNodeWithContentDescription("Method and limits for BMI").performClick()
        compose.onNodeWithText(CalculatorQualityCatalog.get(CalculatorDestination.BMI).method).assertIsDisplayed()
        compose.onNodeWithText("Done").performScrollTo().performClick()
        compose.waitForIdle()
        CalculatorQualityCatalog.all.forEach { info ->
            compose.onNode(hasScrollToIndexAction()).performScrollToNode(hasText(info.title))
            compose.onNodeWithText(info.title).performClick()
            compose.runOnIdle { assertEquals(info.id, opened) }
        }
    }

    @Test fun insights_usesEngineEvidence_andEveryDestination() {
        val insights = DeterministicInsightEngine.generate(DeterministicInsightInput(LocalDate.of(2026, 10, 26)))
        var action = ""
        show(Screen.Insights.route) {
            InsightsHubScreen({action="weekly"}, {action="trends"}, {action="ai"}, {action="milestones"}, {action="learn"}, {action="history"}, insights, {action=it})
        }
        verifyVisibleText("insights")
        compose.onNodeWithText(insights.first().message).assertIsDisplayed()
        compose.onNodeWithText("View detail").performClick()
        compose.runOnIdle { assertEquals(insights.first().actionRoute, action) }
        listOf("Weekly wellness summary" to "weekly", "Trends" to "trends", "AI Wellness Assistant" to "ai", "Milestones" to "milestones", "Learn" to "learn", "All history" to "history").forEach { (title, expected) ->
            compose.onNode(hasScrollToIndexAction()).performScrollToNode(hasText(title))
            compose.onNodeWithText(title).performClick()
            compose.runOnIdle { assertEquals(expected, action) }
        }
    }

    @Test fun profile_allFields_tabs_photoAndSaveRemainConnected() {
        var field = ""
        var saved = 0
        var photo = 0
        show(Screen.Profile.route) {
            var state by remember { mutableStateOf(ProfileUiState()) }
            ForestScreenTheme {
            ForestProfileContent(state.copy(completion = ProfileCompletionUseCase().calculate(state.profile)), HealthJourneySummary(), null,
                {saved++}, {photo++}, {state=state.copy(selectedTab=it)}, {}, {}, {}, {}, {}, {}) {
                ForestMyInfoSection(state.profile, true, {field="Name"}, {field="Date of Birth"}, {field="Gender"},
                    {field="Height"}, {field="Weight"}, {field="Goal Weight"}, {field="Activity Level"},
                    {field="Health Goals"}, {field="Frame Size"}, {field="Ethnicity / Region"})
            }
            }
        }
        verifyVisibleText("profile")
        val avatar = compose.onNodeWithContentDescription("Edit profile photo").fetchSemanticsNode().boundsInRoot
        val more = compose.onNodeWithContentDescription("More profile actions").fetchSemanticsNode().boundsInRoot
        val save = compose.onNodeWithText("Save").fetchSemanticsNode().boundsInRoot
        assertFalse("Avatar must not cover the profile menu", avatar.overlaps(more))
        assertFalse("Avatar must not cover Save", avatar.overlaps(save))
        compose.onNodeWithText("Save").performClick()
        compose.runOnIdle { assertEquals(1, saved) }
        compose.onNodeWithContentDescription("Edit profile photo").performClick()
        compose.runOnIdle { assertEquals(1, photo) }
        listOf("Name", "Date of Birth", "Gender", "Height", "Weight", "Goal Weight", "Frame Size", "Activity Level", "Health Goals", "Ethnicity / Region").forEach { title ->
            compose.onNodeWithText(title).performScrollTo().performClick()
            compose.runOnIdle { assertEquals(title, field) }
        }
        compose.onNodeWithText("Health Overview").performScrollTo().performClick()
        compose.onNodeWithText("Your Health Status").performScrollTo().assertIsDisplayed()
    }

    @Test fun botanicalThemeFollowsBrightnessAndStaysLocal() {
        var outer: Boolean? = null
        var inner: Boolean? = null
        var restored: Boolean? = null
        show(Screen.Track.route) {
            val before = AppColors.isDark
            ForestScreenTheme {
                val inside = AppColors.isDark
                SideEffect { inner = inside }
            }
            val after = AppColors.isDark
            SideEffect { outer = before; restored = after }
        }
        compose.runOnIdle {
            assertEquals(themeMode == ThemeMode.DARK, outer)
            assertEquals(themeMode == ThemeMode.DARK, inner)
            assertEquals(outer, restored)
        }
    }

    private fun verifyVisibleText(name: String) {
        compose.waitForIdle()
        val visible = compose.onRoot().fetchSemanticsNode().boundsInRoot
        compose.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsActions.GetTextLayoutResult), true).fetchSemanticsNodes()
            .filter { it.boundsInRoot.overlaps(visible) }.forEach { node ->
                val layouts=mutableListOf<TextLayoutResult>()
                node.config[SemanticsActions.GetTextLayoutResult].action?.invoke(layouts)
                layouts.forEach { layout ->
                    val tooWide=(0 until layout.lineCount).any { layout.getLineRight(it)-layout.getLineLeft(it)>layout.size.width+1f }
                    assertFalse("Clipped $name: ${layout.layoutInput.text}", layout.multiParagraph.didExceedMaxLines || tooWide || layout.multiParagraph.height>layout.size.height+1f)
                }
            }
    }
}
