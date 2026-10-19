# Play Store readiness audit — 5–6 October 2026

**Status: local verification passed; production upload has outstanding gates.**
This report supersedes completion claims in September audits and the 1 October
readiness text. Health content has been corrected, but build success is not
medical certification or Play approval. No store submission or policy publication
has been performed.

## Release identity

- App: Health Metrics Tracker; application ID `com.healthmetrics.tracker`.
- Current source: version `1.0.1` / code `2`, min SDK 26, target/compile SDK 36.
- Kotlin 2.0.21, AGP 8.13.2, Gradle 8.13, JDK 17; Room schema remains 16.
- This is an uncommitted working-tree audit. Record the eventual release commit,
  exact bundle hash and signing certificate before distribution.
- Confirm code `2` exceeds every version already uploaded to this app's Play
  account. The local repository cannot establish Console version history.

## Corrections made

See the [claims and suggestions ledger](CLAIMS_AND_SUGGESTIONS_2026-10-05.md)
for the exact affected surfaces and references. Major changes remove personalized
BSA medication examples, unsupported heart-rate fuel/calorie promises and
generated WHO schedules, universal hydration limits and waist-change promises,
and BP lifestyle/medication claims. Severe BP guidance is now symptom-first.

Behavior fixes cover workout calorie/macro consistency, energy illustrations based
on the displayed intake after the planning minimum, invalid meal shares and
energy inputs, BMI goal progress after crossing a target, conflicting goal
prompts, hydration prompts, history/BP storage errors and atomic bulk deletion.
Health Connections and the complete Profile page now scroll together at large
text sizes; navigation labels, water title and weight comparison/filter layouts
were corrected after screenshot review. The populated weight statistics header
wraps its trend badge as a whole at large text sizes and uses the correct entry
count. Smart suggestion actions now reach water, food and weight tracking;
near-goal copy stays neutral after a target is crossed. Settings
describe calculator-history deletion correctly. Current listing copy no longer
advertises disabled family tracking or the removed AI assistant.

Release tooling adds `bundlePlayRelease`, which requires signing configuration,
and a reproducible native ELF alignment checker in CI. The build metadata limit
was increased after an actual compiler allocation failure. Ordinary unsigned
`bundleRelease` remains useful for engineering validation.

## Coverage and limits

The static inventory contains 504 main Kotlin files. Searches covered navigation,
calculator/recommendation models, user-facing strings/education, persistence,
exports, Android permissions, background entry points and listing/privacy drafts.
This inventory is not a claim that every line or every possible user journey was
manually tested. The feature matrix below identifies evidence and remaining scope.

| Feature group | Audit / automated coverage | Remaining device scope |
|---|---|---|
| Onboarding, Home, Track, Calculators, Insights, Profile, Settings | Existing onboarding tests plus new destination smoke in light and dark/2x text | TalkBack, landscape, tablets, process death and additional Android versions |
| BMI and weight goals | Adult arithmetic/input/goal tests; fixed target-crossing behavior | Complete keyboard/unit/goal journeys across locales |
| BMR, TDEE, calories and macros | Formula boundaries, TEF accounting, finite input and meal/workout allocation regressions | All result tabs, diet presets, restored forms and restrictive-diet clinical review |
| BSA, reference weight, metabolic indicators, waist ratios | Formula/boundary tests; removed dosing surface; informational framing | Clinician/editorial review of retained reference context; no disease/organ-function validation |
| BP calculator, journal, trends and education | Either-value severe boundaries; shared urgency copy; storage failure handling | Physical monitor workflow, repeated-reading result journey and clinical review |
| Water journal, history, education, streaks, tools and electrolytes | Model/storage policies, reminder policies and route smoke; corrected drinking copy | Day rollover, repeated taps, rapid widget logs, real fluid-restriction scenarios |
| Heart-rate zones and exercise journal | Adult/max/reserve tests; recommendation regression; explicit intensity-selection UI test | Exercise/provider measurements and medicines cannot be validated by an age equation |
| Food diary and presets | Existing storage/corruption/retention tests; manual-food scope retained | Disk-full, many entries, locale input and shared-target behavior on real devices |
| History, export, data management | Existing SQLite transaction/retention/FileProvider/encoding tests; safe error display and atomic selected deletion | Recipient apps, cache expiration, user-picked destinations and full deletion walkthrough |
| Reports, achievements and wellness score | Aggregation/policy tests; informational logging-score label | Long-history performance, date-window UI and chart accessibility |
| Room upgrades | Instrumented 13→14, 14→15, 15→16 and chained upgrade tests | Confirm the oldest DB ever distributed; add older fixtures if pre-13 was shipped |
| Reminders and widgets | Schedule/entry allow-list/manifest policies; settings-route smoke | Actual denial/revocation, reboot, clock/timezone/DST and OEM battery behavior |
| Health Connect | Separate read-only steps/weight permissions; unavailable/denied paths retain manual tracking | Real provider, multiple sources, permission revocation and read failures; some manager failures still appear as empty data |
| Analytics/privacy/security | Default-off collection, event allow-list, backup/TLS/scoped-provider review and merged manifest inspection | Network capture with consent off/on and owner Firebase retention/processing review |

## Verification ledger

The final source passed the combined debug/release build, unit, lint, APK and AAB
gate in **9 minutes 56 seconds**. The machine-readable
[verification record](PLAY_VERIFICATION_2026-10-05.json) includes artifact hashes,
source identity and local evidence paths.

