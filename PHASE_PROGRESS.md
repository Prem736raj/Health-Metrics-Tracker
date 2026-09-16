# Product development progress

This file is the source of truth for the sequential product-development phases. Phase 1 was complete before this takeover and was not reworked except where a genuine trust regression was found.

## Phase 1 — Production & Play-Store Safety

- **Status:** Complete (inherited)
- **Major changes:** Modern Android/Firebase configuration, release safety, App Check architecture, Health Connect steps permission, notification/reminder safety, Room migrations, privacy/legal pages, CI and release artifacts.
- **Tests:** Inherited green CI, unit tests, lint, debug/release APK and release AAB builds.
- **Known limitations:** Firebase Console, Play Console and signing credentials remain deployment-owner tasks.
- **Next phase:** Medical accuracy and trust audit.

## Phase 2 — Medical Accuracy & Trust

- **Status:** Complete
- **Major changes:** Centralized adult input policy; corrected BMI, BMR, ideal-weight, calorie/TDEE, hydration, waist-ratio, metabolic-marker, blood-pressure, BSA, macro, heart-rate and VO₂ estimate behavior; removed unsupported visceral-fat and population-average claims; reframed the custom Health Score as the non-clinical Wellness Score; fixed TEF double-counting; added transparent methodology, limitations, references and safer result copy.
- **Tests:** Added `MedicalCalculatorAccuracyTest` coverage for formulas, boundaries, invalid values, unit behavior, AHA-style blood-pressure categories, South Asian waist cutoffs, TDEE/TEF accounting, macro/BSA/heart-rate/VO₂ behavior and non-clinical score semantics. Debug unit tests pass.
- **Known limitations:** These calculators remain informational estimates. Clinical validation, personalized medical advice, pediatric/pregnancy interpretation and any medication decisions are intentionally outside the app scope.
- **Next phase:** Simplify navigation and product information architecture.

## Phase 3 — Simplify Product Structure

- **Status:** Complete
- **Major changes:** Replaced the old four-item Home/History/Profile/Settings bottom navigation with five focused areas: Home, Track, Calculators, Insights and Profile. Added dedicated Track, Calculators and Insights hubs that keep high-value actions discoverable while moving detailed logs, reports, education, settings and advanced tools behind progressive navigation. Preserved existing routes for deep links and older saved navigation state.
- **Tests:** Added `NavigationStructureTest` to lock the primary route set and ensure History/Settings remain secondary destinations. Debug unit tests pass.
- **Known limitations:** Existing feature screens remain available as secondary destinations; the next phase will reduce the Home surface itself and unify its daily-value hierarchy.
- **Next phase:** Redesign Home around a concise daily check-in and progressive disclosure.

## Phase 4 — Redesign Home Experience

- **Status:** Complete
- **Major changes:** Replaced the calculator-heavy, long Home grid with a compact daily wellness dashboard. Added a five-second greeting and context, a clearly non-clinical Wellness Score card, four priority daily metric cards (steps, water, weight and calories), latest saved metrics, at most two explainable recommendation previews, focused quick actions, concise calculator discovery and first-use/partial-data/Health Connect empty states. Kept deterministic app prompts visibly separate from the AI Wellness Assistant.
- **Tests:** Added `HomeDashboardPolicyTest` to lock the four daily-value metrics and progressive-disclosure limits for latest metrics and insight previews. Debug unit tests pass.
- **Known limitations:** Home step data remains optional and read-only through the existing minimal Health Connect permission. Weight history and comparisons are expanded in Phase 7.
- **Next phase:** Establish a cohesive, accessible Material 3 brand and visual system.

## Phase 5 — Brand & Visual Identity

- **Status:** Complete
- **Major changes:** Established a calm blue-green wellness palette with explicit light/dark foreground and container pairs, semantic interpretation colors, a restrained chart palette, shared spacing tokens, and a tighter Material 3 shape scale. Replaced runtime Google Font fetching with the platform sans-serif stack for reliable offline startup and accessibility settings. Updated widgets and onboarding accents, standardized exported/report branding to Health Metrics Tracker, and removed obsolete font certificate/dependency resources. Corrected onboarding copy that implied medical-grade or blanket WHO validation.
- **Tests:** Added `ThemeTokensTest` for primary/container contrast targets, spacing-token invariants and semantic color aliases. Debug unit tests pass after the theme refactor.
- **Known limitations:** Older calculator screens still contain some local legacy color literals; new and refreshed surfaces use the shared theme. A follow-up calculator UI pass will migrate remaining high-traffic screens to semantic tokens.
- **Next phase:** Upgrade every calculator with consistent input validation, explanations, sources, persistence and reusable result structure.

## Phase 6 — Upgrade Every Calculator

- **Status:** Complete
- **Major changes:** Added a catalog-driven quality contract for all ten calculator entry points with plain-language purpose, required inputs, method, interpretation, limitations, sources and related tools. The Calculators hub now exposes this context before launch through a consistent information dialog. Removed the obsolete WHR visceral-fat/advanced-metrics flow, model and share output because waist measurements cannot estimate visceral fat. Aligned calorie input validation with the adult 18–120 policy, accepted the domain-supported 2–75% body-fat range, and rejected malformed optional body-fat input.
- **Tests:** Added `CalculatorQualityCatalogTest` to guarantee one complete, linked definition per calculator and extended `MedicalCalculatorAccuracyTest` for adult and body-fat boundaries. Focused debug tests, `test`, `lintDebug`, `assembleDebug`, `assembleRelease` and `bundleRelease` all pass.
- **Known limitations:** Existing calculator result screens still have some legacy local styling and several calculators use their own history UI; the hub contract provides consistent discovery while a later UI consolidation can migrate remaining screens to shared components.
- **Next phase:** Improve daily tracking, trends, goals and healthy retention loops.

## Phase 7 — Tracking & Retention Engine

- **Status:** Complete
- **Major changes:** Added a shared tracking-quality policy for safe weight/water/note/date bounds, optional-clock tolerance and explainable period comparisons. Weight logging now supports edit and delete, metric/imperial validation, seven- and thirty-day average comparisons, goal progress that handles overshoot correctly, and trend-based estimates only when the observed direction supports the goal. Water logging now validates quick-add entries and surfaces save errors through a real snackbar. Home uses the latest logged weight instead of treating the profile baseline as current data, and the wellness summary includes that latest log.
- **Tests:** Added `TrackingQualityPolicyTest` coverage for inclusive boundaries, invalid values, note limits, future-date tolerance, average/percent comparisons, zero-baseline handling and consecutive-day streaks. Full unit tests, lint, debug APK, release APK and release AAB gates pass.
- **Known limitations:** Weight and water remain manual trackers; Health Connect imports are intentionally handled in Phase 8. Streaks remain informational and are not used to shame users or gate safety content.
- **Next phase:** Add optional, feature-led Health Connect integrations with graceful permission handling.

## Phase 8 — Health Connect 2.0

- **Status:** Complete
- **Major changes:** Replaced the single global permission assumption with feature-scoped read-only permissions. Steps continues to request only `READ_STEPS`; weight is now a separate, explicitly requested `READ_WEIGHT` feature with a bounded latest-record reader. Settings and the Connections screen explain why each feature is optional, show refreshable values, handle denied/unavailable access without crashing, and point users to Android Health Connect settings for revocation. Removed the misleading Backup & Restore entry from Settings because no secure portable backup architecture is ready.
- **Tests:** Extended `HealthConnectScopeTest` to verify one-permission steps scope, separate weight scope and absence of write permissions. Full unit tests, lint, debug APK, release APK and release AAB gates pass.
- **Known limitations:** Health Connect availability and records depend on the user’s installed provider and granted access. The app does not write records, import sleep/heart-rate data, or silently request permissions; Play Console health-data declarations remain required before publishing.
- **Next phase:** Build deterministic, explainable insights from local tracking history before any AI interpretation.

## Phase 9 — Smart Insights

- **Status:** Complete
- **Major changes:** Added a deterministic, explainable insight engine for weight, hydration, steps, blood-pressure logging frequency and saved weight-goal context. Insights use bounded seven-day comparisons, show their evidence, avoid causal or diagnostic claims, and link directly to the relevant tracker or connection screen. Home and the Insights hub now surface these observations before optional AI interpretation, with a useful empty state for people who have not logged recently.
- **Tests:** Added `DeterministicInsightEngineTest` coverage for week-over-week weight comparisons, small-change stability wording, hydration goal/tracked-day counts, no-data actions and recorded-day step comparisons. Full unit tests, lint, debug APK, release APK and release AAB gates pass.
- **Known limitations:** Steps currently arrive as the latest Health Connect value rather than a persisted multi-day history, so step comparisons become richer after a history-backed reader is added. Insights remain informational pattern descriptions and do not infer causes, risk or diagnoses.
- **Next phase:** Strengthen the consent-based, context-aware AI Wellness Assistant.

## Phase 10 — Context-Aware AI Wellness Assistant

- **Status:** Complete
- **Major changes:** Added a second, independent consent switch for optional app context. When enabled, only a bounded summary of recent locally logged weight and water patterns is sent; notes, names, raw entries and calculator payloads are excluded, and the summary is not stored in chat history. Added untrusted-prompt delimiters, input-length and rapid-request limits, connectivity-aware offline messaging, failure classification, retry and clear-conversation actions, automatic scroll-to-latest behavior, and deterministic output screening for medication instructions and diagnostic certainty. Potentially urgent symptom prompts receive a clear local-care escalation while preserving the wellness-only role.
- **Tests:** Added prompt-policy, response-safety, context-minimization and failure-classification tests, including control-character cleanup, prompt injection delimiters, rate limits, medication/diagnosis blocking, emergency escalation, recent-window filtering and omission of notes/raw water amounts. Full unit tests, lint, debug APK, release APK and release AAB gates pass.
- **Known limitations:** Firebase AI Logic availability, quotas, App Check and model behavior still depend on Firebase Console configuration and network service. Deterministic screening is deliberately conservative and is not a substitute for professional review; no health measurements are sent to analytics.
- **Next phase:** Build healthy retention loops around summaries, milestones, reminders and widgets.

## Phase 11 — Retention & Engagement

- **Status:** Complete
- **Major changes:** Made inactivity and evening check-ins explicitly opt-in and synchronized their Settings toggles with schedulers/receivers, including boot restoration and notification-permission prompting only when a feature is enabled. Reframed weekly reports from A–F health grades to a non-judgmental logging-rhythm snapshot while retaining legacy database fields for compatibility. Removed fabricated exercise shortfalls and hard-coded widget streaks, softened milestone/streak language, capped all notification categories with the rate limiter, switched repeating schedules to inexact alarms, and added private lock-screen redaction for health reminders and weekly summaries. Notification copy no longer exposes raw BP, weight, calorie or hydration values by default and avoids medication, “fat-burn” and shame-oriented claims.
- **Tests:** Added `WellnessEngagementPolicyTest` for opt-in defaults, rhythm copy and non-punitive streak language. Full unit tests, lint, debug APK, release APK and release AAB gates pass after the memory-safe Gradle configuration update.
- **Known limitations:** Widgets can still show intentionally selected tracker values because Android does not provide a universal widget lock-screen redaction API; users can remove widgets or adjust device privacy. Exercise minutes are omitted from weekly reports until a real exercise data source is persisted. Existing Android notification-channel importance choices remain under system/user control.
- **Next phase:** Make every report/export path user-controlled, privacy-explicit and safe to share.

## Phase 12 — Reports, Export & Sharing

- **Status:** Complete
- **Major changes:** Added a shared `ExportDisclosurePolicy` so text, CSV, JSON, PDF, image, blood-pressure and weekly/profile shares carry consistent provenance, informational-wellness labeling and a non-diagnostic disclaimer. JSON exports now identify the real package and export metadata, CSV output escapes user content safely, empty exports avoid divide-by-zero progress, and history preserves the selected export format when sharing. Weekly reports now require at least one selected section, generic export screens explain what is shared, and the obsolete user-facing Backup route was removed while legacy local backup code remains unrouteable for compatibility.
- **Tests:** Added `ExportDisclosurePolicyTest` for provenance, disclaimer, CSV metadata and idempotent share footers. Full unit tests, lint, debug APK, release APK and release AAB gates pass.
- **Known limitations:** The unfinished backup/restore product remains intentionally absent; no portable transfer or cloud backup is exposed. Play Console data-safety declarations and any future portable encrypted transfer design still require product-owner review.
- **Next phase:** Define a trust-preserving free tier and optional premium value without gating core wellness safety information.

