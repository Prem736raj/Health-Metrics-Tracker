# UI, accessibility, repository and operational review

19–20 September 2026. Read alongside the source-backed feature inventory and
verification ledger. Static findings are not screenshot/device approval.

## UI/UX review by screen group

| Screens | Evidence / implemented change | Remaining verification or improvement |
|---|---|---|
| Onboarding | Removed delayed visibility; scrolling, growing cards, saved selection, correct theme roles; three API 34 tests pass | Real rotation/process death and TalkBack reading order |
| Home / Track / Calculators / Insights | Source-backed routes and shared food state; existing consolidated hubs retained | Populated/empty/error state on small/landscape screens, cold-start traces |
| Profile / connections | Overview uses actual persisted keys/routes; specific category color precedence repaired | Provider unavailable/denied/revoked, error versus no records, photo selection |
| Weight | Save guard and recoverable write/delete errors, decimal-comma edit handling | Edit/delete/save interaction and rotation in both units; large-font chart |
| BMI input/result/goals | Existing slider and boundary policies tested; no random visual rewrite | Keyboard/inset/slider collision checks at 320dp and 2x font |
| BMR/formula comparisons | Form/calculator adult bounds aligned | Long labels, horizontal tables, sensible secondary formula disclosure |
| BP calculator/log/trends/export/education/reminders | CSV injection guard; presentation precedence corrected; shared severe-reading baseline policies | End-to-end CRUD, measurement guidance, chart text alternatives, reminder delivery |
| Waist input/result/progress/education | Baseline measurement policy/reference tests retained | Restored route arguments, long reference labels, both units |
| Water input/result/tracker/history | Calendar label correction; baseline separate result route retained | Quick-add rapid taps, midnight, edits, widget update and denied permission |
| Water gamification/tools/education/electrolytes | Actual implementations inventoried | Keep secondary; review claims, visual/color-only observations and chart descriptions |
| Calories/food/history/macros/meal plans | Shared repository, >90-day retention, macro parsing and safe progress | Disk corruption, day rollover, missing versus zero, longer food names and locales |
| BSA / reference weight / metabolic / heart-rate | Formula/policy tests exist; estimates are not diagnoses | Clinical/editorial review of remaining historical-formula and risk copy; large charts |
| Reminders / welcome back / milestones / weekly report | Source and scheduling-policy tests inspected | Reboot, time change, denied notification, missing data, screen reader labels |
| AI | Optional-context disclosure now names weight values sent | Signed App Check, offline/retry/clear conversation, keyboard and safety adversarial tests |
| Export / scheduled export | Explicit failure result and safe error text | Share targets, cancellation, storage failures, actual WorkManager retry/delivery |
| Settings / data management | Removed unsafe automatic repair; atomic cleanup and state reset | Confirmations, rollback UI, full reset and return-to-onboarding on device |
| Widgets | Manifest boot component narrowed; baseline accessibility/lifecycle policy tests | Real launcher layouts, PendingIntent interaction, process death and reboot |

## Design system and themes

`ui/theme/Color.kt`, `Theme.kt`, typography/shape tokens and shared components
already form a design-system starting point. This audit deliberately changed
only two light-theme role colors with demonstrated white-text contrast failures,
plus onboarding usage of semantic on-colors. Fourteen tested opaque role pairs
meet the test's 4.5:1 contrast threshold. That is not all-screen WCAG conformance.

A lexical search found **142 white/black/hex-color matches across 33 screen
files**. Some are intentional chart/illustration colors; blanket replacement
would be incorrect. Feature gradients still use bright endpoints with white
foregrounds, and chart lines/alpha overlays need surface-specific measurement.
Health categories must have visible labels/text alternatives, not only colors.

The dark 2x-font onboarding test passed; no claim is made that every dark screen,
system bar, tooltip, bottom sheet or chart has been reviewed on a real device.
The existing device matrix remains relevant. Extract hard-coded strings in
coherent screen batches; avoid an unreviewable mass translation refactor.

The established full lint run reports 851 warnings and one hint per variant,
with zero errors. Largest groups: DefaultLocale 214, UseKtx 174, UnusedResources
155, HardcodedText 87, UnusedAttribute 38 and SmallSp 31. Nine content-description
warnings are in medium/large widget layouts; inspect runtime RemoteViews labels
and decorative-image roles before altering them. Passing lint is not closure of
these accessibility/localization issues.

