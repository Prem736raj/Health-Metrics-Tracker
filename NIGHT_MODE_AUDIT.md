# Night-mode audit — 2026-10-26

Planning policy: deep; dependency context; impact verification; conditional delegation for broad UI fanout. Scope: UI/theme and presentation-only display corrections, tests/scripts and this audit. No formulas, models, storage, routes, dependencies or disclaimer text changes. Preserve every existing light colour expression. Existing dirty build/config files are excluded.

This table was written before implementation. Findings are from the twelve supplied screenshots plus source searches; planned checks are not claims of runtime verification. Every row is subject to the 15-point checklist below.

Paths are relative to app/src/main/java/com/health/calculator/bmi/tracker/.

| Screen | Problem | File | Planned fix |
|---|---|---|---|
| Theme and shared components | Inconsistent dark palette, dim alpha text, feature colours bypass theme | ui/theme/Color.kt, Theme.kt, Type.kt; ui/components/WellnessDesignSystem.kt | Central dark surface/text/accent roles; preserve light branches exactly; executable contrast checks |
| Onboarding welcome, features and starting point | Faint orbit art and subtitles; repeated Home completion action | ui/screens/onboarding/OnboardingScreen.kt | Dark-only visible accent/text; remove duplicate Home text action, preserve completion callback |
| Home | Saturated score gradient, fine print, mismatched tracker heights, BMI chip, AI border | presentation/home/HomeScreen.kt; ui/components/WellnessDesignSystem.kt | Neutral dark hero/callout; reserve progress slot; accessible labels/icons |
| Track | Feature-colour cards/text; check clipping/insets | presentation/navigation/HubScreens.kt | Dark-only neutral surfaces, consistent text and accents; audit action targets |
| Calculate list | Many feature accents; text/ellipsis and card sizing checks | presentation/navigation/HubScreens.kt; ui/components/home/CalculatorCardsGrid.kt, DynamicCalculatorCards.kt | Preserve light; dark shared roles and readable discovery cards |
| Insights | Colour and summary/action checks | presentation/navigation/HubScreens.kt | Consistent dark palette and grounded wording |
| Profile My Info | Header band, placeholder U and camera strip, unrelated cards | presentation/profile/ProfileScreen.kt; ui/components/ProfilePictureSection.kt, ProfileCompletionBar.kt, ProfileMilestonesPreview.kt | Dark unified surfaces; initial/person avatar with circular camera badge |
| Profile Health Overview | Shared profile surfaces, coloured summaries | ui/components/HealthOverviewCard.kt, HealthJourneySummaryCard.kt, ProfileWeightSection.kt | Audit contrast and single accent; preserve data and light colours |
| History Log | Header band; compact icons/labels/filter actions | ui/screens/history/HistoryScreen.kt; ui/components/history/ | Dark matching app bar; audit semantics, spacing and formatted values |
| Statistics | Arrow reflects interpretation rather than sign; fixed heatmap layout; Days plural | ui/screens/history/StatisticsScreen.kt; data/repository/StatisticsRepository.kt | Arrow from signed change; real weekday-aligned calendar; singular/plural; neutral advice without target |
| Milestones and Records | Different surface families; compact text and accents | ui/screens/profile/milestones/MilestonesScreen.kt; ui/components/MilestonesTimeline.kt, PersonalRecordsCard.kt | Neutral dark surfaces, legible status and labels |
| Settings and data/connection settings | App bar and muted text checks | presentation/settings/SettingsScreen.kt; presentation/profile/HealthConnectionsScreen.kt; ui/screens/settings/DataManagementScreen.kt | Shared dark role audit, targets and insets |
| AI Wellness Assistant | Strong feature accents and dim supporting text | ui/screens/aicoach/AiCoachScreen.kt; presentation/home/HomeScreen.kt | Neutral dark cards/outlines, primary actions, existing safety text retained |
| BMI Calculator | Three header actions; truncation, nested fields, oversized gaps, heavy gender buttons | ui/screens/calculators/bmi/BmiCalculatorScreen.kt; components/BMIAnimatedComponents.kt, BMISliderInputSection.kt, BMIQuickCheck.kt | Two purposeful header actions; compact segmented controls and single outlined fields; compact memory row |
| BMI last-used/profile greeting | Escaped Kotlin templates including relative time | ui/screens/calculators/bmi/components/BMIQuickCheck.kt | Restore interpolation; regression guard and separate commit |
| BMI Result | Category colours/text and nested cards | ui/screens/calculators/bmi/BmiResultSection.kt, BmiGauge.kt, BmiUiTokens.kt | Dark role consistency, category chip/range bar; no formula changes |
| BMI Trends | Dim chart axes/legend/bands; duplicate date labels; redundant See trends | ui/screens/calculators/bmi/BmiTrendSection.kt | Same-day time labels; distinguishable zones; remove redundant action |
| BMI Learn | Dim subtitle, rainbow chevrons and maroon disclaimer | ui/screens/calculators/bmi/BmiEducationContent.kt | Neutral chevrons, surface disclaimer with warning outline; preserve words |
| BSA input/result/trends/learn | Hard-coded colour and alpha inventory | ui/screens/bsa/BSAScreen.kt, BSAResultScreen.kt, BSATrackingUI.kt, BSAMedicalApplications.kt | Audit all tabs, accessible dark roles, header/actions |
| Water input/result/tracking/history/education/tools/reminders | Extensive feature colours and alpha inventory | ui/screens/waterintake/ | Audit text/surface pairs, bar matching, chart labels and bottom padding |
| Daily energy/calories input/result/history/food log | Orange feature text and dim small labels | ui/screens/calorie/ | Dark text neutral; icons/progress retain metric accent; audit formatting/actions |
| BMR calculator/result/trends/education | Many component-specific colours | ui/screens/bmr/ | Dark role audit including charts, limits and tiny labels |
| Heart rate zones/result/history and recovery | Feature accents, component chart/label checks | ui/screens/heartrate/; ui/components/HeartRateModels.kt | Audit zone text, recovery UI, actions and same-day chart labels |
| Blood pressure calculator/log/trends/learn/reminders/recommendations/export | Category colours must retain meaning, dark readability | ui/screens/bloodpressure/ | Preserve interpretation and disclaimers; accessible dark surfaces and category text |
| Ideal body weight input/result/goals | Feature-colour result text and nested components | ui/screens/ibw/; ui/screens/calculators/idealweight/IdealWeightCalculatorScreen.kt | Dark role audit; no goal or calculation changes |
| Steps tracking/history | Track hub and step components | presentation/navigation/HubScreens.kt; data/repository/StepHistoryRepository.kt | Verify actual source inventory; dark contrast, calendar/date and target wording |
| Weight tracking/trends/goals | Shared chart and metric colours | presentation/weight/WeightTrackingScreen.kt; ui/components/WeightTrendGraph.kt, WeightStatisticsCard.kt, WeightGoalProgressCard.kt | Dark chart/text roles and finite display values |
| Waist-to-hip calculator/result/progress/education | Additional calculator discovered | ui/screens/whr/ | Same dark/action/formatting/inset checklist |
| Metabolic syndrome calculator/result/tracking | Additional calculator discovered | ui/screens/metabolicsyndrome/ | Same dark checklist; do not alter clinical logic/disclaimers |
| Reports/articles/reminders/export/welcome-back/splash | Additional screens discovered | ui/screens/reports/, articles/, reminders/, export/, welcomeback/, splash/ | Shared dark consistency and smaller-label/action/inset audit |