| Final check | Result |
|---|---|
| Debug unit suite | 219 tests; zero failures, errors or skips |
| Release unit suite | 219 tests; zero failures, errors or skips |
| Debug / release lint | Zero errors; 895 warnings and one informational finding in each variant |
| API 34 instrumentation | 30 tests passed in 43.095 seconds |
| Destination smoke | 40 primary destinations in light/1x and dark/2x Compose text; 82 foreground-checked captures |
| Minified release QA | Four cold-launch destinations passed; saved synthetic weight entries present; populated weight header checked at OS text scale 2x |
| Release bundle structure | Official bundletool validation passed |
| Native alignment | Eight bundled native libraries inspected; all four required 64-bit libraries passed 16 KB ELF alignment checks |
| APK / bundle page alignment | APK `zipalign -P 16` passed; bundle reports `PAGE_ALIGNMENT_16K` |
| Upload signing | Release APK/AAB remain unsigned; `bundlePlayRelease` correctly failed before bundle packaging when signing values were absent |
| Listing file formats | Icon, feature graphic and two refreshed 24-bit screenshots passed the technical asset checker |

The minified release was tested using a separate copy signed with the standard
Android debug certificate. This is QA evidence, not a production upload identity.
The API 34 emulator uses 4 KB pages, so static alignment results do not establish
16 KB runtime compatibility. Remaining lint warnings have not been waived as
universally harmless. No app crash or ANR was found in the final device event
scan; an earlier emulator boot produced a SystemUI ANR before the app launched.

The navigation smoke covers 40 primary destinations in each of two modes. It
checks navigation and rendered text, asserts that Profile details can be reached
by scrolling, and captures screenshots. It does not submit
every calculator, open every educational accordion, or prove every CRUD/error
journey. The separate BSA and exercise-intensity tests target removed unsafe
presentation and the new required intensity choice.

## Privacy, manifest and store assets

The merged release manifest targets 36/minimum 26, disables Android backup and
has no advertising-ID or broad storage permission. Health permissions are only
`READ_STEPS` and `READ_WEIGHT`. Notification, network, boot/wake-lock and
dependency-contributed install-referrer/foreground-service permissions remain.
Core health records stay in app-private stores. Exported files and optional
Firebase processing have separate privacy implications; no app-managed database
encryption, cloud restore or server-side deletion is claimed.

Both public privacy and terms URLs returned HTTP 200 in this session. The privacy
page served **older content**, not the locally updated 5 October version. Publish
[privacy.html](../privacy.html), then verify the deployed page and contact details.
The existing Data Safety worksheet requires review against actual Firebase and
Play configuration; local storage alone is not Play off-device collection.

Local artwork was checked visually and technically: icon 512×512, feature graphic
1024×500 and two refreshed screenshots 1080×1920. The screenshots show the final
minified release QA copy, use synthetic data and were converted losslessly to
24-bit PNG without alpha. They are listing candidates; review them against the
actual Play-signed build and Console preview. The separate 1080×2400 device QA
captures exceed Play's 2:1 screenshot aspect limit and are not upload assets.
See the [artwork provenance](../store/README.md) and
[Play preview-asset requirements](https://support.google.com/googleplay/android-developer/answer/9866151).

## Gates before distribution

1. Supply private upload-key configuration and confirm the certificate expected
   by Play. An ignored `.jks` exists locally, but no signing credentials were
   available. Do not generate a replacement key blindly.
2. Build with `gradlew.bat bundlePlayRelease`; validate the actual bundle's
   signature/certificate with JDK/Play tooling. The ELF checker's signature-marker
   flag is only a sanity check, not cryptographic verification.
3. Publish the updated policy; finish Data Safety, Health apps, Health Connect,
   content rating, target audience, ads declaration and store listing in Console.
   Use the actual implementation/configuration, not blanket “no data collected.”
4. Run the signed Play-delivered build through pre-launch testing, including API
   26/33/35/36, a 16 KB environment, small/large displays, TalkBack, real Health
   Connect, notification lifecycle and OEM behavior. Retain any failure evidence.
5. Complete the applicable closed test and production-access review. For personal
   accounts created after 13 November 2023, current guidance specifies at least
   12 continuously opted-in testers for 14 days. Confirm account applicability.
6. Obtain qualified review of retained health educational content, resolve
   significant device failures, record final artifact identity and use a staged
   rollout with crash/ANR monitoring.

## Reproduction

```powershell
./gradlew.bat testDebugUnitTest assembleDebug assembleDebugAndroidTest --max-workers=1
./gradlew.bat testReleaseUnitTest lintDebug lintRelease assembleRelease bundleRelease --max-workers=1
# A running Android device/emulator is required:
./gradlew.bat connectedDebugAndroidTest --max-workers=1
python tools/verify_release_artifact.py app/build/outputs/bundle/release/app-release.aab
python tools/verify_store_assets.py
# After private signing values are configured:
./gradlew.bat bundlePlayRelease --max-workers=1
python tools/verify_release_artifact.py app/build/outputs/bundle/release/app-release.aab --require-signed-bundle
```

## Current authoritative release references

- [Google Play target API requirements](https://support.google.com/googleplay/android-developer/answer/11926878)
- [Google Play health-content policy](https://support.google.com/googleplay/android-developer/answer/16679511)
- [Health apps declaration](https://support.google.com/googleplay/android-developer/answer/14738291)
- [Android 16 KB page-size guidance](https://developer.android.com/guide/practices/page-sizes)
- [Personal-account testing requirements](https://support.google.com/googleplay/android-developer/answer/14151465)
- [Data Safety engineering worksheet](../PLAY_DATA_SAFETY_GUIDE.md)
