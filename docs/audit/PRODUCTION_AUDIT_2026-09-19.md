# Production audit — 19–23 September 2026

Status: **IN PROGRESS; not a production-readiness certificate.** Source baseline:
`1efccdf44fd54b24607000868a404f8b3e3082a7` on `ui-polish-dark-light-mode`.
This report supersedes earlier release-readiness assertions, not historical evidence.
The nested local clone is not the active checkout and has not been modified.

## Evidence rules and scope

Build success verifies compilation/packaging, not user journeys. JVM tests verify
only their assertions. Static inspection is not runtime verification. GitHub CI
evidence is tied to its commit, never attributed to an uncommitted revision.
No physical device, configured AVD or system image was present at audit start;
`adb devices -l` returned no devices. WHPX acceleration is available.

The repository has one Android application module, approximately 500 production
Kotlin files, Compose screens, Room v16, DataStore/SharedPreferences, Hilt,
WorkManager/AlarmManager, RemoteViews widgets, optional Health Connect and Firebase.
Repository-wide indexing/search is not equivalent to a line-by-line review or
runtime testing of every file. Areas still requiring deeper verification are
explicitly retained below.

## Architecture map

`MainActivity -> NavGraph -> Compose screens / ViewModels -> repositories ->
Room, DataStore, SharedPreferences, Health Connect or optional Firebase services`.

- `presentation/` and `ui/`: overlapping UI ownership; 1,000+ line screens and
  ViewModels make targeted tests harder. Refactor incrementally, not wholesale.
- `data/local`: Room database, entities and most DAOs; other DAOs/models reside
  under `data/dao`, `data/model`, `data/models`. No schema renaming in this audit.
- `calculator`, `data/calculator`, `domain`, `util`: calculations and policies.
- `notifications`, `notification`, `receiver`, `widget`: alarms, work and widgets.
- `HealthCalculatorApp` exposes lazy services alongside Hilt. Consolidation is
  desirable when touching a feature, not a reason to rewrite working persistence.
- `docs/`: policy, medical references, launch and historical release evidence.

## Findings register

Each entry records area, severity, path, problem, impact, evidence/root cause,
recommended fix, implementation status and verification. P0 = critical,
P1 = high, P2 = medium, P3 = low, P4 = improvement.

### AUD-001 — P1 — Data integrity / heuristic deletion

- Paths: `data/management/DataIntegrityChecker.kt`, `DataCleanupManager.kt`,
  `ui/screens/settings/DataManagementScreen.kt`, `DataManagementViewModel.kt`.
- Problem/impact: “Fix Issues” deletes records, including two calculations with
  the same displayed result within one second but different inputs or notes.
  A future timestamp is also treated as corruption; a clock change is not proof.
- Evidence/root cause: `isDuplicate` compares only type/value/time;
  `fixIntegrityIssues` deletes the union of heuristic IDs without per-record review.
- Fix: make integrity diagnostics non-destructive; require deliberate history
  review for deletion. Tighten duplicate diagnostics and avoid claiming all data verified.
- Status: implemented. Three integrity-policy JVM tests passed in the 206-test
  run; diagnostic screen changes compiled. Manual history review remains required.

### AUD-002 — P1 — Health Connect integration

- Paths: `data/healthconnect/HealthConnectManager.kt`, `AndroidManifest.xml`.
- Problem/impact: latest weight is selected from the oldest-first first page;
  Android 14+ permission-usage alias is missing; older rationale action opens the
  dashboard, not the privacy policy. Broad catches swallow coroutine cancellation.
- Evidence/root cause: default `ReadRecordsRequest`, launcher activity filter,
  three `catch (Exception)` blocks. Android official integration guide requires
  rationale/privacy entry points for both provider generations.
- Fix: descending single-record query; dedicated rationale entry; preserve cancellation.
- Status: query/cancellation and dedicated rationale activity/Android 14 alias
  implemented. Debug build and manifest instrumentation passed. Actual provider
  grant/read/revoke behavior is not runtime verified.

