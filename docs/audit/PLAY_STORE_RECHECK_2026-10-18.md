# Play Store first-upload recheck — 18 October 2026

**Verdict: not ready for a production release or for uploading the current unsigned AAB.** This review separates freshly executed checks from historical evidence and unverified release-owner/Play Console requirements. The owner confirmed this is the first upload. The decision is supported by the unsigned artifact, stale public disclosures, a reproduced locale defect, and incomplete fresh release/device verification.

## Release identity and scope

- Current checkout: `P:\Projects\health`; the previous `R:\New folder` location no longer exists.
- Health Metrics Tracker, application ID `com.healthmetrics.tracker`, version `1.0.1`, version code `2`, minimum SDK 26, target/compile SDK 36.
- The owner confirmed this will be the first upload. Code 2 is valid for the initial upload; later uploads must use a larger version code.
- All files in the previous release source snapshot match their recorded hashes. The current app has 504 main Kotlin files and contains uncommitted changes; this review does not claim that the current source has passed remote CI.
- No application source, signing key, published website, Firebase configuration or Play Console setting was changed in this review. Generated verification output and this new audit report are the write scope.

## Findings

### P1 — Existing AAB is unsigned

The existing `app/build/outputs/bundle/release/app-release.aab` has no JAR signature markers. JDK `jarsigner -verify` reports `jar is unsigned.` The release Gradle configuration deliberately permits ordinary unsigned engineering builds. A keystore existing on disk does not make the AAB signed.

Configure the intended private upload key and build using `gradlew.bat bundlePlayRelease`. Verify the resulting signature and retain its certificate/hash before uploading. Do not upload a debug-signed QA copy or generate a replacement key without identifying the existing local key first.

Evidence: `app/build.gradle.kts:24`, `app/build.gradle.kts:245`, and signature/asset verification output recorded during this review.

### P1 — Public privacy policy and terms do not match the app

Both canonical public URLs returned HTTP 200, but their content differs from the local corrected HTML:

- The public privacy policy is dated 30 August 2026 and describes AI Wellness Assistant queries, Firebase AI Logic/Gemini and App Check/Play Integrity.
- The current app does not implement those AI services. Its local privacy policy dated 5 October describes the actual profile, food, calculator, optional analytics and deletion handling.
- The public terms still discuss the AI assistant and AI prompts; the corrected local terms remove them.
- The live policy's deletion statement omits the corrected distinction between local resets, Health Connect source records, recipient export copies and third-party processing.

Publish the reviewed local `docs/privacy.html` and `docs/terms.html` to the canonical public URLs, then recheck those URLs before submission. This audit did not publish them.

Evidence: `scratch/play-recheck-2026-10-18-links-and-assets.json`, `scratch/public-recheck-privacy.html`, `scratch/public-recheck-terms.html`, and `app/src/main/java/com/health/calculator/bmi/tracker/core/constants/AppLinks.kt:8`.

### P2 — Water calculator breaks numeric input in comma-decimal locales

`WaterIntakeViewModel.toggleUnit()` and `loadFromProfile()` use default-locale `String.format("%.1f", ...)`, but `validate()` parses the resulting string with locale-independent `toFloatOrNull()`. Under `Locale.FRANCE`, conversion produces `154,3` and profile autofill produces `70,0`; Kotlin's actual `toFloatOrNull` returns null for both. The US-locale control produces `154.3` and parses successfully.

This is a reproduced formatting/parsing defect in the source input path, not a reproduced complete Android UI journey. Keep numeric backing state locale-independent or parse localized input consistently, and add a locale regression check before a broad public launch. Similar formatting elsewhere merits focused follow-up; this finding does not claim every `DefaultLocale` warning is a bug.

Evidence: `app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/waterintake/WaterIntakeViewModel.kt:71`, `:112`, `:139`; reproducer `scratch/LocaleNumberRecheck.java`; observed output `scratch/play-recheck-2026-10-18-locale.log`.

### First-release checklist correction

The older production checklist recommends a 1% staged rollout. Google permits percentage-based staged rollouts for updates, not a first production release. Use internal/closed testing to limit the initial audience. The first production release goes to the selected production countries/regions.

For personal developer accounts created after 13 November 2023, production access requires at least 12 testers continuously opted in for 14 days, followed by the production-access application. The account creation date/type has not been established here.

## Fresh verification ledger

| Check | Observed result |
|---|---|
| Debug unit tests | 219 tests, zero failures/errors/skips; reports generated 18 October |
| Release unit tests | 219 tests, zero failures/errors/skips; reports generated 18 October |
| Debug lint | Fresh report: zero errors, 895 warnings and one informational finding |
| Release lint | Not completed in this recheck; the existing 6 October report is historical and is not counted as a fresh pass |
| Complete debug/instrumentation/release APK and AAB rebuild | Not completed; compilation and unit checks executed, but the remaining lint/packaging gate was stopped under host memory pressure |
| API 34 instrumentation and navigation smoke | Not rerun in this session; the new audit emulator booted, but tests were not executed against a completed rebuild |
| Minified release QA cold launches | Not rerun in this session; older launch evidence is historical |
| Existing AAB structure | Revalidated today using official bundletool; exit 0 |
| Existing release APK ZIP alignment | Revalidated today using SDK 36 zipalign with `-P 16`; exit 0 |
| Existing bundle native ELF alignment | Eight libraries inspected; all four required 64-bit libraries pass 16 KB LOAD alignment; this is not a 16 KB runtime test |
| Existing AAB signature | JDK jarsigner reports `jar is unsigned.`; upload signing is blocked |
| Existing icon and feature graphic formats | Passed; 512 × 512 RGBA icon and 1024 × 500 RGB feature graphic |
| Screenshot pack | All 12 polished captures are unchanged from their recorded hashes, 1080 × 1920, 24-bit RGB PNG, each below 8 MB; choose up to 8 for phone uploads |
| Store description sizes | Name 22, short description 73, full description 1,820 characters; within 30/80/4,000 limits |
| Public policy URLs | HTTP 200; substantive stale-content mismatch confirmed |
| Source identity | No differences from the previous final release source snapshot |

