# Release status

Updated: 2026-09-16

This file records evidence that can be reproduced from the repository. It does
not replace device, Play Console, Firebase Console, signing, or closed-test
verification.

## Current code and build gates

- Application ID remains `com.healthmetrics.tracker`.
- Latest completed audit commit is tracked in Git history; verify the SHA with
  `git log -1 --oneline` before creating a release artifact.
- Required local gates are `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease`, and `bundleRelease`.
- Release signing is intentionally credential-gated through
  `RELEASE_STORE_FILE`, `RELEASE_STORE_PASSWORD`, `RELEASE_KEY_ALIAS`, and
  `RELEASE_KEY_PASSWORD`; no key or password is stored in this repository.

## Audit fixes now covered by code/tests

- P1 privacy and medical/data correctness fixes are complete; multi-profile
  switching remains disabled until records are profile-scoped.
- P2 history details parsing, food-log day boundaries, BMR ranges, WHR wording,
  BP wall-clock reminders, VO2 false-precision surfaces, hydration heuristic
  disclosure, and 48dp targets are implemented.
- A Room `MigrationTestHelper` test covers the checked-in 15→16 migration and
  verifies row preservation plus the `step_history` table.
- Dead quick-action/repository artifacts were removed after reference checks;
  the live large-widget resource under `app/src/main/res` was intentionally
  retained.

## Open runtime or owner gates

These cannot be proven by a Windows unit/build run:

1. Confirm the oldest database version ever distributed to users. If a real
   release predates version 13, restore that database and add schema fixtures
   plus migrations through version 16 before launch.
2. Run the migration test on a connected emulator/device; the repository only
   has schema fixtures for versions 15 and 16.
3. Run cold-start and macrobenchmark measurements with Firebase/App Check
   enabled in a release-like build; no startup performance number is claimed.
4. Exercise BP reminders across reboot, `TIME_SET`, timezone and DST changes,
   notification denial, and OEM battery restrictions.
5. Run Compose/accessibility checks on small and large screens, 1.5–2.0x font
   scale, TalkBack, dark/light mode, and API 26/33/34/35/36 devices.
6. Validate Health Connect aggregation with multiple data sources, denied
   permissions, unsupported providers, and process death.
7. Complete Play Console/Firebase owner tasks: release signing, App Check
   production registration, privacy/data-safety declarations, closed testing,
   store listing, and crash/ANR monitoring.

## Post-audit medical and AI trust hardening — 2026-09-11

- **Status:** Code-fixable items complete locally; device/provider validation
  remains open.
- **Changes:** WHR now uses one documented rib-to-iliac-crest waist landmark and
  one selected population reference point; the unsupported synthesized `+8 cm`
  second threshold and three-band presentation were removed while legacy enum
  data remains readable. Blood-pressure severe-reading constants are shared by
  categorization, recommendations and widget accessibility copy at inclusive
  `≥180 systolic or ≥120 diastolic`. Heart-rate zones no longer present fixed
  duration prescriptions and instead provide talk-test/planning guidance. The
  unused BSA placeholder reader was removed. AI retry now replaces the transient
  error bubble and reuses the persisted user turn rather than inserting a
  duplicate user message. Public privacy/terms/support links now use the
  canonical `Health-Metrics-Tracker` Pages/repository URL.
- **Follow-up:** Widget blood-pressure badges now delegate to the same
  categorization precedence as the calculator, so isolated severe systolic or
  diastolic readings cannot be mislabeled as Stage 1.
- **Follow-up:** BMI slider previews now remain readable when the category name
  wraps, expose a textual category summary to TalkBack, clamp edge markers
  inside the scale, and give weight/height fine-tune actions 48 dp targets with
  specific labels. The full local release gate remains green.
- **Follow-up:** The BMI Learn tab now replaces emoji headings, category-risk
  markers and local colour literals with the shared vector icon and semantic
  palette roles, keeping the same informational content and disclaimer.
- **Follow-up:** The WHR education route now uses vector icons and shared
  semantic palette roles throughout. Reference bands and BMI/WHR comparisons
  are labeled as informational context, while unsupported individual risk
  multipliers, fixed waist-change timelines and causal wording were removed.
- **Follow-up:** The BSA education route now maps legacy illustration values to
  shared vector icons and semantic palette roles. Formula history and guidance
  avoid “gold standard” or universally-most-accurate claims and no longer give
  unsupported precision about agreement between equations.
- **Follow-up:** The WHO exercise-guidelines surface now uses vector icons and
  shared semantic colours for zones, goals, sessions, and progress. The weekly
  progress ring has a spoken summary and session removal uses a 48 dp target;
  decorative emoji markers are no longer rendered.
- **Tests:** Added WHR landmark/reference-boundary, blood-pressure severe-edge,
  heart-rate guidance and AI conversation-turn policy tests. The full local
  Gradle gate is green; connected instrumentation built its APK but stopped
  before execution on 2026-09-11 with `No connected devices!`.
- **Owner/runtime notes:** Repository history contains an early Room version 12
  and checked-in migrations through version 16, but no tags, releases or
  Firebase App Distribution evidence identify the oldest distributed schema.
  Do not invent a pre-13 migration: the release owner must confirm distribution
  history and provide fixtures if needed. Connected migration, Health Connect,
  accessibility, signed-artifact and Play/Firebase checks remain listed in
  `docs/DEVICE_QA_2026-09.md` and this file.

## Heart-rate recommendation visual follow-up — 2026-09-12

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Goal, fitness-level, zone, calorie, workout and tip markers now
  render as stable Material icons instead of emoji. Legacy emoji fields remain
  only for backwards-compatible saved data and generated copy, which is
  cleaned before display. Shared semantic colors replace the local green,
  expandable rows use a 48 dp minimum touch target, and zero-valued zone
  distributions render a safe empty state without invalid Canvas arcs.
- **Verification:** `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Run TalkBack, large-font, light/dark theme and chart
  rendering checks on a connected device; signing, Firebase and Play Console
  tasks remain owner-only.

## VO₂ max estimate visual and wording follow-up — 2026-09-12

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** The VO₂ max surface uses stable heart, chart, timer,
  assignment and recovery icons rather than emoji, and shared semantic health
  colors replace local literals. User-facing copy now consistently describes
  an informational VO₂ max estimate; fitness-age and recovery wording avoid
  unsupported certainty while legacy resource/model values remain compatible.
- **Verification:** `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Physical-device TalkBack, large-font, theme and route
  rendering checks, plus signing/Firebase/Play Console work, remain owner-only.

## Resting heart-rate guide trust and visual follow-up — 2026-09-12

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Measurement steps now use stable Material icons and shared
  semantic colors. Resting-heart-rate bands use neutral reference language
  instead of “normal/concerning” labels, and the sheet explains common sources
  of individual variation without diagnosing the reader.