Local API 34 smoke checks subsequently launched onboarding and Home, switched
Home from light to dark, opened Track → Weight and saved/read a synthetic entry.
Screenshots are in ignored `scratch/android-audit/`. This revealed keyboard
occlusion of weight-dialog controls and precision loss on edit (AUD-019), so the
weight journey is not marked fully verified. One cold debug launch reported
2834 ms while Gradle compilation ran; that is not a controlled startup benchmark.

## Repository and history

- One app module; mixed `presentation/ui`, `data/model/models`, `dao/local` and
  `notification/notifications` package naming. Incremental ownership cleanup is
  appropriate, not a package-wide rename alongside database fixes.
- Baseline inventory: 682 tracked files, 557 Kotlin files including tests;
  about 500 production Kotlin files. `SOURCE_INVENTORY.md` is a lexical map, not
  proof that every line has been manually reviewed.
- Added README, contribution/security guidance and changelog. Historical root
  reports moved to `docs/archive/` with contents retained and status qualified.
  No license was invented: the owner must choose one and confirm asset rights.
- `.gitignore` covers Gradle/IDE/local properties, environment files, signing
  files and scratch output; `app/.gitignore` excludes the app build directory.
  The blanket `*.ps1` rule may hide useful future automation scripts; selectively
  allow reviewed scripts if added. No private script was force-added.
- Tracked binary inspection found runtime images, three store/launcher icon
  locations and the Gradle wrapper JAR, not a reason for deletion. The store icon
  duplication should be consolidated only after tracing Pages/listing consumers.
- Branches/PRs inspected: master remains the default; current work is based on
  `ui-polish-dark-light-mode` / PR #5. Prior PRs #2–#4 merged, #1 closed; v1.0.1
  tag exists, no release entry observed. No public Git history was rewritten.
- Recent commit titles are mostly descriptive but broad polish/fix series make
  change isolation harder. Prefer one concern per PR, test evidence in its
  description, protected default branch and immutable release tags going forward.
- No master→main rename is justified for this task. Branch protection, license,
  repo description/topics and publishing a release are owner actions, not
  prerequisites for safely compiling the local improvements.

## CI/CD and release

Workflow runs JVM tests, lintRelease, debug/release packaging, AAB packaging and
API 34 instrumentation. Added read-only contents permission, cancellation of
superseded runs and a build timeout. Baseline GitHub instrumentation failed;
local fixed tests passing does not prove this unpushed workflow ran on GitHub.
Action version tags remain mutable; pinning reviewed SHAs is a future hardening
step, not a reason to add a large enterprise pipeline.

Application ID remains `com.healthmetrics.tracker`; version 1.0.1/code 2 unchanged.
R8/resource shrinking are enabled for release. Signing requires owner-provided
credentials; a generated unsigned release is not installable/Play-ready signing
evidence. Do not increment versionCode without checking the highest uploaded code.

## Security/privacy and operations

- Cleartext disabled; system trust anchors; app-private stores; backup disabled.
  Scoped FileProvider paths expose intended exports/profile photos only.
- Dedicated Health Connect rationale handles a fixed policy URL; Android 14
  usage alias is protected. Manifest tests check the effective installed package.
- Advertising permissions and ad-ID collection removed/disabled; analytics is
  default-off in application policy. Firebase technical network behavior and
  optional AI remain separate from the local diary.
- Tracked-file and historical-filename heuristics did not find committed
  keystores/local properties; secret-pattern filename-only scan found no selected
  private-key/token signatures. This is not a complete secret-history scan.
- Raw crash logs remain private, outside sharing paths, but append without a
  size cap. Review retention and exception contents before external crash upload.
- Rooted-device extraction, screenshots/recents and manually shared files are
  outside “local/private” guarantees. An optional screen-privacy control may be
  useful but is not silently enabled because it would affect legitimate sharing.

## Performance and maintainability

No GlobalScope, allowMainThreadQueries or destructive Room fallback was found by
the production-source search. Compose screen search found no raw collectAsState
calls in the searched presentation/screen directories; this is not proof of no
lifecycle issues elsewhere. Compilation still emits deprecated-icon/API warnings.

Evidence-backed improvements: one canonical food repository avoids duplicate
day-rollover scopes; date-range queries avoid the wrong 30-record streak
limit; batch deletion avoids hundreds of independent transactions. No arbitrary
startup/memory/battery improvement percentage is claimed.

Remaining targets: whole-history Flow emissions and in-memory filtering, growing
preference JSON, repeated chart formatting and sequential Health Connect day
aggregations. Measure traces before adding caching, pagination or baseline
profiles. Large composables can be split along independently testable sections
as touched; no generic multi-module/clean-architecture rewrite is warranted.
