# Release status

Updated: 2026-09-11

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