- **Verification:** `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Physical-device TalkBack, large-font, theme and sheet
  rendering checks, plus signing/Firebase/Play Console work, remain owner-only.

## Blood-pressure education trust and visual follow-up — 2026-09-12

- **Status:** Code-fixable audit complete; device/accessibility validation
  remains open.
- **Changes:** BP education sections, instructions, comparisons, myth/fact
  blocks and analogies now use stable vector icons and shared semantic colors.
  Expandable section headers have a 48 dp minimum target. Copy was aligned to
  current AHA/CDC home-monitoring guidance by removing unsupported fixed
  effect-size claims, universal “most accurate” language, fixed targets and
  medication certainty; reference bands are explicitly non-diagnostic.
- **Verification:** `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Physical-device TalkBack, large-font, theme and route
  rendering checks, plus signing/Firebase/Play Console work, remain owner-only.

## Blood-pressure recommendation visual follow-up — 2026-09-12

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Persisted recommendation markers now resolve to stable Material
  icons instead of rendered emoji. Urgency, advice, risk, and white-coat
  sections use shared semantic health colors, and expandable recommendation
  headers provide a 48 dp minimum touch target while preserving legacy data.
- **Verification:** `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Physical-device TalkBack, large-font, theme and route
  rendering checks, plus signing/Firebase/Play Console work, remain owner-only.

## Blood-pressure history visual follow-up — 2026-09-12

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** BP history and detail views use stable Material icons for
  time-of-day and averaged readings instead of emoji. Medication and pulse
  accents now use shared semantic/theme colors, with legacy/unknown values
  handled safely and all edit/delete/detail behavior preserved.
- **Verification:** `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Physical-device TalkBack, large-font, theme,
  swipe-to-delete and sheet rendering checks, plus signing/Firebase/Play
  Console work, remain owner-only.

## Blood-pressure trend correctness and visual follow-up — 2026-09-12

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Trend charts, filters, legends, calendar colors and summary
  cards now use a shared semantic series palette, and the trend card uses a
  stable icon instead of the persisted emoji marker. The existing systolic
  path is now drawn in the line chart; previously only its points were drawn.
  Category-derived zone fills keep the interpretation colors consistent.
- **Verification:** `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Physical-device TalkBack, large-font, theme, chart
  hit-testing/native-canvas rendering, plus signing/Firebase/Play Console
  work, remain owner-only.

## Blood-pressure reminder and export visual follow-up — 2026-09-12

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Reminder and export surfaces use shared semantic colors for
  appointment, PDF, spreadsheet, clinician-report, text-share and image-share
  actions. Enabled reminder time rows provide a 48 dp minimum target; existing
  permission, scheduling and export behavior is preserved.
- **Verification:** `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Notification-denial, file-sharing, TalkBack, large-font,
  theme and route rendering checks, plus signing/Firebase/Play Console work,
  remain owner-only.

## Home water card visual follow-up — 2026-09-12

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Hydration progress, completion and streak markers now use stable
  vector icons; shared semantic hydration colors replace local literals and
  quick-add labels use plain measurements without emoji. Progress, goal,
  streak and quick-log behavior remain unchanged.
- **Verification:** `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device home rendering, contrast at large
  font sizes, TalkBack descriptions, theme checks, and signing/Firebase/Play
  Console work remain owner-only.

## Calorie result visual and wording follow-up — 2026-09-12

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Calorie result targets, BMR/TDEE, macro charts, projections,
  warnings and actions use shared semantic colors and stable Material icons
  instead of emoji. Projection language now clearly frames weight change as a
  planning estimate (“may”), while formulas, values and safety-floor behavior
  remain unchanged.
- **Verification:** `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device result rendering, large-font wrapping,
  TalkBack labels, theme contrast, and signing/Firebase/Play Console work
  remain owner-only.

## Calorie input visual consistency follow-up — 2026-09-12

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Calorie input controls render stable Material icons for gender,
  activity and goal choices instead of legacy emoji. Goal loss/maintenance/gain
  states now use shared semantic colors, and the header fire marker is a vector
  icon; persisted option fields remain unchanged for compatibility.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device TalkBack, large-font, theme and route
  rendering checks, plus signing/Firebase/Play Console work, remain owner-only.

## Food log visual consistency follow-up — 2026-09-12

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Food-log calorie status, targets, macro bars and entry chips use
  shared semantic colors. Meal groups, preset chips, status markers and the
  empty state render stable vector icons instead of emoji while preserving
  logging, edit/delete, custom-preset and legacy-storage behavior.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device TalkBack, large-font, theme and food
  log interaction checks, plus signing/Firebase/Play Console work, remain
  owner-only.

## Calorie history visual consistency follow-up — 2026-09-12

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Calorie history summary stats, calendar adherence, trend and
  weekly comparison surfaces now use shared semantic colors. Balance, macro,
  empty-history and detail rows render stable vector icons instead of emoji;
  filtering, navigation, calculations and day selection are unchanged.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device chart rendering, TalkBack, large-font,
  theme contrast and history interaction checks, plus signing/Firebase/Play
  Console work, remain owner-only.

## Meal-planning visual consistency follow-up — 2026-09-12

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Meal distribution, macro rows, intermittent-fasting cards,
  timeline, workout nutrition and meal ideas now use shared semantic colors and
  stable vector icons instead of rendered emoji. Legacy marker fields and all
  meal-planning behavior remain compatible.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device TalkBack, large-font, theme and
  interaction checks, plus signing/Firebase/Play Console work, remain
  owner-only.

## Hydration tracking visual consistency follow-up — 2026-09-12

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Daily hydration progress, goal completion, quick-add actions,
  glass indicators, logs, score, achievements, tools and celebration surfaces
  now use shared palette tokens and stable vector icons instead of rendered
  emoji. Logging, undo, sharing, reminders and persistence behavior remain
  unchanged.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device TalkBack, large-font, theme and
  hydration interaction checks, plus signing/Firebase/Play Console work,
  remain owner-only.

## Water history visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Calendar, trends, weekly report, streak, statistics and record
  surfaces use shared semantic colors and stable vector icons instead of
  rendered emoji. Theme-aware empty/zero-intake states and chart colors improve
  contrast while preserving history, filtering and persistence behavior.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device TalkBack, large-font, theme, chart and
  calendar interaction checks, plus signing/Firebase/Play Console work, remain
  owner-only.

## Hydration achievements visual consistency follow-up — 2026-09-12

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Achievement title, active/inactive streak cards, score ring,
  breakdown, tiers, badges, milestones and unlock overlay use shared semantic
  colors and stable vector icons instead of rendered emoji. Legacy badge/grade
  marker fields and behavior remain compatible.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device TalkBack, large-font, theme and
  achievement interaction checks, plus signing/Firebase/Play Console work,
  remain owner-only.