The initial two-worker forced rebuild was stopped after reaching high JVM heap pressure. A single-worker retry generated both fresh unit suites and the fresh debug-lint report. Release lint then made very slow progress with roughly 0.5–0.7 GB free physical RAM. A further smaller-heap attempt also progressed poorly, so the agent stopped the verification JVMs and recorded the remaining checks as unverified. These interrupted jobs are not evidence of an app compilation failure. Application source was not changed or left partially edited.

The APK/AAB revalidated above are the existing 6 October artifacts, not a newly packaged release. Their exact hashes, report timestamps, source identity, native alignment and file-format evidence are recorded in [the machine-readable recheck](PLAY_STORE_RECHECK_2026-10-18.json). Both unit suites' report timestamps are 18 October. Debug lint retains 214 DefaultLocale warnings, 31 SmallSp warnings and 9 ContentDescription warnings, among other categories; zero lint errors does not establish that all warnings are harmless.

## Privacy and health implementation checks

The newly merged release manifest requests only read steps and read weight health permissions; no health writes, advertising identifiers or broad storage permissions are shipped. Android backup is disabled, the release manifest does not enable debugging, and network security rejects cleartext traffic. Firebase analytics and ad-personalization defaults are false.

Source review confirms a default-off analytics adapter with event/parameter allowlisting, a visible opt-in setting, feature-specific optional Health Connect access, a scoped export FileProvider, export medical disclaimers, and full local reset through Android's `clearApplicationUserData()` API. These source checks do not prove actual SDK network traffic or Firebase server retention/deletion settings.

Health Connect read exceptions can still be represented as empty/no data. Provider failure/revocation and real-device sync behavior remain explicit device-test scope. Calculator unit tests are not clinical certification of health education or individual medical suitability.

## Owner/Console requirements still to verify

- Intended upload signing key/certificate; Play App Signing setup and final signed AAB.
- Health apps declaration; Health Connect permission/use-case declaration for READ_STEPS and READ_WEIGHT.
- Data Safety answers based on optional Firebase processing and actual release/network behavior; do not submit blanket “no data collected” solely because core records stay local.
- Target audience, content rating, ads declaration, support contact, countries, price and final listing preview.
- Real Health Connect provider, analytics consent/network behavior, notification denial/revocation/reboot, exports to recipient apps and a full local-reset walkthrough.
- Additional Android versions including minimum API 26 and target API 36, 16 KB runtime testing, small/large screens, TalkBack, rotation/process restoration and OEM behavior.
- Play-delivered signed build, pre-launch report and any applicable closed test/production-access review.

## Official references checked in this review

- [Target API requirements](https://support.google.com/googleplay/android-developer/answer/11926878?hl=en): current phone-app submissions target API 36 or later.
- [Health content and services](https://support.google.com/googleplay/android-developer/answer/16679511?hl=en).
- [Health apps declaration](https://support.google.com/googleplay/android-developer/answer/14738291?hl=en).
- [Android 16 KB guidance](https://developer.android.com/guide/practices/page-sizes): check ELF/ZIP alignment and actual runtime behavior; RELRO padding alone does not establish a defect when writable data is on a separate page.
- [Screenshot specifications](https://support.google.com/googleplay/android-developer/answer/9866151?hl=en).
- [Personal-account testing](https://support.google.com/googleplay/android-developer/answer/14151465?hl=en).
- [Staged rollouts apply to updates](https://support.google.com/googleplay/android-developer/answer/6346149?hl=en-en).

## Exact next steps

1. Fix the water calculator's locale formatting/parsing mismatch and add a comma-decimal regression test.
2. Free memory on the verification machine and finish the remaining local gate, then rerun tests after any source fix:

   ```powershell
   $env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot'
   .\gradlew.bat testDebugUnitTest testReleaseUnitTest lintRelease assembleDebug assembleDebugAndroidTest assembleRelease bundleRelease --max-workers=1 --no-daemon
   # After starting a dedicated Android test device/emulator:
   .\gradlew.bat connectedDebugAndroidTest --max-workers=1 --no-daemon
   ```

3. Test the actual minified release copy, including core calculator/logging/export/reset journeys, and inspect the Play-delivered build's pre-launch report after a testing-track upload. The prepared local cold-launch helper is `scratch/play_recheck_minified.py`; it targets only the dedicated `emulator-5556` and requires an installed QA-signed release copy and completed onboarding.
4. Publish the corrected privacy and terms pages and verify the canonical URLs again.
5. Configure the intended private upload signing credentials, run `gradlew.bat bundlePlayRelease`, and validate the final AAB/certificate. The ordinary `bundleRelease` command alone can produce an unsigned artifact.
6. Complete Play Console setup/declarations and begin the appropriate test track. If the personal-account testing rule applies, complete it and obtain production access before the first public release.

No store upload, public policy publication, signing-key change, Firebase configuration change, or app-code fix was performed.

VERDICT: FAIL -- release prerequisites are unresolved and fresh release/device verification is incomplete.
