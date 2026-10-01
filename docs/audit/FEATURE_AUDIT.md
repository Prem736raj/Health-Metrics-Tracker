# Source-backed feature and product inventory

Audit dates: 19–20 September 2026. Paths below are relative to
`app/src/main/java/com/health/calculator/bmi/tracker/` unless stated otherwise.
`core/navigation/NavGraph.kt` is the source for reachability; `Screen.kt` defines
most route names. **L = LIKELY WORKING BUT NOT RUNTIME VERIFIED**, **P = PARTIALLY
IMPLEMENTED**, **R = NEEDS REDESIGN**, **D = DEAD/UNREACHABLE**. These statuses do
not mean every operation succeeds. Specific device tests are recorded in the main
audit ledger; no feature is promoted to globally WORKING just because a test passes.

## Navigation / state / persistence inventory

| Feature / entry | Route | UI / logic | Data / operations / permission | Status / disposition |
|---|---|---|---|---|
| Initial launch | splash, onboarding | SplashScreen, OnboardingScreen, NavGraph | Settings DataStore completion; no runtime permission | L; IMPROVE responsive layout and state restoration |
| Daily overview | home | HomeScreen, HomeViewModel | Room + food prefs + optional HC; cached metrics | L; KEEP, verify updates after logging |
| Logging directory | track | TrackHubScreen | Navigation only | L; KEEP |
| Calculator directory | calculators | CalculatorsHubScreen | Navigation only | L; KEEP advanced formulas secondary |
| Insights directory | insights | InsightsHubScreen, HomeViewModel | Trend/score aggregation | L; IMPROVE missing-data explanations |
| Calculator history | history | HistoryScreen/HistoryViewModel | Room HistoryDao; read/delete/notes; no permission | L; IMPROVE failure handling and large-history performance |
| General calculation deep link | calculation/{calculationId} | NavGraph/PlaceholderScreen | Only whr_result has an implemented destination here | P; unsupported IDs show unavailable, not an implemented detail view |
| Profile | profile | ProfileScreen/ProfileViewModel | Profile DataStore + health overview | L; IMPROVE corrected overview keys/routes |
| Profile connections | health_connections | HealthConnectionsScreen/MultiProfileViewModel | HC step/weight reads; permissions separately requested | L; IMPROVE provider/denial testing |
| Multiple people | no enabled tracker flow | MultiProfileViewModel, FamilyProfileRepository | FamilyProfile table exists; core records lack profile ID | D for multi-person tracking; keep disabled, do not delete schema |
| Weight journal | weight_tracking | WeightTrackingScreen/ViewModel | WeightRepository/WeightDao; create/read/update/delete, goals/reminder | L; KEEP; failure-path/device checks required |
| BMI | calculator/bmi | BmiCalculatorScreen/BmiViewModel | BmiCalculator + history/goal prefs; adult-input policy | L; KEEP; secondary metrics not diagnoses |
| BMR | calculator/bmr | BMRCalculatorScreen/BmrViewModel | BMRCalculator, historical equations, history/prefs | L; KEEP Mifflin default, OPTIONAL formula comparisons |
| Blood-pressure calculation | calculator/blood_pressure | BloodPressureScreen/ViewModel | BloodPressureCalculator, BP and history repositories | L; KEEP; classifications are reading context |
| BP journal | blood_pressure_log | BpLogScreen/ViewModel | BloodPressureDao; log/read/edit/delete | L; KEEP; manual measured values, not phone measurement |
| BP trends | blood_pressure_trends | BpTrendScreen/ViewModel | BP repository aggregations | L; IMPROVE large text/chart semantics |
| BP reminders | blood_pressure_reminders | BpReminderScreen/ViewModel | BP prefs + BpNotificationHelper/receivers; notification permission | L; MERGE UX into common reminders without deleting saved schedules |
| BP export | blood_pressure_export | BpExportScreen/BpExportManager | CSV/PDF/text, FileProvider grant | L; IMPROVE encoder and export failures |
| BP education | blood_pressure_education | BpEducationalScreen | Bundled content; no persistence/permission | L; KEEP concise references, clinician review still needed |
| Waist/hip input/result | calculator/waist_hip; calculation/whr_result | WhrInputScreen, WhrResultScreen/ViewModel | WhrCalculator, WhrRepository, profile height | L; KEEP; validate units and invalid route arguments |
| Waist/hip progress | whr_progress | WhrProgressScreen/ViewModel | WHR preference-backed history | L; KEEP, separate from generic history storage |
| Waist education | whr_education | WhrEducationalScreen | Bundled reference/measurement guidance | L; MERGE lengthy secondary panels into progressive disclosure |
| Beverage target | calculator/water_intake; water_intake_result | WaterIntakeInput/ResultScreen/ViewModel | WaterIntakeCalculator, Room calculation history | L; KEEP estimates, no illness/fluid-restriction prescription |
| Water journal | water_tracker | WaterTrackingScreen/ViewModel | WaterIntakeRepository/DAO; quick log/edit/delete; widgets | L; KEEP; rapid-action/day-rollover verification needed |
| Water history | WATER_HISTORY_ROUTE | WaterHistoryScreen/ViewModel | Room water logs/day aggregation | L; KEEP |
| Water reminder settings | WATER_REMINDER_SETTINGS_ROUTE | WaterReminderSettingsScreen | Preferences + alarms/receiver; notifications | L; MERGE reminder entry points, not scheduling behavior |
| Hydration badges/streaks | WATER_GAMIFICATION_ROUTE | WaterGamificationScreen/ViewModel | WaterGamificationDao/engine, streak prefs | L; OPTIONAL; never reward unsafe excess drinking |
| Hydration tools | WATER_TOOLS_ROUTE | HydrationToolsScreen/ViewModel | Bundled tools + urine-color DAO | R; OPTIONAL, observation cannot diagnose dehydration |
| Water education | WATER_EDUCATION_ROUTE | WaterEducationScreen | Bundled content | L; MERGE into relevant tracker help |
| Electrolyte education | ELECTROLYTE_INFO_ROUTE | ElectrolyteInfoScreen | Bundled content | R; OPTIONAL; avoid supplement/treatment advice |
| Metabolic indicators | calculator/metabolic_syndrome | MetabolicSyndromeScreen/ViewModel | MetabolicSyndromeCalculator/partial assessment/tracking | R; retain only explicit informational criteria; no diagnosis |
| BSA | calculator/bsa | BSAScreen/BSAViewModel | BSACalculator + tracking repo | L; OPTIONAL advanced calculator; no dosing |
| Reference weight | ideal_body_weight | IBWScreen/IBWViewModel | IBWCalculatorUseCase, IdealWeightCalculator, goal/history prefs | R; MERGE prominent BMI reference range; keep historical equations secondary |
| Calories/macros | calculator/daily_calorie | CalorieScreen/CalorieViewModel | CalorieCalculatorUseCase/MacroCalculatorUseCase | L; KEEP estimates; invalid input/goal handling |
| Manual food diary | food_log | FoodLogScreen/FoodLogViewModel | SharedPreferences JSON, shared FoodLogRepository | L; KEEP lightweight manual diary, no food-database claims |
| Calorie history | calorie_history | CalorieHistoryScreen, CalorieViewModel/FoodLogViewModel | Calculator history + food history preferences | L; IMPROVE distinguish calculated needs from eaten food |
| Heart-rate zones | calculator/heart_rate_zone | HeartRateZoneScreen/ResultScreen | HeartRateZoneCalculator, VO2MaxCalculator + history prefs | L; OPTIONAL training estimates, not sensor readings/clinical fitness age |
| Achievements | achievements | MilestonesScreen/ViewModel | MilestonesDao, evaluation use case | L; OPTIONAL; habit/data-availability score, not health grade |
| Reminders | reminders | RemindersScreen/ViewModel | ReminderDao/Repository/Scheduler; notifications/reboot | L; KEEP; deny/revoke/DST/reboot verification |
| Welcome back | welcome_back | WelcomeBackScreen/ViewModel | Inactivity prefs, health overview | L; OPTIONAL, respectful opt-in prompts |
| Weekly report | weekly_report | WeeklyReportScreen/ViewModel | WeeklyReportGenerator, Room reports and tracker stores | L; KEEP, validate date windows and missing data |
| Educational articles | health_articles | HealthArticlesScreen | Bundled DailyContentData | L; OPTIONAL, sources/editorial maintenance required |
| AI assistant | ai_coach | AiCoachScreen/AiCoachViewModel | Room chat, GeminiHelper, Firebase AI Logic/App Check; INTERNET | L; OPTIONAL cloud feature, runtime service/abuse testing outstanding |
| Exports | export_data | ExportDataScreen/ViewModel/DataExportManager | HistoryDao → PDF/CSV/JSON in private cache → user share | P; not a full-app backup/restore |
| Scheduled export | export settings | AutoExportWorker/ExportScheduleManager | WorkManager local cache export; no email sender | P; do not describe as email or durable backup |
| Data management | data_management | DataManagementScreen/ViewModel | StorageAnalyzer/DataCleanupManager; user-confirmed deletion | L; IMPROVE diagnostic checks now non-destructive |
| Settings/theme/privacy | settings | SettingsScreen/ViewModel | Settings DataStore; analytics/context consent; external policy links | L; KEEP |
| Widgets | Android launcher | Water/summary/streak/calculator providers and config activities | Room/prefs snapshots; PendingIntents; boot refresh | L; KEEP useful quick log widgets; launcher/process-death tests outstanding |