## Water reminder settings visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Reminder status, toggle rows, schedule/time controls, frequency,
  smart features, notification style and summary surfaces use shared semantic
  colors and stable vector icons instead of rendered emoji/raw water colors.
  Permission, autosave and scheduler behavior remain compatible.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device notification permission, time-picker,
  TalkBack, large-font and light/dark theme checks, plus signing/Firebase/Play
  Console work, remain owner-only.

## Water intake input visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Daily water-needs input hero, selection borders and button
  shadow use shared feature tokens; vector icons, profile autofill, validation,
  unit conversion and navigation behavior remain compatible.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device keyboard/scroll, result navigation,
  TalkBack, large-font and light/dark theme checks, plus signing/Firebase/Play
  Console work, remain owner-only.

## Water intake result visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Post-calculation goal, animated bottle, unit conversion, hourly
  recommendation and save feedback surfaces use shared feature/health tokens;
  calculation, disclosure, save/share/recalculate and tracking behavior remain
  compatible.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device bottle rendering, bottom actions,
  TalkBack, large-font and light/dark theme checks, plus signing/Firebase/Play
  Console work, remain owner-only.

## Hydration education visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Hydration guide sections, benefits, guidelines, myth/reality,
  overhydration, exercise and special-needs cards use shared semantic colors and
  stable vector icons. Legacy marker strings remain compatible but are not
  rendered as emoji; copy, safety guidance and disclaimer behavior remain.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device expandable-content rendering, TalkBack,
  large-font and theme checks, plus signing/Firebase/Play Console work, remain
  owner-only.

## Hydration tools visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Urine, symptom, water-from-food and electrolyte tabs use shared
  semantic colors and stable vector icons for headers, status/risk indicators,
  foods, recommendations and quick facts. Legacy markers remain compatible but
  are not rendered; recommendation copy is cleaned of leading glyphs.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device tab interactions, TalkBack, large-font
  and theme checks, plus signing/Firebase/Play Console work, remain owner-only.

## BP advanced metrics visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Pulse pressure, MAP and heart-rate advanced cards/scales use
  shared semantic health tokens and feature accents instead of isolated raw hex
  colors. Reference ranges, interpretations and expansion behavior remain.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device advanced-card rendering, TalkBack,
  large-font, theme and interaction checks, plus signing/Firebase/Play Console
  work, remain owner-only.

## Calorie education trust and visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Calorie education sections, food-category rows, minimum-needs
  context, mindful note and disclaimer now use shared semantic colors and
  stable vector icons. Simplified calorie-balance math is labeled as an
  estimate; universal daily minimum targets and overconfident restrictive-diet
  claims were removed or qualified.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device expandable-guide rendering, TalkBack,
  large-font and light/dark theme checks, plus signing/Firebase/Play Console
  work, remain owner-only.

## Ideal-weight metrics trust and visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Ideal-weight category scales, adjusted-weight and body-
  composition surfaces now use shared semantic colors and stable vector icons;
  sport-note legacy markers remain compatible but are not rendered. Adjusted
  body weight is described as an informational estimate used in some clinical
  methods, never as a dosing instruction or weight goal.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device category-scale, expandable-sport-note,
  TalkBack, large-font and light/dark theme checks, plus signing/Firebase/Play
  Console work, remain owner-only.

## Electrolyte information trust and visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Electrolyte, food-source, situation, ORS and warning surfaces
  now use shared hydration/health semantic colors and stable vector icons.
  Homemade mixtures are explicitly not WHO ORS or dehydration treatment, and
  claims about universal supplementation, altitude, and hangover relief were
  qualified or removed.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device expandable-guide, ORS warning, TalkBack,
  large-font and light/dark theme checks, plus signing/Firebase/Play Console
  work, remain owner-only.

## Heart-rate education trust and visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** The heart-rate education route now maps legacy emoji markers to
  stable Material icons and shared semantic colors. Reference ranges,
  monitoring comparisons, warning signs and myths use calmer, theme-aware
  presentation. Population estimates and training guidance were qualified so
  they are not framed as diagnoses, safety limits or personal prescriptions.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device warning/expand interactions, TalkBack,
  large-font and light/dark theme checks, plus signing/Firebase/Play Console
  work, remain owner-only.

## WHR result and animation trust/visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** WHR result categories and thresholds use calm action-point
  wording. Risk, warning, body-shape and trend surfaces share theme-aware
  semantic colors and stable Material vector icons; decorative emoji was
  removed from the export summary. Calculation, persistence, save and history
  behavior remain unchanged.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device gauge animation, TalkBack, large-font,
  light/dark theme and route rendering checks, plus signing/Firebase/Play
  Console work, remain owner-only.

## Heart-rate zone input/result trust and visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Heart-rate zone input and result routes use shared feature/health
  tokens and stable vector icons for gender, fitness level, formulas, reserve,
  zones and detail rows. The Tanaka badge now says “Age-adjusted” rather than
  implying universal accuracy. Zone bars, pulse animation and export actions
  no longer render emoji or isolated raw colors; calculations, persistence and
  history behavior are unchanged.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device pulse/expanded-card rendering,
  TalkBack, large-font and light/dark theme checks, plus signing/Firebase/Play
  Console work, remain owner-only.

## Heart-rate dashboard and trend trust/visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Heart-rate dashboard, formula comparison, edge-warning and
  resting-trend surfaces now share feature/health tokens and stable vector
  icons. Formula guidance is presented as a starting estimate without a
  universal-accuracy claim; warning and trend copy avoids diagnosis or causal
  fitness conclusions. Heart-rate image/text exports use the semantic palette
  and plain labels while calculations, history callbacks and legacy marker
  compatibility remain unchanged.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device trend/export rendering, TalkBack,
  large-font and light/dark theme checks, plus signing/Firebase/Play Console
  work, remain owner-only.

## Heart-rate activity reference trust and visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** The activity guide uses stable Material vector icons for its
  header, category filters, activity rows, detail chips and empty search state.
  Legacy activity/category emoji remain in source models for compatibility but
  are not rendered. Calorie markers now use the shared calorie semantic token
  instead of an isolated orange literal; calculations and zone estimates are
  unchanged.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device expandable-row and filter-chip checks,
  TalkBack, large-font, light/dark theme validation, plus signing/Firebase/Play
  Console work, remain owner-only.

## BSA result trust and visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** BSA result summaries, comparison context and formula
  recommendations now use stable Material vector icons and shared semantic
  colors instead of emoji or isolated literals. Missing profile sex no longer
  silently defaults to a male comparison; the route shows a neutral adult
  context instead. Formula guidance describes population/source context
  without universal accuracy or clinical-prescription claims. Calculation,
  persistence, history and export callbacks remain unchanged.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** BSA animations, large-font, TalkBack, light/dark theme
  and route rendering checks require a connected device; signing/Firebase/Play
  Console work remains owner-only.

