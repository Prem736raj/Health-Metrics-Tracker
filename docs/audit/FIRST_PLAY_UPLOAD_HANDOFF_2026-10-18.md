# Health Metrics Tracker — first Play upload handoff

Checked on 18–19 October 2026 against the current local working tree in `P:\Projects\health`.

## Current decision

**A signed and technically validated first-upload AAB is ready for a Play Console internal/closed-testing submission.** The app is **not yet approved for public production release**: Play Console declarations, account/testing eligibility, device verification and review remain. No Play Console upload was performed here.

## Upload file and release identity

| Field | Verified value |
| --- | --- |
| App name | Health Metrics Tracker |
| Application ID | `com.healthmetrics.tracker` |
| Version | `1.0.1`, code `2` (allowed for first upload) |
| Minimum / target SDK | 26 / 36 |
| Signed AAB | `app/build/outputs/bundle/release/app-release.aab` |
| AAB size | 16,577,705 bytes |
| AAB SHA-256 | `ABA4B30881CF4798914D8078E80CB58BC8BAD052ACAFC7B373096DF655F3CEF0` |
| Upload key alias | `health-metrics-upload` |
| Upload certificate SHA-256 | `44:8D:50:29:37:D0:69:96:B7:61:2A:75:A7:9D:09:C5:8B:2A:5E:EE:71:1C:91:17:A6:ED:EC:94:85:B0:48:4F` |
| R8 mapping | `app/build/outputs/mapping/release/mapping.txt` (back this up) |

The separate, newly authorized upload key is stored under the Windows account's private directory `C:\Users\prem7\.android\health-metrics-tracker-upload-2026-10-18\`. Its keystore is `health-metrics-upload.jks`. The local `signing.json` uses Windows DPAPI for this account and **cannot** recover the password on another machine. `RECOVERY-PRIVATE.txt` contains the private password: store the key and password in secure, separate backups, never commit them or include them in Play listing/media packages. The previous root keystore was preserved.

Google Play App Signing typically distinguishes the **upload certificate** from the **app signing certificate** managed by Google. Enroll in Play App Signing during first upload and retain the upload key for future updates.

## Evidence

