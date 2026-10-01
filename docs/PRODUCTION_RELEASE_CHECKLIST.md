# Production Release Checklist

Updated: 2026-09-28

This checklist covers every step from signed AAB to staged Play Store rollout.
Items are ordered by dependency. Do not skip items unless explicitly noted.

Current local pre-signing evidence (28 September): debug and release JVM suites
each pass 218 tests; debug/test APKs build; debug/release lint passes with zero
errors (846 warnings, 1 hint each); unsigned release APK/AAB packaging succeeds;
audit34/API 34 passes 25 instrumentation tests with animations disabled and a
final cached combined Gradle gate reports `BUILD SUCCESSFUL in 48s`. A manual
cold launch reaches onboarding. `verifyPlayReleaseSigning` remains red
until the owner supplies the existing/new Play upload-key credentials.

## Prerequisites

- [ ] Owner must confirm the oldest database version ever distributed to real users.
      If any user received DB version ≤12, create migration fixtures and tests
      before proceeding. If no distribution predates version 13, the current
      migration chain (13→14→15→16) is covered by tests. Checked-in schema
      fixtures do not establish which versions real users received.
- [ ] Verify CI is green on the actual release commit (`test`, `lintRelease`,
      `assembleDebug`, `assembleRelease`, `bundleRelease`, `connectedDebugAndroidTest`).
      Historical run #35362161609 on commit 9651ddb is not evidence for the
      current uncommitted audit changes. See the current audit verification ledger.

---

## 1. Release Signing

- [ ] Confirm whether an upload key already exists and which certificate Play
      Console expects. Do not generate a replacement for an existing app without
      checking its signing history. If this is a new app and no upload key exists,
      generate one in a secure directory outside the repository:
      ```
      keytool -genkeypair -v -keystore /secure/path/health-metrics-release.jks \
        -keyalg RSA -keysize 2048 -validity 10000 \
        -alias health-metrics-key \
        -dname "CN=Health Metrics Tracker, O=Prem736raj"
      ```
- [ ] Store the keystore file outside the repository in a secure location.
- [ ] **Never commit the keystore or passwords to Git.**
- [ ] Set signing credentials as environment variables or `gradle.properties`:
      ```
      RELEASE_STORE_FILE=/secure/path/health-metrics-release.jks
      RELEASE_STORE_PASSWORD=<password>
      RELEASE_KEY_ALIAS=health-metrics-key
      RELEASE_KEY_PASSWORD=<password>
      ```
- [ ] Run `./gradlew verifyPlayReleaseSigning` to confirm credentials are found.

## 2. Build the Signed Release AAB

- [ ] Run the full gate:
      ```
      ./gradlew clean test lintRelease bundleRelease --stacktrace
      ```
- [ ] Verify the AAB exists at `app/build/outputs/bundle/release/app-release.aab`.
- [ ] Validate with bundletool (optional but recommended):
      ```
      bundletool validate --bundle=app/build/outputs/bundle/release/app-release.aab
      ```
- [ ] Extract the upload-key certificate SHA-256 and retain it with the release
      signing records:
      ```
      keytool -list -v -keystore /secure/path/health-metrics-release.jks -alias health-metrics-key
      ```
- [ ] Save the R8 mapping file from
      `app/build/outputs/mapping/release/mapping.txt` — needed for Play Console.

## 3. Firebase Configuration

- [ ] Build a release APK, install on a device, and verify:
      - Analytics remains off before consent.
      - Analytics events appear only after consent is granted.
- [ ] Review Firebase data retention and processor settings.

## 4. Google Play Console Setup

### 4.1 App Creation
- [ ] Create app in Play Console (if not exists).
      - App name: `Health Metrics Tracker`
      - Default language: English (United States)
      - App or Game: App
      - Free or Paid: Free

### 4.2 Store Listing
- [ ] App name: `Health Metrics Tracker` (22 chars, within 30-char limit)
- [ ] Short description: see `docs/ASO_LAUNCH_PLAN.md` (within 80 chars)
- [ ] Full description: see `docs/ASO_LAUNCH_PLAN.md` (within 4000 chars)
- [ ] Upload 512×512 app icon (32-bit PNG with alpha, <1 MB)
- [ ] Review and upload the local [1024×500 feature graphic](store/feature-graphic-1024x500.png)
- [ ] Review and upload at least 2 phone screenshots (min 320px, max 3840px, 16:9 or 9:16); two unedited [API 34 candidates](store/README.md) are now prepared locally
      - Recommended: 8 screenshots per `docs/ASO_LAUNCH_PLAN.md` plan