## Phase 13 — Monetization

- **Status:** Complete (store-independent foundation)
- **Major changes:** Added a pure `PremiumFeaturePolicy` with stable future product IDs, an explicit Free/Plus boundary, and a safe entitlement seam for a later Play Billing adapter. Core calculators, basic tracking, local history, deterministic insights, weekly summaries, basic exports, privacy controls and optional Health Connect access remain free. Added `docs/MONETIZATION_PLAN.md` covering calm pricing, non-sensitive measurement, purchase/restore/refund requirements, and a no-ads-until-configured rule; no fake paywall, AdMob placement or local-only entitlement was introduced.
- **Tests:** Added `PremiumFeaturePolicyTest` for free-core coverage, unique product IDs, tier behavior and non-pressure copy. Full unit tests, lint, debug APK, release APK and release AAB gates pass.
- **Known limitations:** Play Billing products, regional pricing, entitlement verification, support/refund configuration and any AdMob account are console-owner tasks. The proposed Plus features are not active until those pieces are implemented and tested.
- **Next phase:** Research current competitors and prepare research-backed Play Store listing assets without publishing.

## Phase 14 — ASO & Launch Preparation

- **Status:** Complete (not published)
- **Major changes:** Added `docs/ASO_LAUNCH_PLAN.md` with a current competitor scan, policy-backed listing copy, keyword intent matrix, screenshot and feature-graphic brief, onboarding/promotional copy, release notes and a pre-publish checklist. Added `StoreListingPolicy` and tests for title/short-description limits, promotional/medical-certainty wording and safe screenshot overlays. Corrected public privacy, terms and support pages to use Health Metrics Tracker and removed inaccurate encrypted-Room/encrypted-backup claims; storage and export behavior now matches the implementation.
- **Tests:** Added `StoreListingPolicyTest`. Full `test`, `lintDebug`, `assembleDebug`, `assembleRelease` and `bundleRelease` gates pass.
- **Known limitations:** Play Console listing upload, screenshots/feature graphic, Health apps/Data Safety/content-rating/Health Connect declarations, public URL validation, developer account, signing and release review remain console-owner tasks. Public competitor pages do not expose reliable keyword volume or conversion data; acquisition/search data and listing experiments should validate the matrix after launch.
- **Next phase:** Add privacy-safe product analytics architecture and a measurable retention funnel.

## Phase 15 — Analytics-Driven Growth

- **Status:** Complete (privacy-safe, opt-in foundation)
- **Major changes:** Added a stable `ProductAnalytics` event contract and strict allowlist that drops sensitive, numeric and free-form values; added a Firebase Analytics reflection adapter with manifest default-off collection and an explicit Settings opt-in; instrumented app open/onboarding/navigation, calculators, trackers, Health Connect, reminders, AI and reports; documented the measurable funnel and retention metrics; updated the privacy policy with the optional analytics disclosure; and configured CI to retain verification reports and release artifacts for failed or successful runs.
- **Tests:** Added `ProductAnalyticsPolicyTest` for stable event names, allowlisted dimensions, sensitive-value dropping and required growth events. Full `test`, `lintDebug`, `assembleDebug`, `assembleRelease` and `bundleRelease` gates pass.
- **Known limitations:** Firebase Console event registration, retention windows, access roles, Data Safety declaration and any BigQuery export remain console-owner tasks. No health measurements are sent in app-defined parameters, and product analytics stays off until the user enables it.
- **Next phase:** All requested product phases are implemented; complete console-owned launch work and validate the funnel in a closed test.

## Post-Phase visual refinement — Human-centered wellness design system

- **Status:** Complete
- **Major changes:** Replaced the one-accent/one-card visual pattern with role-based warm canvas, surface, action, on-track and clay accent tokens; added a serif display voice, sans-serif body hierarchy and tabular monospace measurement style; introduced distinct hero, metric-tile, action-row, insight-callout and empty-state components; removed visible Home/profile emoji placeholders in favor of outlined vector badges; made profile completion encouraging rather than error-colored; aligned profile avatar, widget and dark-theme resource colors; reserved the Wellness Score ring as the single signature load motion; fixed edge-to-edge inset ownership so child top bars no longer receive a duplicated root status-bar gap; and made Profile a calmer top-level destination with compact milestones and an overflow menu for secondary actions.
- **Tests:** Extended `ThemeTokensTest` for role separation and numeric typography and added navigation inset coverage to `NavigationStructureTest`. The exact default `test`, `lintDebug`, `assembleDebug`, `assembleRelease` and `bundleRelease` gates all pass with the documented 4 GB Gradle heap; CI now uses the same budget to avoid Kotlin compiler OOMs.
- **Known limitations:** Some deep calculator/education screens still contain legacy local styling and emoji-backed domain copy; the five primary surfaces and their shared entry components now use the new system. A future screenshot/device QA pass should validate light/dark rendering, large font scales and OEM contrast.
- **Next phase:** Closed-test visual QA, Play Console setup and measurement of first-session activation.

## Phases 6–15

| Phase | Status | Next focus |
| --- | --- | --- |
| 6 — Calculator quality | Complete | Reusable calculator structure, validation, history |
| 7 — Tracking and retention engine | Complete | Fast logging, trends, goals and reminders |
| 8 — Health Connect 2.0 | Complete | Permission-led visible integrations |
| 9 — Smart insights | Complete | Deterministic explainable insight engine |
| 10 — Context-aware AI assistant | Complete | Consent-based context, safe UX and limits |
| 11 — Retention and engagement | Complete | Opt-in summaries, gentle reminders and truthful widgets |
| 12 — Reports, export and sharing | Complete | User-controlled wellness reports |
| 13 — Monetization | Complete | Trust-preserving free/premium boundaries |
| 14 — ASO and launch preparation | Complete | Research-backed store assets and copy |
| 15 — Analytics-driven growth | Complete | Console setup and closed-test funnel validation |
| Visual refinement | Complete | Closed-test visual QA and screenshot validation |

## Post-brief production hardening — Reliability, privacy and trust regression fixes

- **Status:** Complete
- **Major changes:** Persisted and reboot-restored reminder preferences without re-enabling disabled categories; replaced repeating reminders with one-shot inexact rescheduling, fixed cross-midnight hydration windows and weekly date math, and sanitized notification content with private lock-screen visibility. Corrected blood-pressure stage classification and validation, added permission prompts when notification features are enabled, removed false-precision pregnancy/lactation hydration adjustments, and clarified informational limitations. Made AI chat loading/streaming state deterministic, prevented duplicate startup requests, cancelled active requests safely when conversations are cleared, preserved profile edits and calculator inputs through configuration changes, and switched remaining screen collection to lifecycle-aware state. Restricted FileProvider and exports to explicit app-owned directories, removed diagnostic stack traces and raw-health logging, and deleted unreachable insecure backup/QR/cloud code and dependencies. Replaced several emoji placeholders/notification titles, aligned icon resources with the flat brand mark, and kept Health Connect permissions feature-scoped (optional Steps and Weight only).
- **Tests:** Added deterministic `ReminderSchedulePolicyTest` cases for same-day, cross-midnight (including after-midnight), outside-window and weekly scheduling; extended medical tests for blood-pressure boundaries and reproductive-health behavior. The required gates all pass: `./gradlew.bat test`, `./gradlew.bat lintDebug`, `./gradlew.bat assembleDebug`, `./gradlew.bat assembleRelease`, and `./gradlew.bat bundleRelease`.
- **Known limitations:** Device/emulator QA, accessibility sweeps at large font scales, signed artifact verification, Play Console declarations, Firebase Console/App Check configuration and production analytics validation still require external release-owner access. Existing legacy PNG icon assets used for pre-API-26 resources and store artwork should be replaced/checked during the screenshot and listing pass.
- **Next phase:** Closed-test device QA, signed release validation, Play Console setup and measured activation/retention experiments.

## Deep audit — water calculator, visibility and stale-state hardening

- **Status:** Complete locally; device and store-owner gates remain open
- **Major changes:** Fixed the Water Needs result route so it reuses the calculator's back-stack ViewModel instead of rendering a blank screen, and added a visible recovery state when a result is unavailable. Removed duplicated root status-bar insets that created the blank strip above child screens, added visible startup/unknown-route states, corrected Profile unit labels and latest-weight sourcing, replaced stale streak-protection and welcome-back placeholders with Room/DataStore-backed values, and softened onboarding/water/BSA copy that could imply medical precision. Rebuilt onboarding illustrations and buttons with stable Material vector icons and theme roles instead of emoji orbitals and unrelated pink/blue/purple gradients. Reworked the water input/result surfaces, unit/tip/timeline graphics and action states for dark-theme contrast and consistent vector icons. Hardened BSA calculation fallback behavior for stale formula IDs and replaced the BSA empty-state emoji with accessible vector icons.
- **Tests:** Focused BSA unit tests pass. The complete `test`, `lintDebug`, `assembleDebug`, `assembleRelease` and `bundleRelease` suite is rerun after the audit changes; signed-artifact verification remains intentionally blocked without release credentials.
- **Known limitations:** ADB currently has no usable physical device; the small local emulator previously hit a System UI ANR, so screenshot and accessibility validation remain owner/device gates. Legacy deep calculator/education screens still contain some local colors, small text and emoji-backed explanatory copy. Release APK/AAB outputs are unsigned until the owner supplies signing properties.
- **Next phase:** Install the signed release on representative Android devices, exercise every primary and calculator route, run large-font/dark-mode/accessibility checks, then use closed-test telemetry to prioritize remaining UI polish.

## Post-audit targeted UI fixes — History and BMI slider layout

- **Status:** Complete locally; release signing and broad device coverage remain open
- **Major changes:** Replaced History's large collapsing app bar with a compact `TopAppBar` so the back button, title and actions share one row without the oversized top gap. Made BMI keyboard and slider input layouts mutually exclusive instead of animating both columns over one another; this removes the visible Weight/live-preview overlap on short screens. Verified the BMI Learn page has one `Learn About BMI` header in the current build, so the sole education entry was intentionally preserved rather than deleting content that is not duplicated in the repository.
- **Tests:** `:app:compileDebugKotlin` and `:app:assembleDebug` pass. Installed the debug APK on `emulator-5554`; History screenshot/UI hierarchy confirms the compact one-row app bar, BMI slider screenshot confirms separated Weight/Height/live-preview surfaces, and UI hierarchy reports one `Learn About BMI` occurrence. No crash was observed during these flows.
- **Known limitations:** This is targeted smoke coverage, not a full route/accessibility sweep. The current source still contains legacy emoji-backed BMI education labels and broad lint warnings outside these screens; the release APK/AAB remain unsigned without owner signing properties.
- **Next phase:** Run the complete five-command verification suite, commit the audit fixes, then continue signed-device and closed-test QA.

## Item 1 — Visual system consolidation across primary surfaces

- **Status:** Complete locally; device accessibility and screenshot QA remain open
- **Major changes:** Consolidated the visual language around named palette roles, shared spacing/elevation tokens, a serif display voice, a tabular measurement style and explicit 48 dp touch targets. Added genuinely distinct hero, metric/data, navigation-row, loading and recoverable-error components. Home now reserves the deep gradient treatment for the Wellness Score hero, while tracking/calculator entry cards use tonal surfaces and feature-specific accents. Hub cards and top app bars use the same roles, BMI loading uses the shared state, and calculator cards expose semantic summaries. Replaced visible BSA/WHR emoji illustrations with stable vector icons and removed local feature-color literals from the primary dashboard and hubs.
- **Tests:** Theme token tests now cover hero contrast, component-tier elevation and touch-target minimums. `test`, `assembleDebug`, `assembleRelease` and `bundleRelease` pass after this pass; release lint-vital also passes. A full debug lint analysis was attempted twice but exceeded the local Windows host memory budget before producing a fresh report; the pre-existing lint report and CI configuration remain unchanged.
- **Known limitations:** Some deep educational/calculator content still owns legacy local colors and emoji-backed semantic data; these are the next visual migration targets after the first-session flow. Large-font, TalkBack, tablet and OEM dark-mode checks still require a real device/emulator.
- **Next phase:** Improve the first five minutes so a new user gets one useful result before optional profile, notification or Health Connect setup.

