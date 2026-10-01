# Health Metrics Tracker — production-readiness report

Audit: 19–28 September 2026. Baseline `1efccdf` on
`ui-polish-dark-light-mode`. **Local implementation/build verification is current;
this is not a production approval because owner/Play/Firebase/device-matrix gates
remain.** Latest exact results are in the
[verification ledger](PRODUCTION_AUDIT_2026-09-19.md#verification-ledger).

## 1. Executive summary

The app is a substantial, implemented Android product, not a blank prototype,
but earlier “complete” documentation overstated readiness. This audit found
data-loss risks, stale/misleading state, export failure reporting, privacy-entry
gaps and UI defects missed by the previous test suite. Changes are deliberately
targeted; no broad rewrite, dependency sweep or schema reset was performed.

High-priority repairs include non-destructive integrity checks, transactional
cleanup, honest export outcomes, retained food history, shared food state,
correct Health Connect queries/entry points and advertising-permission removal.
Device re-audit exposed weight-dialog defects that were repaired and rechecked
on the audit emulator. A later manual launch with system animations disabled
exposed a blank splash destination; the targeted repair now passes its focused
test, the full 25-test API 34 suite, and a fresh animation-disabled cold launch.

## 2. Architecture

One application module: Compose/Navigation → ViewModels → repositories → Room,
DataStore/SharedPreferences, optional Health Connect/Firebase. Hilt coexists with
application-owned services. Food repository ownership is now consistent across
manual and Hilt consumers. Pure, small policies isolate CSV/date/food/input/color
behavior for testing. Room v16 and existing migration identities are preserved.
Mixed package ownership and large composables remain incremental refactoring work.

## 3. Market analysis

Position around quick, local-first daily measurements and useful exports, not
clinical diagnosis or a full nutrition database. Samsung Health, Cronometer,
MyFitnessPal, MedM and WaterMinder offer different strengths. Sample current
reviews reinforce the value of reliable logging and understandable history;
they do not establish representative complaint rates or market demand.
[Dated research and primary links](MARKET_AND_DEPENDENCIES.md).

## 4. Feature audit

The [feature inventory](FEATURE_AUDIT.md) maps UI entry points, routes,
ViewModels/services, stores, permissions and classifications. It covers the
navigation graph and non-navigation widgets/workers. A feature's existence or
compilation is not treated as proof that all its operations work. Most remain
“likely working but not runtime verified”; generic detail navigation and backup
claims have explicit partial-scope limitations.

## 5. Features to remove/merge

- Removed automatic heuristic repair/deletion and unused advertising permissions.
- Recommend merging reminder entry points and overlapping educational panels,
  preserving stored schedules and measurement history.
- Keep AI, gamification, BSA, historical equation comparisons and hydration-color
  tools secondary/optional, not reasons for a feature deletion spree.
- Keep multi-profile tracking disabled until health records are profile-scoped.
- Archive historical reports; do not confuse old “complete” status with approval.

## 6. Features to add

Must-have: reliable failure states, tested critical CRUD and permission journeys,
accurate public privacy/Play declarations and signed-release validation.
Should-have: explicit Health Connect error states, chart text alternatives and
versioned user-controlled backup/restore if backup is promised.
Nice-to-have: responsive tablet polish and measured startup optimization.
Future: device/nutrition integrations only after user demand justifies their
maintenance, privacy and data-quality cost. Do not add accounts/ads/social feeds
merely because competitors have them.

## 7. GitHub audit

README, contributing/security guidance and changelog added; root audit history
archived. No public history rewritten, default branch renamed, release published,
commit pushed or PR created by this audit. Existing PR #5 is baseline context.
Owner still chooses a license, repository metadata, branch protection and release
version. [Repository/CI findings](UI_AND_REPOSITORY_REVIEW.md).

## 8. Code quality

Shared CSV encoder and small policies replace duplicated/untestable branches.
Canonical food state prevents independent background scopes and stale UI copies.
Cancellation is preserved in touched suspend operations. Existing deprecated
icons/APIs, hard-coded strings, oversized screens and mixed package naming remain.
Lint “unused” flags are candidates, not deletion authority: resource/reflection,
widgets and migration references need tracing before removal.

## 9. Dependencies

No versions upgraded blindly. Current SDK target is 36/minimum 26 with Java 17.
Version catalog/BOM already centralize much of the graph. Wrapper checksum added.
The dependency table records current/available versions, reasons and breaking
risks. A narrow OSV query returned no IDs for five selected direct coordinates;
this does not clear the full transitive graph. [Dependency review](MARKET_AND_DEPENDENCIES.md).

## 10. UI/UX

Onboarding now has immediate content, scrolling, growing action cards and saved
selection. Weight save/error/edit behavior and keyboard accessibility were
rechecked on the audit emulator. Diagnostic-only data management is clearer.
The splash completion path no longer depends on an animation callback. On
28 September the full API 34 suite passed with animation scales at zero and a
fresh cold launch reached visible onboarding content instead of a blank screen.
No random visual redesign was applied. [Screen-group review](UI_AND_REPOSITORY_REVIEW.md).

## 11. Light/dark theme

Two demonstrated light-role contrast failures repaired. Fourteen selected opaque
role pairs passed tests; Home was visually inspected in light and dark on API 34.
Bright gradients, charts and alpha-composited colors still need targeted checks.
The 142 lexical color matches across 33 screen files are not all defects.

## 12. Accessibility

Onboarding's 2x-font dark-theme test passes in the established test revision.
New weight-dialog test covers a large-font enabled save control. All-screen
TalkBack, keyboard, gesture insets, landscape and contrast are not certified.
The [device matrix](../DEVICE_QA_2026-09.md) remains a release gate.

## 13. Functional verification

Prior verified weight-dialog revision: 218 JVM tests passed in each of debug/release
variants and 19 Android 14 tests passed. Those include
onboarding, installed manifest behavior, Room migrations/CRUD/rollback and real
export success/failure. Manual debug smoke covered onboarding → Home, light/dark,
Track → Weight, save/read and persisted Home summary after force-stop/relaunch.
The 23 September edit check confirmed full 70.53 kg precision, then revealed
the Update button still clipped by the keyboard. The targeted layout repair
passed a second emulator check: the Update button is fully visible above the
open keyboard, and tapping it completed the edit.
The food-storage safety revision passed 24 Android 14 instrumentation tests on
26 September, including five new corruption/preservation cases. The subsequent
splash repair was verified on 28 September: the revised debug/test APKs passed
`OK (25 tests)` in 105.689s with all three animation scales set to zero. A fresh
cold launch focused `MainActivity`, exposed onboarding text through the UI
hierarchy, and produced no matching app fatal-exception or app-ANR log entry.
No blanket “all features work” claim is supported.

## 14. Health calculations

Input contracts, formula boundaries, units and estimate/diagnosis wording have
explicit evidence and limitations in [HEALTH_AND_EDGE_CASES.md](HEALTH_AND_EDGE_CASES.md).
BMR validation inconsistency repaired; category-color ordering fixed without
changing clinical thresholds. Historical equations, heuristic calorie/water
guides and educational claims still need appropriate medical/editorial review.
Engineering tests do not establish clinical accuracy or regulatory status.

## 15. Database

Room v16 unchanged; current tests exercise 13→14→15→16 and chained migration
with row preservation. A 1,200-row cleanup rollback is tested. No destructive
fallback introduced. Owner must confirm the oldest distributed schema; fixtures
13–16 do not prove no earlier release existed. Food preference JSON still needs
failure-safe corruption recovery and stronger long-history persistence tests.

## 16. Performance

Shared food ownership removes redundant rollover scopes. Bulk cleanup uses one
transaction. Storage walks moved off the main dispatcher. No improvement
percentage or production startup/memory/battery number is claimed. Whole-history
emissions, JSON growth, charts and sequential Health Connect aggregation deserve
profiling. One debug launch under simultaneous compilation is not a benchmark.

## 17. Security/privacy

Cleartext disabled, scoped FileProvider, backups disabled, unnecessary ad
permissions removed, protected Health Connect usage alias tested. Local core
records are separate from optional AI/context/analytics and technical SDK traffic.
AI weight-context and deletion disclosures corrected. A local startup crash log
now keeps only the latest bounded trace and omits exception messages; the current
source revision passes debug/release JVM tests, debug/test packaging, lint and
release packaging. Source scans are not a penetration test;
local Room is not app-encrypted, and service-side handling requires owner
confirmation.

## 18. Testing

New policy and instrumentation tests target actual defects, not test-count goals.
Read tests' assertions and the revision-specific ledger for their scope. Further
gaps include ViewModel storage failure injection, food recovery, full tracker
CRUD/UI, notifications, HC provider behavior, process death and signed R8/cloud
requests. No code-coverage percentage is claimed.

## 19. Play Store readiness

Not ready for unconditional launch sign-off. Required: signed artifact validation,
current public policy, owner-confirmed Data Safety/health declarations, real
provider/notification/OEM testing, store screenshots/metadata, account-specific
closed testing if applicable, production App Check and monitoring.
The corrected Data Safety guide is a worksheet, not legal/console approval.

## 20. Changes made

Every changed path and why/what changed is in [CHANGE_INVENTORY.md](CHANGE_INVENTORY.md).
All changes remain local and uncommitted. Ignored scratch artifacts/logs contain
audit evidence, not production app assets.

## 21. Files removed

Only two historical reports moved out of root into `docs/archive/`; contents
retained. Unsafe automatic repair methods/UI and duplicate CSV encoder bodies
removed after call-site tracing. No schema/migration/runtime asset or user health
data was deleted. The emulator uses synthetic audit data only.

## 22. Build results

Established full run: `gradlew.bat testDebugUnitTest assembleDebug
connectedDebugAndroidTest lintDebug lintRelease assembleRelease bundleRelease
--max-workers=2 --console=plain` passed in 16m6s. Results: 210 JVM tests,
16 instrumentation tests, unsigned release APK/AAB, lint zero errors with
851 warnings/one hint per variant. This is not a zero-warning project.

The follow-up date/food/storage run was deliberately cancelled to include
runtime-discovered weight-dialog fixes. That combined batch produced 218 passing
debug JVM tests and debug/test APKs before its session ended without final
release/lint completion. Those remaining gates, plus release-variant JVM tests
and explicitly targeted audit34 instrumentation, were restarted on 21 September.
Both JVM variants passed 218 tests each; instrumentation reports OK (19 tests)
in 89.57 seconds. The complete 21 September build finished successfully in
22m17s: debug APK, test APK, unsigned release APK, and release app bundle were
produced. Debug/release lint both completed with zero errors, 846 warnings and
one hint each. A manual UI check on 23 September reproduced keyboard overlap in
the weight editor. A smaller keyboard-visible scroll area passed the full build
again on 23 September in 18m39s, with 218 JVM tests per variant and the same
lint counts. The second device check confirmed visible Update and successful
editing with the keyboard open. The 19-test API 34 device suite passed again on
the revised APK in 17.594 seconds.
An earlier System UI ANR while Windows had ~300 MB free RAM
is not reported as a proven app defect.
The food-storage safety revision's first combined run failed in KSP with
`EOFException` while loading an incremental classpath snapshot after an
interrupted run. A focused non-incremental KSP retry passed, then its full gate
passed in 21m16s with 218 JVM tests per variant and 846 lint warnings/zero
errors per variant. A 26 September repeat of unit/build/lint packaging passed in
10m4s. The audit34 suite then passed `OK (24 tests)` in 51.771 seconds. Manual
launch exposed the animation-disabled splash defect described above. On
28 September the current splash/crash-log revision passed `testDebugUnitTest`,
`testReleaseUnitTest`, `assembleDebug` and `assembleDebugAndroidTest` in 7m28s;
both JVM variants report 218 tests, zero failures/errors. A final cached combined
gate covering both JVM suites, debug/test packaging, both lint variants,
`assembleRelease` and `bundleRelease` then completed with `BUILD SUCCESSFUL in
48s` (172 actionable tasks; 2 executed, 170 up-to-date). Current debug/release
lint reports contain 0 errors, 846 warnings and 1 hint per variant. The regenerated
release APK/AAB remain unsigned. `verifyPlayReleaseSigning` correctly failed
because the required
`RELEASE_STORE_FILE`, `RELEASE_STORE_PASSWORD`, `RELEASE_KEY_ALIAS` and
`RELEASE_KEY_PASSWORD` values are not supplied. The freshly installed debug and
test APKs then passed `OK (25 tests)` on audit34/API 34 in 105.689s with all system
animation scales at zero. A manual cold launch reached visible onboarding and a
targeted logcat scan found no app fatal exception or app ANR. No signed
distribution or fresh GitHub CI run is claimed.

## 23. Remaining issues

Repository work: recovery/export guidance for unreadable food JSON beyond the
new non-destructive pause; explicit HC read-error states;
remaining coroutine/UI failure states; localization/chart accessibility;
reference-traced lint/deprecation cleanup; systematic overflow/form
tests for every historical calculation; additional CRUD/UI/performance tests.

Device/external/owner work: supported API/form-factor/TalkBack/OEM matrix,
Health Connect/notifications, oldest shipped DB, license/assets rights,
signing/versionCode, Firebase enforcement/retention, public policy, Play forms,
phone screenshots and final staged-release monitoring. A candidate 1024×500
feature graphic is prepared locally, but has not been uploaded. These categories must not be
conflated: not all remaining work is external/manual.

## 24. Final production checklist

Checkboxes refer to the latest combined revision, not the earlier passing batch.

- [x] Debug build — current revision packaged 28 September
- [x] Release build — current unsigned APK/AAB packaged; signing remains open
- [x] Unit tests — 218 debug + 218 release, 0 failures/errors
- [x] UI/device suite — audit34/API 34 `OK (25 tests)`; not a full device matrix
- [x] Lint — debug/release 0 errors, 846 warnings, 1 hint each
- [x] Database migrations — 13→14, 14→15, 15→16 and chained 13→16 passed in current 25-test suite
- [ ] Security review closure
- [ ] Published accurate privacy policy
- [ ] Owner-confirmed Data Safety
- [ ] All-screen light theme
- [ ] All-screen dark theme
- [ ] Accessibility
- [ ] Full edge-case matrix
- [ ] Performance
- [ ] Crash handling
- [ ] Notifications
- [ ] Play Store metadata
- [ ] Signing configuration
- [ ] Final device testing
