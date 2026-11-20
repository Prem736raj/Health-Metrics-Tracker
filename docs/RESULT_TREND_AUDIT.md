# Results and trends: pre-edit audit

Baseline: `3e151729ba0302f1a48ecf8a2452bbd683a5a102`, 2026-10-29.
Scope: the existing result and trend/history views of all ten calculators, in day
and night mode. Approved main-screen designs, equations, models, storage, routes,
dependencies and clinical disclosures are protected. Findings below were read in
the source and its callers before implementation; runtime checks are pending.

| Screen | Problem | File (under app/src/main/java/com/health/calculator/bmi/tracker/ui/screens) | Planned fix |
| --- | --- | --- | --- |
| BMI result / Trends | Result actions use a fixed legacy accent; empty-state ornament is faded and fake action chips have no action. Canvas points are pointer-only; the four-column stats crowd narrow screens. | calculators/bmi/BmiResultSection.kt; BmiTrendSection.kt | Use feature roles, remove inert action-like chips, show the actual single reading, wrap stats, add accessible dated reading selection. Preserve measurement folio and reference scale. |
| BMR result / Trends | Calculate scrolls to the bottom of the entire long report rather than its answer. Unit toggle is tiny. Canvas selection is pointer-only and same-day labels repeat dates. | bmr/BMRCalculatorScreen.kt; components/BMRResultSection.kt; BMRTrendSection.kt | Focus the result heading after calculation; reserve 48dp unit target; readable date/time ticks and keyboard/TalkBack reading selection. Preserve energy ledger. |
| Water result / History | Thirty-day charts omit all date ticks; goal annotation can clip above canvas; values are not accessible. | waterintake/WaterHistoryScreen.kt; WaterIntakeResultScreen.kt | Sparse endpoint ticks, label headroom, date/intake/goal descriptions and reading list. Preserve reservoir and existing result recovery/actions. |
| Calories result / History | Period selection counts records instead of calendar days and can omit today when caller appends it. Latest endpoint tick can be missing; no accessible chart values. | calorie/CalorieHistoryScreen.kt (caller: core/navigation/NavGraph.kt; existing analytics: domain/usecase/CalorieHistoryAnalyticsUseCase.kt) | Normalize/filter presentation input by calendar period, retain newest date, add endpoint/quantity labels and accessible rows. Keep calculation/safety targets and day dialog. |
| Blood pressure result / Trends | Point selection truncates index, biasing left; pointer-only details. | bloodpressure/BpTrendScreen.kt; BloodPressureResultComponents.kt | Select nearest point; expose focusable reading actions and existing details. Preserve paired stems, categories and series. |
| Heart rate result / history | Result passes an always-empty local history; graph clamps at 120bpm; zone segments disagree with the displayed BPM axis. | heartrate/HeartRateZoneResultScreen.kt; HeartRatePolishComponents.kt (caller wiring to inspect) | Use existing saved observations where available; derive plot bounds from observations, align low/high segments on one scale and expose readings. No new history store. |
| Waist / hip result / Progress | Male boundaries shown as 0.90/1.00 contradict unchanged model 0.85/0.90. Range padding is applied only to Elvis fallback; flat/extreme points can clip. Multi-series unit scaling is unexplained; no chart data semantics. | whr/WhrResultScreen.kt; WhrProgressScreen.kt (reference: data/model/WhrResult.kt) | Match existing model boundaries; robust axis limits; explicit series units and accessible dated values. Preserve measuring ribbon. |
| BSA result / Progress | Formula marker assumes 280dp track width. Average label is faded and outside plot edge; chart has no data semantics. | bsa/BSAResultScreen.kt; BSATrackingUI.kt | Measure actual track width, move opaque average value to readable bounds/legend, expose dated values. Preserve specimen sheet and formula comparison. |
| Ideal weight result / History | Weight-axis min/max appear horizontally below chart; dates absent. Delete has no confirmation and action rows reserve only 32dp. | ibw/IBWResultScreen.kt; IBWHistorySection.kt | Correct axis placement and date ticks, explain successive-reading spacing; 48dp actions, state announcements and confirmation. Preserve branching range and kg/lb behavior. |
| Metabolic markers result / Progress | Improvement always draws down, wrong for HDL increases. Previous values/change explanations omitted. Timeline hides all but five records; Canvas data inaccessible. | metabolicsyndrome/MetabolicSyndromeTrackingScreen.kt; MetabolicSyndromeResultScreen.kt | Neutral improvement symbol or numeric direction from values; previous/current/change context; local all-record expansion/details; accessible readings. Preserve five-criterion design and disclosures. |

Shared implementation should expose existing values through native text and
focusable controls alongside charts, retain each feature palette, and format
same-day ticks with times. No ungrounded advice or new medical claims are needed.

Verification plan: unit tests for calendar/axis presentation policies; native
result fixtures for all ten features in both themes; existing palette and
approved-main-screen tests; build and diff checks. Hardware and live services
remain outside emulator verification.