## Item 2 — First-session activation flow

- **Status:** Complete locally; device/onboarding smoke QA remains open
- **Major changes:** Shortened onboarding from four pages to three, replaced the profile-first ending with a calm first-action chooser, and made the copy explicit that setup is optional and missed days do not erase progress. New users can choose Water, Weight, Steps or a direct BMI calculator route; Steps lands on the Track hub so Health Connect remains permission-led rather than being requested on launch. Profile setup is still available as a secondary choice, while Explore Home remains the no-commitment path. Added a privacy-safe `onboarding_action_selected` analytics event using a fixed vocabulary.
- **Tests:** Added route mapping coverage for all four first actions and analytics sanitization coverage for the new event. Focused Kotlin compilation and unit tests pass after the flow change.
- **Known limitations:** A first action does not yet automatically offer a reminder after the user completes it; reminder prompts remain feature-led in the relevant trackers and belong to the retention pass. Device-level first-run, process-death and large-font testing still require a usable emulator/device.
- **Next phase:** Deepen tracking so the selected action has fast logging, history, trends, goals and reliable restoration.

## Item 3 — Durable step history and tracking depth

- **Status:** Complete locally; Health Connect device validation remains open
- **Major changes:** Added a Room-backed `step_history` table with an explicit 15→16 migration, bounded 35-day local retention and read-only Health Connect daily snapshots. Home now restores the latest steps value from local storage after process death, syncs a 30-day history when Steps access is already granted, and feeds that history into deterministic week-over-week insights. The Home steps tile surfaces the recorded percentage comparison when two complete windows exist. Health Connections now shows a compact seven-day bar summary with average steps, clear read-only provenance and non-diagnostic wording. Settings sync uses the same durable history path, while permissions remain optional and feature-scoped.
- **Tests:** Added `StepTrendPolicyTest` for complete/incomplete windows, zero baselines and trend direction, plus `StepHistoryRepositoryTest` for restored duplicate/invalid rows. Focused Kotlin compilation and the complete unit suite pass after the implementation. The Room schema export now includes version 16.
- **Known limitations:** Health Connect is unavailable in the current shell (no usable ADB/device), so provider-specific record behavior, permission denial and chart rendering remain device QA gates. Step snapshots are aggregated by record start date and retain only the recent local window; the app still never requests write access or sends step counts to analytics.
- **Next phase:** Make calculator flows consistently premium-quality with shared result/history patterns and the remaining boundary/conversion tests.

## Item 4 — Consistent calculator evidence and limitation disclosures

- **Status:** Complete locally; device route/accessibility QA remains open
- **Major changes:** Added one reusable, scrollable “How this estimate works” sheet to every direct calculator route and the calculator hub. The sheet makes the existing evidence catalog available at the point of use: purpose, required inputs, method, interpretation, limitations, sources, related calculators and a plain-language informational-wellness disclaimer. Replaced the previous no-op Metabolic Syndrome info button and removed the hub-only duplicate dialog so every calculator now presents the same disclosure surface. The blood-pressure route only shows the new action before values are entered, preserving room for its existing export, trend, history and clear actions.
- **Tests:** Extended `CalculatorQualityCatalogTest` to require non-empty purpose, inputs, method, interpretation, limitations and sources for all calculator destinations. Focused Kotlin compilation and unit tests pass.
- **Known limitations:** This pass exposes and tests the existing reviewed formula documentation; it does not replace the different legacy calculation input/result layouts or add device-level TalkBack, large-font and route smoke coverage. No health values are added to analytics.
- **Next phase:** Strengthen the focused product identity so the visual, onboarding, report and trust language all communicate a calm, privacy-conscious wellness companion.

## Item 5 — Focused product identity and wellness voice

- **Status:** Complete locally; device and console QA remain open
- **Major changes:** Established one product voice for onboarding, splash, settings, reports and future store materials; centralized the app name and tagline; replaced the clinical-looking splash cross with a calm outlined wellness mark; removed the remaining visible footer emoji; and documented the approved positioning, visual signature and safety language in `docs/BRAND_IDENTITY.md`.
- **Tests:** Added `BrandVoiceTest` to protect calm, privacy-conscious and non-diagnostic wording. The complete `test`, `lintDebug`, `assembleDebug`, `assembleRelease` and `bundleRelease` gates pass.
- **Known limitations:** Store artwork, launcher icon exports and Play Console listing assets still require a visual asset pass and owner-side console work. GitHub may flag the Firebase Android client key in `google-services.json`; Firebase documents this config as public by design, but the Firebase-provisioned key must still be restricted to Firebase-related APIs in Google Cloud and monitored. That restriction requires console access.
- **Next phase:** Validate reliability and accessibility across representative devices, then use closed-test evidence to prioritize remaining polish.

## Production audit — P1 safety and correctness fixes

- **Status:** Complete locally; device/provider verification remains open
- **Major changes:** Disabled the exposed multi-profile switch/add/share controls until health records are profile-scoped, made DataStore migration always materialize one active Room profile, and kept profile weight edits synchronized with the active profile row. Replaced raw Health Connect step-record summation with per-local-day `StepsRecord.COUNT_TOTAL` aggregation. Corrected mixed blood-pressure precedence so hypertensive patterns are not hidden by a low diastolic value. Removed the unsupported waist-medication metabolic criterion while preserving legacy storage compatibility. Fixed AI chat state so the user's message is rendered immediately before streaming. Standardized filtered and all-history CSV exports on one deterministic universal schema. Stopped extrapolating historical ideal-weight equations below 60 inches while retaining the BMI reference range and safe unavailable states.
- **Tests:** Added short-height IBW boundary tests, CSV schema/escaping tests and mixed blood-pressure boundary cases. The required `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate passes.
- **Known limitations:** Existing health records remain device-wide; multi-profile UI must stay disabled until a profileId migration is designed and tested. Health Connect aggregate behavior and accessibility need physical-device/provider validation. Release artifacts remain unsigned without owner signing credentials.
- **Next phase:** Complete the audit's P2 evidence, parsing, midnight, validation, migration, accessibility and scheduling hardening items.

## Production audit — P2 data, trust, scheduling and accessibility hardening

- **Status:** Complete for code-fixable items; runtime gates remain explicitly open.
- **Major changes:** Replaced fragile history detail splitting with `JSONObject`-first parsing plus a conservative JVM fallback and legacy pipe compatibility. Made the food repository day-aware at its source, including cold-start archival across midnight. Unified BMR calculation and validation boundaries, renamed WHR's below-threshold proximity band, and labeled hydration adjustments as heuristic starting points. Replaced BP 24-hour repeating alarms with one-shot local-wall-clock scheduling that re-arms after delivery and on reboot/timezone/time changes. Removed unsupported VO2 fitness-age, percentile, classification-gauge, projection and recovery-quality surfaces from the consumer flow; raw estimates now show method and limits. Raised audited interactive targets to 48dp and added Room 15→16 migration coverage.
- **Tests:** Added history JSON/legacy parsing, food day-policy, BMR boundary, WHR wording, hydration disclosure, VO2 safety, and timezone/DST schedule unit tests. Added an instrumentation `MigrationTestHelper` for 15→16.
- **Known limitations:** The oldest real distributed Room version is unknown and only schemas 15/16 are checked in. Migration instrumentation, macrobenchmark startup timing, device accessibility, Health Connect providers, notification reboot/timezone behavior, and Firebase/Play Console checks still require owner/device access.
- **Next phase:** Complete P3 repository/manifest cleanup and retain only the documented runtime and publishing gates.

## Production audit — P3 repository and manifest cleanup

- **Status:** Complete for verified dead artifacts; larger architecture migrations remain deferred by design.
- **Major changes:** Removed the dead `LOG_MEAL` quick-action intent, deleted the obsolete root handoff/prompt/extractor files, and removed the unreferenced `app/src/layout/widget_health_large.xml` while retaining the live `app/src/main/res/layout` widget resource. No application behavior or user data migration was changed.
- **Tests:** Re-ran reference searches to confirm the deleted artifacts and action are not referenced; the live large-widget resource remains referenced by its provider and updater.
- **Known limitations:** The `notification`/`notifications` package split and legacy Hilt/service-locator overlap remain incremental architecture work, not release blockers. Migration history below version 13, macrobenchmark, device accessibility, Health Connect provider behavior, and Play/Firebase owner gates remain open in `docs/RELEASE_STATUS.md`.
- **Next phase:** Maintain the documented runtime/publishing gates and avoid enabling multi-profile until profile-scoped migrations are designed and tested.

## Post-audit medical, AI and release-trust hardening

- **Status:** Complete locally for code-fixable items; device/provider and
  publishing gates remain open.
- **Major changes:** Standardized WHR measurement landmarks and one sourced
  population reference point, removed the unsupported synthesized waist band,
  unified the inclusive severe blood-pressure boundary, removed fixed
  heart-rate duration prescriptions, deleted an unused BSA placeholder reader,
  and corrected AI retry so it cannot duplicate the persisted user turn. Added
  data-safety and device/accessibility QA documentation and corrected stale
  public links to the canonical repository and Pages site.
- **Tests:** Added WHR, blood-pressure, heart-rate guidance and AI conversation
  policy tests. Full `test`, `lintRelease`, `assembleDebug`, `assembleRelease`
  and `bundleRelease` verification is green; connected instrumentation is
  blocked by the absence of a connected device.
- **Follow-up:** Widget blood-pressure accessibility text now uses the shared
  calculator categorization, with regression coverage for mixed and severe
  readings.
- **Follow-up:** BMI slider preview now keeps its numeric and category surfaces
  separated at compact and large font sizes, keeps edge markers inside the
  visible scale, exposes a spoken category summary, and uses 48 dp labelled
  fine-tune controls. `BmiSliderPolicyTest` covers marker clamping, including
  non-finite input.
- **Follow-up:** The BMI Learn surface now uses the shared vector/icon language
  and semantic theme roles instead of emoji headings, risk markers and local
  colour literals, while retaining the educational copy and disclosure.
- **Follow-up:** The WHR education surface now uses the shared vector/icon
  language and semantic calculator palette instead of emoji illustrations and
  one-off colours. Reference bands and BMI/WHR comparisons are explicitly
  framed as informational context; unsupported individual risk multipliers,
  fixed waist-change timelines and causal wording were removed.
- **Follow-up:** The BSA education surface now maps legacy illustration data to
  shared vector icons and semantic palette roles. Historical formula notes no
  longer call an equation a gold standard or universally most accurate, and
  population variation is explained without unsupported precision claims.
- **Follow-up:** The WHO exercise-guidelines surface now renders zone, goal,
  session, and progress visuals with vector icons and shared semantic colours.
  Its progress ring exposes a spoken summary, destructive session removal is a
  48 dp target, and decorative emoji markers were removed from the rendered
  experience.
- **Verification:** The complete `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` gate is green after this follow-up.
- **Known limitations:** Connected Room migration, Health Connect provider,
  TalkBack/large-font, OEM scheduling, signed artifact and Play/Firebase
  console checks still require owner/device access. The oldest distributed Room
  version is not evidenced by this repository.
- **Next phase:** Execute the documented device/console release checklist;
  continue migrating remaining deep educational screens only when their copy
  or visual treatment has a verified trust or accessibility benefit.

## Follow-up — Heart-rate recommendation visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Replaced rendered goal, fitness-level, zone, calorie,
  workout and tip emoji with stable Material icons while retaining legacy
  labels in stored recommendation data. Recommendation copy is cleaned at
  render time so legacy decorative markers do not leak into the shipped UI.
  The recommended-state treatment now uses the shared semantic health palette,
  expandable rows meet a 48 dp minimum touch target, and an empty/zero-valued
  zone distribution no longer attempts to draw invalid arcs.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** The recommendation engine still keeps emoji fields
  for backwards-compatible persistence and generated copy; no health values
  are sent to analytics. TalkBack, large-font and chart rendering checks need
  a connected device.
- **Next phase:** Execute the documented device/console release checklist;
  migrate another deep screen only when a verified trust, accessibility or
  reliability benefit is identified.

## Follow-up — VO₂ max estimate visual and wording cleanup

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** The VO₂ max surface now uses stable heart, chart, timer,
  assignment and recovery icons instead of rendered emoji and replaces local
  color literals with shared semantic health colors. Copy now consistently
  calls the value an informational VO₂ max estimate; the former fitness-age
  wording and recovery certainty were softened, and legacy strings remain
  compatible for stored data. Unused classification visuals also use the same
  icon/palette language if they are reintroduced later.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** This estimate remains a heart-rate-ratio proxy, not a
  laboratory VO₂ max test. TalkBack, large-font and physical-device rendering
  checks remain open.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only where it improves trust, accessibility or
  reliability.

## Heart-rate activity reference trust and visual consistency follow-up

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Major changes:** The activity guide now uses stable Material vector icons
  for the section header, category filters, activity rows, heart-rate/calorie
  details and empty search state. Legacy activity/category emoji remain in
  source models for compatibility but are no longer rendered. Calorie markers
  use the shared calorie semantic token instead of an isolated orange literal;
  activity descriptions, zone ranges and calorie estimates are unchanged.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Known limitations:** The guide's expandable rows and horizontal filter
  chips still need connected-device checks at large fonts, with TalkBack, and
  in both light and dark themes.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only where it improves trust, accessibility or
  reliability.

## BSA result trust and visual consistency follow-up

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Major changes:** BSA result summaries, comparison context and formula
  recommendations now use stable Material vector icons instead of emoji and
  theme/feature semantic colors instead of isolated literals. Missing profile
  sex no longer silently defaults to a male comparison; it shows a neutral
  adult-context explanation. Formula guidance now describes population/source
  context without universal accuracy or clinical-prescription claims.
  Calculations, persistence, history and export callbacks remain unchanged.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Known limitations:** BSA remains an informational estimate and its
  population comparisons are not reference ranges. Connected-device checks
  are still needed for animations, large fonts, TalkBack and both themes.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only where it improves trust, accessibility or
  reliability.

## Metabolic screening result trust and visual consistency follow-up

- **Status:** Code-fixable polish complete; device/accessibility validation
  remains open.
- **Major changes:** The metabolic screening result route now uses shared
  semantic health colors and stable vector icons for the gauge, screening
  summary, criteria states and risk message instead of a raw red literal or
  rendered emoji. The helper is explicitly named as a screening summary, and
  the existing non-diagnostic wording is preserved. Screening calculations,
  ethnicity thresholds, medication markers, persistence and share callbacks
  remain unchanged.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with `GRADLE_EXIT=0`.
- **Known limitations:** This remains an informational screening aid, not a
  diagnosis. Gauge animation, expandable recommendations, large-font,
  TalkBack and light/dark theme checks require a connected device.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only where it improves trust, accessibility or
  reliability.

## Follow-up — Calorie education trust and visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** The calorie education guide now uses shared calorie,
  health, caution, warning, and informational palette roles and stable vector
  icons for section headers, food categories, guidance, and mindful notes.
  Calorie-balance math is explicitly described as a simplified estimate, and
  fixed daily minimums were replaced with context-aware wording to avoid
  unsafe universal targets. Restrictive-diet copy now encourages gradual,
  individualised guidance without diagnosis or certainty claims.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Legacy content labels remain in source data for
  compatibility, but emoji are not rendered. TalkBack, large-font, theme and
  expandable-content checks require a connected device.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

Updated: 2026-09-13

## Follow-up — Meal timing trust, custom schedule correctness and visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open
- **Major changes:** Meal-timing pattern and fasting surfaces now use stable
  Material vector icons and shared semantic colors instead of rendered emoji,
  raw color literals and a hard-coded white marker. The custom eating-window
  and meal-count controls now drive the schedule calculation (including
  clamped safety bounds), so the preview, timeline and fasting summary cannot
  disagree with the selected values. Meal schedule labels are plain and
  accessible; macro chips use the shared palette. Existing standard-pattern
  portions and BMR navigation remain unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed, including custom
  window/meal-count boundary, overnight end-time and calorie-total regressions.
  The complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` gate is run before this slice is committed.