Checklist per row: (1) text >=4.5:1 and functional icons/borders >=3:1; (2) hard-coded colours; (3) dollar/null/NaN/format leaks; (4) working labelled >=48dp icon actions; (5) header/body matching; (6) grid dimensions; (7) duplicate actions; (8) ellipses; (9) gaps/nesting; (10) directional/plural/data accuracy; (11) grounded advice; (12) chart labels/zones; (13) accent consistency; (14) bottom/system insets; (15) text >=12sp.

Verification plan: build and affected unit tests; contrast script; source/diff guard for unchanged light values; available emulator/device dark screen checks. Record limitations explicitly. Five separate implementation commits: templates, tokens, BMI, Profile, remaining audit fixes.

## Implementation record

- Template commit restores Kotlin interpolation in the greeting, memory summary and all relative-time branches. `\$` was rendering a literal dollar sign, not interpolating a value. Relative-time tests cover minute/hour/day/week/month and invalid/future timestamps.
- Dark tokens define neutral three-level surfaces, lavender actions, readable text and separate decorative/functional outlines. Disabled text is raised from requested #8A849E to #8E88A3 because the requested value failed 4.5:1 on level 3 (4.31:1); adjusted value passes at 4.55:1.
- BMI has two header actions (method/limits and History), segmented gender selection, one field per metric in metric units, fields without outer cards, input-mode control above inputs, and a tonal Use action. Imperial height continues to use its existing feet/inches fields. Clear All was an actual reset action that appeared inert on an empty form; it was removed to meet the two-action limit. Result calculation and gauge mapping are unchanged.
- BMI Trends uses time for same-date records, readable native paint and legends, 18–20% zone fills, and removes the redundant See trends label. Learn uses neutral chevrons and a surface disclaimer with warning outline; disclaimer words are unchanged.
- Profile uses a name initial/person fallback and separate 48dp camera badge; dark app bar/body and cards use consistent surfaces in both tabs.
- Home has neutral dark score/callout surfaces, readable fine print, lavender actions, neutral chip labels and equal row heights plus a reserved progress slot. Onboarding removes the redundant Home text action and raises dark illustration/text contrast; completion routing remains in the existing NavGraph callbacks.
- Statistics uses a real Monday-first calendar, a today outline, active-day fills and accessible cell descriptions. Signed percentage controls arrow direction, independently of interpretation. BMI recommendation no longer refers to an absent target. Month iteration starts at day 1 to avoid calendar rollover skips. Display helpers have regression tests.
- Broader source migration covers text/card roles across calculators, trends/learn/history components, hubs, settings, AI, reminders, articles and reports. See DARK_ROLE_INVENTORY.md. Explicit light expressions are passed through unchanged. Non-chart gradient backgrounds are neutralized only in dark mode. Several undersized icon buttons were enlarged; the heart-rate formula's empty click handler became passive information. Shared labels and explicit text sizes below 12sp were raised to 12sp.
- BSA, blood-pressure and metabolic chart native paint now uses ARGB in dark mode while retaining the legacy light paint expression. This addresses another instance of the BMI chart bug.

