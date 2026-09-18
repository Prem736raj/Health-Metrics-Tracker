# Production Release Checklist

Updated: 2026-09-18

This checklist covers every step from signed AAB to staged Play Store rollout.
Items are ordered by dependency. Do not skip items unless explicitly noted.

## Prerequisites

- [x] Confirm the oldest database version ever distributed to real users.
      If any user received DB version ≤12, create migration fixtures and tests
      before proceeding. If no distribution predates version 13, the current
      migration chain (13→14→15→16) is sufficient. (Schemas 13, 14, 15, 16 verified in androidTest assets).
- [x] Verify CI is green on the release commit (`test`, `lintRelease`,
      `assembleDebug`, `assembleRelease`, `bundleRelease`, `connectedDebugAndroidTest`).
      Verified: Run #35362161609 on commit 9651ddb passed all unit, lint, packaging,
      and emulator instrumented tests.

---

## 1. Release Signing

- [ ] Generate a release keystore (if not already done):
      ```
      keytool -genkeypair -v -keystore health-metrics-release.jks \
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
- [ ] Extract the signing certificate SHA-256 for Firebase:
      ```
      keytool -list -v -keystore health-metrics-release.jks -alias health-metrics-key
      ```
- [ ] Save the R8 mapping file from
      `app/build/outputs/mapping/release/mapping.txt` — needed for Play Console.

## 3. Firebase Configuration

- [ ] In Firebase Console → Project Settings → Your Apps:
      - Verify SHA-256 fingerprint from the release keystore is registered.
- [ ] In Firebase Console → App Check:
      - Register Play Integrity as the attestation provider for the release app.
      - Add the SHA-256 fingerprint.
      - Set enforcement to "Enforced" only after verifying tokens work.
- [ ] Build a release APK, install on a device, and verify:
      - Firebase AI requests succeed (App Check tokens are valid).
      - Analytics events appear (if consent is granted).
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
- [ ] Upload 1024×500 feature graphic
- [ ] Upload at least 2 phone screenshots (min 320px, max 3840px, 16:9 or 9:16)
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
      - App type: Health & Fitness (not Medical Device)
      - Data handling: Health information is stored locally on-device
      - Health Connect: read-only access to Steps and Weight
      - No diagnostic, treatment, or clinical decision support claims

### 4.5 Data Safety Form
- [ ] Complete using the mapping in `docs/PLAY_DATA_SAFETY_GUIDE.md`.
      Summary of declarations:
      - Personal info collected: No (no names, emails, addresses)
      - Health info collected: Yes (locally, for app functionality)
      - Health info shared: No
      - Data encrypted in transit: Yes (HTTPS/TLS)
      - Data deletion: Available in-app
      - App targets children: No

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
- [ ] Monitor App Check enforcement metrics.
- [ ] Monitor user reviews and ratings.
- [ ] Set up Play Console alerts for crash rate spikes.

## 8. Repository Cleanup

- [x] Verify GitHub branch protection ruleset targets `master`:
      Settings → Rules → Rulesets → "Protect master" → Target branches →
      Include → select "Default branch" or type `master`. (Verified and updated with required checks `build` and `instrumented-tests`).
- [x] Close the obsolete "Audit hardening and launch readiness" draft PR. (PR #1 closed).
- [ ] Tag the release commit: `git tag -a v1.0.1 -m "Release 1.0.1"`.
- [ ] Update `docs/RELEASE_STATUS.md` with final evidence.
