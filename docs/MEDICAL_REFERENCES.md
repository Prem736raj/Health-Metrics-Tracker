# Medical calculation references

This app presents estimates and screening context for personal wellness. None
of the calculators is a medical device, diagnosis, treatment plan, or substitute
for a qualified professional.

| Area | Reference used | Product interpretation |
| --- | --- | --- |
| Adult BMI | [WHO obesity and overweight fact sheet](https://www.who.int/en/news-room/fact-sheets/detail/obesity-and-overweight) | BMI is weight/height²; adult reference bands are used as screening context. Children need age/sex growth charts, which are intentionally not estimated in-app. |
| Waist-to-height ratio | [NICE NG246 rationale](https://www.nice.org.uk/guidance/ng246/chapter/Rationale-and-impact) | 0.5 is an action point; the app does not convert it into a diagnosis. |
| Waist-to-hip ratio | [WHO waist circumference and waist–hip ratio report](https://www.who.int/publications/i/item/9789241501491) | Common population action points are 0.90 for men and 0.85 for women; measure the waist midway between the lowest palpable rib and the top of the iliac crest after a normal exhale, and measure the hip at the widest part of the buttocks. |
| Ethnicity-specific waist | [IDF consensus definition](https://idf.org/media/uploads/2023/05/attachments-30.pdf) | The selected population's published action/reference point is shown as one threshold (for example, South Asian/Chinese/Japanese references use 90 cm for men and 80 cm for women); the app does not synthesize a second threshold. |
| Blood pressure | [American Heart Association 2025 guideline summary](https://professional.heart.org/en/science-news/2025-high-blood-pressure-guideline/top-things-to-know) | Normal <120/<80, elevated 120–129/<80, stage 1 130–139 or 80–89, stage 2 ≥140 or ≥90. The app uses ≥180 systolic or ≥120 diastolic as its severe-reading repeat/escalation boundary. A reading is not a diagnosis. |
| Resting heart rate | [American Heart Association target heart rates](https://www.heart.org/en/healthy-living/exercise-and-physical-activity/fitness-basics/target-heart-rates) | Typical adult resting range is shown as context only; training status, medicines, symptoms, and measurement conditions matter. |
| Exercise heart-rate zones | [American Heart Association target heart rates](https://www.heart.org/en/healthy-living/exercise-and-physical-activity/fitness-basics/target-heart-rates) | Percentage zones and talk-test descriptions are planning estimates. The app does not prescribe a fixed duration or intensity for an individual. |
| Resting energy expenditure | [Mifflin et al. PubMed](https://pubmed.ncbi.nlm.nih.gov/2305711/) | Mifflin–St Jeor and other historical equations are predictive estimates, not measured metabolism. |
| Body surface area | [Mosteller PubMed](https://pubmed.ncbi.nlm.nih.gov/3657876/) | Mosteller is provided as an estimate; it must not be used for medication dosing. |
| Water | [National Academies Dietary Reference Intakes](https://www.nationalacademies.org/read/10925/chapter/6) | Adequate intake includes food and beverages (about 3.7 L total men/2.7 L total women); the app uses a beverage starting point and avoids automatic illness prescriptions. |

## Product rules

- Labels use “estimate”, “reference”, “screening”, and “informational”.
- The app does not claim a formula is WHO-based unless the cited source truly
  defines it.
- A single input or reading never establishes a diagnosis.
- Waist circumference results use only the selected population reference point;
  the app does not create an additional “high” threshold by adding a fixed
  number of centimetres.
- Visceral fat is not estimated from waist, age, or gender; a validated scan or
  device would be required.
- The Wellness Score is a custom data-completeness and habit-logging indicator,
  not a clinically validated score.
- Heart-rate zone percentages are estimates; talk-test guidance and personal
  goals should take priority over a fixed number, and symptoms warrant stopping
  activity and seeking appropriate professional advice.

## October claim corrections

The [5 October claims/suggestions review](audit/CLAIMS_AND_SUGGESTIONS_2026-10-05.md)
records revised BP urgency, hydration, BSA and exercise copy. WHO activity ranges
are public guidance, not an automatically generated individual workout plan.
The exercise journal now uses the user's intensity selection with talk-test
context. Saved older intensity labels remain recorded values, not independently
verified activity classifications.