## Verification status and boundaries

- PASS: executable dark token contrast script, 36 foreground/background pairings; decorative subtle outlines are intentionally separate from functional boundaries.
- PASS: executable guard confirms the complete LightColorScheme and every pre-existing shared/light Color.kt constant are identical to the baseline.
- PASS: escaped-template source guard across Kotlin/XML.
- Source coverage does not certify rendered contrast on every screen. Pixel-identical light rendering requires screenshot comparison against a baseline build; this has not been established by the constant guard.
- Android emulator audit34 is available; real-phone verification is still needed for the supplied device's font scale, status/navigation insets and every calculator/tab/data state.
- Build/unit/instrumentation and emulator screenshot outcomes are recorded below when actually observed. No unobserved test result should be treated as passed.
- Existing user changes to build files, Gradle versions/properties, .vscode, release output and the deleted production-audit text are intentionally excluded from implementation commits.

## Observed final verification

- `gradlew.bat :app:assembleDebug :app:testDebugUnitTest "-Pksp.incremental=false"`: BUILD SUCCESSFUL; 197 tests, zero failures/errors/skips. Includes the new BMI memory and calendar/direction/plural tests.
- `gradlew.bat :app:connectedDebugAndroidTest "-Pksp.incremental=false" "-Pandroid.testInstrumentationRunnerArguments.class=com.health.calculator.bmi.tracker.ui.screens.onboarding.OnboardingScreenTest"`: BUILD SUCCESSFUL; 3 tests, zero failures/errors/skips on audit34 / Android 14. Includes the explicit dark-mode duplicate-action/completion test.
- Main compilation, Java/Hilt processing, APK packaging, unit tests and focused Android instrumentation passed. Existing Gradle/Kotlin deprecation/annotation warnings remain; no clean baseline warning comparison was obtained, so zero new warnings is not certified.
- `check_dark_contrast.py`, `check_light_palette.py`, `check_ui_templates.py`: PASS after the final source changes. `git diff --check`: PASS for source changes.
- Standalone emulator app startup showed a blank/loading dark surface and yielded no usable hierarchy. There was no AndroidRuntime crash in the captured log. This startup behavior was not diagnosed or fixed; do not treat it as proof of screen rendering. The isolated onboarding instrumentation did render and exercise its screens successfully.
- Outstanding: resolve the standalone emulator startup/loading issue, then render and inspect Home, Profile, BMI input/result/trends/learn, Statistics populated with same-day readings/activity, every other calculator/tab, and real-phone system/bottom insets. Light pixel equivalence still needs a baseline screenshot comparison. The source palette guard alone does not establish it.

VERDICT: PASS — build, unit tests, focused onboarding instrumentation and static guards.
VERDICT: FAIL — full visual audit acceptance remains incomplete; standalone emulator startup/loading and exhaustive screen/data-state visual verification remain open.