## Metabolic screening result trust and visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** The metabolic screening result route uses shared semantic health
  colors and stable vector icons for the gauge, screening summary, criteria
  states and risk message instead of a raw red literal or rendered emoji. The
  helper is explicitly named as a screening summary, while the existing
  non-diagnostic wording, ethnicity thresholds, medication markers,
  persistence and share callbacks remain unchanged.
- **Verification:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Remaining gates:** Connected-device gauge/expand rendering, TalkBack,
  large-font and light/dark theme checks, plus signing/Firebase/Play Console
  work, remain owner-only.

## Metabolic-syndrome standards comparison trust and visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** The ATP III, IDF and WHO comparison surface now uses stable
  Material vector icons instead of rendered emoji/flag characters. WHO is
  explicitly shown as **Not scored** when the app does not collect the
  laboratory or clinical inputs required by that definition. IDF and ATP III
  copy now describes screening/reference context without implying diagnostic
  accuracy; ethnicity and “which standard” guidance is clearer and calmer.
  Existing calculations, persistence and selection behavior are unchanged.
- **Verification:** Focused Kotlin compilation passed. The complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  is run before this slice is committed.
- **Remaining gates:** Connected-device comparison expansion, TalkBack,
  large-font, light/dark theme and route-rendering checks, plus signing/
  Firebase/Play Console work, remain owner-only.

## BMR education trust and visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** BMR education and edge-warning surfaces now use stable Material
  vector icons and shared calculator/health tokens instead of emoji markers
  and isolated raw colors. Explanations remove unsupported fixed percentages
  and clarify that BMR/TDEE values are estimates, not a personal calorie
  floor or a prescription. Calculator formulas, validation, persistence and
  navigation are unchanged.
- **Verification:** Focused Kotlin compilation passed. The complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  is run before this slice is committed.
- **Remaining gates:** Connected-device expandable education, warning rows,
  TalkBack, large-font, light/dark theme and route-rendering checks, plus
  signing/Firebase/Play Console work, remain owner-only.

## BMR result trust and visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** BMR result, breakdown and formula-comparison surfaces now use
  stable Material vector icons and shared calculator/health tokens instead of
  rendered emoji and isolated color literals. Result language calls BMR a
  resting-energy estimate, removes a misleading calorie-floor reading, and
  presents broad estimate bands as context rather than clinical ranges. Share
  output is plain, explicit and informational; calculation, history and unit
  toggle behavior remain unchanged.
- **Verification:** Focused Kotlin compilation and BMR unit tests passed. The
  complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` gate is run before this slice is committed.
- **Remaining gates:** Connected-device animation, formula expansion,
  TalkBack, large-font, light/dark theme and route-rendering checks, plus
  signing/Firebase/Play Console work, remain owner-only.

## TDEE goals and activity trust/visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** TDEE activity and goal surfaces now use stable Material vector
  icons, shared calculator/health tokens and a theme-aware hero instead of
  emoji markers and isolated raw colors. Goal descriptions and the safety note
  present calorie targets as planning estimates, remove universal “healthy
  pace”/minimum-intake implications, and encourage professional context for
  very low targets. Formula, selection, unit and callback behavior are
  unchanged.
- **Verification:** Focused Kotlin compilation and unit tests passed. The
  complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` gate must pass before this slice is committed.
- **Remaining gates:** Connected-device activity/goal expansion, chart
  rendering, TalkBack, large-font, light/dark theme and route checks, plus
  signing/Firebase/Play Console work, remain owner-only.

## BMR trend trust and visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** BMR trend history now uses shared calculator/health tokens and
  stable Material vector icons for empty, comparison, statistics and insight
  states instead of rendered emoji and isolated raw colors. Trend markers use
  the current surface color for light/dark theme compatibility. Insight copy
  is observational and non-diagnostic, avoids inferring muscle gain or
  metabolic health, and invalid previous values no longer produce a
  divide-by-zero percentage. Chart, history, selection and persistence
  behavior remain unchanged.
- **Verification:** Focused Kotlin compilation and unit tests passed. The
  complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` gate is run before this slice is committed.
- **Remaining gates:** Connected-device chart interaction, TalkBack,
  large-font, light/dark theme and route-rendering checks, plus signing/
  Firebase/Play Console work, remain owner-only.

## BMR formula and body-fat input trust/visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Formula selection and body-fat help surfaces now use shared
  semantic colors and clear vector method icons. The recommended formula tag
  is plain “Recommended”, historical formula copy avoids an unsupported
  accuracy percentage, and body-fat method/range guidance explains
  measurement uncertainty without presenting population bands as personal
  targets. Input visibility and selection behavior are unchanged.
- **Verification:** Focused Kotlin compilation and unit tests passed. The
  complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` gate must pass before this slice is committed.
- **Remaining gates:** Connected-device chip scrolling, body-fat dialog,
  TalkBack, large-font, light/dark theme and route checks, plus signing/
  Firebase/Play Console work, remain owner-only.

## BMR age comparison trust and visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** The age-comparison card now uses stable Material chart,
  information and schedule icons with shared semantic tokens. The chart's user
  marker follows the active surface color in both themes, and long comparison
  and age notes align safely at the top. Reference copy no longer implies that
  an estimate identifies muscle mass, fitness or metabolic health; the
  age-curve text is population context rather than a personal forecast.
  Interpolation and chart interaction behavior remain unchanged.
- **Verification:** Focused Kotlin compilation and unit tests passed. The
  complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` gate is run before this slice is committed.
- **Remaining gates:** Connected-device chart rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing/Firebase/Play Console work,
  remain owner-only.

## BMR calculator input and quick-info trust/visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** The primary BMR input flow now uses stable Material icons for
  the result header, profile-data banner, gender choices, explanation card and
  quick-info rows instead of emoji placeholders. Profile-data guidance uses
  the shared informational palette rather than a health-status green. Quick
  info copy no longer presents fixed energy-use percentages or formula claims
  as universal facts, and save feedback is plain and accessible. Input, unit,
  validation, calculation and navigation behavior remain unchanged.
- **Verification:** Focused Kotlin compilation and unit tests passed. The
  complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` gate is run before this slice is committed.
- **Remaining gates:** Connected-device input, large-font, TalkBack,
  light/dark theme and route checks, plus signing/Firebase/Play Console work,
  remain owner-only.

## TEF trust, denominator correctness and visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** The thermic-effect view now uses shared calculator and semantic
  health colors plus stable Material icons for TEF, macro, insight and energy
  breakdown states instead of rendered emoji and isolated raw colors. The
  “% of food intake” value now uses the macro-calorie intake that feeds the
  estimate, with a zero-input guard; it no longer labels TDEE as food intake.
  TEF explanations describe a model estimate, avoid causal weight or muscle
  promises, and do not prescribe changing protein intake. Animation, chart,
  macro selection and TDEE behavior remain unchanged.