- Both debug and release JVM test suites passed on 18 October: **222 tests each, 0 failures/errors/skips**. Source: `app/build/test-results/testDebugUnitTest/` and `app/build/test-results/testReleaseUnitTest/`.
- `tools/Build-PlayRelease.ps1 -VerifyOnly` passed. `tools/Build-PlayRelease.ps1` then passed the signed release AAB and release APK build (`BUILD SUCCESSFUL in 9m 27s`). Build log: `scratch/play-signed-build-2026-10-18.log`.
- JDK `jarsigner -verify` returned exit `0` with a verified AAB. `keytool -printcert -jarfile` matched the new upload certificate fingerprint above. Android `apksigner verify` returned exit `0` with a single RSA-3072 signer. The signed APK was successfully verified by SDK 36 `zipalign -c -P 16 -v 4`.
- Official bundletool 1.18.3 `validate` exited `0`; its manifest inspection confirmed package, SDK levels and version. The signed bundle passed an archive integrity scan (588 ZIP entries, no corruption). `tools/verify_release_artifact.py` reported all required 64-bit ELF native libraries aligned for 16 KB pages (8 libraries inspected); **runtime** 16 KB-page testing remains outstanding.
- `docs/store/listing-2026-10-09/` contains **12 polished 1080 × 1920 PNG screenshots** captured from the real minified QA app with synthetic data. Its `recommended-8/` folder is the proposed upload order; `preview.jpg` is a contact sheet, not an upload image. On **19 October**, all 12 `polished/<id>.png` files were rehashed against `validation.json` (`finalSha256`): **12 matched, 0 missing/mismatched**, and the recommended folder contained eight PNGs. The `docs/store/Health-Metrics-Tracker-Play-Store-Images.zip` package was also inspected: **28 entries, CRC clean, all 12 polished-image SHA-256 hashes matched, and all eight recommended copies were identical to their polished originals**. The separate icon at `docs/play_store_icon_512.png` and feature graphic at `docs/store/feature-graphic-1024x500.png` passed technical checks via `tools/verify_store_assets.py`.
- Corrected public privacy/terms disclosures were merged in [PR #10](https://github.com/Prem736raj/Health-Metrics-Tracker/pull/10) after both CI jobs succeeded. The deployed pages responded HTTP 200 and no longer reference removed AI Wellness Assistant/Firebase AI Logic features: https://prem736raj.github.io/Health-Metrics-Tracker/privacy.html and https://prem736raj.github.io/Health-Metrics-Tracker/terms.html.
- Water-intake/BMR comma-decimal parsing and locale-invariant formatting were fixed in `DecimalInput.kt`, with three additional regression tests covering several locales and invalid values.
- **Full release lint: passed** (`:app:lintRelease`, `BUILD SUCCESSFUL in 10m 15s`), from `scratch/play-lint-release-2026-10-18.log`. The XML report at `app/build/reports/lint-results-release.xml` contains **0 errors, 885 warnings, and 1 hint**. Frequent advisory findings are `UnusedResources` (211), `DefaultLocale` (204), `UseKtx` (172), `HardcodedText` (87), `UnusedAttribute` (38), and `SmallSp` (31); nine `ContentDescription` warnings merit future accessibility review. This clean Gradle exit does **not** mean the lint report has zero warnings. The `DataExtractionRules` warning references `android:allowBackup="false"` in the current manifest; verify actual transfer/backup behavior on devices rather than assuming the warning indicates that backup is enabled.
- **19 October artifact confirmation:** the signed AAB SHA-256 still matches `ABA4B30881CF4798914D8078E80CB58BC8BAD052ACAFC7B373096DF655F3CEF0`. The release JVM XML results were recounted: **56 suites, 222 tests, zero failed/errored/skipped**. `adb devices -l` still listed no connected devices.

## Steps in Play Console

1. Create **Health Metrics Tracker** as an **App**, **Free**, default language English. Set category **Health & Fitness**, support email, and intended distribution countries. Do not change the existing package ID.
2. Complete **App content**: privacy policy URL, app access, ads declaration, target audience, content rating, **Health apps declaration** and Health Connect read use cases (`READ_STEPS`, `READ_WEIGHT`). Prepare Data Safety using the real handling of local health records, export sharing and optional consent-gated Firebase Analytics. Do not assert that optional analytics never transmits data without device/network verification.
3. Add title and short/full descriptions from `docs/ASO_LAUNCH_PLAN.md`; upload `docs/play_store_icon_512.png`, `docs/store/feature-graphic-1024x500.png`, and **up to eight** images from `docs/store/listing-2026-10-09/recommended-8/`. Compare the marketing screenshots with the **final Play-delivered build** before submission; the screenshot captures are dated 9–10 October and app code has since changed.
4. Start with an **internal test** or **closed test** release. Upload `app/build/outputs/bundle/release/app-release.aab`, enroll in Play App Signing, inspect the generated app certificate, and save/associate the upload certificate fingerprint above. Check policy warnings and the pre-launch report, then install the Play-delivered build for end-to-end QA.
5. If this is a **personal developer account created after 13 November 2023**, Google requires at least **12 testers opted into closed testing for 14 continuous days** and a production-access application before production launch. Check account eligibility directly in Console. A percentage-based staged rollout is for updates; it does not gate a first production launch.

## Remaining technical / owner checks

- Run device QA on the Play-delivered build: onboarding, profile, BMI/BMR, water/food/weight/BP history, Health Connect permissions/revocation, exports, local data deletion, consent-gated analytics network behavior, reminders across reboot/timezone, and crash/ANR monitoring.
- Exercise API 26, 33, 34, 35, 36 and a 16 KB page-size device; check larger font/TalkBack and small/large screens. At the time of the audit, `adb devices` returned no connected emulator/device, so there is no fresh 18 October instrumentation run or Play pre-launch report for the signed build.
- Capture a reproducible **release source commit** and rerun CI on it before broad rollout. The local branch `ui-polish-dark-light-mode` has pre-existing uncommitted changes; the green CI for privacy PR #10 covers the policy pages, **not** this working-tree AAB. Preserve/resolve those local edits deliberately; do not reset or commit them wholesale.
- Confirm Play Console declaration answers, supported countries, tester eligibility, signed delivery, and Google review outcome. These account-specific facts are not established by the successful local build.

For future builds, set `JAVA_HOME` to the installed JDK 17, then run `tools/Build-PlayRelease.ps1`; it reads the private signing config outside Git and validates the generated bundle. Keep the keystore and private recovery materials outside the repository.
