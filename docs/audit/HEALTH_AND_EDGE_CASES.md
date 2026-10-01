# Calculation and edge-case verification map

19–20 September 2026. This is an engineering verification record, not medical
certification. Tests assert the implemented equations/policies, not clinical
validity for a person. Reference context lives in [MEDICAL_REFERENCES.md](../MEDICAL_REFERENCES.md).
No equation coefficients or medical category thresholds were changed in this
audit. BMR input validation and category-color presentation were corrected.

## Calculation inventory

| Area | Source / equation or rule observed | Tests / limitations |
|---|---|---|
| BMI | BmiCalculator and adult edge policy; kg / metres²; adult bands | MedicalCalculatorAccuracyTest, BMIEdgeCaseHandlerTest, BMIGoalDataTest; not pediatric percentiles or diagnosis |
| BMR | BMRCalculator; Mifflin, original/revised Harris–Benedict, WHO weight/age equation, Katch–McArdle, Cunningham | Finite/adult bounds and formula references tested; consistency tests now include WHO form validation; historical equations are estimates |
| Calories/TDEE | CalorieCalculatorUseCase; BMR × activity factor; TEF not added twice | Tests for adult/body-fat bounds and no double-counted TEF; fixed calorie floors/7700 kcal-per-kg are planning conventions, not universal safe prescriptions |
| Macros | MacroCalculatorUseCase; 4/4/9 kcal per gram, percentages total 100, meal count 1–12 | Tests for balanced percentages, finite values, meal count and saturated-fat guide; diet/protein presets need contextual advice, not treatment claims |
| Blood pressure | BloodPressureCalculator; either pressure can select the higher category; severe-reading product boundary ≥180 or ≥120 | Boundary tests including discordant values; this conservative product boundary is not a verbatim restatement of all AHA wording; repeated measurements/symptoms matter |
| Waist ratios | WhrCalculator and measurement policy; waist/hip, waist/height; selected population action points | Waist measurement and South Asian boundary tests; never derive a diagnosis or visceral-fat measurement |
| Water | WaterIntakeCalculator; transparent adult beverage starting point and context adjustments | Tests for target assumptions and reproductive-context restraint; total water includes food; no kidney/heart disease or fluid-restriction prescription |
| BSA | BSACalculator; Mosteller √(kg×cm/3600), Du Bois and six historical comparisons | Mosteller/reference and invalid/unknown-ID tests; no dose calculations. Alternate historical formulas/population copy still require clinical/editorial validation |
| Reference body weight | IBWCalculatorUseCase / IdealWeightCalculator, BMI-derived range and historical equations | Upper BMI reference 24.9 and input tests; “ideal” is not an individual treatment goal |
| Heart-rate zones | HeartRateZoneCalculator; estimated max HR / reserve methods | Adult validity, Karvonen consistency and guidance tests; no phone sensor measurement; medication/training effects unresolved by age formula |
| VO2 estimate | VO2MaxCalculator; non-clinical heart-rate-ratio estimate | Test asserts non-clinical framing and no promised projection; unsupported age/percentile presentation suppressed in baseline; not measured VO2 |
| Metabolic indicators | MetabolicSyndromeCalculator; counted reference criteria including population waist point | Boundary/partial-input tests; cannot establish clinical diagnosis, missing data must remain explicit |
| Wellness/engagement | HealthScoreCalculator and overview completeness/logging fields | Tests assert non-clinical nature; not a validated health/risk score |
| Weight goal / meal plan | WeightGoalPlannerUseCase, MealPlanningUseCase, AdjustedWeightUseCase | Some bounded presentation and scheduling tests; full physiological validity is not established |

Current authoritative context checked during the audit:

- [CDC adult BMI](https://www.cdc.gov/bmi/adult-calculator/bmi-categories.html):
  bands and screening limitations. CDC adult page uses age 20+; the application's
  adult policy uses 18+. Do not claim that all age assumptions are CDC-derived.
- [AHA blood pressure](https://www.heart.org/en/health-topics/high-blood-pressure/understanding-blood-pressure-readings):
  measurements, categories, symptoms and appropriate medical follow-up.
- [Mifflin publication](https://pubmed.ncbi.nlm.nih.gov/2305711/) and
  [Mosteller publication](https://pubmed.ncbi.nlm.nih.gov/3657876/) are primary
  references, not evidence of app-wide medical validation.

## Edge-case matrix

“Tested” means assertions in a passing suite for the listed revision in the main
audit ledger. Tests newly added after that revision stay pending until rerun.
Static guards are not substitutes for every keyboard/UI journey.

| Case | Evidence / result scope | Still required |
|---|---|---|
| Age 0, child, excessive age | Adult formula guards and unit tests | All screen error/focus behavior |
| Weight/height 0, negative, NaN/infinity | Formula/TrackingQualityPolicy tests; BMR validation tightened | Uniform parsing and bounds across every legacy calculator |
| Extreme positive numeric input | Several model ranges tested | Full overflow matrix for every alternate historical equation |
| Blank/negative/custom food macros | FoodInputPolicy/FoodSafetyTest | Repository-level protection for every caller and malformed saved JSON |
| Zero/non-finite calorie target | DailyFoodLog progress guard tested | All macro progress and corrupted preference combinations |
| BP 179/119, 180/119, 179/120, 180/120 | BloodPressureBoundaryTest / medical tests | Device result/widget/notification consistency |
| Unsupported heart-rate inputs | Adult/HR validation tests | Every equation mode and restored input in UI |
| Decimal input / imperial switching | Conversion/formula tests; weight edit accepts decimal comma | Locale-specific parsing for every screen, unit-switch round trips |
| Future timestamps | TrackingQualityPolicy rejects invalid timestamps; integrity diagnostic no longer deletes | Clock-change recovery on widgets/background flows |
| Future birth date / malformed dates | Partial validation in profile/date paths | Explicit full profile UI regression cases |
| Leap day / DST / duplicates in one day | CalendarStreakTest; reminder/day-policy tests | Device TIME_SET/TIMEZONE_CHANGED and restart |
| Food history >90 days | FoodHistoryPolicy retains 120-day fixture | Disk-full/corrupt JSON and long-history performance |
| No records | Pure summaries and onboarding tested; several UI empty states inspected | Every chart/report with no records |
| 1,200 history rows / failed cleanup | New real SQLite transaction rollback test | Hundreds/thousands of rendered rows/chart timing |
| Duplicate/rapid actions | Weight save guard; cleanup guard; onboarding page-transition guard | Repeated taps across water/food/export/widget entry points |
| Database upgrades | API 34 tests for 13→14→15→16 | Real oldest-distributed DB; pre-13 fixtures if shipped |
| Rotation/process death | rememberSaveable onboarding selection; existing configuration-policy tests | Whole-app navigation and in-progress form restoration |
| Notification denied/revoked | Source permission gates/policy tests | Actual delivery/denial/reboot/OEM battery tests |
| Health Connect denied/unavailable | Scope/edge policies and manifest tests | Real provider/multi-source/history-window tests |
| Failed exports | New FileProvider failure test and explicit worker result | WorkManager failure/retry cadence, sharing apps and cache lifecycle |
| 2x font/dark theme | Onboarding instrumentation passed | All screens, charts, TalkBack and landscape |

## Database and privacy boundaries

Room schema remains version 16; no table, column, migration or destructive
fallback was changed. Bulk selected-history deletion is now a transaction, with
chunks under the SQLite parameter limit. This changes DAO behavior, not schema.
Food/WHR/preferences remain separate stores; generic exports are not a complete
restore-capable backup. Deleting local app data cannot revoke an already shared
file or guarantee deletion of cloud processing records.

Health entries are in app-private storage, not SQLCipher-encrypted by this app.
Android sandbox/device encryption is not the same as app-managed encryption.
Backups are disabled and exports use scoped FileProvider grants; optional AI,
analytics and technical SDK traffic must be described separately from local logs.
No penetration-test, legal-compliance or vulnerability-free claim is supported.