- **Verification:** Focused Kotlin compilation and unit tests passed, including
  the macro-intake denominator and zero-input regression. The complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate is
  run before this slice is committed.
- **Remaining gates:** Connected-device donut/bar rendering, large-font,
  TalkBack, light/dark theme and route checks, plus signing/Firebase/Play
  Console work, remain owner-only.

## Macro planning trust and visual consistency follow-up — 2026-09-13

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Macro planning now uses stable Material icons for diet presets,
  macro legends, sliders, detail cards and meal breakdowns instead of emoji
  markers and isolated raw colors. Ratio feedback uses the shared semantic
  palette and a readable icon. Preset descriptions and “best for” labels are
  framed as optional planning patterns rather than weight-loss, blood-sugar or
  muscle-building promises. Share output uses plain labels for accessibility.
  Macro percentage balancing, meal-count selection, charts and callbacks remain
  unchanged.
- **Verification:** Focused Kotlin compilation and unit tests passed, including
  a plain-label share regression. The complete `test`, `lintRelease`,
  `assembleDebug`, `assembleRelease` and `bundleRelease` gate is run before
  this slice is committed.
- **Remaining gates:** Connected-device slider/chart rendering, large-font,
  TalkBack, light/dark theme and route checks, plus signing/Firebase/Play
  Console work, remain owner-only.

## Calorie macro planning trust and visual consistency follow-up — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** The calorie-result macro planner now uses stable Material icons
  and shared semantic colors for diet presets, macro sliders, detail cards and
  per-meal references instead of emoji and legacy color literals. Preset
  descriptions are planning-oriented and avoid promising weight-loss or
  muscle-building outcomes. Per-meal chart fractions remain finite when an
  invalid meal count is restored, and plain food labels improve accessibility.
  Percentage balancing, callbacks and calculations remain unchanged.
- **Verification:** Focused Kotlin compilation and unit tests passed, including
  preset-copy safety and invalid meal-count finite-value regressions. The
  complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` gate is run before this slice is committed.
- **Remaining gates:** Connected-device chart/slider rendering, TalkBack,
  large-font, light/dark theme and route checks, plus signing/Firebase/Play
  Console work, remain owner-only.

## Meal timing trust, custom schedule correctness and visual consistency follow-up — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Meal-timing pattern and fasting surfaces now use stable Material
  vector icons and shared semantic colors instead of rendered emoji, raw color
  literals and a hard-coded white marker. The custom eating-window and
  meal-count controls now drive the schedule calculation, including clamped
  safety bounds, so the preview, timeline and fasting summary stay in sync.
  Meal schedule labels are plain and accessible; macro chips use the shared
  palette. Standard-pattern portions and BMR navigation remain unchanged.
- **Verification:** Focused Kotlin compilation and unit tests passed, including
  custom window/meal-count boundary, overnight end-time and calorie-total
  regressions. The complete `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` gate is run before this slice is
  committed.
- **Remaining gates:** Connected-device timeline animation, TalkBack,
  large-font, light/dark theme and route checks, plus signing/Firebase/Play
  Console work, remain owner-only.

## Calorie education and safety trust/visual consistency follow-up — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Calorie education now describes energy-use categories as
  variable rather than repeating fixed population percentages. Weight-gain
  guidance no longer promises a particular muscle/fat outcome or treats a
  surplus as a prescription; it explains relevant context and uncertainty.
  Calorie safety warning surfaces now use the shared semantic palette instead
  of isolated legacy color literals. Education navigation, food references
  and warning severity behavior remain unchanged.
- **Verification:** Existing calorie/macro unit coverage remains green. The
  complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` gate is run before this slice is committed.
- **Remaining gates:** Connected-device education rendering, TalkBack,
  large-font, light/dark theme and route checks, plus signing/Firebase/Play
  Console work, remain owner-only.

## Weight goal progress/trend trust and visual consistency follow-up — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Weight goal progress now clamps malformed or restored
  percentages before drawing and uses shared semantic colors for progress and
  goal states. Equal-to-start goals are treated as maintenance goals with a
  small 0.1 kg tolerance instead of appearing complete accidentally. Trend
  segments use shared toward/away-goal colors, while goal completion and
  estimated dates use accessible Material icons and approximate wording rather
  than emoji. Logging, filtering, tapping and navigation behavior remain
  unchanged.
- **Verification:** Focused Kotlin compilation and unit tests passed, including
  maintenance-goal and non-finite-progress coverage. The complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  is run before this slice is committed.
- **Remaining gates:** Connected-device graph/tap rendering, TalkBack,
  large-font, light/dark theme and route checks, plus signing/Firebase/Play
  Console work, remain owner-only.

## History and quick-action visual consistency follow-up — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** History entries and filter chips now use a shared
  calculator-to-vector-icon mapping, with plain labels that remain readable
  across fonts and platforms. History category indicators use the shared
  semantic palette rather than legacy raw color literals. Home quick actions
  now render the vector icon already supplied by each action, with an
  accessible content description, instead of a second emoji representation.
  Tap, long-press, selection, filtering and navigation behavior remain
  unchanged.
- **Verification:** Focused Kotlin compilation and unit tests passed. The
  complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` gate is run before this slice is committed.
- **Remaining gates:** Connected-device rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing/Firebase/Play Console work,
  remain owner-only.

## Home wellness overview visual consistency follow-up — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** The home wellness overview now uses vector icons for its
  header, score categories, quick stats, empty state and last-activity row
  instead of placeholder emoji. Health-score categories and quick-stat accents
  now draw from the shared semantic/feature palette, including a theme-aware
  no-data ring. Score methodology, metric selection, click targets and
  navigation remain unchanged.
- **Verification:** Focused Kotlin compilation and unit tests passed. The
  complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` gate is run before this slice is committed.
- **Remaining gates:** Connected-device score-ring and large-font rendering,
  TalkBack, light/dark theme and route checks, plus signing/Firebase/Play
  Console work, remain owner-only.

## Home recommendations visual consistency follow-up — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Recommendation cards now use a shared icon vocabulary by
  recommendation type, with plain accessible labels instead of emoji. Dismiss,
  priority and completed-state surfaces use shared semantic colors; the
  recommendation engine now supplies feature/semantic palette tokens instead
  of raw literals. Ordering, dismissal, actions and copy remain unchanged.
- **Verification:** Focused Kotlin compilation and unit tests passed. The
  complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` gate is run before this slice is committed.
- **Remaining gates:** Connected-device swipe/animation and contrast checks,
  TalkBack, large-font, light/dark theme and route checks, plus signing,
  Firebase and Play Console work, remain owner-only.

## Home calculator discovery card consistency follow-up — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Home calculator cards now use reusable vector icons and shared
  feature/semantic palette tokens instead of per-card emoji and raw color
  literals. Progress-ring centers are accessible icons, image cards use shared
  overlay/text tokens, and WHR card status coloring follows the saved category
  label rather than reclassifying with a fixed threshold. Metabolic card copy
  now says criteria are flagged/reviewable instead of asserting a diagnosis.
  Navigation, calculations, progress values and layout remain unchanged.
- **Verification:** Focused Kotlin compilation and unit tests passed. The
  complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` gate is run before this slice is committed.