- **Known limitations:** Connected-device timeline animation, TalkBack,
  large-font, light/dark theme and route checks remain open; signing,
  Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue the deep calculator audit only where it improves
  truthful interpretation, accessibility or reliability.

Updated: 2026-09-14

## Follow-up — Home dashboard score and action semantics

- **Status:** Code-fixable polish complete; device/accessibility validation remains open
- **Major changes:** The dashboard Wellness Score subtitle now accurately
  describes recent check-ins and daily logging instead of naming inputs the
  score does not use. Restored score values are clamped before progress-ring
  rendering, and home quick actions use matching feature colors/icons for
  weight and blood pressure. AI/action icons now use the shared hero token,
  and recommendation icon mapping matches the underlying metric type.
  Navigation and score methodology remain unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate is run before this slice is committed.
- **Known limitations:** Connected-device ring/contrast rendering, TalkBack,
  large-font, light/dark theme and route checks remain open; signing, Firebase
  and Play Console tasks still require owner access.
- **Next phase:** Continue auditing deep calculator result/history and report
  surfaces for stale emoji, fixed-color styling or no-op actions.

Updated: 2026-09-14

## Follow-up — Home calculator discovery card consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open
- **Major changes:** Home calculator cards now use reusable vector icons and
  shared feature/semantic palette tokens instead of per-card emoji and raw
  color literals. Progress-ring centers are accessible icons, image cards use
  shared overlay/text tokens, and WHR card status coloring follows the saved
  category label rather than reclassifying with a fixed threshold. Metabolic
  card copy now says criteria are flagged/reviewable instead of asserting a
  diagnosis. Navigation, calculations, progress values and card layout remain
  unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate is run before this slice is committed.
- **Known limitations:** Connected-device card/animation rendering, TalkBack,
  large-font, light/dark theme and route checks remain open; signing, Firebase
  and Play Console tasks still require owner access.
- **Next phase:** Continue the visual audit of remaining deep calculator and
  share/report surfaces where emoji or stale styling is still visible.

Updated: 2026-09-14

## Follow-up — Home recommendations visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open
- **Major changes:** Recommendation cards now use a shared icon vocabulary by
  recommendation type, with plain accessible labels instead of emoji. Dismiss,
  priority and completed-state surfaces use shared semantic colors; the
  recommendation engine now supplies feature/semantic palette tokens instead
  of raw literals. Recommendation ordering, dismissal, actions and copy remain
  unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate is run before this slice is committed.
- **Known limitations:** Connected-device swipe/animation and contrast checks,
  TalkBack, large-font, light/dark theme and route checks remain open; signing,
  Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue auditing calculator discovery cards for remaining
  legacy emoji/raw styling and status semantics.

Updated: 2026-09-14

## Follow-up — Home wellness overview visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open
- **Major changes:** The home wellness overview now uses vector icons for its
  header, score categories, quick stats, empty state and last-activity row
  instead of placeholder emoji. Health-score categories and quick-stat accents
  now draw from the shared semantic/feature palette, including a theme-aware
  no-data ring. Existing score methodology, metric selection, click targets
  and navigation remain unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate is run before this slice is committed.
- **Known limitations:** Connected-device score-ring and large-font rendering,
  TalkBack, light/dark theme and route checks remain open; signing, Firebase
  and Play Console tasks still require owner access.
- **Next phase:** Continue auditing home recommendations and calculator
  discovery cards for remaining legacy styling and misleading status color.

Updated: 2026-09-14

## Follow-up — History and quick-action visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open
- **Major changes:** History entries and filter chips now use a shared
  calculator-to-vector-icon mapping, with plain labels that remain readable
  across fonts and platforms. History category indicators use the shared
  semantic palette rather than legacy raw color literals. Home quick actions
  now render the vector icon already supplied by each action, with an
  accessible content description, instead of a second emoji representation.
  Tap, long-press, selection, filtering and navigation behavior remain
  unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate is run before this slice is committed.
- **Known limitations:** Connected-device rendering, TalkBack, large-font,
  light/dark theme and route checks remain open; signing, Firebase and Play
  Console tasks still require owner access.
- **Next phase:** Continue auditing the remaining home recommendation and
  calculator discovery surfaces for visible legacy styling.

Updated: 2026-09-14

## Follow-up — Weight goal progress/trend trust and visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open
- **Major changes:** Weight goal progress now clamps malformed or restored
  percentages before drawing and uses shared semantic colors for progress and
  goal states. Equal-to-start goals are treated as maintenance goals with a
  small 0.1 kg tolerance instead of appearing complete accidentally. Trend
  segments use shared toward/away-goal colors, while goal completion and
  estimated dates use accessible Material icons and approximate wording rather
  than emoji. Existing logging, filtering, tapping and navigation behavior
  remain unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed, including
  maintenance-goal and non-finite-progress coverage. The complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  is run before this slice is committed.
- **Known limitations:** Connected-device graph/tap rendering, TalkBack,
  large-font, light/dark theme and route checks remain open; signing, Firebase
  and Play Console tasks still require owner access.
- **Next phase:** Continue the deep audit of remaining history and home
  surfaces only where it improves truthful interpretation, accessibility or
  reliability.

Updated: 2026-09-14

## Follow-up — BMR trend trust and visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open
- **Major changes:** BMR trend history now uses shared calculator/health tokens and
  stable Material vector icons for empty, comparison, statistics and insight
  states instead of rendered emoji and isolated raw colors. Trend markers use the
  current surface color so they remain readable in both themes. Insight copy is
  explicitly observational and non-diagnostic, avoids inferring muscle gain or
  metabolic health, and flags invalid previous values without producing a
  divide-by-zero percentage. Existing chart, history, selection and persistence
  behavior remain unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate is run before this slice is committed.
- **Known limitations:** Connected-device chart interaction, TalkBack,
  large-font, light/dark theme and route-rendering checks remain open.
- **Next phase:** Continue the deep calculator audit only where it improves
  truthful interpretation, accessibility or reliability.

Updated: 2026-09-13

## Follow-up — Calorie macro planning trust and visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open
- **Major changes:** The calorie-result macro planner now uses stable Material
  icons and shared semantic colors for diet presets, macro sliders, detail
  cards and per-meal references instead of emoji and legacy color literals.
  Preset descriptions are planning-oriented and avoid promising weight-loss or
  muscle-building outcomes. Per-meal chart fractions remain finite when an
  invalid meal count is restored, and plain food labels improve accessibility.
  Existing percentage balancing, callbacks and calculations remain unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed, including preset
  copy safety and invalid meal-count finite-value regressions. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate is run before this slice is committed.
- **Known limitations:** Connected-device chart/slider rendering, TalkBack,
  large-font, light/dark theme and route checks remain open; signing,
  Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue the deep calculator audit only where it improves
  truthful interpretation, accessibility or reliability.

Updated: 2026-09-14

## Follow-up — BMR age comparison trust and visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open
- **Major changes:** The age-comparison card now uses a stable Material chart,
  information and schedule icon set with shared semantic tokens. The chart's
  user marker follows the active surface color in both themes, and long
  comparison/age notes align safely at the top. Reference copy no longer
  implies that an estimate identifies muscle mass, fitness or metabolic health;
  age-curve text is clearly population context rather than a personal forecast.
  Interpolation and chart interaction behavior remain unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate is run before this slice is committed.
- **Known limitations:** Connected-device chart rendering, TalkBack,
  large-font, light/dark theme and route checks remain open.
- **Next phase:** Continue the deep calculator audit only where it improves
  truthful interpretation, accessibility or reliability.

Updated: 2026-09-13

## Follow-up — BMR formula and body-fat input trust/visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Formula selection and body-fat help surfaces now use
  shared semantic colors and clear vector method icons. The recommended
  formula tag is plain “Recommended”, historical formula copy avoids an
  unsupported accuracy percentage, and body-fat method/range guidance now
  explains measurement uncertainty and avoids presenting population bands as
  personal targets. Input visibility and selection behavior are unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate must pass before this slice is committed.
- **Known limitations:** Connected-device chip scrolling, body-fat dialog,
  TalkBack, large-font, light/dark theme and route checks remain open.
- **Next phase:** Continue the deep calculator audit only where it improves
  truthful interpretation, accessibility or reliability.

Updated: 2026-09-13