### AUD-003 — P1 — CSV security and correctness

- Paths: `data/export/CsvExportHelper.kt`, `BpExportManager.kt`, `CsvCellEncoder.kt`.
- Problem/impact: notes beginning with spreadsheet operators can execute as formulas
  in downstream spreadsheet software. Carriage returns were not quoted.
- Evidence/root cause: duplicate encoders escaped only commas/quotes/newlines.
- Fix: shared encoder neutralizes formula prefixes including leading whitespace,
  handles CR/LF and quotes. CSV is an export format, not a lossless restore format.
- Status: implemented; four regression tests passed in the 206-test run.

### AUD-004 — P1 — Failing onboarding UI tests / P2 responsive UX

- Paths: `ui/screens/onboarding/OnboardingScreen.kt`, matching instrumentation test.
- Problem/impact: initial content is delayed; UI tests access absent/invisible
  nodes. Fixed-height action cards and non-scrollable page clip large text.
- Evidence: [CI run 35436165095](https://github.com/Prem736raj/Health-Metrics-Tracker/actions/runs/35436165095)
  ran eight instrumentation tests: two onboarding failures; other six passed.
- Root cause: arbitrary startup delay, fixed heights and early assertions.
- Fix: show content immediately, scrollable pages, growing cards/buttons,
  saveable selection, page bounds/transition guards and correct theme on-colors.
- Status: patched; three onboarding tests passed on the local API 34 emulator,
  including the 2x-font dark-theme case. This is not all-screen accessibility approval.

### AUD-005 — P1 — Privacy disclosure / unnecessary advertising permissions

- Paths: merged debug manifest; `docs/privacy.html`, `data/ai/AiWellnessContext.kt`.
- Problem/impact: Firebase Analytics adds advertising ID/attribution permissions
  even though no advertising feature is present. Privacy policy omits optional
  AI context, which contains latest weight and weight change, not just habit counts.
- Evidence/root cause: merged manifest includes AD_ID and AdServices permissions;
  `toPromptSection()` emits weight values. Library manifest merging and stale policy.
- Fix: remove unneeded advertising permissions, disable advertising collection,
  accurately disclose optional context and distinguish local deletion from server retention.
- Status: implemented. Three manifest tests passed on API 34 (rationale,
  protected permission alias, unnecessary ad permissions absent/network present).
  Updated policy is local source only; public publication/console details pending.

### AUD-006 — P2 — Dates / streak correctness

- Paths: `data/repository/HistoryRepository.kt`, `data/util/WaterDataIntegrity.kt`.
- Problem/impact: history streak subtracts fixed 24-hour intervals (DST breaks),
  and a query limited to 30 records is described as 30 days. Water startup summary
  labels yesterday's total with the last-open date after multiple missed days.
- Root cause: timestamp/day conflation. Fix: calendar-date arithmetic and correctly
  bounded queries; do not alter recorded health measurements or historical schemas.
- Status: implemented; calendar streak tests passed for DST, leap date,
  duplicates and missed days. Water startup label fix is statically verified.

### AUD-007 — P2 — Repository entry points / CI hygiene

- Paths: root, `.github/workflows/android.yml`, `gradle/wrapper/gradle-wrapper.properties`.
- Problem/impact: no README/contribution/security entry docs or license; root holds
  large historical reports; workflow lacks explicit permissions/concurrency/build timeout;
  Gradle distribution lacks checksum. Unsigned release build is not a signed Play release.
- Fix: concise docs and historical index, minimal CI hardening/checksum; owner chooses
  license and branch protection. Keep master; cosmetic branch migration adds no product value.
- Status: docs, archival, workflow controls and wrapper checksum implemented.
  Owner decisions remain open. No public history rewritten, no keystore or
  credentials read/committed. Workflow changes have not been pushed/run remotely.

### AUD-008 — P2 — Release verification gaps

- Paths: application-wide and release configuration.
- Problem/impact: build/test coverage does not establish all-screen usability,
  performance, notification delivery, Health Connect/provider behavior or signed R8 AI behavior.
- Evidence: 191 prior JVM tests; baseline CI instrumentation failed. The first
  stable audit revision passed 206 JVM and 12 local API 34 instrumentation tests.
  No local device at start; isolated emulator installed for this audit. No
  controlled startup/memory/battery benchmarks in repository.
- Fix: run current build/test gates and device matrix; owner completes signing,
  Firebase App Check enforcement, Play declarations and pre-launch testing.
- Status: open. Never mark Play-ready solely on unsigned packaging.

### AUD-009 — P1 — Food history retention / state ownership

- Paths: `data/repository/FoodLogRepository.kt`, `FoodHistoryPolicy.kt`,
  `di/FoodRepositoryModule.kt`, Home/Calorie/FoodLog ViewModels, `data/model/FoodEntry.kt`.
- Problem/impact: history silently truncated at 90 days; separate repository
  instances hold independent mutable state and day-rollover coroutines. A zero
  target creates invalid progress. Negative/non-finite custom macros accepted.
- Root cause/evidence: `take(90)` and constructor calls in multiple ViewModels;
  unchecked division and permissive parsing.
- Fix/status: retain days, replace the same date rather than append duplicates,
  share the application-owned repository including Hilt, validate food inputs,
  guard target division. Implemented; four food-safety JVM tests passed.
- Verification limit: malformed JSON is now guarded against overwrite by
  AUD-022, but recovery and disk-full persistence still need work;
  SharedPreferences JSON is not a durable versioned backup format.

### AUD-010 — P1 — Scheduled export reports false success

- Paths: `data/export/DataExportManager.kt`, `AutoExportWorker.kt`.
- Problem/impact: manager swallowed export errors and returned Unit; worker
  always updated the last-success timestamp. Raw exception messages reached UI.
- Root cause: caller had no explicit success result and cancellation was caught.
- Fix/status: return Boolean, update timestamp only on success, bounded retry,
  preserve cancellation, present a safe error. Implemented; FileProvider success/
  failure tests passed in the final 19-test API 34 run.
- Verification: new real FileProvider success/failure instrumentation tests;
  WorkManager periodic execution and cache retention are not full backup tests.

### AUD-011 — P1 — Partial cleanup and unhandled storage failures

- Paths: `data/local/HistoryDao.kt`, `HistoryRepository.kt`, `DataCleanupManager.kt`,
  `DataManagementViewModel.kt`, `presentation/weight/WeightTrackingViewModel.kt`.
- Problem/impact: cleanup loops commit individual deletions; mid-operation failure
  leaves partial history and an active spinner. Weight save/delete errors can
  escape ViewModel coroutines. Repeated save taps can insert duplicate entries.
- Root cause: no bulk transaction, only IllegalArgumentException caught on save.
- Fix/status: transactional/chunked selected-ID deletion (no schema change),
  real affected-row count, cancellation-safe errors/finally state reset, save
  guard before launch and shared decimal-comma edit support. Implemented; Room
  CRUD and 1,200-row rollback tests passed in the final 19-test API 34 run.
- Verification: new Room CRUD + 1,200-row rollback tests inject a failure in the
  second chunk. Weight UI failure branches inspected; not failure-injected end to end.

### AUD-012 — P2 — Overview queries, routes and category color precedence

- Path: `data/repository/HealthOverviewRepository.kt`, `data/model/HealthCategoryPresentation.kt`.
- Problem/impact: uppercase lookup keys do not match persisted lowercase keys;
  route aliases do not match the graph. Broad `normal`/`hypertension` substring
  checks run before `high normal`/`stage 2`, understating status visually.
- Fix/status: use CalculatorType keys/Screen routes; specific-first presentation
  mapping with word boundaries for normal/healthy. No medical thresholds changed.
- Verification: compiled key/route fix; presentation regression tests passed in
  both 218-test JVM variants. Full profile overview journey still needs device coverage.

### AUD-013 — P2 — Theme role contrast

- Paths: `ui/theme/Color.kt`, onboarding.
- Problem/impact: white text over light secondary/tertiary roles was about
  2.40:1/2.78:1. Alpha-reduced text and fixed-size onboarding cards impair reading.
- Fix/status: darken only the affected light-theme roles (about 5.32:1/5.50:1),
  use correct on-colors, allow card growth/scrolling. Implemented.
- Verification: 14 opaque role pairs pass JVM contrast assertions; dark 2x-font
  onboarding passes instrumentation. Does not validate all gradients/charts/alpha.

### AUD-014 — P2 — BMR validation contract mismatch

- Paths: `data/calculator/BMRCalculator.kt`, `BMRValidation.kt`.
- Problem/impact: one public weight validator accepted NaN; WHO form validation
  exempted height while calculation rejects it, allowing unexplained null results.
- Fix/status: reject non-finite weight; form consistently enforces existing
  adult height contract. No equation coefficients or clinical categories changed.
- Verification: all-formula validation consistency tests passed in both 218-test JVM variants.

### AUD-015 — P2 — Localization and date formatter debt

- Paths: `core/utils/DateUtils.kt`, numerous Compose/ViewModel files.
- Problem/impact: shared mutable SimpleDateFormat instances capture old locale/
  timezone and are unsafe across threads; many English strings remain inline.
- Root cause: process-wide formatters and feature-local string definitions.
- Recommendation: per-call/current-locale formatters or java.time, regression
  tests for locale/timezone changes; incremental string extraction.
- Status: formatter/thread/locale/timezone fix implemented; three regression tests
  passed in both 218-test JVM variants. Broader string extraction remains open.

### AUD-016 — P2 — Remaining persistence/provider failure states

- Paths: `FoodLogRepository.kt`, `HealthConnectManager.kt`, other feature ViewModels.
- Problem/impact: malformed preference JSON can fall back to empty state; HC
  exceptions often map to empty/null. User cannot distinguish no records from a
  read failure. Some observed flows still lack recoverable error UI.
- Recommendation: explicit loading/error/empty states, preserve unreadable bytes,
  failure injection before a versioned persistence migration.
- Status: open; no automatic corrupt-data deletion or schema rewrite performed.

### AUD-017 — P2 — Food weekly summary and rapid-entry identity

- Paths: `FoodLogRepository.kt`, `FoodHistoryPolicy.kt`.
- Problem/impact: `(history + today).take(7)` drops today once history has seven
  rows and is not restricted to this week; one-shot Flow never updates after
  logging. Millisecond ID collisions can make deleting one food delete another.
- Root cause: record-count/date confusion, snapshot flow, timestamp-only IDs.
- Fix/status: reactive today-log mapping and seven-calendar-day filter, preserve
  both rapid entries with distinct IDs. Implemented; tests passed in both JVM variants.
- Tests: today/stale/future/malformed-day summary and duplicate-ID retention.

### AUD-018 — P2 — Storage accounting, main-thread disk work and cache scope

- Paths: `data/management/StorageAnalyzer.kt`, `DataManagementViewModel.kt`.
- Problem/impact: exports counted twice in total; WAL/SHM omitted; recursive disk
  walks run on the UI dispatcher. Ordinary cache cleanup removes export reports
  even though a separate exports action exists; cleared-byte count can be false.
- Fix/status: IO dispatcher, include DB sidecars, count exports once, preserve
  exports during ordinary cleanup, report actual removed bytes and safe failures.
- Verification: two isolated-directory Android tests passed in the final API 34 run.

### AUD-019 — P2 — Weight dialog keyboard and edit fidelity

- Paths: `ui/components/LogWeightDialog.kt`, `WeightTrackingViewModel.kt`.
- Evidence: API 34 dark-theme screenshot `scratch/android-audit/weight-dialog.png`
  shows keyboard over Save; manually saved 70.53 kg reopens as 70.5. Dialog uses
  `toDoubleOrNull` while ViewModel accepts comma decimals, leaving Save disabled.
- User impact/root cause: inaccessible keyboard-open actions, inconsistent form
  validation and rounding a measurement during a note/date edit.
- Fix: scroll/IME-aware dialog and shared unrounded input policy.
- Status: shared WeightInputPolicy, unrounded edit value, scroll/IME-aware dialog,
  keyboard Done action and resource strings implemented. Three JVM tests passed
  in both variants; 2x-font dark dialog test passed on API 34. On 23 September,
  the saved 70.53 value reopened at full precision, but a real keyboard screenshot
  (`scratch/android-audit/weight-ime-2026-09-23.png`) showed Update clipped at
  the IME boundary. The content now has a smaller scrollable height while the
  keyboard is visible. The 23 September rebuild passed, and a second API 34
  screenshot (`scratch/android-audit/weight-ime-fixed-2026-09-23.png`) shows the
  full Update button above the open keyboard. Tapping it completed the edit and
  displayed “Weight entry updated.” The earlier System UI ANR occurred under
  ~300 MB free RAM and did not establish an application ANR.

### AUD-020 — P1 — Unsupported Data Safety and release assertions

- Paths: `docs/PLAY_DATA_SAFETY_GUIDE.md`, `DATA_SAFETY_MAPPING.md`,
  `PRODUCTION_RELEASE_CHECKLIST.md`, `RELEASE_STATUS.md`.
- Problem/impact: old guide claims exact answers from a signed release, equates
  local storage with off-device collection and assumes Firebase crash uploads.
  Checklist treats schema fixtures as proof of the oldest shipped database.
- Evidence/root cause: documentation overstates source/build/owner evidence;
  no Crashlytics SDK declaration or signed-release validation supports these claims.
- Fix/status: corrected data-flow/owner review guide, exact optional AI weight
  context, local versus server deletion limits; reopened unverified checkboxes.
  Implemented; Google/Firebase primary guidance checked 20 September.
- Verification: source/document comparison only, not console/legal approval.

### AUD-021 — P1 — Published policy is behind local corrections

- Paths: `docs/privacy.html` and public GitHub Pages privacy URL.
- Evidence: read-only HTTP GET on 20 September returned 200 HTML, but the served
  content did not contain the newly added latest-weight-context or deletion-limit
  wording. The local patch has not been published by this audit.
- User impact/root cause: users/Play reviewers can see an older policy despite
  accurate local source. Local file edits do not deploy GitHub Pages.
- Fix/status: owner/reviewed release workflow must publish the corrected policy
  and verify the served content; open. No push/deployment performed.
- Verification: network reachability verified, current disclosure publication not verified.
  A 26 September GET returned HTTP 200, but public HTML still lacked the local
  `recent weight change`, exported-copy and server-side deletion-limit wording;
  publication remains open.

### AUD-022 — P1 — Malformed food JSON could be silently overwritten

- Area/paths: food persistence and UI; `data/repository/FoodLogRepository.kt`,
  `ui/screens/calorie/FoodLogViewModel.kt`, `FoodLogScreen.kt`,
  `CalorieViewModel.kt`, and `res/values/strings.xml`.
- Problem/user impact: malformed saved today, history, or preset JSON was read
  as empty in some paths. Later saves or day rollover could replace an unreadable
  value, making recovery of the original records harder.
- Evidence/root cause: `getHistoricalLogs()` returned `emptyList()` on parse
  failure; `saveToHistory()` then serialized that fallback. Today and preset
  loaders also swallowed parse errors. No write guard or user-visible state existed.
- Recommended fix/status: preserve raw preferences, expose a read-only state,
  refuse food mutations and day rollover after corruption is detected, and show
  a Food Log warning instead of fabricated empty totals. Implemented locally.
  Archive validation occurs on Food Log access or before archive writes rather
  than on every app startup. Five isolated preference/device tests added.
- Verification: 26 September full local gate passed; 24 Android 14 device tests
  passed, including five isolated preference corruption cases. This prevents
  overwriting malformed records; it does not repair JSON or guarantee recovery from disk
  failure, and other screens may still show an empty food aggregate.

### AUD-023 — P1 — Splash could leave the app permanently blank

- Area/paths: startup navigation; `ui/screens/splash/SplashScreen.kt` and
  `ui/screens/splash/SplashScreenTest.kt`.
- Problem/user impact: a manual launch on audit34 after a green 24-test suite
  showed only a dark canvas for more than ten seconds. The app process and
  MainActivity stayed alive, but onboarding/Home was unreachable.
- Evidence/root cause: audit34 had all three system animation scales set to
  `0`. The splash composable faded its content to alpha zero and invoked
  `onSplashComplete` only from `animateFloatAsState`'s finished listener. That
  listener was not observed in this device state, leaving a transparent splash
  destination. Ignored scratch screenshot: `scratch/android-audit/health-screen-2026-09-26.png`.
- Recommended fix/status: invoke completion once from the splash coroutine after
  its fade interval, independent of animation callbacks; use the latest callback
  across recompositions. Implemented locally, with an instrumentation test.
- Verification: verified 28 September. The revised debug/test APKs passed the
  complete audit34/API 34 suite (`OK (25 tests)`, 105.689s) with window,
  transition and animator scales all set to `0`. A fresh force-stop/cold launch
  then focused `MainActivity`; the UI hierarchy contained the onboarding title,
  privacy copy and Next/Skip controls. A targeted post-launch logcat scan found
  no app `FATAL EXCEPTION` or `ANR in com.healthmetrics.tracker` entry.

### AUD-024 — P2 — Startup crash diagnostics grew without bound

- Area/path: `MainActivity.kt` private diagnostics file.
- Problem/user impact: repeated startup failures appended complete throwable
  messages and traces to an unbounded file. Exception messages can contain
  entered values, and a crash loop can grow storage usage.
- Evidence/root cause: `FileOutputStream(file, true)` and
  `throwable.printStackTrace(writer)` in the startup catch path.
- Recommended fix/status: overwrite the prior local crash record and keep only
  exception types plus a bounded number of stack frames, without messages.
  Implemented locally; no crash telemetry service was added.
- Verification: source inspection complete. The current revision also passes
  both JVM variants, debug/test APK packaging, debug/release lint and release
  APK/AAB packaging. The handler does not cover crashes outside the guarded
  startup block.

## Current market research (retrieved 19 September 2026)

[Samsung Health](https://www.samsung.com/us/apps/samsung-health/) emphasizes a
connected activity/sleep/nutrition ecosystem with supported wearable features.
[Cronometer](https://cronometer.com/) emphasizes detailed nutrition, verified foods
and barcode entry. Those are substantial capabilities this source does not implement.
Do not describe the manual food diary as equivalent to a food database/scanner.

Recommended positioning (product inference, not validated demand): **a local-first,
quick daily measurement journal with useful exports and transparent educational
calculators**, with optional connectivity. AI, advanced formula comparisons and
gamification should remain secondary; do not compete by adding every competitor feature.
Market-size, ratings, revenue and representative complaint prevalence are not established.
See [market and dependency review](MARKET_AND_DEPENDENCIES.md) for dated competitor
review examples, current library comparisons and upgrade risks, and
[feature inventory](FEATURE_AUDIT.md) for source-backed reachability/disposition.

## Health and policy reference verification

- [CDC adult BMI categories](https://www.cdc.gov/bmi/adult-calculator/bmi-categories.html):
  category boundaries use unrounded values; BMI is screening, not diagnosis.
- [AHA blood pressure guidance](https://www.heart.org/en/health-topics/high-blood-pressure/understanding-blood-pressure-readings):
  symptoms and repeated measurement matter; avoid diagnosing from one reading.
- [Health Connect integration](https://developer.android.com/health-and-fitness/health-connect/get-started):
  feature-led permissions and privacy rationale entry points.
- [Play health content policy](https://support.google.com/googleplay/android-developer/answer/16679511):
  health disclosures, privacy and medical-functionality requirements require owner review.
- [Health apps declaration](https://support.google.com/googleplay/android-developer/answer/14738291):
  complete the applicable Play Console declaration, including testing tracks.
- [Firebase models](https://firebase.google.com/docs/ai-logic/models): do not infer
  the hard-coded model is available in the owner's project solely from a model name.
- [OWASP CSV injection](https://owasp.org/www-community/attacks/CSV_Injection):
  CSV quoting alone is not formula neutralization.

## Verification ledger

| Revision / command | Actual result |
|---|---|
| Prior local onboarding revision: `testDebugUnitTest assembleDebugAndroidTest` | 191 JVM tests passed; test APK compiled; not device execution |
| Prior local revision: `lintDebug` | Passed with existing warnings; not zero-warning lint |
| GitHub baseline `1efccdf`, run 35436165095 | Instrumentation 6 passed / 2 failed; baseline is not green |
| Early audit combined build | Failed: new files were added after main-source compilation started; a later consistent snapshot fixed the unresolved test references |
| Stable audit: `testDebugUnitTest assembleDebug assembleDebugAndroidTest --max-workers=2` | BUILD SUCCESSFUL, 16m20s; 206 JVM tests, 0 failures/errors |
| Stable audit: `connectedDebugAndroidTest --max-workers=2` | BUILD SUCCESSFUL, 57s; 12 tests passed on isolated API 34 AOSP emulator |
| Export/cleanup/category/validation fixes: `testDebugUnitTest assembleDebug connectedDebugAndroidTest lintDebug lintRelease assembleRelease bundleRelease --max-workers=2` | BUILD SUCCESSFUL, 16m6s; 210 JVM tests, 16 API 34 tests; unsigned release APK/AAB; debug/release lint each 851 warnings, 1 hint, 0 errors |
| Subsequent date/weekly-food/storage/privacy-copy build (`final-verification-2.log`) | Deliberately cancelled to include the runtime-discovered weight dialog repairs; not a passing build |
| Combined date/food/storage/weight-dialog batch (`final-verification-3.log`) | 218 debug JVM tests passed; debug/test APK built; session ended without final release/lint completion |
| Resumed full gate including release JVM tests (`verification-2026-09-21.log`) | BUILD SUCCESSFUL, 22m17s; debug and release each 218 JVM tests passed; debug/test APK, unsigned release APK and AAB produced; lint each 846 warnings, 1 hint, 0 errors; no physical-device installation |
| Explicit audit34-only `adb -s emulator-5554 ... am instrument -w -r` | `OK (19 tests)`, 89.57s, 21 September; all passed |
| Weight-dialog keyboard layout revision (`verification-2026-09-23.log`) | BUILD SUCCESSFUL, 18m39s; debug and release each 218 JVM tests, 0 failures/errors; debug/test APK, unsigned release APK and unsigned AAB produced; lint each 846 warnings, 1 hint, 0 errors. Manual keyboard-visible edit and 19-test API 34 device suite passed. |
| Final audit34-only instrumentation (`instrumentation-2026-09-23.log`) | `OK (19 tests)`, 17.594s, 23 September; all passed against the revised APK |
| First food-safety combined build (`verification-food-2026-09-23.log`) | Failed before Kotlin compilation: KSP `EOFException` loading an incremental classpath snapshot after an interrupted run; not a code-pass result |
| Focused non-incremental KSP retry (`ksp-food-noincremental-2026-09-23.log`) | BUILD SUCCESSFUL, 45s; full compile/test/lint/release verification still pending |
| Food storage full gate (`verification-food-final-2026-09-23.log`) | BUILD SUCCESSFUL, 21m16s; 218 debug and 218 release JVM tests passed; debug/test APK, unsigned release APK/AAB; lint each 846 warnings, 1 hint, 0 errors |
| Current source before splash/crash repair: `test lintRelease assembleDebug assembleRelease bundleRelease` | BUILD SUCCESSFUL in 10m4s, 26 September; 218 debug and 218 release JVM tests passed; release artifacts remain unsigned; release lint 0 errors / 846 warnings |
| Explicit audit34-only instrumentation (`instrumentation-2026-09-26.log`) | `OK (24 tests)`, 51.771s; includes five food corruption/preservation tests; afterward, a manual animation-disabled launch exposed AUD-023 |
| Current splash/crash revision: `testDebugUnitTest testReleaseUnitTest assembleDebug assembleDebugAndroidTest --no-daemon --max-workers=1` | BUILD SUCCESSFUL in 7m28s on 28 September; debug and release each 218 JVM tests, 0 failures/errors; current debug APK and test APK produced |
| Current lint: `lintDebug lintRelease --no-daemon --max-workers=1` | BUILD SUCCESSFUL in 11m7s; debug/release each 0 errors, 846 warnings, 1 hint |
| Current release packaging: `assembleRelease bundleRelease --no-daemon --max-workers=1` | Regenerated 28 September; `app-release-unsigned.apk` (10,548,475 bytes) and `app-release.aab` (18,529,825 bytes) produced; not a signed Play artifact |
| `verifyPlayReleaseSigning --no-daemon --max-workers=1` | Expected release blocker: FAILED because RELEASE_STORE_FILE / STORE_PASSWORD / KEY_ALIAS / KEY_PASSWORD are not configured |
| Final cached combined gate: `testDebugUnitTest testReleaseUnitTest assembleDebug assembleDebugAndroidTest lintDebug lintRelease assembleRelease bundleRelease --no-daemon --max-workers=1` | `BUILD SUCCESSFUL in 48s`; 172 actionable tasks (2 executed, 170 up-to-date); both 218-test JVM variants remain green; debug/test/release packaging and both lint variants remain green |
| Fresh audit34/API 34 current-revision suite | `OK (25 tests)`, 105.689s with all three animation scales at 0; migration/CRUD/export/storage/food/onboarding/weight/splash coverage passed; no physical-device install |
| Fresh animation-disabled cold launch | `MainActivity` focused; UI hierarchy showed onboarding title/copy/Next/Skip; targeted logcat scan found no app fatal exception or app ANR |

## Deletion log

Root `PHASE_PROGRESS.md` and `WHO_Health_Tracker_Production_Audit_2026-09-08.txt`
were moved into `docs/archive/` with historical-context notes; original report
content is retained. They are not lost historical evidence. Duplicate CSV encoder
bodies were replaced by a shared encoder after tracing both call sites. Automatic
heuristic integrity deletion methods/UI were removed because false positives
could delete valid records. No database fields/migrations or user records removed.

## Release checklist — evidence only

- [x] Current debug build — current revision packaged 28 September
- [x] Current release build — unsigned APK/AAB packaged; signing remains open
- [x] Current unit tests — 218 debug + 218 release, 0 failures/errors
- [x] Current UI/device suite — audit34/API 34 `OK (25 tests)`; broader device matrix remains open
- [x] Current lint — debug/release 0 errors, 846 warnings, 1 hint each
- [x] Database migration validation on current revision — 13→14, 14→15, 15→16, chained 13→16 all pass
- [ ] Security review closure
- [ ] Published accurate privacy policy
- [ ] Owner-confirmed Data Safety
- [ ] All-screen light theme device review
- [ ] All-screen dark theme device review
- [ ] Accessibility / TalkBack / large fonts
- [ ] Edge-case matrix
- [ ] Performance measurements
- [ ] Crash/failure-path testing
- [ ] Notification denial/delivery/reboot testing
- [ ] Play Store metadata
- [ ] Signing and App Check configuration
- [ ] Final physical-device testing