- **Remaining gates:** Connected-device card/animation rendering, TalkBack,
  large-font, light/dark theme and route checks, plus signing, Firebase and
  Play Console work, remain owner-only.

## Home dashboard score and action semantics follow-up — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** The dashboard Wellness Score subtitle now accurately describes
  recent check-ins and daily logging instead of naming inputs the score does
  not use. Restored score values are clamped before progress-ring rendering,
  and home quick actions use matching feature colors/icons for weight and blood
  pressure. AI/action icons use the shared hero token, and recommendation icon
  mapping matches the underlying metric type. Navigation and score methodology
  remain unchanged.
- **Verification:** Focused Kotlin compilation and unit tests passed. The
  complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` gate is run before this slice is committed.
- **Remaining gates:** Connected-device ring/contrast rendering, TalkBack,
  large-font, light/dark theme and route checks, plus signing, Firebase and
  Play Console work, remain owner-only.

## Calorie history day details — 2026-09-14

- **Status:** Code-fixable interaction complete; device/accessibility validation remains open.
- **Changes:** Tapping a logged calendar day now opens a read-only detail dialog
  with date, total calories, target comparison, macros and logged foods.
  Duplicate current/history snapshots resolve to populated, most recently
  logged data. The previous no-op navigation callback was removed.
- **Verification:** Added unit coverage for date selection and duplicate
  resolution. Focused compilation/tests and the complete `test`, `lintRelease`,
  `assembleDebug`, `assembleRelease` and `bundleRelease` gate are required before
  commit.
- **Remaining gates:** Connected-device rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## Share/export accessibility and visual consistency — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Changes:** BMR, WHR, BP, weekly-report and hydration share outputs now use
  plain labels instead of emoji markers while retaining measurements and the
  informational disclosure footer. WHR waist-to-height output is framed as a
  reference flag. BP share buttons use theme tokens, report toggles use stable
  icons and full touch targets, and invalid BMR meal counts are clamped.
- **Verification:** Added share accessibility/disclosure coverage and a
  zero-meal formatter regression. Focused compilation/tests and the complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed.
- **Remaining gates:** Chooser/file-opening behavior, TalkBack, large-font,
  light/dark theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## Health Connections and Insights affordance clarity — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Changes:** Health Connections reference fields no longer look tappable when
  they have no action, and connection cards use stable vector icons instead of
  legacy emoji. The Insights Trends copy now accurately describes its weight
  trend shortcut and points users to Track for other logs. Health Connect
  permission/sync behavior is unchanged.
- **Verification:** Focused compilation/tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** Health Connect denial/revocation, connected-device
  rendering, TalkBack, large-font, theme and route checks, plus signing,
  Firebase and Play Console work, remain owner-only.

## Notification status affordance clarity — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Changes:** High-priority reminder and streak-freeze values are now visibly
  non-interactive status surfaces instead of no-op chips, and the freeze marker
  uses a stable shield icon. Notification toggle, edit/delete and scheduling
  behavior are unchanged.
- **Verification:** Focused compilation/tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** Notification permission/OS rendering, TalkBack,
  large-font, theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## Data management visual and trust polish — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Data Management cleanup actions, storage-ring segments and
  integrity states now use shared semantic/theme tokens. Calculator cleanup
  rows and the full-reset warning list use stable vector icons and plain labels
  instead of device-dependent emoji. Deletion safeguards and data operations
  are unchanged.
- **Verification:** Focused compilation/tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** Destructive-flow rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## Settings visual consistency — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Theme selectors use vector icons and plain labels instead of
  emoji. Theme, hydration, reminder, export, privacy, legal, rating and share
  rows now use shared semantic/theme tokens. The destructive confirmation
  retains its existing safeguards and presents a clear text warning.
- **Verification:** Focused compilation/tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** Settings rendering, TalkBack, large-font, light/dark
  theme and route checks, plus signing, Firebase and Play Console work, remain
  owner-only.

## WHR input accessibility polish — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** WHR gender choices now use stable Material vector icons with
  selected-state tint instead of device-dependent emoji. Gender values,
  validation, calculations and navigation are unchanged.
- **Verification:** Focused compilation/tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** WHR selector rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## BMI result visual polish — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** The BMI result card uses a stable vector status icon instead of
  category emoji. Saved-state, weight-range, advice, share and guidance
  surfaces use shared semantic/theme tokens, and the professional guidance
  note has an explicit icon. Calculations, thresholds and disclosures are
  unchanged.
- **Verification:** Focused compilation/tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** BMI result/gauge rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## BSA medical education icon polish — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** BSA medical-application sections use stable vector icons for
  medication, burn, renal and cardiac topics. The Rule of Nines table uses
  plain body-region labels instead of emoji; percentages and calculations are
  unchanged.
- **Verification:** Focused compilation/tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** BSA education rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## IBW education visual consistency — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** IBW education cards, comparison panels, frame-size guidance and
  disclaimer callouts use stable vector icons and shared semantic/theme tokens
  instead of emoji and legacy color literals. Educational copy, formulas and
  navigation are unchanged.
- **Verification:** Focused compilation/tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** IBW education rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## BMI comparison and range-bar polish — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** BMI comparison surfaces use vector status icons and shared
  semantic colors instead of emoji and isolated literals. The obsolete
  population-average panel (which could display a misleading 0.0 average) was
  removed because no representative dataset is bundled. The adult reference
  range highlight now scales to the available card width instead of a fixed dp
  offset, preventing overlap on different screen sizes. Pediatric compatibility
  data remains unchanged and is not approximated by the adult flow.
- **Verification:** Focused compilation/tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** BMI comparison rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## Metabolic syndrome education trust polish — 2026-09-14

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Metabolic syndrome education, risk-factor, treatment, blood-work
  and retest surfaces use stable vector icons instead of emoji. Explanations
  now avoid universal causal or diagnostic claims; prevalence is framed as
  definition-dependent, South Asian waist references are shown with the ATP III
  example, and blood-test preparation/retest timing defers to lab or clinician
  instructions. Long chips scroll safely on narrow screens.
- **Verification:** Focused compilation/tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** Metabolic education rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## BMI risk context visual consistency — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** BMI risk-context status badges, tone cards, expandable sections,
  risk items, recommendations, action steps and provider notes render stable
  vector icons and shared semantic colors. Legacy model emoji fields remain
  only for persistence compatibility and are not displayed in the screen.
- **Verification:** Focused compilation/tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** BMI risk-context rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## BMI trend and goal safety polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** BMI trend chips and category colors use stable vector icons and
  shared chart/semantic tokens. The BMI goal editor replaces emoji with vector
  icons, shows context when a target is outside the adult reference range,
  converts timeline rates for kg/lb units, and disables invalid target saves.
  ViewModel and formula guardrails prevent malformed or non-finite goal data.
- **Verification:** Added BMIGoalData formula/boundary tests. Focused
  compilation/unit tests and the complete `test`, `lintRelease`,
  `assembleDebug`, `assembleRelease` and `bundleRelease` gate passed.
- **Remaining gates:** Goal/trend rendering, TalkBack, large-font, light/dark
  theme and route checks, plus signing, Firebase and Play Console work, remain
  owner-only.

## BMI edge-case and validation polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** BMI edge-case cards use stable vector icons and shared semantic
  colors instead of emoji and raw literals. Non-finite or invalid BMI, weight
  and height values are now rejected visibly; optional guidance avoids emergency
  terminology for a calculator warning.
- **Verification:** Added edge-case boundary and non-finite-input tests.
  Focused compilation/unit tests plus `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` all passed.
- **Remaining gates:** Edge-case rendering, TalkBack, large-font, light/dark
  theme and route checks, plus signing, Firebase and Play Console work, remain
  owner-only.

## Cross-link and daily-tip icon consistency — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Metabolic-syndrome and calorie cross-links plus the home daily-tip
  card use stable Material vector icons and shared semantic colors instead of
  emoji. All daily-tip categories, including mental health and weight
  management, now have a visible icon mapping.
- **Verification:** Focused compilation/unit tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** Cross-link/daily-content rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## Water celebration visual polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Hydration-goal celebration surfaces now use vector trophy,
  water, progress and streak icons, shared theme surfaces and chart colors.
  The existing haptics, auto-dismiss and share behavior remain intact while
  the copy avoids competitive or emoji-led presentation.
- **Verification:** Focused compilation/unit tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** Celebration rendering, TalkBack, large-font, light/dark
  theme and route checks, plus signing, Firebase and Play Console work, remain
  owner-only.

## Blood-pressure streak visual consistency — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** The blood-pressure streak card uses vector fire/flag icons and
  semantic warning/caution tokens instead of an emoji and raw colors.
  Medication tracking uses the shared wellness color token for its icon,
  surface and focused field. Doctor-suggestion and milestone actions use
  theme-aware/shared semantic colors; reminder and celebration behavior is
  unchanged.
- **Verification:** Focused compilation/unit tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** Blood-pressure streak, medication and milestone
  rendering, TalkBack, large-font, light/dark theme and route checks, plus
  signing, Firebase and Play Console work, remain owner-only.

## Ideal-weight comparison visual consistency — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** The ideal-weight journey card uses typed motivational content
  and Material vector icons instead of emoji. Current/ideal/range bubbles,
  range markers, legend and motivational surfaces use shared semantic theme
  colors, while marker rings follow the active surface for light/dark
  contrast. Existing estimates, copy and unit conversions are unchanged.
- **Verification:** Focused compilation/unit tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** Ideal-weight comparison rendering, TalkBack,
  large-font, light/dark theme and route checks, plus signing, Firebase and
  Play Console work, remain owner-only.

## Blood-pressure alert and profile selection polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Blood-pressure quick-log uses a labeled vector time icon instead
  of a legacy suggestion emoji. Markedly elevated-reading, saved-reading and
  validation surfaces use Material theme/error containers and shared semantic
  colors instead of fixed light-theme literals, with existing safety guidance
  and behavior preserved. Activity-level and health-goal profile dialogs use
  consistent vector icons and descriptions rather than emoji.
- **Verification:** Focused compilation/unit tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** Blood-pressure alert/quick-log and profile-dialog
  rendering, TalkBack, large-font, light/dark theme and route checks, plus
  signing, Firebase and Play Console work, remain owner-only.

## Quick food log icon consistency — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Quick food presets render a consistent Material icon mapped to
  the preset type with an accessible preset-name description, replacing
  platform-dependent food emoji in the dialog. The legacy `QuickFoodPreset`
  emoji field remains for custom/persisted-data compatibility but is not
  rendered here.
- **Verification:** Focused compilation/unit tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** Quick food dialog rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## WHR history and greeting icon polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Waist-to-hip history cards and detail dialogs use vector
  measurement, body-shape and risk icons instead of emoji, shared semantic
  risk tokens and theme-safe WHtR surfaces. Gender metadata is readable text
  without emoji. The personalized greeting uses a time-of-day vector icon
  while retaining its calm copy and gradient treatment.
- **Verification:** Focused compilation/unit tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed; lint report generated at `app/build/reports/lint-results-release.html`.
- **Remaining gates:** WHR history/detail and greeting rendering, TalkBack,
  large-font, light/dark theme and route checks, plus signing, Firebase and
  Play Console work, remain owner-only.

## Return journey icon polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** The health-journey summary uses typed Material icons for
  calculation, milestone and record stats with semantic trend colors. The
  welcome-back route replaces wave, streak-break, freeze, plant and
  last-metric/quick-calculator emoji with accessible vector icons mapped to
  each metric or route; re-engagement copy remains gentle and non-punitive.
- **Verification:** Focused compilation/unit tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed; lint report generated at `app/build/reports/lint-results-release.html`.
- **Remaining gates:** Return-journey rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## Ideal-weight and WHR progress polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Ideal-weight goal progress uses vector result/milestone icons,
  shared semantic progress colors and theme-safe celebration surfaces. WHR
  progress history replaces empty-state and goal emojis, removes emoji-led
  motivational copy, uses shared trend/chart/risk tokens, and keeps graph
  point markers readable against the active surface. Calculations, saved data
  and goal behavior are unchanged.
- **Verification:** Focused compilation/unit tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed; lint report generated at `app/build/reports/lint-results-release.html`.
- **Remaining gates:** Ideal-weight and WHR progress rendering, TalkBack,
  large-font, light/dark theme and route checks, plus signing, Firebase and
  Play Console work, remain owner-only.

## Progress milestone and trend-token polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Ideal-weight milestones use vector achievement/progress icons
  and semantic progress colors instead of emoji and fixed literals. WHR
  progress empty, goal, comparison, graph, statistics and distribution
  surfaces use shared healthy/warning/danger/chart tokens; trend messages no
  longer inject emoji into the banner, and the goal-reference note wraps
  safely on narrow screens.
- **Verification:** Focused compilation/unit tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** Progress milestone/trend rendering, TalkBack,
  large-font, light/dark theme and route checks, plus signing, Firebase and
  Play Console work, remain owner-only.

## Metabolic preview and reminder icon polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Metabolic-syndrome live preview criteria and input cards render
  mapped Material icons with accessible criterion labels instead of emoji.
  Reminder category selection and edit flows use a consistent category-to-icon
  treatment for water, blood pressure, weight, medication, exercise, calories
  and custom reminders; saved reminder icon strings remain compatible.
- **Verification:** Focused compilation/unit tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** Metabolic preview and reminder-dialog rendering,
  TalkBack, large-font, light/dark theme and route checks, plus signing,
  Firebase and Play Console work, remain owner-only.

## Weekly report icon polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Weekly report metrics, highlights, next-week goals and no-data
  states render mapped Material icons with accessible descriptions instead of
  platform-dependent emoji. Wellness Score change colors use shared
  healthy/danger tokens; report copy remains informational and non-diagnostic.
- **Verification:** Focused compilation/unit tests and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Remaining gates:** Weekly report rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## Ideal-weight and IBW theme-token polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Ideal-weight healthy-range pills, IBW comparison links, home
  summary deltas and result warning/save states now use shared semantic
  `HealthColors` and Material theme containers instead of isolated raw color
  literals. Existing calculations, copy, navigation and saved-result behavior
  are unchanged; the surfaces remain readable in light and dark themes.
- **Verification:** Focused Kotlin compilation/unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed with exit code 0; the only emitted warning was the known SDK XML
  version compatibility notice.
- **Remaining gates:** Ideal-weight and IBW rendering, TalkBack, large-font,
  light/dark theme and route checks, plus signing, Firebase and Play Console
  work, remain owner-only.

## BMI scale and history surface polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Shared semantic BMI category/status tokens now drive the BMI
  result header, gauge, live slider preview and additional metrics instead of
  legacy UI color literals; model color fields remain for compatibility. The
  gauge value badge clamps to the actual available width rather than a fixed
  300dp assumption, preventing edge overlap. WHR home risk states and IBW
  history/statistics surfaces use shared semantic colors and vector icons
  instead of emoji placeholders; trend chips provide accessible icon
  descriptions.
- **Verification:** Extended `BmiSliderPolicyTest` for category-role and
  invalid-preview behavior. Focused compilation/unit tests plus the complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed with exit code 0; only known SDK XML/deprecation warnings were
  emitted.
- **Remaining gates:** BMI gauge/slider, WHR home, IBW history and statistics
  rendering, TalkBack, large-font, light/dark theme and route checks, plus
  signing, Firebase and Play Console work, remain owner-only.

## Blood-pressure and risk-surface color-token polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Blood-pressure category and risk mappings, gauge scale, reading
  values, input fields, pulse field, education action and crisis action now use
  shared semantic `HealthColors` or Material error tokens instead of isolated
  legacy literals. Metabolic-syndrome risk and consultation surfaces,
  personal-record celebrations and notification-channel badges follow the same
  role-based palette with theme-aware foregrounds. Calculations, safety
  guidance, persistence and navigation are unchanged.
- **Verification:** Focused compilation/unit tests plus the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with exit code 0; only known SDK XML/deprecation warnings were emitted.
- **Remaining gates:** Blood-pressure/metabolic-risk rendering, TalkBack,
  large-font, light/dark theme and route checks, plus signing, Firebase and Play
  Console work, remain owner-only.

## Residual visual token cleanup — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Changes:** Home calculator cards now use the shared calculator palette; the
  blood-pressure home widget uses semantic warning/danger tokens and a vector
  streak icon; splash colors are sourced from the brand theme; and the
  hydration plant illustration uses named wellness illustration tones and a
  vector streak icon instead of emoji. Existing navigation, calculations,
  persistence and copy are unchanged.
- **Verification:** The complete `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` gate passed with exit code 0 in 9m24s;
  only known SDK XML/deprecation warnings were emitted.
- **Remaining gates:** Remaining raw-color/placeholder audits, route rendering,
  TalkBack, large-font, light/dark theme and connected-device checks, plus
  signing, Firebase and Play Console work, remain owner-only.

## Deep-screen header and action-bar layout polish — 2026-09-16

- **Status:** Code-fixable polish complete; focused emulator smoke checks passed.
- **Changes:** The BMI calculator now uses a compact responsive top bar instead
  of the oversized large app bar. Its title and context remain readable without
  wrapping/clipping, and the clear action is an accessible icon-only control.
  Water result actions use responsive content padding and a minimum share width
  so Recalculate, Save and share stay legible on narrow layouts.
- **Verification:** On `emulator-5554`, the BMI route rendered the full
  `BMI Calculator` title and `Clear all BMI inputs` action. The Water input route
  rendered to a `3.0 Liters` (`3000 ml per day`) result with visible actions; no
  blank screen or app fatal exception was observed. The complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with exit code 0 in 31m50s (known SDK XML/deprecation warnings only).
- **Remaining gates:** Broader device/accessibility/theme/process-death checks,
  release signing, Firebase console/secret rotation and Play Console work remain
  owner-only.

## Legacy emoji surface cleanup — 2026-09-16

- **Status:** Code-fixable visual polish complete; device/accessibility
  validation remains open.
- **Changes:** Milestone, personal-record, metabolic criterion/status and
  reminder-category surfaces now use shared semantic Material icons. Streak and
  hydration widgets use vector assets and plain labels rather than
  font-dependent emoji. Legacy marker fields are retained for persisted and
  exported compatibility.
- **Verification:** Added `WellnessIconMappingTest` for all affected mappings;
  the complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` gate passed with exit code 0. Known SDK XML/deprecation
  warnings only.
