# Tracked repository inventory

Generated from the active checkout during the September 2026 audit. This is a recursive static index, **not a claim of line-by-line review or runtime verification**. Generated build output, ignored signing material and the nested checkout are excluded. Newly added untracked audit files are listed in the change report rather than this baseline index.

| Path | Lines | Top-level types (lexical index) |
|---|---:|---|
| `.github/workflows/android.yml` | 115 |  |
| `.gitignore` | binary |  |
| `PHASE_PROGRESS.md` | 1860 |  |
| `WHO_Health_Tracker_Production_Audit_2026-09-08.txt` | 3232 |  |
| `app/.gitignore` | binary |  |
| `app/build.gradle.kts` | 269 |  |
| `app/google-services.json` | binary |  |
| `app/proguard-rules.pro` | binary |  |
| `app/schemas/com.health.calculator.bmi.tracker.data.local.AppDatabase/13.json` | binary |  |
| `app/schemas/com.health.calculator.bmi.tracker.data.local.AppDatabase/14.json` | binary |  |
| `app/schemas/com.health.calculator.bmi.tracker.data.local.AppDatabase/15.json` | binary |  |
| `app/schemas/com.health.calculator.bmi.tracker.data.local.AppDatabase/16.json` | binary |  |
| `app/src/androidTest/java/com/health/calculator/bmi/tracker/ExampleInstrumentedTest.kt` | 24 | ExampleInstrumentedTest |
| `app/src/androidTest/java/com/health/calculator/bmi/tracker/data/local/AppDatabaseMigrationTest.kt` | 211 | AppDatabaseMigrationTest |
| `app/src/androidTest/java/com/health/calculator/bmi/tracker/ui/screens/onboarding/OnboardingScreenTest.kt` | 117 | OnboardingScreenTest |
| `app/src/debug/java/com/health/calculator/bmi/tracker/core/security/AppCheckInitializerImpl.kt` | 15 | AppCheckInitializerImpl |
| `app/src/main/AndroidManifest.xml` | 292 |  |
| `app/src/main/ic_launcher-playstore.png` | binary |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/HealthCalculatorApp.kt` | 85 | HealthCalculatorApp |
| `app/src/main/java/com/health/calculator/bmi/tracker/MainActivity.kt` | 198 | MainActivity |
| `app/src/main/java/com/health/calculator/bmi/tracker/calculator/BSACalculator.kt` | 243 | BSAFormulaInfo, BSAResult, BSACalculator |
| `app/src/main/java/com/health/calculator/bmi/tracker/calculator/BSAEdgeCaseValidator.kt` | 141 | BSAValidationResult, BSAEdgeCaseValidator |
| `app/src/main/java/com/health/calculator/bmi/tracker/calculator/MetabolicSyndromeCalculator.kt` | 118 | MetabolicCriterion, MetabolicSyndromeResult, MetabolicRiskLevel, MetabolicSyndromeCalculator |
| `app/src/main/java/com/health/calculator/bmi/tracker/calculator/MetabolicSyndromePartialAssessment.kt` | 69 | PartialCriterionResult, PartialAssessmentResult, MetabolicSyndromePartialAssessment |
| `app/src/main/java/com/health/calculator/bmi/tracker/calculator/MetabolicSyndromeRecommendations.kt` | 75 | CriterionRecommendation, CardiovascularRiskSummary, MetabolicSyndromeRecommendations |
| `app/src/main/java/com/health/calculator/bmi/tracker/calculator/MetabolicSyndromeStandards.kt` | 195 | EthnicityWaistCutoff, StandardResult, StandardCriterionDetail, MultiStandardComparison, Ethnicity, MetabolicSyndromeStandards |
| `app/src/main/java/com/health/calculator/bmi/tracker/core/brand/BrandVoice.kt` | 14 | BrandVoice |
| `app/src/main/java/com/health/calculator/bmi/tracker/core/constants/AppConstants.kt` | 52 | AppConstants |
| `app/src/main/java/com/health/calculator/bmi/tracker/core/constants/AppLinks.kt` | 17 | AppLinks |
| `app/src/main/java/com/health/calculator/bmi/tracker/core/extensions/ModifierExtensions.kt` | 55 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/core/navigation/AppEntryRouteResolver.kt` | 78 | AppEntryRouteResolver |
| `app/src/main/java/com/health/calculator/bmi/tracker/core/navigation/BottomNavItem.kt` | 74 | BottomNavItem |
| `app/src/main/java/com/health/calculator/bmi/tracker/core/navigation/NavGraph.kt` | 1387 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/core/navigation/Screen.kt` | 91 | Screen, Splash, Onboarding, Home, Track, Calculators, Insights, History, Profile, Settings, BmiCalculator, BmrCalculator, BloodPressureCalculator, WaistToHipCalculator, WaterIntakeCalculator, MetabolicSyndromeCalculator, BsaCalculator, IdealWeightCalculator, DailyCalorieCalculator, HeartRateZoneCalculator, FoodLog, CalorieHistory, CalculationDetail, WaterTracker, BloodPressureLog, BloodPressureTrends, BloodPressureReminders, BloodPressureExport, BloodPressureEducation, WhrEducation, HealthArticles, Achievements, ExportData, DataManagement, HealthConnections, WeightTracking, Reminders, WeeklyReport, AiCoach |
| `app/src/main/java/com/health/calculator/bmi/tracker/core/security/AppCheckInitializer.kt` | 8 | AppCheckInitializer |
| `app/src/main/java/com/health/calculator/bmi/tracker/core/util/BroadcastReceiverExt.kt` | 24 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/core/utils/DateUtils.kt` | 93 | DateUtils |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/BSATrackingRepository.kt` | 92 | BSATrackingRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/MetabolicSyndromeTrackingRepository.kt` | 255 | MetabolicSyndromeTrackingRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/WaterWidgetRepository.kt` | 177 | WaterWidgetRepository, WaterWidgetData |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/ai/AiPromptPolicy.kt` | 106 | AiPromptValidation, AiPromptPolicy |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/ai/AiResponseSafety.kt` | 41 | AiResponseSafety |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/ai/AiWellnessContext.kt` | 79 | AiWellnessContext, AiWellnessContextBuilder |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/ai/GeminiHelper.kt` | 175 | GeminiHelper, AiCoachFailureReason, AiCoachException |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/analytics/FirebaseProductAnalytics.kt` | 109 | FirebaseProductAnalytics, FirebaseAnalyticsBridge |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/calculator/BMRCalculator.kt` | 168 | BMRCalculator |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/calculator/BMRValidation.kt` | 127 | BMRValidation, ValidationResult, ReferenceTest |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/calculator/IdealWeightCalculator.kt` | 117 | IdealWeightCalculator |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/calculator/MedicalCalculationPolicy.kt` | 78 | MedicalCalculationPolicy, BmiCalculation, BmiReferenceCategory, BmiCalculator |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/calculator/ReproductiveHealthPolicy.kt` | 18 | ReproductiveHealthPolicy |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/dao/UrineColorDao.kt` | 32 | UrineColorDao |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/dao/WaterGamificationDao.kt` | 37 | WaterGamificationDao |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/dao/WaterIntakeDao.kt` | 66 | WaterIntakeDao |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/datastore/ProfileDataStore.kt` | 138 | ProfileDataStore |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/datastore/SettingsDataStore.kt` | 215 | SettingsDataStore |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/export/AutoExportWorker.kt` | 58 | AutoExportWorker |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/export/BpExportManager.kt` | 722 | BpExportManager |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/export/CsvExportHelper.kt` | 113 | CsvExportHelper, CsvExportSchema |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/export/DataExportManager.kt` | 159 | DataExportManager |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/export/ExportDisclosurePolicy.kt` | 43 | ExportDisclosurePolicy |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/export/ExportModels.kt` | 63 | ExportFormat, ExportScope, ExportConfig, ExportProgress, ExportSchedule, ExportFrequency, HealthReportCard |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/export/ExportScheduleManager.kt` | 111 | ExportScheduleManager, Keys |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/export/JsonExportHelper.kt` | 168 | JsonExportHelper |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/export/PdfExportHelper.kt` | 349 | PdfExportHelper |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/export/ShareImageHelper.kt` | 211 | ShareImageHelper |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/healthconnect/HealthConnectManager.kt` | 166 | HealthConnectManager, HealthConnectWeight, HealthConnectStepsDay |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/healthconnect/HealthConnectPermissionPolicy.kt` | 32 | HealthConnectFeature, HealthConnectPermissionPolicy |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/AppDatabase.kt` | 140 | AppDatabase |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/DailyContentData.kt` | 57 | DailyContentData |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/HistoryDao.kt` | 90 | HistoryDao, CalculatorUsage |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/SearchPreferences.kt` | 102 | SearchPreferences, Keys |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/dao/BloodPressureDao.kt` | 55 | BloodPressureDao |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/dao/ChatDao.kt` | 20 | ChatDao |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/dao/FamilyProfileDao.kt` | 46 | FamilyProfileDao |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/dao/FavoriteQuoteDao.kt` | 28 | FavoriteQuoteDao |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/dao/MilestonesDao.kt` | 52 | MilestonesDao |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/dao/ReminderDao.kt` | 46 | ReminderDao |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/dao/StepHistoryDao.kt` | 28 | StepHistoryDao |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/dao/WeeklyReportDao.kt` | 44 | WeeklyReportDao |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/dao/WeightDao.kt` | 49 | WeightDao |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/datastore/UserPreferences.kt` | 126 | UserPreferences |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/entity/BloodPressureEntity.kt` | 30 | BloodPressureEntity |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/entity/ChatMessageEntity.kt` | 14 | ChatMessageEntity |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/local/entity/FavoriteQuoteEntity.kt` | 14 | FavoriteQuoteEntity |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/management/DataCleanupManager.kt` | 125 | DataCleanupManager |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/management/DataIntegrityChecker.kt` | 125 | DataIntegrityChecker |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/management/FullAppDataResetter.kt` | 22 | FullAppDataResetter |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/management/StorageAnalyzer.kt` | 97 | StorageAnalyzer |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/management/StorageModels.kt` | 91 | StorageInfo, IntegrityReport, CleanupAge, CleanupPreview, UndoableDelete, DataManagementState |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/manager/BpStreakManager.kt` | 136 | StreakInfo, BpStreakManager |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/BMIGoalData.kt` | 101 | BMIGoalData |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/BMIHealthRiskData.kt` | 136 | HealthRiskInfo, HealthRiskItem, RecommendationItem, RiskLevel, RiskSeverity, BMIHealthRiskProvider, BmiContext |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/BMIValidationState.kt` | 26 | BMIValidationState |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/BMRAgeCurveData.kt` | 81 | BMRAgeCurveData, AgeBMRPoint |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/BMREducationalContent.kt` | 209 | EducationalSection, EducationalParagraph, BMREducationalContent |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/BMRFormula.kt` | 91 | BMRFormula, BMRInputState, BMRValidationState |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/BMRResultData.kt` | 123 | BMRResultData, BMRLevel |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/BMRTrendData.kt` | 97 | BMRHistoryPoint, BMRTrendStats, BMRChangeInsight |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/BSATrackingData.kt` | 25 | BSARecord, BSAStatistics |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/BloodPressureModels.kt` | 291 | BpArm, BpPosition, BpTimeOfDay, BloodPressureReference, BpCategory, BpRiskLevel, BloodPressureReading, BloodPressureCalculator |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/BpAdvancedMetricsData.kt` | 275 | PulsePressureAnalysis, PpCategory, MapAnalysis, MapCategory, HeartRateAnalysis, HrCategory, BpAdvancedMetrics |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/BpEducationalData.kt` | 533 | BpEducationalSection, BpEducationalItem, Paragraph, Heading, BulletPoint, NumberedStep, HighlightBox, ComparisonRow, MythBuster, Analogy, DividerItem, HighlightType, BpEducationalContent |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/BpHomeCardData.kt` | 12 | BpHomeCardInfo |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/BpRecommendationsData.kt` | 118 | BpRecommendation, BpCategoryGuidance, BpGuidanceTone, BpRecommendationsProvider, Quad |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/CalorieHistoryEntry.kt` | 19 | CalorieHistoryEntry |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/CalorieHistoryStats.kt` | 69 | CalorieHistoryStats, DayCalorieStatus, CalorieAdherenceStatus, WeeklyCalorieSummary |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/CalorieResult.kt` | 47 | CalorieResult |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/DailyContent.kt` | 41 | HealthTip, DailyTipCategory, MotivationalQuote, QuoteCategory, DailyContent |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/EthnicityRegion.kt` | 23 | EthnicityRegion |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/FamilyProfile.kt` | 137 | FamilyProfile, ProfileColor |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/FoodEntry.kt` | 51 | FoodEntry, FoodPreset, DailyFoodLog |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/FrameSize.kt` | 8 | FrameSize |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/HealthConnection.kt` | 173 | HealthConnection, CalculatorDataLink, HealthConnectionMap, HealthConnectionsRegistry |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/HealthOverview.kt` | 31 | HealthOverview, HealthMetricSummary, HealthCategoryColor |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/HeartRateHistoryEntry.kt` | 61 | HeartRateHistoryEntry |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/HistoryDisplayModels.kt` | 153 | CalculatorType, DateGroup, HistoryDisplayEntry, CategoryColor, GroupedHistoryEntries, HistorySortOption, HistoryFilter |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/HistoryEntry.kt` | 46 | HistoryEntry, CalculatorLastCalc |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/HistoryExtensions.kt` | 169 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/HydrationPlant.kt` | 33 | PlantStage, PlantMood, PlantState |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/HydrationTools.kt` | 62 | UrineColor, HydrationLevel, UrineColorEntry, DehydrationSymptom, SymptomSeverity, WaterRichFood, DehydrationRisk |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/IBWAdditionalMetrics.kt` | 26 | IBWAdditionalMetrics |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/IBWGoal.kt` | 47 | IBWGoal, WeightPaceOption |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/IBWHistoryEntry.kt` | 25 | IBWHistoryEntry |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/IBWResult.kt` | 66 | IBWResult |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/MacroData.kt` | 124 | MacroBreakdown, DietApproach, MacroColors |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/MacroResult.kt` | 45 | MacroResult, DietPreset |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/MealPlan.kt` | 55 | MealPlan, MealSlot, MealIdea, IntermittentFastingPlan, WorkoutNutrition, MealDistributionPreset |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/MealTimingData.kt` | 245 | MealTimingConfig, MealPortion, TimedMealSlot, MealType, EatingPattern |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/MetabolicSyndromeTrackingData.kt` | 67 | MetabolicSyndromeRecord, CriterionTrend, MetabolicTrendDirection, AssessmentComparison |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/ParsedHistoryEntry.kt` | 19 | ParsedHistoryEntry |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/ProfileData.kt` | 249 | ProfileData, Gender, HeightUnit, WeightUnit, ActivityLevel, HealthGoal |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/ProfilePicture.kt` | 9 | ProfilePicture |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/ProfileShareConfig.kt` | 77 | ProfileShareConfig |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/SearchResult.kt` | 35 | SearchResult, SearchResultType, QuickAction, SearchEntry |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/SettingsData.kt` | 76 | SettingsData, UnitSystem, ThemeMode |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/StatisticsModels.kt` | 83 | OverallStatistics, CalculatorUsageStat, TrendDirection, HealthTrendSummary, HeatmapMonth, HeatmapDay, PeriodReport |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/StepHistoryEntry.kt` | 24 | StepHistoryEntry |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/TDEEData.kt` | 107 | TDEEData, CalorieGoal, GoalType |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/TEFData.kt` | 128 | TEFData, EnergyComponent |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/WaterGamification.kt` | 118 | BadgeType, BadgeTier, EarnedBadge, WaterStreakData, HydrationScore, ScoreBreakdownItem |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/WaterIntakeData.kt` | 78 | WaterActivityLevel, ClimateType, HealthStatus, WaterIntakeCalculator, WaterIntakeCalculation, WaterIntakeLog |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/WaterReminderSettings.kt` | 54 | WaterReminderSettings, ReminderFrequency |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/WeightEntry.kt` | 35 | WeightEntry, WeightSource |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/WeightGoalProgress.kt` | 31 | WeightGoalProgress |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/WeightStatistics.kt` | 21 | WeightStatistics, WeightTrendDirection |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/WhrData.kt` | 58 | WhrInput, WhrMeasurementGuide, WhrGuideData |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/WhrHistoryEntry.kt` | 69 | WhrHistoryEntry, WhrGoal, WhrProgressStats, WhrComparison, WhrTrendDirection, WhrTimeRange |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/model/WhrResult.kt` | 156 | WhrResult, WhrCategory, WaistRiskLevel, BodyShape, WhrCalculator |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/models/HealthJourneySummary.kt` | 21 | HealthJourneySummary |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/models/HealthMilestone.kt` | 187 | HealthMilestone, MilestoneType, MilestoneCategory |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/models/InactivityState.kt` | 107 | InactivityState, WelcomeBackData, LastKnownMetric, FrequentCalculator, StreakStatus, PlantWelcomeStatus, InactivityLevel |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/models/PersonalRecord.kt` | 93 | PersonalRecord, PersonalRecordType |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/models/QuietHours.kt` | 32 | QuietHours |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/models/Reminder.kt` | 121 | Reminder, ReminderCategory |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/models/WeeklyReport.kt` | 112 | WeeklyReport, WeeklyReportSummary, MetricWeeklySummary, MetricTrend, WeeklyHighlight, HighlightType, NextWeekGoal |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/preferences/BMIGoalPreferences.kt` | 64 | BMIGoalPreferences |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/preferences/BMIInputMemoryPreferences.kt` | 87 | BMILastUsedInput, BMIInputMemoryPreferences |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/preferences/BMRLastValuePreferences.kt` | 55 | BMRLastValue, BMRLastValuePreferences |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/preferences/BpReminderPreferences.kt` | 141 | BpReminderSettings, BpReminderPreferences |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/preferences/PlantPreferences.kt` | 37 | PlantPreferences |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/preferences/RecommendationPreferences.kt` | 92 | RecommendationPreferencesManager |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/preferences/ReminderSchedulePolicy.kt` | 107 | ReminderSchedulePolicy |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/preferences/WaterReminderPreferences.kt` | 76 | WaterReminderPreferences |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/preferences/WeightReminderPreferences.kt` | 57 | WeightReminderSettings, WeightReminderPreferences |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/provider/BpHomeCardProvider.kt` | 108 | BpHomeCardProvider |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/BloodPressureRepository.kt` | 141 | BloodPressureRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/CalorieHistoryRepository.kt` | 86 | CalorieHistoryRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/DailyContentRepository.kt` | 62 | DailyContentRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/FamilyProfileRepository.kt` | 131 | FamilyProfileRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/FoodLogRepository.kt` | 385 | FoodLogRepository, FoodLogDayPolicy |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/HealthOverviewRepository.kt` | 99 | HealthOverviewRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/HistoryRepository.kt` | 170 | HistoryRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/IBWGoalRepository.kt` | 89 | IBWGoalRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/IBWHistoryRepository.kt` | 166 | IBWHistoryRepository, IBWStatistics |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/InactivityRepository.kt` | 159 | InactivityRepository, Keys |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/MilestonesRepository.kt` | 226 | MilestonesRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/ProfileRepository.kt` | 84 | ProfileRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/QuickActionRepository.kt` | 67 | QuickActionRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/ReminderRepository.kt` | 117 | ReminderRepository, Keys |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/SearchRepository.kt` | 73 | SearchRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/SettingsRepository.kt` | 57 | SettingsRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/StatisticsRepository.kt` | 237 | StatisticsRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/StepHistoryRepository.kt` | 48 | StepHistoryRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/WaterGamificationRepository.kt` | 37 | WaterGamificationRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/WaterIntakeRepository.kt` | 131 | WaterIntakeRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/WeightRepository.kt` | 194 | WeightRepository, WeightTimeFilter |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/repository/WhrRepository.kt` | 152 | WhrRepository |
| `app/src/main/java/com/health/calculator/bmi/tracker/data/util/WaterDataIntegrity.kt` | 255 | WaterDataIntegrity |
| `app/src/main/java/com/health/calculator/bmi/tracker/di/AnalyticsModule.kt` | 29 | AnalyticsModule, AnalyticsEntryPoint |
| `app/src/main/java/com/health/calculator/bmi/tracker/di/DataStoreModule.kt` | 29 | DataStoreModule |
| `app/src/main/java/com/health/calculator/bmi/tracker/di/DatabaseModule.kt` | 61 | DatabaseModule |
| `app/src/main/java/com/health/calculator/bmi/tracker/di/UseCaseModule.kt` | 23 | UseCaseModule |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/WaterGamificationEngine.kt` | 258 | WaterGamificationEngine |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/analytics/ProductAnalytics.kt` | 196 | ProductAnalyticsEvent, ProductAnalytics, NoOpProductAnalytics, ProductAnalyticsPolicy |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/engagement/WellnessEngagementPolicy.kt` | 41 | WellnessEngagementPolicy |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/growth/StoreListingPolicy.kt` | 48 | StoreListingPolicy |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/insights/DeterministicInsightEngine.kt` | 246 | InsightMetric, DeterministicInsightInput, WellnessInsight, DeterministicInsightEngine |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/model/UserProfile.kt` | 49 | UserProfile, Gender |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/monetization/PremiumFeaturePolicy.kt` | 72 | SubscriptionTier, PremiumFeature, PremiumFeaturePolicy |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/tracking/StepTrendPolicy.kt` | 26 | StepTrendPolicy |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/tracking/TrackingQualityPolicy.kt` | 83 | TrackingQualityPolicy, TrackingTrend, TrackingComparison |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/usecase/AdjustedWeightUseCase.kt` | 133 | AdjustedWeightMetrics, AdjustedWeightUseCase, SportWeightNote |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/usecase/CalorieCalculatorUseCase.kt` | 100 | CalorieCalculatorUseCase |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/usecase/CalorieHistoryAnalyticsUseCase.kt` | 187 | CalorieHistoryAnalyticsUseCase |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/usecase/IBWCalculatorUseCase.kt` | 114 | IBWCalculatorUseCase |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/usecase/MacroCalculatorUseCase.kt` | 224 | MacroCalculatorUseCase |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/usecase/MealPlanningUseCase.kt` | 365 | MealPlanningUseCase |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/usecase/WeightGoalPlannerUseCase.kt` | 82 | WeightGoalPlannerUseCase |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/usecases/MilestoneEvaluationUseCase.kt` | 154 | MilestoneEvaluationUseCase |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/usecases/ProfileCompletionUseCase.kt` | 55 | ProfileCompletionResult, ProfileCompletionUseCase |
| `app/src/main/java/com/health/calculator/bmi/tracker/domain/usecases/WeeklyReportGenerator.kt` | 563 | WeeklyReportGenerator, WeightWeekData, BpWeekData, WaterWeekData, CalorieWeekData |
| `app/src/main/java/com/health/calculator/bmi/tracker/notification/BpNotificationHelper.kt` | 257 | BpNotificationHelper, BpReminderReceiver, LocalReminderSchedulePolicy |
| `app/src/main/java/com/health/calculator/bmi/tracker/notification/WaterNotificationHelper.kt` | 139 | WaterNotificationHelper |
| `app/src/main/java/com/health/calculator/bmi/tracker/notification/WaterReminderScheduler.kt` | 83 | WaterReminderScheduler |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/AppUsageTracker.kt` | 38 | AppUsageTracker |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/BootReceiver.kt` | 141 | BootReceiver |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/EnhancedNotificationBuilder.kt` | 179 | EnhancedNotificationBuilder |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/InactivityChecker.kt` | 156 | InactivityCheckScheduler, InactivityCheckReceiver |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/NotificationChannelsManager.kt` | 164 | NotificationChannelsManager, ChannelConfig, ChannelSettingInfo |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/NotificationContextDataProvider.kt` | 168 | NotificationContextDataProvider |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/NotificationPermission.kt` | 19 | NotificationPermission |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/NotificationPermissionHelper.kt` | 36 | NotificationPermissionHelper |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/NotificationRateLimiter.kt` | 182 | NotificationRateLimiter, NotificationRecord, RateLimitResult |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/NotificationStatistics.kt` | 70 | NotificationStatistics, StatsSummary |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/QuickActionReceiver.kt` | 60 | QuickActionReceiver |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/ReminderBroadcastReceiver.kt` | 94 | ReminderBroadcastReceiver |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/ReminderScheduler.kt` | 185 | ReminderScheduler |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/SmartNotificationContentBuilder.kt` | 251 | SmartNotificationContent, SmartNotificationContentBuilder, NotificationContextData |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/SnoozedReminderReceiver.kt` | 39 | SnoozedReminderReceiver |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/StreakFreezeReceiver.kt` | 46 | StreakFreezeReceiver |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/StreakProtectionChecker.kt` | 249 | StreakProtectionScheduler, StreakProtectionReceiver |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/WeeklyReportScheduler.kt` | 161 | WeeklyReportScheduler, WeeklyReportReceiver |
| `app/src/main/java/com/health/calculator/bmi/tracker/notifications/WeightReminderManager.kt` | 169 | WeightReminderReceiver, WeightReminderManager |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/components/BottomNavigationBar.kt` | 123 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/components/HealthSnackbar.kt` | 67 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/components/HealthTopAppBar.kt` | 155 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/components/MedicalDisclaimer.kt` | 133 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/home/DailyContentViewModel.kt` | 42 | DailyContentViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/home/HomeDashboardPolicy.kt` | 11 | HomeDashboardPolicy |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/home/HomeScreen.kt` | 946 | LatestMetric, HomeAction |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/home/HomeSearchViewModel.kt` | 108 | HomeSearchViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/home/HomeViewModel.kt` | 407 | HomeUiState, HomeViewModel, StepsSnapshot |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/home/components/CalculatorCard.kt` | 474 | CalculatorCardData |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/home/components/DashboardHeader.kt` | 336 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/home/components/QuickActionCard.kt` | 195 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/home/components/SectionHeader.kt` | 79 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/navigation/CalculatorQualityCatalog.kt` | 138 | CalculatorQualityInfo, CalculatorQualityCatalog |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/navigation/HubScreens.kt` | 314 | CalculatorDestination, CalculatorEntry |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/profile/HealthConnectionsScreen.kt` | 503 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/profile/HealthOverviewSection.kt` | 186 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/profile/MultiProfileViewModel.kt` | 257 | MultiProfileUiState, MultiProfileViewModel, Factory |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/profile/MyInfoSection.kt` | 251 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/profile/ProfileScreen.kt` | 522 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/profile/ProfileViewModel.kt` | 398 | ProfileUiState, ProfileTab, HeightUnit, WeightUnit, ProfileViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/settings/SettingsScreen.kt` | 1332 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/settings/SettingsViewModel.kt` | 451 | SettingsUiState, SettingsViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/weight/WeightTrackingScreen.kt` | 333 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/presentation/weight/WeightTrackingViewModel.kt` | 253 | WeightTrackingUiState, WeightTrackingViewModel, Factory |
| `app/src/main/java/com/health/calculator/bmi/tracker/receiver/WaterQuickLogReceiver.kt` | 54 | WaterQuickLogReceiver |
| `app/src/main/java/com/health/calculator/bmi/tracker/receiver/WaterReminderReceiver.kt` | 112 | WaterReminderReceiver |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/BSAHomeCard.kt` | 54 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/CalculatorQualityDisclosure.kt` | 135 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/CalorieCrossLinks.kt` | 127 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/CalorieSafetyWarningCard.kt` | 84 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/CategoryPickerDialog.kt` | 110 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/CelebrationDialogs.kt` | 267 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/EmptyRemindersState.kt` | 59 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/HealthJourneySummaryCard.kt` | 214 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/HealthOverviewCard.kt` | 138 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/HeartRateModels.kt` | 56 | HeartRateFormula, FitnessLevel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/InactivityNotificationSettings.kt` | 141 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/InfoCard.kt` | 56 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/LogWeightDialog.kt` | 170 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/MetabolicSyndromeHomeCard.kt` | 58 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/MilestonesTimeline.kt` | 303 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/MultiProfileDialogs.kt` | 306 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/NotificationChannelSettings.kt` | 198 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/PermissionBanner.kt` | 54 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/PersonalRecordsCard.kt` | 205 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/ProfileCompletionBar.kt` | 135 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/ProfileMilestonesPreview.kt` | 305 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/ProfilePictureSection.kt` | 134 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/ProfileSwitcher.kt` | 287 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/ProfileWeightSection.kt` | 151 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/QuietHoursDialog.kt` | 168 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/QuietHoursIndicator.kt` | 49 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/ReminderCard.kt` | 144 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/ReminderEditDialog.kt` | 205 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/ReminderIcons.kt` | 30 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/SelectionDialogs.kt` | 242 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/TimePickerDialog.kt` | 68 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/WeightGoalProgressCard.kt` | 225 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/WeightStatisticsCard.kt` | 219 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/WeightTrendGraph.kt` | 330 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/WellnessDesignSystem.kt` | 469 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/history/BatchOperationsBar.kt` | 110 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/history/CalculatorTypeIcon.kt` | 35 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/history/ExportBottomSheet.kt` | 254 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/history/ExportScheduleDialog.kt` | 131 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/history/HistoryDateHeader.kt` | 74 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/history/HistoryEmptyState.kt` | 99 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/history/HistoryEntryCard.kt` | 334 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/history/HistoryFilterSheet.kt` | 458 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/history/HistoryList.kt` | 57 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/history/NoteEditDialog.kt` | 115 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/home/CalculatorCardsData.kt` | 144 | CalculatorCardsState, CalculatorInfo |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/home/CalculatorCardsGrid.kt` | 134 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/home/DailyContentSection.kt` | 162 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/home/DynamicCalculatorCards.kt` | 923 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/home/HealthOverviewSection.kt` | 591 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/home/HealthScoreBreakdownSheet.kt` | 296 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/home/HomeSearchBar.kt` | 234 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/home/PersonalizedGreeting.kt` | 78 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/home/QuickActionsRow.kt` | 93 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/components/home/RecommendationsSection.kt` | 461 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/dialogs/QuickFoodLogDialog.kt` | 344 | QuickFoodPreset |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/aicoach/AiCoachScreen.kt` | 382 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/aicoach/AiCoachViewModel.kt` | 320 | ChatMessage, AiCoachViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/aicoach/AiConversationTurnPolicy.kt` | 36 | AiConversationTurnPolicy |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/articles/HealthArticlesScreen.kt` | 52 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BloodPressureResultComponents.kt` | 940 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BloodPressureScreen.kt` | 1383 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BloodPressureViewModel.kt` | 679 | BloodPressureUiState, BloodPressureViewModel, Factory |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BpAdvancedMetricsComponents.kt` | 699 | ScaleZone |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BpEducationalScreen.kt` | 733 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BpExportScreen.kt` | 394 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BpExportViewModel.kt` | 183 | BpExportUiState, BpExportViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BpLogScreen.kt` | 940 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BpLogViewModel.kt` | 155 | BpLogDetailState, BpLogUiState, BpLogViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BpPolishComponents.kt` | 499 | QuickLogSuggestion |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BpRecommendationsScreen.kt` | 745 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BpReminderScreen.kt` | 362 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BpReminderViewModel.kt` | 150 | BpReminderUiState, BpReminderViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BpShareComponents.kt` | 185 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BpStreakAndMedicationComponents.kt` | 291 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BpTrendScreen.kt` | 1557 | BpTrendColors |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BpTrendViewModel.kt` | 352 | BpTimeRange, BpTrendDirection, BpDataPoint, BpDayData, BpCategoryDistribution, BpStatistics, BpTrendUiState, BpTrendViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bmr/BMRCalculatorScreen.kt` | 1071 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bmr/BMRShareFormatter.kt` | 111 | BMRShareFormatter |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bmr/BMRViewModel.kt` | 691 | BMRViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bmr/components/BMRActionButtons.kt` | 58 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bmr/components/BMRAgeCurveSection.kt` | 512 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bmr/components/BMRBodyFatInput.kt` | 269 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bmr/components/BMREdgeCaseWarning.kt` | 86 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bmr/components/BMREducationalSection.kt` | 585 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bmr/components/BMRFormulaSelector.kt` | 303 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bmr/components/BMRResultSection.kt` | 859 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bmr/components/BMRTrendSection.kt` | 830 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bmr/components/MacroSection.kt` | 1175 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bmr/components/MealTimingSection.kt` | 1001 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bmr/components/TDEESection.kt` | 1090 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bmr/components/TEFSection.kt` | 1160 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bsa/BSAAnimations.kt` | 72 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bsa/BSAEducationContent.kt` | 1139 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bsa/BSAMedicalApplications.kt` | 1230 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bsa/BSAResultScreen.kt` | 842 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bsa/BSAScreen.kt` | 726 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/bsa/BSATrackingUI.kt` | 825 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/BmiAdditionalMetrics.kt` | 860 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/BmiCalculatorScreen.kt` | 976 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/BmiComparisonSection.kt` | 770 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/BmiEducationContent.kt` | 1063 | EducationSection, CategoryRow, LimitationItem, TipItem, DoctorTrigger |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/BmiGauge.kt` | 317 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/BmiResultSection.kt` | 701 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/BmiTrendSection.kt` | 884 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/BmiUiTokens.kt` | 48 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/BmiViewModel.kt` | 1507 | BmiCategory, BmiResult, WeightAdvice, BmiPrimeResult, BmiPrimeStatus, PonderalIndexResult, PonderalStatus, AsianBmiCategory, AdditionalBmiMetrics, BmiTrendPoint, BmiTrendStats, BmiTrendData, AgeGroupType, PediatricBmiCategory, PopulationAverage, BmiComparisonData, InputFieldState, BmiInputUiState, BmiViewModel, Factory |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/components/BMIAnimatedComponents.kt` | 454 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/components/BMIEdgeCaseHandler.kt` | 332 | EdgeCaseMessage, EdgeCaseSeverity, BMIEdgeCaseHandler, ValidationResult |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/components/BMIGoalSection.kt` | 1067 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/components/BMIHealthRiskSection.kt` | 663 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/components/BMIQuickCheck.kt` | 273 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/components/BMISliderInputSection.kt` | 65 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/components/BMISliderPickers.kt` | 657 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/idealweight/IdealWeightCalculatorScreen.kt` | 210 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/idealweight/IdealWeightEducationContent.kt` | 137 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/idealweight/IdealWeightInputSection.kt` | 241 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/idealweight/IdealWeightResultSection.kt` | 211 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calculators/idealweight/IdealWeightViewModel.kt` | 380 | IdealWeightInputState, IdealWeightValidationState, IdealWeightResultData, IdealWeightViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calorie/CalorieEducationalContent.kt` | 842 | FoodCategoryRef |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calorie/CalorieHistoryScreen.kt` | 1052 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calorie/CalorieInputScreen.kt` | 729 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calorie/CalorieResultScreen.kt` | 978 | CalorieVisualColors |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calorie/CalorieScreen.kt` | 177 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calorie/CalorieViewModel.kt` | 679 | ActivityLevelOption, GoalOption, CalorieUiState, CalorieViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calorie/FoodLogScreen.kt` | 1030 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calorie/FoodLogViewModel.kt` | 214 | FoodLogUiState, FoodLogViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calorie/MacroCalculatorSection.kt` | 843 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/calorie/MealPlanningSection.kt` | 1001 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/common/PlaceholderScreen.kt` | 68 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/export/ExportDataScreen.kt` | 69 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/heartrate/ActivityReferenceSection.kt` | 675 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/heartrate/HeartRateDashboardCard.kt` | 187 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/heartrate/HeartRateEducationalContent.kt` | 1395 | MonitorMethod |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/heartrate/HeartRatePolishComponents.kt` | 888 | EdgeWarning, WarningSeverity |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/heartrate/HeartRateZoneResultScreen.kt` | 1072 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/heartrate/HeartRateZoneScreen.kt` | 1162 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/heartrate/RestingHRGuideSheet.kt` | 311 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/heartrate/VO2MaxSection.kt` | 1041 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/heartrate/WHOGuidelinesSection.kt` | 1154 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/heartrate/ZoneRecommendationsSection.kt` | 816 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/history/HistoryScreen.kt` | 368 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/history/HistoryViewModel.kt` | 411 | HistoryUiState, HistoryDialogState, None, EditNote, ConfirmDelete, ClearAll, HistoryViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/history/StatisticsScreen.kt` | 411 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/history/StatisticsViewModel.kt` | 69 | StatisticsUiState, ReportPeriod, StatisticsViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/home/BpHomeCardWidget.kt` | 132 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/home/WhrHomeCard.kt` | 126 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/home/components/WaterHomeCard.kt` | 365 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/ibw/IBWAdditionalMetricsSection.kt` | 885 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/ibw/IBWCrossCalculatorLinks.kt` | 141 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/ibw/IBWEducationalContent.kt` | 657 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/ibw/IBWGoalPlanScreen.kt` | 885 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/ibw/IBWHistorySection.kt` | 628 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/ibw/IBWHomeSummary.kt` | 85 | IBWHomeSummaryData |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/ibw/IBWMotivationalComparison.kt` | 412 | MotivationalContent |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/ibw/IBWResultScreen.kt` | 564 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/ibw/IBWScreen.kt` | 545 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/ibw/IBWViewModel.kt` | 428 | IBWUiState, IBWViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/metabolicsyndrome/MetabolicSyndromeAnimations.kt` | 94 | CriterionAnimState |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/metabolicsyndrome/MetabolicSyndromeCrossLinks.kt` | 184 | CrossLink |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/metabolicsyndrome/MetabolicSyndromeEducationContent.kt` | 1175 | StatItem, RetestItem |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/metabolicsyndrome/MetabolicSyndromeRecommendationsUI.kt` | 583 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/metabolicsyndrome/MetabolicSyndromeResultScreen.kt` | 420 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/metabolicsyndrome/MetabolicSyndromeScreen.kt` | 768 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/metabolicsyndrome/MetabolicSyndromeStandardsComparisonUI.kt` | 896 | StandardContext |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/metabolicsyndrome/MetabolicSyndromeTrackingScreen.kt` | 1001 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/metabolicsyndrome/MetabolicSyndromeViewModel.kt` | 569 | MetabolicSyndromeUiState, MetabolicSyndromeViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/onboarding/OnboardingScreen.kt` | 369 | OnboardingPage, OnboardingStartAction |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/profile/milestones/MilestonesScreen.kt` | 102 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/profile/milestones/MilestonesViewModel.kt` | 190 | MilestonesUiState, MilestonesViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/reminders/ReminderScreenComponents.kt` | 137 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/reminders/RemindersScreen.kt` | 325 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/reminders/RemindersViewModel.kt` | 329 | RemindersUiState, RemindersViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/reports/ReportScheduleDialog.kt` | 121 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/reports/ReportShareDialog.kt` | 89 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/reports/WeeklyReportScreen.kt` | 534 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/reports/WeeklyReportViewModel.kt` | 252 | WeeklyReportUiState, WeeklyReportViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/settings/DataManagementScreen.kt` | 901 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/settings/DataManagementViewModel.kt` | 243 | DataManagementViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/splash/SplashScreen.kt` | 334 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/ElectrolyteInfoScreen.kt` | 1508 | ElectrolyteData, SituationData, FoodItem |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/HydrationToolsScreen.kt` | 1592 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/HydrationToolsViewModel.kt` | 123 | HydrationToolsViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/HydrationToolsViewModelFactory.kt` | 9 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterEducationScreen.kt` | 1253 | BenefitItem |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterGamificationScreen.kt` | 931 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterGamificationViewModel.kt` | 105 | WaterGamificationViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterGamificationViewModelFactory.kt` | 9 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterHistoryScreen.kt` | 1223 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterHistoryViewModel.kt` | 358 | DailyWaterData, SelectedDayInfo, WaterStats, WeeklyReport, TrendDirection, WaterHistoryViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterHistoryViewModelFactory.kt` | 9 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterIntakeInputScreen.kt` | 833 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterIntakeResultScreen.kt` | 1438 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterIntakeViewModel.kt` | 252 | WaterIntakeViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterIntakeViewModelFactory.kt` | 9 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterReminderSettingsScreen.kt` | 879 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterTrackingScreen.kt` | 1488 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterTrackingViewModel.kt` | 292 | WaterTrackingViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterTrackingViewModelFactory.kt` | 9 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/components/HydrationPlantComposable.kt` | 793 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/components/WaterCelebration.kt` | 309 | ConfettiParticle |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/welcomeback/WelcomeBackScreen.kt` | 426 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/welcomeback/WelcomeBackViewModel.kt` | 231 | WelcomeBackUiState, WelcomeBackViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/whr/WhrAnimations.kt` | 299 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/whr/WhrEdgeCaseHandler.kt` | 101 | WhrEdgeCaseHandler, ValidationResult |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/whr/WhrEducationalScreen.kt` | 1670 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/whr/WhrHistoryDisplay.kt` | 391 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/whr/WhrInputScreen.kt` | 1069 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/whr/WhrProgressScreen.kt` | 1204 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/whr/WhrProgressViewModel.kt` | 140 | WhrProgressState, WhrGraphLine, WhrProgressViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/whr/WhrResultScreen.kt` | 1378 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/whr/WhrResultViewModel.kt` | 112 | WhrResultViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/whr/WhrShareUtils.kt` | 78 | WhrShareUtils |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/whr/WhrViewModel.kt` | 246 | WhrInputState, WhrViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/theme/Color.kt` | 259 | FeatureColors, WellnessPalette, HealthColors, ChartColors, CalculatorColors, WellnessIllustrationColors |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/theme/Shape.kt` | 84 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/theme/Spacing.kt` | 35 | HealthSpacing, HealthElevation |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/theme/Theme.kt` | 173 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/theme/Type.kt` | 156 |  |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/utils/AnimationUtils.kt` | 234 | ShakeController, ShakeState |
| `app/src/main/java/com/health/calculator/bmi/tracker/ui/utils/HapticUtils.kt` | 142 | HapticManager |
| `app/src/main/java/com/health/calculator/bmi/tracker/util/ActivityHeartRateReference.kt` | 419 | ActivityReference, ActivityCategory, PersonalizedActivity, ActivityHeartRateReferenceEngine |
| `app/src/main/java/com/health/calculator/bmi/tracker/util/CalorieEdgeCaseHandler.kt` | 138 | CalorieEdgeCaseHandler, CalorieValidationResult, WarningSeverity |
| `app/src/main/java/com/health/calculator/bmi/tracker/util/HealthScoreCalculator.kt` | 209 | HealthScoreResult, HealthScoreComponent, ComponentStatus, HealthScoreCategory, HealthMetricsSnapshot, QuickStat, LastActivity, HealthScoreCalculator, WellnessScoreCalculator |
| `app/src/main/java/com/health/calculator/bmi/tracker/util/HeartRateZoneCalculator.kt` | 223 | HeartRateZone, HeartRateZoneResult, HeartRateZoneCalculator, ZoneRange |
| `app/src/main/java/com/health/calculator/bmi/tracker/util/HeartRateZoneRecommendations.kt` | 283 | ZoneDistribution, WorkoutRecommendation, ZoneCalorieBurn, HeartRateZoneRecommendationEngine |
| `app/src/main/java/com/health/calculator/bmi/tracker/util/SmartRecommendationEngine.kt` | 423 | SmartRecommendation, RecommendationPriority, RecommendationType, UserHealthContext, SmartRecommendationEngine |
| `app/src/main/java/com/health/calculator/bmi/tracker/util/VO2MaxCalculator.kt` | 256 | VO2MaxResult, VO2Classification, RecoveryHRGuideline, VO2MaxCalculator |
| `app/src/main/java/com/health/calculator/bmi/tracker/util/WHOExerciseGuidelines.kt` | 532 | WHOGuideline, WeeklyPlanDay, WeeklyPlan, ExerciseLogEntry, ExerciseIntensity, WHOExerciseGuidelinesEngine |
| `app/src/main/java/com/health/calculator/bmi/tracker/util/WaterShareHelper.kt` | 122 | WaterShareHelper |
| `app/src/main/java/com/health/calculator/bmi/tracker/viewmodel/BSAViewModel.kt` | 315 | BSAUiState, BSAViewModel |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/BootReceiver.kt` | 21 | BootReceiver |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/CalculatorResultSyncManager.kt` | 58 | CalculatorResultSyncManager |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/CalculatorType.kt` | 65 | CalculatorType |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/HealthSummaryLargeWidget.kt` | 36 | HealthSummaryLargeWidget |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/HealthSummaryMediumWidget.kt` | 36 | HealthSummaryMediumWidget |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/HealthWidgetSyncManager.kt` | 103 | HealthWidgetSyncManager |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/QuickCalculateWidget.kt` | 155 | QuickCalculateWidget |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/SingleCalculatorWidget.kt` | 111 | SingleCalculatorWidget |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/SingleWidgetConfigActivity.kt` | 95 | SingleWidgetConfigActivity |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/StreakWidget.kt` | 138 | StreakWidget |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/WaterIntakeMediumWidget.kt` | 41 | WaterIntakeMediumWidget |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/WaterIntakeSmallWidget.kt` | 41 | WaterIntakeSmallWidget |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/WaterIntakeWidget.kt` | 350 | WaterIntakeWidget |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/WaterWidgetActionReceiver.kt` | 32 | WaterWidgetActionReceiver |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/WaterWidgetActions.kt` | 15 | WaterWidgetActions |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/WaterWidgetDataProvider.kt` | 170 | WaterWidgetDataProvider, WaterWidgetData |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/WaterWidgetSyncManager.kt` | 59 | WaterWidgetSyncManager |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/WidgetConfigActivity.kt` | 159 | WidgetConfigActivity |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/WidgetDataNotifier.kt` | 44 | WidgetDataNotifier |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/WidgetPreferencesManager.kt` | 191 | WidgetPreferencesManager |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/WidgetThemeHelper.kt` | 80 | WidgetTheme, WidgetThemeHelper, ThemeColors |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/core/PolishedWidgetUpdater.kt` | 309 | PolishedWidgetUpdater |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/core/WidgetAccessibilityHelper.kt` | 172 | WidgetAccessibilityHelper |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/core/WidgetBootReceiver.kt` | 49 | WidgetBootReceiver |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/core/WidgetDataChangeReceiver.kt` | 106 | WidgetDataChangeReceiver |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/core/WidgetErrorHandler.kt` | 91 | WidgetErrorHandler, ErrorConfig |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/core/WidgetMaterialYouHelper.kt` | 66 | WidgetMaterialYouHelper |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/core/WidgetPerformanceManager.kt` | 126 | WidgetPerformanceManager |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/core/WidgetStateManager.kt` | 135 | WidgetStateManager, WidgetState |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/core/WidgetThemeEngine.kt` | 244 | WidgetThemeEngine, WidgetColorScheme |
| `app/src/main/java/com/health/calculator/bmi/tracker/widget/core/WidgetUpdateScheduler.kt` | 226 | WidgetUpdateScheduler |
| `app/src/main/res/drawable/ai_coach_bg.jpg` | binary |  |
| `app/src/main/res/drawable/bmi_bg.jpg` | binary |  |
| `app/src/main/res/drawable/bp_bg.jpg` | binary |  |
| `app/src/main/res/drawable/config_btn_add.xml` | 7 |  |
| `app/src/main/res/drawable/config_item_bg.xml` | 7 |  |
| `app/src/main/res/drawable/config_preview_frame.xml` | 8 |  |
| `app/src/main/res/drawable/config_spinner_bg.xml` | 8 |  |
| `app/src/main/res/drawable/config_theme_card_dark.xml` | 8 |  |
| `app/src/main/res/drawable/config_theme_card_light.xml` | 8 |  |
| `app/src/main/res/drawable/config_theme_card_transparent.xml` | 8 |  |
| `app/src/main/res/drawable/config_theme_selected.xml` | 8 |  |
| `app/src/main/res/drawable/health_metric_card_bg.xml` | 10 |  |
| `app/src/main/res/drawable/ic_bmi.xml` | 11 |  |
| `app/src/main/res/drawable/ic_bp.xml` | 11 |  |
| `app/src/main/res/drawable/ic_calories.xml` | 11 |  |
| `app/src/main/res/drawable/ic_launcher_background.xml` | 75 |  |
| `app/src/main/res/drawable/ic_launcher_foreground.xml` | 16 |  |
| `app/src/main/res/drawable/ic_launcher_monochrome.xml` | 17 |  |
| `app/src/main/res/drawable/ic_notification.xml` | 10 |  |
| `app/src/main/res/drawable/ic_streak.xml` | 11 |  |
| `app/src/main/res/drawable/ic_water.xml` | 11 |  |
| `app/src/main/res/drawable/ic_water_drop_widget.xml` | 11 |  |
| `app/src/main/res/drawable/ic_water_widget_preview.xml` | 7 |  |
| `app/src/main/res/drawable/ic_weight.xml` | 11 |  |
| `app/src/main/res/drawable/ic_widget_arrow.xml` | 11 |  |
| `app/src/main/res/drawable/ic_widget_settings.xml` | 11 |  |
| `app/src/main/res/drawable/preview_widget_health_medium.xml` | 58 |  |
| `app/src/main/res/drawable/preview_widget_quick_calc.xml` | 46 |  |
| `app/src/main/res/drawable/preview_widget_single_calc.xml` | 37 |  |
| `app/src/main/res/drawable/preview_widget_streak.xml` | 29 |  |
| `app/src/main/res/drawable/preview_widget_water_small.xml` | 47 |  |
| `app/src/main/res/drawable/water_bg.jpg` | binary |  |
| `app/src/main/res/drawable/widget_background_large.xml` | 10 |  |
| `app/src/main/res/drawable/widget_background_medium.xml` | 10 |  |
| `app/src/main/res/drawable/widget_background_small.xml` | 10 |  |
| `app/src/main/res/drawable/widget_btn_blue.xml` | 7 |  |
| `app/src/main/res/drawable/widget_btn_blue_outline.xml` | 10 |  |
| `app/src/main/res/drawable/widget_circle_bg.xml` | 6 |  |
| `app/src/main/res/drawable/widget_circle_bg_large.xml` | 6 |  |
| `app/src/main/res/drawable/widget_quick_bg.xml` | 8 |  |
| `app/src/main/res/drawable/widget_quick_card.xml` | 8 |  |
| `app/src/main/res/drawable/widget_single_bg.xml` | 8 |  |
| `app/src/main/res/drawable/widget_single_icon_bg.xml` | 6 |  |
| `app/src/main/res/drawable/widget_streak_bg.xml` | 8 |  |
| `app/src/main/res/layout/activity_single_widget_config.xml` | 328 |  |
| `app/src/main/res/layout/activity_widget_config.xml` | 340 |  |
| `app/src/main/res/layout/layout_widget_nav_button.xml` | 45 |  |
| `app/src/main/res/layout/widget_health_large.xml` | 192 |  |
| `app/src/main/res/layout/widget_health_medium.xml` | 178 |  |
| `app/src/main/res/layout/widget_quick_calculate.xml` | 231 |  |
| `app/src/main/res/layout/widget_single_calculator.xml` | 80 |  |
| `app/src/main/res/layout/widget_streak.xml` | 90 |  |
| `app/src/main/res/layout/widget_water_medium.xml` | 169 |  |
| `app/src/main/res/layout/widget_water_small.xml` | 91 |  |
| `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml` | 6 |  |
| `app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml` | 6 |  |
| `app/src/main/res/mipmap-hdpi/ic_launcher.webp` | binary |  |
| `app/src/main/res/mipmap-hdpi/ic_launcher_foreground.webp` | binary |  |
| `app/src/main/res/mipmap-hdpi/ic_launcher_round.webp` | binary |  |
| `app/src/main/res/mipmap-mdpi/ic_launcher.webp` | binary |  |
| `app/src/main/res/mipmap-mdpi/ic_launcher_foreground.webp` | binary |  |
| `app/src/main/res/mipmap-mdpi/ic_launcher_round.webp` | binary |  |
| `app/src/main/res/mipmap-xhdpi/ic_launcher.webp` | binary |  |
| `app/src/main/res/mipmap-xhdpi/ic_launcher_foreground.webp` | binary |  |
| `app/src/main/res/mipmap-xhdpi/ic_launcher_round.webp` | binary |  |
| `app/src/main/res/mipmap-xxhdpi/ic_launcher.webp` | binary |  |
| `app/src/main/res/mipmap-xxhdpi/ic_launcher_foreground.webp` | binary |  |
| `app/src/main/res/mipmap-xxhdpi/ic_launcher_round.webp` | binary |  |
| `app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp` | binary |  |
| `app/src/main/res/mipmap-xxxhdpi/ic_launcher_foreground.webp` | binary |  |
| `app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp` | binary |  |
| `app/src/main/res/values-night/colors.xml` | 21 |  |
| `app/src/main/res/values-night/themes.xml` | 11 |  |
| `app/src/main/res/values/colors.xml` | 68 |  |
| `app/src/main/res/values/splash_theme.xml` | 15 |  |
| `app/src/main/res/values/strings.xml` | 1425 |  |
| `app/src/main/res/values/themes.xml` | 11 |  |
| `app/src/main/res/xml/file_paths.xml` | 16 |  |
| `app/src/main/res/xml/network_security_config.xml` | 9 |  |
| `app/src/main/res/xml/widget_health_large_info.xml` | 15 |  |
| `app/src/main/res/xml/widget_health_medium_info.xml` | 15 |  |
| `app/src/main/res/xml/widget_quick_calculate_info.xml` | 20 |  |
| `app/src/main/res/xml/widget_single_calculator_info.xml` | 20 |  |
| `app/src/main/res/xml/widget_streak_info.xml` | 19 |  |
| `app/src/main/res/xml/widget_water_medium_info.xml` | 19 |  |
| `app/src/main/res/xml/widget_water_small_info.xml` | 19 |  |
| `app/src/release/java/com/health/calculator/bmi/tracker/core/security/AppCheckInitializerImpl.kt` | 15 | AppCheckInitializerImpl |
| `app/src/test/java/com/health/calculator/bmi/tracker/ExampleUnitTest.kt` | 17 | ExampleUnitTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/ai/AiCoachSafetyTest.kt` | 34 | AiCoachSafetyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/ai/AiPromptPolicyTest.kt` | 61 | AiPromptPolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/ai/AiResponseSafetyTest.kt` | 36 | AiResponseSafetyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/ai/AiWellnessContextTest.kt` | 55 | AiWellnessContextTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/aicoach/AiConversationTurnPolicyTest.kt` | 57 | AiConversationTurnPolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/analytics/ProductAnalyticsPolicyTest.kt` | 86 | ProductAnalyticsPolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/calculator/BMIEdgeCaseHandlerTest.kt` | 31 | BMIEdgeCaseHandlerTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/calculator/BMIGoalDataTest.kt` | 37 | BMIGoalDataTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/calculator/BSACalculatorTest.kt` | 69 | BSACalculatorTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/calculator/BloodPressureBoundaryTest.kt` | 37 | BloodPressureBoundaryTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/calculator/HeartRateGuidanceSafetyTest.kt` | 31 | HeartRateGuidanceSafetyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/calculator/IBWCalculatorUseCaseTest.kt` | 43 | IBWCalculatorUseCaseTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/calculator/MedicalCalculatorAccuracyTest.kt` | 419 | MedicalCalculatorAccuracyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/calculator/WhrMeasurementPolicyTest.kt` | 72 | WhrMeasurementPolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/core/ConfigurationResilienceTest.kt` | 114 | ConfigurationResilienceTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/core/brand/BrandVoiceTest.kt` | 26 | BrandVoiceTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/core/navigation/AppEntryRouteResolverTest.kt` | 45 | AppEntryRouteResolverTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/engagement/WellnessEngagementPolicyTest.kt` | 36 | WellnessEngagementPolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/export/CsvExportSchemaTest.kt` | 53 | CsvExportSchemaTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/export/ExportDisclosurePolicyTest.kt` | 37 | ExportDisclosurePolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/export/ShareTextAccessibilityTest.kt` | 61 | ShareTextAccessibilityTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/growth/StoreListingPolicyTest.kt` | 32 | StoreListingPolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/healthconnect/HealthConnectEdgeCaseTest.kt` | 106 | HealthConnectEdgeCaseTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/healthconnect/HealthConnectScopeTest.kt` | 32 | HealthConnectScopeTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/history/HistoryDetailsParsingTest.kt` | 39 | HistoryDetailsParsingTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/insights/DeterministicInsightEngineTest.kt` | 102 | DeterministicInsightEngineTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/monetization/PremiumFeaturePolicyTest.kt` | 47 | PremiumFeaturePolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/navigation/CalculatorQualityCatalogTest.kt` | 39 | CalculatorQualityCatalogTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/navigation/NavigationStructureTest.kt` | 48 | NavigationStructureTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/notifications/LocalReminderSchedulePolicyTest.kt` | 40 | LocalReminderSchedulePolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/notifications/NotificationHelperTest.kt` | 69 | NotificationHelperTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/notifications/ReminderLifecycleTest.kt` | 100 | ReminderLifecycleTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/notifications/ReminderSchedulePolicyTest.kt` | 112 | ReminderSchedulePolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/presentation/home/HomeDashboardPolicyTest.kt` | 30 | HomeDashboardPolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/privacy/AppLinksTest.kt` | 15 | AppLinksTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/tracking/CalorieHistorySelectionTest.kt` | 56 | CalorieHistorySelectionTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/tracking/FoodLogDayPolicyTest.kt` | 34 | FoodLogDayPolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/tracking/StepHistoryRepositoryTest.kt` | 42 | StepHistoryRepositoryTest, FakeStepHistoryDao |
| `app/src/test/java/com/health/calculator/bmi/tracker/tracking/StepTrendPolicyTest.kt` | 48 | StepTrendPolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/tracking/TrackingQualityPolicyTest.kt` | 59 | TrackingQualityPolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/ui/AccessibilityContentDescriptionTest.kt` | 130 | AccessibilityContentDescriptionTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/ui/components/WellnessIconMappingTest.kt` | 49 | WellnessIconMappingTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/ui/screens/bloodpressure/BloodPressureColorPolicyTest.kt` | 38 | BloodPressureColorPolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/ui/screens/calculators/bmi/components/BmiSliderPolicyTest.kt` | 43 | BmiSliderPolicyTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/ui/theme/ThemeTokensTest.kt` | 80 | ThemeTokensTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/widget/WidgetAccessibilityHelperTest.kt` | 31 | WidgetAccessibilityHelperTest |
| `app/src/test/java/com/health/calculator/bmi/tracker/widget/WidgetLifecycleTest.kt` | 154 | WidgetLifecycleTest |
| `build.gradle.kts` | 9 |  |
| `docs/ANALYTICS_GROWTH_PLAN.md` | 78 |  |
| `docs/ASO_LAUNCH_PLAN.md` | 163 |  |
| `docs/BRAND_IDENTITY.md` | 33 |  |
| `docs/DATA_SAFETY_MAPPING.md` | 76 |  |
| `docs/DEVICE_QA_2026-09.md` | 179 |  |
| `docs/FIREBASE_APPCHECK_GUIDE.md` | 138 |  |
| `docs/MEDICAL_REFERENCES.md` | 36 |  |
| `docs/MONETIZATION_PLAN.md` | 52 |  |
| `docs/PLAY_DATA_SAFETY_GUIDE.md` | 194 |  |
| `docs/PRODUCTION_RELEASE_CHECKLIST.md` | 182 |  |
| `docs/RELEASE_STATUS.md` | 1532 |  |
| `docs/SECURITY_OPERATIONS.md` | 36 |  |
| `docs/index.html` | 39 |  |
| `docs/play_store_icon_512.png` | binary |  |
| `docs/privacy.html` | 65 |  |
| `docs/terms.html` | 57 |  |
| `gradle.properties` | 37 |  |
| `gradle/libs.versions.toml` | 75 |  |
| `gradle/wrapper/gradle-wrapper.jar` | binary |  |
| `gradle/wrapper/gradle-wrapper.properties` | 9 |  |
| `gradlew` | binary |  |
| `gradlew.bat` | binary |  |
| `play_store_icon_512.png` | binary |  |
| `settings.gradle.kts` | 27 |  |