## Follow-up — Heart-rate dashboard and trend trust/visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Heart-rate dashboard, formula comparison, edge-warning and
  resting-trend surfaces now use shared feature/health tokens and stable vector
  icons. Formula guidance is framed as a starting estimate rather than a
  universally best or guaranteed-accurate method; warning and trend copy avoids
  diagnosis or causal fitness claims. Exported heart-rate images/text use the
  same semantic palette and plain, accessible labels. Existing calculations,
  history callbacks and legacy marker compatibility remain unchanged.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Export rendering and trend animation still need
  connected-device checks for TalkBack, large fonts and light/dark themes.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only where it improves trust, accessibility or
  reliability.

## Follow-up — Heart-rate zone input/result trust and visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Heart-rate zone input and result routes now use shared
  feature/health tokens, stable vector icons and clearer visual hierarchy for
  gender, fitness level, formula, heart-rate reserve and training zones. The
  age-adjusted formula badge avoids an unsupported accuracy promise. Legacy
  zone markers remain compatible, while zone bars, detail rows, pulse
  animation and export actions no longer render emoji or isolated raw colors.
  Calculation, persistence and history behavior are unchanged.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Legacy model markers/colors remain in calculation
  data for compatibility. Connected-device checks are still needed for pulse
  animation, expanded zone cards, TalkBack, large fonts and light/dark themes.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only where it improves trust, accessibility or
  reliability.

## Follow-up — Electrolyte information trust and visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Electrolyte headers, foods, situations, ORS notes and
  warning rows now use shared hydration/health semantic colors and stable
  vector icons. The guide no longer renders emoji or one-off colors. Homemade
  drink content is clearly labelled as a beverage illustration, not treatment
  or WHO ORS; guidance now avoids universal supplement, altitude, or hangover
  claims and points to commercial ORS/clinical care where appropriate.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Legacy food/situation marker fields remain in source
  data for compatibility, but are not rendered. TalkBack, large-font, theme
  and long-guide scrolling checks require a connected device.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

Updated: 2026-09-13

## Follow-up — Ideal-weight metrics trust and visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Ideal-weight category scales, adjusted-weight and body-
  composition cards now use shared semantic/feature colors and stable vector
  icons for category and sport markers. Adjusted body weight is labelled as an
  informational estimate used in some clinical methods, not a medication or
  weight target; the existing persistence and calculations are unchanged.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Body-composition values remain formula-based
  estimates and require context; TalkBack, large-font, theme and expandable
  content checks require a connected device.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

Updated: 2026-09-13

Updated: 2026-09-12

## Follow-up — BMR education trust and visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** BMR education and edge-warning surfaces now use stable
  Material vector icons and shared calculator/health tokens instead of emoji
  markers and isolated raw colors. Explanations remove unsupported fixed
  percentages and clarify that BMR/TDEE values are estimates, not a personal
  calorie floor or a prescription. The calculator formulas, validation,
  persistence and navigation remain unchanged.
- **Tests:** Focused Kotlin compilation passed. The complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  is run before this slice is committed.
- **Known limitations:** Connected-device expandable education, warnings,
  TalkBack, large-font, light/dark theme and route-rendering checks remain
  open.
- **Next phase:** Continue the deep calculator audit only where it improves
  truthful interpretation, accessibility or reliability.

Updated: 2026-09-13

## Follow-up — Metabolic-syndrome standards comparison trust and visual cleanup

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** The ATP III, IDF and WHO comparison surface now uses
  stable Material vector icons instead of rendered emoji/flag characters.
  WHO is explicitly shown as **Not scored** when the app does not collect the
  laboratory or clinical inputs required by that definition. IDF and ATP III
  explanatory copy now describes screening/reference context without implying
  diagnostic accuracy; ethnicity and “which standard” guidance is clearer and
  calmer while existing calculations and selection behavior remain unchanged.
- **Tests:** Focused Kotlin compilation passed. The complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  is run before this slice is committed.
- **Known limitations:** Connected-device comparison expansion, TalkBack,
  large-font, light/dark theme and route-rendering checks remain open.
- **Next phase:** Continue the deep-screen trust/visual audit only where it
  improves accessibility, reliability or truthful interpretation.

Updated: 2026-09-13

## Follow-up — WHR result and animation trust/visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** WHR result categories and thresholds now use calm
  action-point wording instead of implying diagnosis. Risk, warning, body-shape
  and trend surfaces use shared semantic/theme colors with stable Material
  vector icons; the export summary no longer includes decorative emoji. The
  persisted WHR model, calculation, save flow and history behavior are
  unchanged.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Legacy risk/body-shape fields may retain emoji values
  for compatibility, but they are not rendered. Gauge animation, large-font,
  TalkBack, theme and route rendering checks require a connected device.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only where it improves trust, accessibility or
  reliability.

Updated: 2026-09-13

## Follow-up — Heart-rate education trust and visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** The heart-rate education guide now renders legacy markers as
  stable Material icons and uses shared heart, health, warning, and neutral
  palette roles instead of one-off colors. Resting-rate, maximum-rate, training
  distribution, and monitoring-accuracy copy was qualified so population
  estimates are not presented as diagnoses, safety limits, or prescriptions.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Legacy education data still stores emoji labels for
  compatibility, but they are not rendered. TalkBack, large-font, theme and
  expandable-content checks require a connected device.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

Updated: 2026-09-13

## Follow-up — BP advanced metrics visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Pulse-pressure, mean-arterial-pressure and heart-rate
  advanced cards now use shared semantic colors for categories, scales and
  highlights, with feature-specific accents for their headers. Informational
  interpretation, reference ranges and expandable behavior are unchanged.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** These derived metrics are informational context, not
  a diagnosis or treatment target. Connected-device TalkBack, large-font,
  theme and expandable-card checks remain open.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

## Follow-up — Hydration tools visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Urine-color, dehydration-symptom, water-from-food and
  electrolyte tabs now use shared feature/health tokens and stable Material
  icons for headers, statuses, risks, foods, recommendations and quick facts.
  Legacy model markers remain compatible but are not rendered; explanatory
  recommendation text is cleaned without changing its meaning.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Urine color and symptom checks are informational, not
  diagnostic. Connected-device TalkBack, large-font, theme and tab interaction
  checks remain open.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

## Follow-up — Hydration education visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** The hydration guide now uses shared water/health semantic
  tokens and stable Material icons for section headers, benefit rows, guideline
  callouts, myth/reality cards, exercise phases and special-needs cards.
  Legacy marker strings remain data-compatible but are stripped from rendered
  explanatory text. Educational copy, safety notes and disclaimer behavior are
  preserved.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Guidance remains informational and should not be read
  as a personalized fluid prescription. Connected-device TalkBack, large-font,
  theme and expandable-content checks remain open.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

## Follow-up — Water intake result visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** The post-calculation water-goal hero, bottle fill,
  conversion card, hourly recommendation and saved-result feedback now use
  shared water feature and health semantic tokens instead of local raw blue or
  green values. Existing calculations, disclosures, save/share/recalculate and
  tracking actions remain unchanged.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** The route still needs connected-device verification
  for bottle rendering, bottom actions, large fonts, TalkBack and light/dark
  theme contrast.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

## Follow-up — Water intake input visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** The daily water-needs input route now uses shared water
  feature tokens for its hero gradient, selection borders and button shadow;
  existing metric/imperial inputs, profile autofill, validation and navigation
  behavior are unchanged. The route keeps the stable vector/icon language and
  readable theme surfaces used by the rest of the hydration flow.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** The calculator still needs connected-device checks for
  keyboard/scroll behavior, large fonts, TalkBack, theme contrast and result
  navigation.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

## Follow-up — Water reminder settings visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Reminder status, notification controls, schedule/time
  pickers, frequency selection, smart features and summary rows now use stable
  Material icons and shared water/theme tokens instead of rendered emoji and
  legacy raw color constants. Notification permission behavior, autosave and
  inexact scheduling remain unchanged; selected controls retain clear contrast.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Notification permission and exact rendered time-picker
  behavior still require connected-device checks across light/dark themes,
  large fonts and TalkBack.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

## Follow-up — Water history visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Calendar, trend, weekly-report, streak, statistics and
  personal-record surfaces now use shared water/health semantic tokens and
  stable Material icons. Legacy emoji marker resources are no longer rendered;
  persisted data and history interactions remain compatible. Empty, future-day
  and zero-intake states use theme-aware surfaces for readable contrast.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Water history is informational and does not establish
  a clinical hydration target. Connected-device TalkBack, large-font, theme,
  chart rendering and calendar interaction checks remain open.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

## Follow-up — Hydration achievements visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Hydration achievements now use shared semantic tier/score
  colors and vector icons for streaks, grades, badges, milestones and unlock
  celebrations. Legacy model emoji/icon fields remain unchanged for storage
  compatibility, but are not rendered. Active and inactive streak treatments
  keep readable contrast.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Hydration scores and streaks are informational and
  should not create pressure around missed days. Connected-device TalkBack,
  large-font, theme and interaction checks remain open.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

## Follow-up — Hydration tracking visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** The daily water tracker now uses shared water and health
  palette tokens for progress, rings, quick actions, glass indicators, logs,
  score and dialogs. Goal completion, score, achievements, tools and the
  celebration overlay use vector icons instead of rendered emoji; logging,
  undo, reminders, sharing and persistence behavior are unchanged.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Health guidance and scores remain informational.
  Connected-device TalkBack, large-font, theme and hydration interaction
  checks are still required.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

## Follow-up — Meal-planning visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Meal planning now uses shared semantic palette tokens for
  meal distribution, macros, fasting windows, workout nutrition and ideas.
  Fasting, workout, meal and food-idea markers render as stable vector icons;
  legacy emoji fields remain only in the data model for compatibility. Timeline
  semantics and all planning controls are unchanged.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Intermittent-fasting and workout guidance remains
  informational; connected-device accessibility, large-font and interaction
  checks are still required.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only where it improves trust, accessibility or
  reliability.

## Follow-up — Calorie history visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Calorie history now uses semantic health colors across
  summary stats, calendar adherence, trend charts and weekly comparisons.
  Balance, macro, empty-history and detailed-stat surfaces use stable vector
  icons instead of rendered emoji; the existing filters, calendar navigation,
  trend calculations and day taps are unchanged.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Connected-device chart rendering, TalkBack, large
  fonts, theme contrast and history interaction checks remain open.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only where it improves trust, accessibility or
  reliability.

## Follow-up — Food log visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Food logging now uses shared semantic colors for calorie
  status, targets and macro chips. Daily status, meal groups, food presets and
  the no-entry state use stable vector icons instead of rendered emoji, with a
  clearer designed empty state. Legacy preset markers remain unchanged in
  storage and are mapped only at render time.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** The custom preset picker still stores marker tokens
  for compatibility, although it renders icon choices. TalkBack, large-font,
  theme and food-log interaction checks require a connected device.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

## Follow-up — Calorie input visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Calorie input now maps legacy gender, activity and goal
  markers to stable Material icons at render time. Stored model fields remain
  backwards compatible, while goal loss/maintenance/gain states use shared
  semantic colors and the header fire marker is rendered as an icon.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Legacy option models retain emoji labels only for
  compatibility. TalkBack, large-font, theme and route rendering checks
  require a connected device.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

Updated: 2026-09-12

## Follow-up — Calorie result visual and wording consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Calorie results now use a shared semantic palette for
  targets, BMR/TDEE, macro breakdowns, projections, warnings and actions.
  Emoji center/projection markers were replaced with stable Material icons;
  the educational toggle and quick result copy are plain, calm labels. Weight
  change projections now say “may” to make their planning-estimate nature
  explicit; calculation values and safety-floor handling are unchanged.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Calculator result rendering, large-font wrapping,
  TalkBack labels and dark/light contrast still need a connected-device check.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

Updated: 2026-09-12

## Follow-up — Home water card visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** The home hydration card now uses vector water/progress,
  completion and streak icons instead of emoji placeholders. Its goal and
  success treatments use the shared semantic hydration palette, and quick-add
  labels are plain, locale-friendly measurements. Progress animation, goal
  completion, streak display and quick logging behavior remain unchanged.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Home rendering, contrast at large font sizes,
  TalkBack descriptions and dark/light theme behavior still need a connected
  device check.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

Updated: 2026-09-12