- [ ] Category: Health & Fitness
- [ ] Tags: select up to 5 relevant tags

### 4.3 Content Rating
- [ ] Complete the content rating questionnaire.
      Key answers:
      - Health/medical information: Yes (informational wellness, not medical device)
      - User-generated content: No
      - Violence: No
      - In-app purchases: No (current version)

### 4.4 Health Apps Declaration
- [ ] Complete the Health apps policy declaration.
      This is **required** for apps that handle health data, including on
      closed/open testing tracks — not just production.
      - Review the relevant Health & Fitness categories and any medical-functionality
        questions against the final app; do not assume a legal classification.
      - Core records are stored locally; optional Firebase Analytics may send
        technical/product-usage data only after consent.
      - Health Connect: read-only access to Steps and Weight
      - Check all calculator, education and listing copy for unsupported diagnosis,
        treatment or clinical decision-support claims.

### 4.5 Data Safety Form
- [ ] Complete using the mapping in `docs/PLAY_DATA_SAFETY_GUIDE.md`.
      There are no pre-approved Yes/No answers: Google Play's collection
      definition concerns off-device transmission, so local storage alone is not
      collection. Review Firebase SDK traffic, user-initiated exports,
      encryption, retention and deletion against the
      final release artifact and service configuration before submitting.

### 4.6 Health Connect Declarations
- [ ] Declare Health Connect permissions used:
      - `android.permission.health.READ_STEPS` — Show daily step totals
      - `android.permission.health.READ_WEIGHT` — Optional weight sync
- [ ] Explain user-facing purpose for each permission.
- [ ] Confirm no write permissions are requested.

### 4.7 Privacy & Terms
- [ ] Set privacy policy URL:
      `https://prem736raj.github.io/Health-Metrics-Tracker/privacy.html`
- [ ] Set terms URL:
      `https://prem736raj.github.io/Health-Metrics-Tracker/terms.html`
- [ ] Verify both URLs resolve correctly from a clean/incognito browser.
- [ ] Set support email address.

## 5. Testing Tracks

### 5.1 Closed Testing (Recommended First)
- [ ] Create a closed testing track.
- [ ] Upload the signed release AAB.
- [ ] Add test users (email list or Google Group).
- [ ] If this is a personal developer account created after 13 November 2023,
      keep at least 12 testers opted into the closed test continuously for at
      least 14 days, then apply for production access in Play Console.
- [ ] Submit for review.

### 5.2 Pre-Launch Report
- [ ] Review the automated pre-launch report in Play Console.
      Check for:
      - Crashes/ANRs
      - Accessibility issues
      - Security vulnerabilities
      - Performance problems
- [ ] Fix any P0/P1 issues found.

### 5.3 Open Testing (Optional)
- [ ] Expand to open testing after closed testing passes.

## 6. Production Release

- [ ] Upload R8 mapping file to Play Console → App Bundle Explorer.
- [ ] Create a managed production release.
- [ ] Set staged rollout percentage:
      - Start at 1%
      - After 48h with no critical issues: 5%
      - After 1 week: 25%
      - After 2 weeks: 100%
- [ ] Write release notes (see `docs/ASO_LAUNCH_PLAN.md` draft).
- [ ] Submit for review.

## 7. Post-Release Monitoring

- [ ] Monitor crash/ANR rates in Play Console → Android Vitals.
- [ ] Monitor Firebase Crashlytics (if configured).
- [ ] Monitor user reviews and ratings.
- [ ] Set up Play Console alerts for crash rate spikes.

## 8. Repository Cleanup

- [x] Verify GitHub branch protection ruleset targets `master`:
      Settings → Rules → Rulesets → "Protect master" → Target branches →
      Include → select "Default branch" or type `master`. (Verified and updated with required checks `build` and `instrumented-tests`).
- [x] Close the obsolete "Audit hardening and launch readiness" draft PR. (PR #1 closed).
- [ ] Tag the release commit: `git tag -a v1.0.1 -m "Release 1.0.1"`.
- [ ] Update `docs/RELEASE_STATUS.md` with final evidence.
