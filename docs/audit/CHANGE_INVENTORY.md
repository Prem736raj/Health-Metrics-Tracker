# Exact audit change inventory

Paths are repository-relative. This records uncommitted changes against baseline
`1efccdf`; it is not a shipped release or a deletion of user data.

| Path | Why / what changed |
|---|---|
| `.github/workflows/android.yml` | Limit CI permissions, cancel superseded runs and bound build duration. |
| `CHANGELOG.md` | Record unshipped audit changes without claiming a released version. |
| `CONTRIBUTING.md` | Describe build/test and safe contribution expectations. |
| `PHASE_PROGRESS.md` | Moved to docs/archive/PHASE_PROGRESS.md; historical information retained. |
| `README.md` | Document implemented scope, architecture, builds, privacy boundaries and outstanding release gates. |
| `SECURITY.md` | Provide responsible reporting and avoid public posting of sensitive diagnostics. |
| `WHO_Health_Tracker_Production_Audit_2026-09-08.txt` | Moved to docs/archive/WHO_Health_Tracker_Production_Audit_2026-09-08.txt; historical information retained. |
| `app/src/androidTest/java/com/health/calculator/bmi/tracker/ManifestPolicyTest.kt` | Verify installed rationale/alias protection and absence of unneeded ad permissions. |
| `app/src/androidTest/java/com/health/calculator/bmi/tracker/data/export/DataExportManagerTest.kt` | Exercise real FileProvider export success and safe failure reporting. |
| `app/src/androidTest/java/com/health/calculator/bmi/tracker/data/local/HistoryCrudTest.kt` | Exercise history CRUD and rollback of a 1,200-row cleanup when its second chunk fails. |
| `app/src/androidTest/java/com/health/calculator/bmi/tracker/data/repository/FoodLogRepositoryTest.kt` | Verify malformed today/history/preset records stay unchanged, day rollover stops, and valid storage still writes. |
| `app/src/androidTest/java/com/health/calculator/bmi/tracker/ui/screens/splash/SplashScreenTest.kt` | Verify the splash sequence invokes navigation exactly once independently of its fade listener. |
| `app/src/androidTest/java/com/health/calculator/bmi/tracker/data/management/StorageAnalyzerTest.kt` | Check isolated-directory storage totals and preservation of reports during cache cleanup. |
| `app/src/androidTest/java/com/health/calculator/bmi/tracker/ui/components/LogWeightDialogTest.kt` | Check comma-decimal save action with 2x text in dark theme. |
| `app/src/androidTest/java/com/health/calculator/bmi/tracker/ui/screens/onboarding/OnboardingScreenTest.kt` | Cover intro, selected action and scrollable 2x-font dark-theme last action. |
| `app/src/main/AndroidManifest.xml` | Declare network-state usage, remove advertising permissions/collection and add protected Health Connect privacy entry points. |
| `app/src/main/java/com/health/calculator/bmi/tracker/MainActivity.kt` | Bound the local startup crash log to the latest event and omit exception messages that might contain entered values. |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/navigation/HubScreens.kt` | Remove a duplicate bottom safe-area inset from the Track/Calculators/Insights hub scaffold; the root already reserves bottom navigation space. |
| `app/src/main/java/com/health/calculator/bmi/tracker/PermissionsRationaleActivity.kt` | Open the fixed privacy policy for Health Connect, with a fallback when no browser is available. |
| `app/src/main/java/com/health/calculator/bmi/tracker/core/utils/CalendarStreak.kt` | Count unique local dates across DST rather than fixed 24-hour periods. |
| `app/src/main/java/com/health/calculator/bmi/tracker/core/utils/DateUtils.kt` | Create formatters per call for thread safety and current locale/timezone; do not label future dates as just now. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/calculator/BMRCalculator.kt` | Reject non-finite values in the public weight validator. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/calculator/BMRValidation.kt` | Align form height validation with the calculator's existing adult input contract. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/export/AutoExportWorker.kt` | Only record successful exports; preserve cancellation and bound retries. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/export/BpExportManager.kt` | Use shared CSV formula/CR escaping instead of a duplicate encoder. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/export/CsvCellEncoder.kt` | Centralize CSV quoting and spreadsheet-formula neutralization. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/export/CsvExportHelper.kt` | Use the shared safe CSV cell encoder. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/export/DataExportManager.kt` | Return explicit success/failure, preserve cancellation and avoid raw exception details in user errors. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/healthconnect/HealthConnectManager.kt` | Read newest weight first with one-record limit; preserve cancellation. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/HistoryDao.kt` | Add chunked, transactional selected-ID deletion with actual affected-row count. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/management/DataCleanupManager.kt` | Remove heuristic automatic deletion and delegate deliberate bulk cleanup to a transaction. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/management/DataIntegrityChecker.kt` | Tighten duplicate heuristics and make diagnostics explicitly non-destructive. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/management/StorageAnalyzer.kt` | Move disk work to IO; count exports once and DB sidecars; preserve exports on ordinary cache cleanup. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/FoodEntry.kt` | Guard calorie progress against zero/non-finite goals and totals. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/HealthCategoryPresentation.kt` | Prioritize specific category labels over misleading broad substrings; presentation only. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/FoodHistoryPolicy.kt` | Retain history, replace same-day snapshots, avoid duplicate food IDs and compute calendar-week summaries. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/FoodLogRepository.kt` | Share one repository, retain older history, use safe append policy/reactive weekly summary, and pause writes instead of overwriting unreadable saved JSON. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/HealthOverviewRepository.kt` | Use persisted calculator keys and valid routes; share tested category presentation. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/HistoryRepository.kt` | Query calendar-date streak range and expose atomic selected-entry deletion. |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/util/WaterDataIntegrity.kt` | Use stable date keys and associate yesterday's total with yesterday, not the last-open date. |
| `app/src/main/java/com/health/calculator/bmi/tracker/di/FoodRepositoryModule.kt` | Bind the application-owned food repository to Hilt so injection does not create independent state. |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/tracking/WeightInputPolicy.kt` | Unify dialog/save parsing and preserve measurement precision through editing. |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/home/HomeViewModel.kt` | Use the shared application food repository. |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/weight/WeightTrackingViewModel.kt` | Handle write/delete failures, preserve cancellation, guard rapid save taps and avoid rounding edit values. |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/LogWeightDialog.kt` | Share input validation, scroll content around keyboard, support Done submission and resource key labels. |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/aicoach/AiCoachScreen.kt` | Show the accurate optional weight/context transfer disclosure. |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calorie/CalorieViewModel.kt` | Use the canonical food repository and surface a paused-write error for its quick-log path. |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calorie/FoodInputPolicy.kt` | Reject malformed, negative and non-finite macros while allowing blank optional values. |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calorie/FoodLogScreen.kt` | Warn when saved food data is unreadable and hide empty-looking totals and mutating controls. |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calorie/FoodLogViewModel.kt` | Share food state, validate food input, check archive data off the UI thread, and relay read-only write failures. |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/onboarding/OnboardingScreen.kt` | Remove delayed visibility; improve scrolling, growable cards/buttons, saved selection and semantic theme roles. |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/splash/SplashScreen.kt` | Complete splash navigation after its sequence even when system animations are disabled. |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/settings/DataManagementScreen.kt` | Remove unsafe automatic repair action and explain diagnostic-only results. |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/settings/DataManagementViewModel.kt` | Remove auto-repair path; guard cleanup, recover from storage failures and reset busy state. |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/theme/Color.kt` | Improve the two light-theme role colors with demonstrated white-text contrast failures. |
| `app/src/main/res/values/strings.xml` | Add accurate AI/onboarding disclosure, weight-dialog labels, and the food storage safety warning. |
| `app/src/test/java/com/health/calculator/bmi/tracker/core/DateUtilsTest.kt` | Cover live locale/timezone changes, concurrent formatting and future dates. |
| `app/src/test/java/com/health/calculator/bmi/tracker/data/calculator/BMRValidationConsistencyTest.kt` | Assert form/calculator agreement for every selected equation. |
| `app/src/test/java/com/health/calculator/bmi/tracker/data/management/DataIntegrityCheckerTest.kt` | Protect valid distinct history records from duplicate heuristics. |
| `app/src/test/java/com/health/calculator/bmi/tracker/data/model/HealthCategoryPresentationTest.kt` | Check high-normal/stage-2 precedence and unknown-label handling. |
| `app/src/test/java/com/health/calculator/bmi/tracker/export/CsvCellEncoderTest.kt` | Cover formula prefixes, delimiters, quotes and carriage returns. |
| `app/src/test/java/com/health/calculator/bmi/tracker/tracking/CalendarStreakTest.kt` | Cover DST, leap dates, duplicates, future records and missed days. |
| `app/src/test/java/com/health/calculator/bmi/tracker/tracking/FoodSafetyTest.kt` | Cover retention, duplicate dates/IDs, input safety, progress and weekly date scope. |
| `app/src/test/java/com/health/calculator/bmi/tracker/tracking/WeightInputPolicyTest.kt` | Cover comma/point parsing, unrounded metric/imperial editing and invalid input. |
| `app/src/test/java/com/health/calculator/bmi/tracker/ui/theme/ThemeContrastTest.kt` | Assert minimum contrast for selected opaque light/dark material role pairs. |
| `docs/DATA_SAFETY_MAPPING.md` | Distinguish local records, optional AI egress, technical services and local-only crash diagnostics. |
| `docs/ASO_LAUNCH_PLAN.md` | Link the candidate local Play feature graphic for owner review. |
| `docs/PLAY_DATA_SAFETY_GUIDE.md` | Replace unsupported exact answers with a source-backed owner review worksheet. |
| `docs/PRODUCTION_RELEASE_CHECKLIST.md` | Reopen oldest-shipped-schema and current release-CI gates lacking evidence. |
| `docs/store/README.md` | Record the generated feature graphic prompt, dimensions and Play review limitations. |
| `docs/store/feature-graphic-1024x500.png` | Supply the missing candidate Play feature graphic in the established wellness palette. |
| `docs/store/screenshots/01-home-api34-dark.png` | Supply an unedited Home listing candidate captured from the API 34 audit emulator with no personal data. |
| `docs/store/screenshots/02-weight-api34-dark.png` | Supply an unedited Weight listing candidate using synthetic measurements on the API 34 audit emulator. |
| `docs/RELEASE_STATUS.md` | Mark historical completion claims as superseded by current evidence. |
| `docs/archive/PHASE_PROGRESS.md` | Preserve historical report content with archival status notes; moved from repository root. |
| `docs/archive/WHO_Health_Tracker_Production_Audit_2026-09-08.txt` | Preserve historical report content with archival status notes; moved from repository root. |
| `docs/audit/CHANGE_INVENTORY.md` | Maintain source-backed change inventory with evidence, limitations and remaining work. |
| `docs/audit/FEATURE_AUDIT.md` | Maintain source-backed feature audit with evidence, limitations and remaining work. |
| `docs/audit/HEALTH_AND_EDGE_CASES.md` | Maintain source-backed health and edge cases with evidence, limitations and remaining work. |
| `docs/audit/MARKET_AND_DEPENDENCIES.md` | Maintain source-backed market and dependencies with evidence, limitations and remaining work. |
| `docs/audit/PRODUCTION_AUDIT_2026-09-19.md` | Maintain source-backed production audit 2026 09 19 with evidence, limitations and remaining work. |
| `docs/audit/READINESS_REPORT.md` | Maintain source-backed readiness report with evidence, limitations and remaining work. |
| `docs/audit/SOURCE_INVENTORY.md` | Maintain source-backed source inventory with evidence, limitations and remaining work. |
| `docs/audit/UI_AND_REPOSITORY_REVIEW.md` | Maintain source-backed ui and repository review with evidence, limitations and remaining work. |
| `docs/privacy.html` | Disclose optional AI weight context and limits of local deletion. |
| `gradle/wrapper/gradle-wrapper.properties` | Pin the verified Gradle distribution SHA-256 checksum. |

## Removal / preservation log

The only whole-file removals from their old locations are the two root reports
moved to `docs/archive/`. No historical report content, Room schema, migration,
health record, keystore or runtime asset was deleted. Unsafe auto-repair methods
and duplicated CSV encoder bodies were removed after tracing their call sites.
The obsolete Data Safety guide was corrected in place; Git retains the prior
text and AUD-020 explains why its unsupported answers were replaced.