## Follow-up — Blood-pressure reminder and export visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Reminder and export routes now use shared semantic colors
  for appointment, PDF, spreadsheet, clinician-report, text-share and
  image-share actions. Enabled reminder time rows have a 48 dp minimum target;
  notification permission timing, scheduling and export behavior are unchanged.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Notification denial, exported-file opening/sharing,
  TalkBack, large-font, theme and route rendering still require device testing.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

Updated: 2026-09-12

## Follow-up — Blood-pressure trend chart correctness and visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** The BP trend route now uses a shared semantic series
  palette for systolic, diastolic, pulse pressure, MAP, pulse, trend cards,
  filters, calendar legends and summary cards. Stable icons replace the trend
  emoji marker. The chart now draws the already-computed systolic path (it was
  previously omitted while systolic points were still shown), and reference
  zone fills reuse calculator category colors.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Chart hit testing and native-canvas rendering still
  need TalkBack, large-font, theme and physical-device validation.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

Updated: 2026-09-12

## Follow-up — Blood-pressure history visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** BP history entries and detail sheets now replace rendered
  time-of-day and averaged-reading emoji with stable Material icons. Medication
  and pulse accents use shared semantic/theme colors, while date, time, unit,
  category, note, edit and delete behavior remain unchanged. Unknown legacy
  time values continue to display safely without a misleading icon.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** The route still needs TalkBack, large-font, theme,
  swipe-to-delete and sheet rendering checks on a connected device.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

Updated: 2026-09-12

## Follow-up — Blood-pressure recommendation visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Blood-pressure recommendation guidance now maps persisted
  emoji markers to stable Material icons at render time, so legacy saved data
  remains readable without shipping emoji illustrations. Urgency, clinician
  advice, risk, and white-coat sections use shared semantic health colors;
  expandable recommendation headers meet a 48 dp minimum touch target and
  safe icon fallbacks remain available for older records.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Legacy recommendation models still retain emoji
  labels for backwards compatibility. TalkBack, large-font, theme and route
  rendering checks require a connected device.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only when it improves trust, accessibility or
  reliability.

Updated: 2026-09-12

## Follow-up — Blood-pressure education trust and visual cleanup

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** The BP education guide now renders section, instruction,
  comparison, myth/fact and analogy markers as stable Material icons with the
  shared semantic health palette. Expandable section headers meet a 48 dp
  minimum target. Educational copy was audited against current AHA/CDC home
  measurement guidance: unsupported universal effect sizes, “most accurate”
  promises, fixed targets and medication certainty were removed or qualified;
  reference ranges are explicitly context rather than diagnosis.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** The guide remains informational and does not replace
  a validated monitor or professional interpretation. TalkBack, large-font,
  theme and route rendering checks require a connected device.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only where it improves trust, accessibility or
  reliability.

Updated: 2026-09-12

## Follow-up — Resting heart-rate guide trust and visual cleanup

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** Replaced the guide's emoji instructions and red literals
  with stable Material icons and shared semantic colors. Reference bands now
  use neutral, non-diagnostic labels, and the sheet explains that medicines,
  illness, stress, sleep and training can change readings. The title, quick
  alternative and measurement tip were also made calmer and more precise.
- **Tests:** Focused Kotlin compilation and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  pass with `GRADLE_EXIT=0`.
- **Known limitations:** Reference bands are educational context rather than
  clinical cut-offs. Physical-device TalkBack, large-font and sheet rendering
  checks remain open.
- **Next phase:** Execute the documented device/console release checklist;
  continue deep-screen cleanup only where it improves trust, accessibility or
  reliability.

Updated: 2026-09-12

## Follow-up — BMR result trust and visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** BMR result, breakdown and formula-comparison surfaces now
  use stable Material vector icons and shared calculator/health tokens instead
  of rendered emoji and isolated color literals. Result language now calls
  BMR a resting-energy estimate, removes a misleading calorie-floor reading,
  and presents broad estimate bands as context rather than clinical ranges.
  Share output is plain, explicit and informational; calculation, history and
  unit-toggle behavior remain unchanged.
- **Tests:** Focused Kotlin compilation and BMR unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate is run before this slice is committed.
- **Known limitations:** Connected-device animation, formula expansion,
  TalkBack, large-font, light/dark theme and route-rendering checks remain
  open.
- **Next phase:** Continue the deep calculator audit only where it improves
  truthful interpretation, accessibility or reliability.

Updated: 2026-09-13

## Follow-up — TDEE goals and activity trust/visual consistency

- **Status:** Complete locally; device/accessibility validation remains open
- **Major changes:** TDEE activity and goal surfaces now use stable Material
  vector icons, shared calculator/health tokens and a theme-aware hero instead
  of emoji markers and isolated raw colors. Goal descriptions and the safety
  note now present calorie targets as planning estimates, remove universal
  “healthy pace”/minimum-intake implications, and encourage professional
  context for very low targets. Formula, selection, unit and callback
  behavior remain unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate must pass before this slice is committed.
- **Known limitations:** Connected-device activity/goal expansion, chart
  rendering, TalkBack, large-font, light/dark theme and route checks remain
  open.
- **Next phase:** Continue the deep calculator audit only where it improves
  truthful interpretation, accessibility or reliability.

Updated: 2026-09-13

## Follow-up — BMR calculator input and quick-info trust/visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open
- **Major changes:** The primary BMR input flow now uses stable Material icons
  for the result header, profile-data banner, gender choices, explanation card
  and quick-info rows instead of emoji placeholders. Profile-data guidance uses
  the shared informational palette rather than a health-status green. Quick
  info copy no longer presents fixed energy-use percentages or formula claims as
  universal facts, and save feedback is plain and accessible. Input, unit,
  validation, calculation and navigation behavior remain unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate is run before this slice is committed.
- **Known limitations:** Connected-device input, large-font, TalkBack,
  light/dark theme and route checks remain open.
- **Next phase:** Continue the deep calculator audit only where it improves
  truthful interpretation, accessibility or reliability.

Updated: 2026-09-13

## Follow-up — Macro planning trust and visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open
- **Major changes:** Macro planning now uses stable Material icons for diet
  presets, macro legends, sliders, detail cards and meal breakdowns instead of
  emoji markers and isolated raw colors. Ratio feedback uses the shared semantic
  palette and a readable icon. Preset descriptions and “best for” labels are
  framed as optional planning patterns rather than weight-loss, blood-sugar or
  muscle-building promises. Share output uses plain labels for accessibility.
  Macro percentage balancing, meal-count selection, charts and callbacks remain
  unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed, including a
  plain-label share regression. The complete `test`, `lintRelease`,
  `assembleDebug`, `assembleRelease` and `bundleRelease` gate is run before
  this slice is committed.
- **Known limitations:** Connected-device slider/chart rendering, large-font,
  TalkBack, light/dark theme and route checks remain open.
- **Next phase:** Continue the deep calculator audit only where it improves
  truthful interpretation, accessibility or reliability.

Updated: 2026-09-13

## Follow-up — TEF trust, denominator correctness and visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open
- **Major changes:** The thermic-effect view now uses shared calculator and
  semantic health colors plus stable Material icons for TEF, macro, insight and
  energy-breakdown states instead of rendered emoji and isolated raw colors.
  The “% of food intake” value now uses the macro-calorie intake that actually
  feeds the estimate, with a zero-input guard; it no longer labels TDEE as food
  intake. TEF explanations describe a model estimate, avoid causal weight or
  muscle promises, and do not prescribe changing protein intake. Existing
  animation, chart, macro selection and TDEE behavior remain unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed, including the
  macro-intake denominator and zero-input regression. The complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate is
  run before this slice is committed.
- **Known limitations:** Connected-device donut/bar rendering, large-font,
  TalkBack, light/dark theme and route checks remain open.
- **Next phase:** Continue the deep calculator audit only where it improves
  truthful interpretation, accessibility or reliability.

Updated: 2026-09-13

## Follow-up — Calorie education and safety trust/visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open
- **Major changes:** Calorie education now describes energy-use categories as
  variable rather than repeating fixed population percentages. Weight-gain
  guidance no longer promises a particular muscle/fat outcome or treats a
  surplus as a prescription; it explains relevant context and uncertainty.
  Calorie safety warning surfaces now use the shared semantic palette instead
  of isolated legacy color literals. Existing education navigation, food
  references and warning severity behavior remain unchanged.
- **Tests:** Existing calorie/macro unit coverage remains green; the complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate is run before this slice is committed.
- **Known limitations:** Connected-device education rendering, TalkBack,
  large-font, light/dark theme and route checks remain open; signing,
  Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue the deep calculator audit only where it improves
  truthful interpretation, accessibility or reliability.

Updated: 2026-09-14

## Follow-up — Calorie history day details

- **Status:** Code-fixable interaction complete; device/accessibility validation remains open.
- **Major changes:** Calendar days with logged food now open a read-only day
  details dialog showing the selected date, total calories versus the saved
  target, macro totals and each logged food entry. Duplicate current/history
  snapshots are resolved deterministically in the analytics use case, preferring
  populated and most recently logged data. The navigation graph no longer wires
  an empty tap callback. No data is edited or removed by this interaction.
- **Tests:** Added selection tests for matching dates, duplicate snapshots and
  unknown dates. The complete `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` gate is run before this slice is committed.
- **Known limitations:** Connected-device dialog/calendar rendering, TalkBack,
  large-font, light/dark theme and route checks remain open; signing, Firebase
  and Play Console tasks still require owner access.
- **Next phase:** Continue the deep tracking and history audit for code-fixable
  gaps that improve reliability, accessibility or retention.

## Follow-up — Share/export accessibility and visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** BMR, WHR, blood-pressure, weekly-report and hydration
  share text now use plain labels instead of emoji markers, preserving the
  selected measurements and the shared wellness disclosure footer. WHR
  waist-to-height wording is explicitly a reference flag rather than a normal/
  risk diagnosis. BP share actions now use theme tokens, and report toggles
  use stable icons with 48 dp touch targets. Invalid BMR meal counts are
  clamped before per-meal formatting.
- **Tests:** Added share-output accessibility/disclosure tests and a zero-meal
  formatter regression. The complete `test`, `lintRelease`, `assembleDebug`,
  `assembleRelease` and `bundleRelease` gate passed before this slice is committed.
- **Known limitations:** Actual target-app chooser behavior, exported-file
  opening, TalkBack, large-font, light/dark theme and route checks remain open;
  signing, Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue the deep tracking and retention audit for code-fixable
  gaps without expanding sensitive analytics or health-data access.

## Follow-up — Health Connections and Insights affordance clarity

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Health Connections calculator fields are now rendered as
  non-interactive reference labels instead of no-op suggestion chips, and the
  connection header uses stable vector icons rather than legacy emoji markers.
  The Insights Trends description now accurately explains that its shortcut
  opens the weight trend, while water and blood-pressure logs remain available
  from Track. Health Connect permission and sync behavior are unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed before this slice is committed.
- **Known limitations:** Connected-device Health Connect permission denial,
  icon/label rendering, TalkBack, large-font, light/dark theme and route checks
  remain open; signing, Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue the route and empty-state audit for any remaining
  misleading placeholders or blank-result states.

## Follow-up — Notification status affordance clarity

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** High-priority reminder status and streak-freeze availability
  are now non-interactive status surfaces, so they no longer look like buttons
  that do nothing. The streak-freeze marker uses a stable shield icon instead
  of an emoji placeholder. Reminder toggles, editing, deletion and scheduling
  behavior remain unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed before this slice is committed.
- **Known limitations:** Notification permission/OS rendering, TalkBack,
  large-font, light/dark theme and route checks remain open; signing, Firebase
  and Play Console tasks still require owner access.
- **Next phase:** Continue the route and empty-state audit for any remaining
  misleading placeholders or blank-result states.

## Follow-up — Data management visual and trust polish

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Data Management cleanup actions, storage segments and
  integrity states now use the shared semantic/theme palette instead of legacy
  hard-coded colors. Calculator cleanup rows and the full-reset warning list
  now use stable vector icons and plain labels, removing device-dependent emoji
  rendering while preserving the existing deletion safeguards and data flows.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed before this slice is committed.
- **Known limitations:** Destructive-flow rendering, TalkBack, large-font,
  light/dark theme and route checks remain open; signing, Firebase and Play
  Console tasks still require owner access.
