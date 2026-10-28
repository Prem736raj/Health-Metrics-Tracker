# Calculator and supporting-screen design report

Implemented 2026-10-28. The previously approved Home, onboarding/splash, Track,
calculator catalog, Insights and Profile designs remain unchanged. The detail
screens use new native vector motifs inspired by the botanical direction.

## Calculator identities

| Calculator | Presentation |
| --- | --- |
| BMI | Measurement folio, compact restore row, segmented controls, single outlined inputs and reference scale |
| BMR | Warm energy ledger, equation cards, numbered measurement sections and allocation tables |
| Water | Reservoir heading, hydration journal and flow/timeline presentation |
| Daily energy/calories | Harvest heading, energy receipt, meal journal and macro planning |
| Blood pressure | Paired SYS/DIA stems, observation ledger and compact purposeful header actions |
| Heart rate/recovery | Pulse heading, labelled training lanes and activity references |
| Waist/hip | Measuring ribbon, aligned circumference inputs and ratio reference |
| Body surface area | Specimen sheet, area reading, formula comparison and tracking |
| Height-based/ideal weight | Branching reference range, separate goal path and formula comparison |
| Metabolic markers | Five-branch assessment, criterion lanes and comparison matrix |

Their results, learning, tracking/history and utility components use the same
feature-specific palette where those screens exist. The alternate, currently
unlinked Ideal Weight implementation was styled without adding a route.

## Supporting screens and visibility

Settings, health connections, weight tracking, History/Statistics, milestones and
records, reminders/editors, weekly reports, AI assistant, articles, export/data
management and Welcome Back receive scoped day/night styling. Supporting content
uses an editorial reading shelf, observation journal, agenda or conversation
layout as appropriate.

Explicit small labels were raised to at least 12sp; faded text was assigned opaque
foreground roles; native chart Paint now receives ARGB values rather than Color
hash codes. Wrapping controls, scrolling/insets and accessible action labels were
improved. The existing IBW goal-plan BMR callback is connected, the BMR calculation
button identifies BMR, and the BP header groups existing actions in an overflow
menu. No new dependency, formula, model, persistence contract or route was added.
Existing clinical disclosures are preserved.

## Verification

- Debug APK, test APK and full debug unit suite build successfully; 201 unit tests
  pass, with no failures/skips or new Kotlin warning signatures versus baseline.
- `python scripts/check_detail_contrast.py` passes 1,496 day/night combinations.
  Body/secondary/accent/status text is checked at 4.5:1, outlines at 3:1.
  `DetailPaletteTest` also verifies the actual Compose-interpolated palette.
- All 16 emulator UI tests pass on a dedicated Android 14 instance: six scoped
  detail-theme/header checks plus ten approved main-screen navigation tests. The
  header checks exercise 320dp width and 1.3 font scale. An earlier combined run on
  a shared emulator lost its Compose window during another app's test; the
  isolated run passed without changing the protected test or application source.
- Runtime captures cover all ten calculator entries and their visible tabs in
  both modes. Fresh day/night contact sheets are under
  `artifacts/detail-app-design/{light,dark}-review.png`.
- Eleven supporting destinations plus Statistics were opened in each mode.
  Sample BMI (70kg, 170cm, age 30) displays 24.2 and sample BMR displays
  1617 kcal/day in readable day/night result cards. A full-page search after a
  system night-to-day change confirms BMR retains its result. An initial capture
  showed the reset viewport/input mode at the top, which was not evidence of
  calculation data loss; no state or navigation change was made.
- Twelve approved screen/mode screenshot pairs (Track, catalog, Insights, Profile,
  name editor, Health Overview) have zero changed content pixels. Only OS status
  and gesture areas were excluded from the comparison.
- Hash checks pass for 382 protected source/asset/configuration files. A bounded
  source audit preserves 72 clinical/disclosure strings and finds no newly
  introduced escaped template, explicit text below 12sp or native chart hash
  conversion in the modified sources.
- The existing light-palette guard now compares with the approved botanical
  baseline `c6f5dac`, replacing its superseded pre-botanical violet reference
  `81271c5`. The historical check initially failed against those already-approved
  palette changes. No `Color.kt` or `Theme.kt` value changed in this rollout;
  the independent hash and screenshot comparisons also pass.

Reproduce the build:

```powershell
.\gradlew.bat --no-daemon :app:assembleDebug :app:assembleDebugAndroidTest :app:testDebugUnitTest -Pksp.incremental=true --max-workers=1 --console=plain
python scripts/check_detail_contrast.py
```

UI test classes:

```text
com.health.calculator.bmi.tracker.ui.components.detail.DetailDesignSystemTest
com.health.calculator.bmi.tracker.presentation.navigation.ForestMainScreensTest
```

## Verification limits

No physical phone was used. Live AI replies, external export/share delivery,
Health Connect hardware/permission variants, all possible saved-history/result
combinations and every device/font configuration are not exhaustively verified.
The assistant's first-use disclosure was reviewed without sending a message.
Welcome Back depends on inactivity and the alternate Ideal Weight screen has no
active navigation caller; their source styling is covered, but those entry states
were not forced by changing storage or routes. No design decision remains pending.

Local evidence and exact logs are retained in `artifacts/detail-app-design`.
The final APK SHA-256 is
`FC2B286FA9FABFB8ECFBC57503EFE27F1BBA4B343D1C361DCD3627ECA926C11D`.
The pre-edit table is in [DETAIL_DESIGN_AUDIT.md](DETAIL_DESIGN_AUDIT.md).