## Error-state and test coverage interpretation

- Existing tests cover many pure formula boundaries, route allow-lists, reminder
  date policies, analytics filtering, AI input policies and tracking summaries.
- Room instrumentation covers 13→14, 14→15, 15→16 and upgrades to current schema.
  That does not establish migration safety from pre-13 databases.
- New tests cover CSV encoding, calendar streaks, conservative integrity checks,
  food history retention/input safety, material contrast, onboarding and manifest policy.
- Health Connect currently maps provider failures to empty/null, making “no data”
  indistinguishable from some read errors. Real-provider testing remains necessary.
- Database writes in some ViewModels still lack user-visible failure handling;
  keep these tracked until fixed, rather than asserting all CRUD works.
- UI smoke tests are narrow, not full journey coverage. No percentage coverage
  claim is supported because code-coverage instrumentation is not configured.

## What should be removed or simplified?

1. **REMOVE automatic heuristic repair/deletion**, implemented: matching displayed
   values do not prove a duplicate. Keep explicit deletion and diagnostics.
2. **REMOVE advertising permissions** that have no product purpose, implemented.
3. **MERGE reminder navigation and overlapping calculator education**, recommendation:
   preserve existing scheduled data; do not rename persisted fields casually.
4. **OPTIONAL AI, achievements, urine-color tools, BSA and extended formula tables**:
   move secondary entry points out of the main daily-logging path. No functionality
   deleted without user-value evidence or a migration plan.
5. **Keep multi-profile disabled** until records are actually scoped; existing
   entities/migrations are not safe dead-code deletion targets.
6. **Archive historical status reports**, implemented with contents preserved;
   they must not present old “Complete” statements as current approval.

## Missing functionality by priority

- **Must have before release:** passing current build/UI/migration gates, clear
  save/export failures, accurate published privacy policy and Play declarations,
  provider/notification permission testing, signed-release smoke testing.
- **Should have:** explicit readable Health Connect error states; encrypted,
  user-controlled export/import with tested schema/versioning if durable backup
  is promised; complete chart text alternatives and localization-ready strings.
- **Nice to have:** tighter advanced-section information hierarchy, tablet layouts,
  measured baseline profiles after startup traces identify a need.
- **Future:** optional device integrations or a nutrition database only if real
  usage demonstrates demand. Do not add accounts, social feeds, ads or broad health
  permissions merely to match competitors.