- **Next phase:** Continue the deep calculator and empty-state audit for
  remaining legacy visual placeholders or misleading interactions.

## Follow-up — Settings visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Settings theme choices now use the existing vector theme
  icons and plain labels rather than emoji text. Theme, hydration, reminder,
  export, privacy, legal, rating and sharing rows now use shared semantic or
  theme colors instead of isolated legacy color literals. The destructive
  confirmation keeps its existing safeguards while presenting a clear text
  warning.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed before this slice is committed.
- **Known limitations:** Settings rendering, TalkBack, large-font, light/dark
  theme and route checks remain open; signing, Firebase and Play Console tasks
  still require owner access.
- **Next phase:** Continue the deep calculator audit for remaining UI-only
  emoji placeholders and legacy visual treatments.

## Follow-up — WHR input accessibility polish

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Waist-to-hip ratio gender choices now use stable Material
  vector icons with selected-state tint instead of device-dependent emoji. The
  existing gender values, validation, calculations and navigation are unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed before this slice is committed.
- **Known limitations:** WHR selector rendering, TalkBack, large-font,
  light/dark theme and route checks remain open; signing, Firebase and Play
  Console tasks still require owner access.
- **Next phase:** Continue the deep calculator audit for result-state
  placeholders and misleading UI affordances.

## Follow-up — BMI result visual polish

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** The BMI result card now uses a stable vector status icon
  instead of category emoji. Saved-state, weight-range, advice, share and
  guidance surfaces now use shared semantic/theme colors, and the professional
  guidance note has an explicit icon for clearer scanning. BMI calculations,
  thresholds and disclosures are unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed before this slice is committed.
- **Known limitations:** BMI result/gauge rendering, TalkBack, large-font,
  light/dark theme and route checks remain open; signing, Firebase and Play
  Console tasks still require owner access.
- **Next phase:** Continue the deep calculator audit for remaining result and
  educational placeholders that affect user trust or accessibility.

## Follow-up — BSA medical education icon polish

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** BSA medical-application sections now use stable vector
  icons for medication, burn, renal and cardiac topics. The Rule of Nines
  table uses plain body-region labels instead of emoji, preserving the
  educational percentages and calculations while improving readability across
  fonts and devices.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed before this slice is committed.
- **Known limitations:** BSA education rendering, TalkBack, large-font,
  light/dark theme and route checks remain open; signing, Firebase and Play
  Console tasks still require owner access.
- **Next phase:** Continue auditing calculator educational and result states
  for remaining legacy emoji and inconsistent affordances.

## Follow-up — IBW education visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Ideal Body Weight education cards, comparison panels,
  frame-size guidance and disclaimer callouts now use stable vector icons and
  shared semantic/theme colors instead of emoji and legacy color literals.
  Educational copy, formulas and navigation are unchanged.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed before this slice is committed.
- **Known limitations:** IBW education rendering, TalkBack, large-font,
  light/dark theme and route checks remain open; signing, Firebase and Play
  Console tasks still require owner access.
- **Next phase:** Continue auditing remaining calculator educational/result
  surfaces for user-visible placeholders and inconsistent interaction cues.

## Follow-up — BMI trend and goal safety polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** BMI trend empty-state chips and chart categories now use
  stable vector icons and shared semantic/chart tokens. The BMI goal editor
  replaced motivational, timeline, direction and celebration emoji with
  accessible vector icons, scales rates correctly for kg/lb units, and explains
  that BMI targets outside the adult reference range need personal context.
  Save is disabled for malformed or out-of-bounds target weights, stored goal
  values are guarded in the ViewModel, and invalid/non-finite formula inputs
  return a safe zero result.
- **Tests:** Added BMIGoalData formula, boundary and invalid-input tests.
  Focused Kotlin compilation/unit tests and the complete `test`, `lintRelease`,
  `assembleDebug`, `assembleRelease` and `bundleRelease` gate passed.
- **Known limitations:** Goal/trend rendering, TalkBack, large-font, light/dark
  theme and route checks remain open; signing, Firebase and Play Console tasks
  still require owner access.
- **Next phase:** Continue auditing remaining calculator educational/result
  surfaces for user-visible placeholders and inconsistent interaction cues.

## Follow-up — BMI edge-case and validation polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** BMI edge-case warnings now use stable vector icons and
  shared semantic colors instead of emoji and isolated literals. Invalid,
  non-finite BMI, weight and height values are surfaced rather than silently
  accepted, and the optional guidance copy no longer points users to emergency
  terminology for a calculator warning.
