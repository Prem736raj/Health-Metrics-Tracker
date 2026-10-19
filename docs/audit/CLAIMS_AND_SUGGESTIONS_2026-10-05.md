# Claims and suggestions review — 5–6 October 2026

This review covers the current reachable app, calculator/recommendation models,
bundled educational copy, and listing/privacy drafts. It is an engineering and
editorial audit, not clinical certification. Historical reports and dormant
resources can describe features that are no longer reachable.

| Surface | Finding and correction | Interpretation retained |
|---|---|---|
| BSA clinical examples | Removed personalized fictional drug doses, burn-fluid examples, kidney normalization factors and cardiac-output examples from results. The clinical-context component accepts no measurement or dose rate. | BSA is an informational body-size estimate; a treating team determines clinical use. |
| Heart-rate recommendations | Removed fixed fuel-burning percentages, zone-based calorie estimates, mandatory workout durations/frequencies, fasted-exercise suggestions and promised outcomes. | Age/max/reserve equations give estimated ranges. Heart rate alone cannot establish fuel use, calorie expenditure, safe intensity or fat loss. |
| WHO exercise section | Removed generated weekly schedules and automatic zone-to-intensity mapping. New sessions require the user to select experienced moderate/vigorous intensity; the dialog explains the talk test. | Public adult activity references remain. The journal uses logged intensity/duration and does not assess fitness or health. Older saved sessions retain their recorded intensity; do not treat them as independently validated measurements. |
| Blood-pressure urgency | Both severe categories share symptom-first escalation. Emergency symptoms require immediate local emergency help without waiting for another reading. Without symptoms, repeat after at least one minute and promptly contact a professional if still severe. | The app's conservative severe boundary is ≥180 systolic **or** ≥120 diastolic. AHA public wording uses higher than 180 and/or 120; one reading does not diagnose a condition. |
| BP education | Removed claims that lifestyle changes outperform medicines, fixed BP-drop promises, blanket age/population medication statements, left-arm preference, red-wine benefits and phone measurement implications. DASH portions now depend on energy needs and clinical context. | The app logs readings from a separate measuring device. Follow care-team advice and do not change/delay medicine using the app. |
| Hydration education | Removed a universal safe daily maximum, guaranteed safe hourly volume, fixed heat/pregnancy/breastfeeding allowances, compulsory drinking schedules and automatic exercise replacement amounts. Removed urinary/skin/joint treatment promises. | Beverage targets are disclosed heuristics. Total water includes food; medical fluid restrictions take priority. |
| Calories and meal planning | A calorie planning floor now explicitly disclaims personal safety/adequacy. Workout portions use matching calorie and macro shares and count within daily totals. Custom meal shares must be finite, match meal count and total 100%. Unsupported numeric/equation inputs are rejected. Deficit/surplus and energy-balance illustrations now use the displayed intake after the planning minimum; exported/history illustrations use the same calculation. | BMR × activity is an estimate. Planning floors, 7,700 kcal/kg and diet presets are conventions, not personal nutrition prescriptions or physiological predictions. |
| Fasting and workout timing | Added contraindication context for optional fasting and removed a compulsory one-hour post-workout window. | Meal timing is an optional illustration; pregnancy/breastfeeding, eating-disorder history and medicines need appropriate professional guidance. |
| BMI/waist/reference weight | Removed a universal safe waist-change rate and accuracy/optimal-health implications. Fixed goal direction/progress after crossing a saved target and overlapping “close”/“achieved” prompts. | Adult ratios and historical reference weights are screening/planning context. Estimated body composition is not a measured scan. |
| Smart prompts | Near a water target, the prompt now asks to record drinks already taken. Reaching a weight goal asks users to review the next goal; near-goal wording stays neutral on either side of the target. Fixed Home dispatch for the water insight; “Log Food” opens the food journal and “View Progress” opens weight tracking. | Suggestions support logging and user-selected goals; they do not instruct excess drinking or continuing weight loss indefinitely. |
| Store and settings | Removed current family-tracking and AI-feature claims. Listing includes explicit medical-device/diagnosis/treatment/cure/prevention disclaimer. Calculator-history deletion explains its limited scope. | Family tracking remains disabled because core records are not profile-scoped. Food tracking is manual/preset-based; no account, barcode database, ads or cloud restore is claimed. |
| Privacy | Expanded local storage disclosure to profile/photo, goals, food/nutrients, notes and the actual Room/DataStore/SharedPreferences stores. | Local deletion cannot delete exported copies, source Health Connect records or previously processed service data. No app-managed database encryption is claimed. |

## References checked

- [AHA blood-pressure readings and severe symptoms](https://www.heart.org/en/health-topics/high-blood-pressure/understanding-blood-pressure-readings)
- [AHA home monitoring](https://www.heart.org/en/health-topics/high-blood-pressure/understanding-blood-pressure-readings/monitoring-your-blood-pressure-at-home)
- [AHA estimated target heart rates](https://www.heart.org/en/healthy-living/exercise-and-physical-activity/fitness-basics/target-heart-rates)
- [CDC measuring physical activity intensity](https://www.cdc.gov/physicalactivity/basics/measuring/index.html)
- [WHO adult activity guidance](https://www.who.int/europe/publications/i/item/9789240014886)
- [NHLBI DASH eating plan](https://www.nhlbi.nih.gov/health/dash-eating-plan)
- [Equation and measurement reference register](../MEDICAL_REFERENCES.md)

The references support specific methods and context, not app-wide clinical
validation. Formula regression tests check arithmetic and implemented boundaries.
They cannot establish a safe personal exercise limit, appropriate diet/fluid
prescription, organ function, disease status or medication dose. A qualified
clinical/editorial review remains appropriate before distributing health content.
