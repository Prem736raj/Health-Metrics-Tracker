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