- **Remaining gates:** Full device/widget-host matrix, TalkBack, large-font,
  light/dark themes, process death, release signing, Firebase console/key
  restriction and Play Console work remain open.

## Widget fallback and recovery-state polish — 2026-09-16

- **Status:** Code-fixable reliability and trust polish complete; device
  validation remains open.
- **Changes:** Widget error/empty markers and stale badges no longer depend on
  emoji fonts. The generic fallback destination now uses a vector info icon and
  accurate recovery copy instead of “Coming soon.”
- **Verification:** Added widget marker tests; `test`, `lintRelease`,
  `assembleDebug`, `assembleRelease` and `bundleRelease` passed with exit code
  0. Known SDK XML/deprecation warnings only.
- **Remaining gates:** Device/widget-host matrix, TalkBack, large-font,
  light/dark themes, process death, release signing, Firebase console/key
  restriction and Play Console work remain open.

## Runtime smoke verification — 2026-09-16

- **Status:** Focused emulator smoke passed for the current `master` build.
- **Verification:** On `emulator-5554` (`small_phone`), cold start, Profile,
  Calculator hub, Water input and Water result were exercised. Entering 70 kg
  and 30 years produced a visible `3.0 Liters` / `3000 ml per day` result;
  Recalculate, Save and Share were visible. `adb logcat -b crash` and a fatal
  exception scan were empty.
- **Remaining gates:** Release-like signed build, broader API/device and
  widget-host matrix, TalkBack, large-font, themes, process death, migration,
  performance, Firebase and Play Console validation remain open.