- **Tests:** Added boundary and non-finite-input coverage for the edge-case
  handler. `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and
  `bundleRelease` all passed after the focused compilation/unit test run.
- **Known limitations:** Edge-case rendering, TalkBack, large-font, light/dark
  theme and route checks remain open; signing, Firebase and Play Console tasks
  still require owner access.
- **Next phase:** Continue auditing remaining calculator educational/result
  surfaces for user-visible placeholders and inconsistent interaction cues.

## Follow-up — Cross-link and daily-tip icon consistency — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Metabolic-syndrome related-calculator links, calorie
  cross-links and the home daily-tip card now use stable Material vector icons
  with semantic theme colors instead of emoji. Daily-tip categories cover all
  supported content types, including mental health and weight management; the
  favorite state uses the shared danger token.
- **Tests:** Focused Kotlin compilation/unit tests passed. The complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Known limitations:** Cross-link and daily-content rendering, TalkBack,
  large-font, light/dark theme and route checks remain open; signing, Firebase
  and Play Console tasks still require owner access.
- **Next phase:** Continue auditing remaining calculator educational/result
  surfaces for user-visible placeholders and inconsistent interaction cues.

## Follow-up — Water celebration visual polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** The hydration-goal celebration now uses vector trophy,
  water, progress and streak icons instead of emoji. Its card, scrim and share
  action follow the active light/dark Material theme; confetti uses the shared
  chart palette; and copy is calmer and non-competitive while preserving the
  existing haptics, auto-dismiss and sharing behavior.
- **Tests:** Focused Kotlin compilation/unit tests passed. The complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed.
- **Known limitations:** Celebration rendering, TalkBack, large-font,
  light/dark theme and route checks remain open; signing, Firebase and Play
  Console tasks still require owner access.
- **Next phase:** Continue auditing remaining calculator educational/result
  surfaces for user-visible placeholders and inconsistent interaction cues.

## Follow-up — BMI risk context visual consistency

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** BMI risk-context status badges, tone cards, expandable
  sections, risk items, recommendations, action steps and provider notes now
  render stable vector icons and shared semantic colors. Legacy model emoji
  fields remain only for persistence compatibility and are not displayed in the
  screen.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed before this slice is committed.
- **Known limitations:** BMI risk-context rendering, TalkBack, large-font,
  light/dark theme and route checks remain open; signing, Firebase and Play
  Console tasks still require owner access.
- **Next phase:** Continue auditing BMI trend and goal surfaces for remaining
  placeholders and inconsistent interaction cues.

## Follow-up — BMI comparison and range-bar polish

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** BMI age/context comparison surfaces now use vector status
  icons and shared semantic colors instead of emoji and isolated literals. The
  obsolete population-average panel (which could show a misleading 0.0 average)
  was removed because no representative dataset is bundled. The adult reference
  range highlight now measures the available card width rather than relying on a
  fixed dp offset, preventing overlap on small or large screens. Pediatric
  compatibility data remains intact but is not approximated by the adult flow.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed before this slice is committed.
- **Known limitations:** BMI comparison rendering, TalkBack, large-font,
  light/dark theme and route checks remain open; signing, Firebase and Play
  Console tasks still require owner access.
- **Next phase:** Continue auditing BMI risk, trend and goal surfaces for
  remaining placeholders and inconsistent interaction cues.

## Follow-up — Metabolic syndrome education trust polish

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Metabolic syndrome education sections, risk-factor cards,
  treatment cards, blood-work cards and retest guidance now use stable vector
  icons instead of emoji. Explanations were softened to evidence-aware
  informational wording, prevalence was framed as definition-dependent, South
  Asian waist references are shown alongside the ATP III example, and blood-test
  preparation/retest timing now defers to the ordering lab or professional.
  Chip rows scroll safely on narrow screens.
- **Tests:** Focused Kotlin compilation and unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed before this slice is committed.
- **Known limitations:** Metabolic education rendering, TalkBack, large-font,
  light/dark theme and route checks remain open; signing, Firebase and Play
  Console tasks still require owner access.
- **Next phase:** Continue auditing remaining calculator educational/result
  surfaces for user-visible placeholders and inconsistent interaction cues.

## Follow-up — Blood-pressure streak visual consistency — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** The blood-pressure streak card now uses accessible vector fire/flag icons instead of a streak emoji, with semantic warning/caution tokens for its gradient and value. Medication tracking uses the shared wellness color token for its icon, surface and focused field. The doctor-suggestion action and milestone dialog use theme-aware/shared semantic colors while preserving the existing reminder and celebration behavior.
- **Tests:** Focused Kotlin compilation/unit tests passed. The complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate passed.
- **Known limitations:** Blood-pressure streak, medication and milestone rendering, TalkBack, large-font, light/dark theme and route checks remain open; signing, Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue auditing remaining calculator and tracker surfaces for user-visible emoji placeholders and inconsistent interaction cues.

## Follow-up — Ideal-weight comparison visual consistency — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** The ideal-weight journey card now uses typed motivational content and Material vector icons instead of emoji. Its current/ideal/range bubbles, range marker, healthy-range legend and motivational surfaces use shared semantic theme colors; marker rings follow the active surface color for light/dark contrast. Existing estimates, wording and unit conversions are unchanged.
- **Tests:** Focused Kotlin compilation/unit tests passed. The complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate passed.
- **Known limitations:** Ideal-weight comparison rendering, TalkBack, large-font, light/dark theme and route checks remain open; signing, Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue auditing remaining calculator and tracker surfaces for user-visible emoji placeholders and inconsistent interaction cues.

## Follow-up — Blood-pressure alert and profile selection polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** The blood-pressure quick-log card now uses a labeled vector time icon instead of rendering a legacy suggestion emoji. Markedly elevated-reading, saved-reading and validation surfaces now use Material theme/error containers and shared semantic colors rather than fixed light-theme literals, keeping the existing safety guidance and behavior intact. Activity-level and health-goal selection dialogs now use consistent vector icons and descriptions, improving scanning and removing emoji from profile setup.
- **Tests:** Focused Kotlin compilation/unit tests passed. The complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate passed.
- **Known limitations:** Blood-pressure alert/quick-log rendering and profile dialogs still need device, TalkBack, large-font, light/dark theme and route checks; signing, Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue auditing remaining user-visible calculator, report and tracker surfaces for placeholders, raw colors and inconsistent interaction cues.

## Follow-up — Quick food log icon consistency — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Quick food presets now render a consistent Material icon mapped from the preset type (water, coffee, protein or general food) with an accessible preset name, rather than platform-dependent food emoji. The legacy emoji field remains on `QuickFoodPreset` for compatibility with custom/persisted data, but is no longer rendered by this dialog.
- **Tests:** Focused Kotlin compilation/unit tests passed. The complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate passed.
- **Known limitations:** Quick food dialog rendering, TalkBack, large-font, light/dark theme and route checks remain open; signing, Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue auditing remaining report, insight and tracker presentation surfaces for direct emoji rendering and raw colors.

## Follow-up — WHR history and greeting icon polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Waist-to-hip history cards and detail dialogs now use vector measurement/body-shape/risk icons instead of emoji, shared semantic risk tokens, and theme-safe WHtR surfaces. Gender metadata is presented as readable text without emoji. The personalized greeting uses a time-of-day vector icon and keeps its warm copy and gradient treatment.
- **Tests:** Focused Kotlin compilation/unit tests passed. The complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate passed (lint report generated at `app/build/reports/lint-results-release.html`).
- **Known limitations:** WHR history/detail and greeting rendering, TalkBack, large-font, light/dark theme and route checks remain open; signing, Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue auditing remaining report, insight and tracker presentation surfaces for direct emoji rendering and raw colors.

## Follow-up — Return journey icon polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** The health-journey summary uses typed Material icons for calculation, milestone and record stats, with semantic trend colors. The welcome-back route replaces wave, streak-break, freeze, plant and last-metric/quick-calculator emoji with accessible vector icons mapped to the metric or route; re-engagement copy remains gentle and non-punitive.
- **Tests:** Focused Kotlin compilation/unit tests passed. The complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate passed (lint report generated at `app/build/reports/lint-results-release.html`).
- **Known limitations:** Return-journey rendering, TalkBack, large-font, light/dark theme and route checks remain open; signing, Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue auditing remaining report, insight and tracker presentation surfaces for direct emoji rendering and raw colors.

## Follow-up — Ideal-weight and WHR progress polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Ideal-weight goal progress now uses vector result/milestone icons, shared semantic progress colors and theme-safe celebration surfaces. WHR progress history replaces empty-state and goal emojis, removes emoji-led motivational copy, uses shared trend/chart/risk tokens, and keeps its graph point markers readable against the active surface. The existing calculations, saved data and goal behavior are unchanged.
- **Tests:** Focused Kotlin compilation/unit tests passed. The complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate passed (lint report generated at `app/build/reports/lint-results-release.html`).
- **Known limitations:** Ideal-weight and WHR progress rendering, TalkBack, large-font, light/dark theme and route checks remain open; signing, Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue auditing remaining report, insight and tracker presentation surfaces for direct emoji rendering and raw colors.

## Follow-up — Progress milestone and trend-token polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Ideal-weight goal milestones now use vector achievement/progress icons and semantic progress colors instead of emoji and fixed literals. WHR progress empty, goal, comparison, graph, statistics and distribution surfaces now use shared healthy/warning/danger/chart tokens; trend messages no longer inject emoji into the banner, and the goal-reference note wraps safely on narrow screens.
- **Tests:** Focused Kotlin compilation/unit tests passed. The complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate passed.
- **Known limitations:** Progress milestone/trend rendering, TalkBack, large-font, light/dark theme and route checks remain open; signing, Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue auditing remaining report, insight and tracker presentation surfaces for direct emoji rendering and raw colors.

## Follow-up — Metabolic preview and reminder icon polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Metabolic-syndrome live preview criteria and input cards now render mapped Material icons with accessible criterion labels instead of emoji. Reminder category selection and edit flows use a shared category-to-icon treatment for water, blood pressure, weight, medication, exercise, calories and custom reminders; model icon strings remain compatible with saved reminders.
- **Tests:** Focused Kotlin compilation/unit tests passed. The complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate passed.
- **Known limitations:** Metabolic preview and reminder-dialog rendering, TalkBack, large-font, light/dark theme and route checks remain open; signing, Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue auditing remaining reports, insights and tracker surfaces for direct emoji rendering and raw colors.

## Follow-up — Weekly report icon polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Weekly report metrics, highlights, next-week goals and no-data states now render mapped Material icons with accessible descriptions instead of platform-dependent emoji. Wellness Score change colors use shared healthy/danger tokens, and report copy remains informational and non-diagnostic.
- **Tests:** Focused Kotlin compilation/unit tests passed. The complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate passed.
- **Known limitations:** Weekly report rendering, TalkBack, large-font, light/dark theme and route checks remain open; signing, Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue auditing remaining insights, tracker and secondary dialog surfaces for direct emoji rendering and raw colors.

## Follow-up — Ideal-weight and IBW theme-token polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Ideal-weight healthy-range pills, IBW comparison links,
  home summary deltas and result warnings/save states now use shared semantic
  `HealthColors` and Material theme containers instead of isolated raw color
  literals. Existing calculations, copy, navigation and saved-result behavior
  are unchanged; the updated surfaces now remain legible across light and dark
  themes.
- **Tests:** Focused Kotlin compilation/unit tests passed. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed with exit code 0 (the only output was the known SDK XML version
  compatibility warning).
- **Known limitations:** Ideal-weight and IBW rendering, TalkBack, large-font,
  light/dark theme and route checks still require connected-device validation;
  signing, Firebase and Play Console tasks remain owner-only.
- **Next phase:** Continue auditing remaining calculator, insight and tracker
  surfaces for raw colors, placeholders and inconsistent interaction cues.

## Follow-up — BMI scale and history surface polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Added shared semantic BMI category/status tokens and
  migrated the BMI result header, gauge, live slider preview and additional
  metrics away from legacy UI color literals while preserving the model's
  compatibility fields. The gauge value badge now clamps against its actual
  available width rather than a hard-coded 300dp, preventing edge overlap on
  compact and large screens. WHR home risk states and IBW history/statistics
  surfaces now use the same semantic palette and vector icons instead of
  emoji placeholders; trend chips expose accessible icon descriptions.
- **Tests:** Extended `BmiSliderPolicyTest` for shared category roles and
  invalid preview values. Focused compilation/unit tests and the complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed with exit code 0 (only the known SDK XML/deprecation warnings
  were emitted).
- **Known limitations:** BMI gauge/slider, WHR home, IBW history and
  statistics rendering still require connected-device checks for TalkBack,
  large fonts, light/dark themes and route behavior; signing, Firebase and
  Play Console tasks remain owner-only.
- **Next phase:** Continue auditing remaining calculator, insight and tracker
  surfaces for raw colors, placeholders and inconsistent interaction cues.

## Follow-up — Blood-pressure and risk-surface color-token polish — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Blood-pressure category and risk mappings, the gauge scale,
  reading values, input fields, pulse field, education action and crisis action
  now use shared semantic `HealthColors` or Material error tokens instead of
  isolated legacy literals. Metabolic-syndrome risk and consultation surfaces,
  personal-record celebrations and notification-channel badges now follow the
  same role-based palette with theme-aware foregrounds. Calculations, safety
  guidance, persistence and navigation are unchanged.
- **Tests:** Focused Kotlin compilation/unit tests passed. The complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with exit code 0 (known SDK XML/deprecation warnings only).
- **Known limitations:** Blood-pressure and metabolic-risk rendering, TalkBack,
  large-font, light/dark theme and route checks still require connected-device
  validation; signing, Firebase and Play Console tasks remain owner-only.
- **Next phase:** Continue auditing remaining report, insight and tracker
  surfaces for direct emoji rendering, raw colors and inconsistent interaction cues.

## Follow-up — Residual visual token cleanup — 2026-09-15

- **Status:** Code-fixable polish complete; device/accessibility validation remains open.
- **Major changes:** Home calculator cards now consume the shared calculator palette; the blood-pressure home widget uses semantic warning/danger tokens and a vector streak icon; splash colors are sourced from the brand theme; the hydration plant illustration uses named wellness illustration tones and a vector streak icon instead of emoji. Existing navigation, calculations, persistence and copy are unchanged.
- **Tests:** The complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate passed with exit code 0 in 9m24s (known SDK XML/deprecation warnings only).
- **Known limitations:** Remaining raw-color/placeholder audits, route rendering, TalkBack, large-font, light/dark theme and connected-device checks remain open; signing, Firebase and Play Console tasks still require owner access.
- **Next phase:** Continue the remaining runtime/accessibility verification and address only issues reproduced on supported devices.

## Follow-up — Deep-screen header and action-bar layout polish — 2026-09-16

- **Status:** Code-fixable polish complete; focused emulator smoke checks passed.
- **Major changes:** Replaced the BMI calculator's oversized `LargeTopAppBar` with a compact responsive top bar so the title and context stay on one line without clipping or excessive top whitespace. The clear action is now an accessible icon-only control (`Clear all BMI inputs`) while back, method and history actions remain available. Tightened the Water result action bar's responsive button padding and minimum share width so `Recalculate`, `Save` and share remain readable on compact screens.
- **Tests:** On emulator `emulator-5554`, the calculator hub opened, the BMI screen rendered a complete `BMI Calculator` title and clear-all action without overlap, and the Water input flow produced a visible `3.0 Liters` / `3000 ml per day` result (no blank route or fatal exception). The complete `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate passed with exit code 0 in 31m50s (known SDK XML/deprecation warnings only).
- **Known limitations:** Broader device matrix, TalkBack, large-font, dark-theme and process-death checks remain open; release signing, Firebase console/secret rotation and Play Console work still require owner access.
- **Next phase:** Continue only with reproduced high-impact runtime/accessibility issues; preserve the stable visual and calculator behavior now verified.

## Follow-up — Legacy emoji surface cleanup — 2026-09-16

- **Status:** Code-fixable visual polish complete; focused unit tests and the
  complete release gate passed.
- **Major changes:** Replaced direct milestone, personal-record, metabolic
  criterion/status and reminder-category emoji rendering with shared semantic
  Material icons. Widget streak and hydration surfaces now use vector assets
  and plain, readable labels rather than font-dependent emoji. Legacy marker
  fields remain in domain/persistence models for compatibility and exports;
  calculations, navigation, reminders and saved data are unchanged.
- **Tests:** Added `WellnessIconMappingTest` coverage for every milestone,
  personal-record, metabolic and reminder category mapping. The complete
  `test`, `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease`
  gate passed with exit code 0 (known SDK XML/deprecation warnings only).
- **Known limitations:** Connected-device rendering across the full size,
  font-scale, theme, TalkBack and widget-host matrix remains open, as do
  release signing, Firebase console/key restriction and Play Console tasks.
- **Next phase:** Perform owner/device verification and address only issues
  reproduced on supported devices; do not remove compatibility marker fields
  without a data/export migration plan.

## Follow-up — Widget fallback and recovery-state polish — 2026-09-16

- **Status:** Code-fixable reliability and trust polish complete; full release
  gate passed.
- **Major changes:** Widget error/empty states now use compact font-stable
  markers instead of emoji in numeric slots and stale badges. The generic
  fallback destination now uses a semantic vector info icon and explains how
  to recover, rather than presenting an unfinished “Coming soon” message.
  Existing navigation, data access and widget actions are unchanged.
- **Tests:** Added coverage ensuring every widget error marker is non-blank and
  independent of emoji fonts. Focused unit tests plus `test`, `lintRelease`,
  `assembleDebug`, `assembleRelease` and `bundleRelease` all passed with exit
  code 0 (known SDK XML/deprecation warnings only).
- **Known limitations:** Device/widget-host, accessibility, font-scale, theme,
  process-death, signing, Firebase and Play Console verification remain open.
- **Next phase:** Validate fallback and widget states on supported hosts and
  continue only with issues reproduced in runtime testing.

## Runtime smoke verification — 2026-09-16

- **Status:** Focused emulator smoke passed for the current `master` build.
- **Evidence:** Installed the debug APK on `emulator-5554` (`small_phone`),
  cold-started the app, opened Profile and the Calculator hub, opened the
  Water input route, entered 70 kg / 30 years, scrolled to the action and
  opened the result. The result exposed `3.0 Liters` and `3000 ml per day`,
  with Recalculate, Save and Share actions visible. The crash buffer and fatal
  log scan were empty throughout the journey.
- **Known limitations:** This is not a substitute for the full API/device,
  widget-host, TalkBack, font-scale, theme, process-death, migration,
  performance or owner-console gates.
- **Next phase:** Repeat the same smoke journey on release-like signed builds
  and complete the broader runtime matrix when those environments are
  available.

## Follow-up — Calendar-aware food log refresh — 2026-09-16

- **Status:** Code-fixable data-boundary polish complete; focused and full
  release gates passed.
- **Major changes:** FoodLogRepository now exposes a stable day-scoped
  StateFlow and refreshes at each local midnight, with bounded clock/timezone
  rechecks. Yesterday's entries are archived before reset, local ISO-date
  conversion is thread-safe, and reset/write operations are serialized. Food
  writes also re-check the day boundary, preventing a tap around midnight from
  being stored in the prior day's log. Existing JSON keys, history retention,
  and persisted data remain compatible.
- **Tests:** Extended FoodLogDayPolicyTest with a daylight-saving/local-midnight
  boundary assertion. Focused regression and the complete `test`,
  `lintRelease`, `assembleDebug`, `assembleRelease` and `bundleRelease` gate
  passed with exit code 0 (known SDK XML/deprecation warnings only).
- **Known limitations:** Device process-death, midnight/timezone-change,
  multi-instance and restore behavior still require connected-device
  verification; signing, Firebase and Play Console gates remain owner-only.
- **Next phase:** Validate calendar rollover on supported devices and continue
  only with reproduced data-boundary issues.
