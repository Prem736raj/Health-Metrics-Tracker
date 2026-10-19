# Health Metrics Tracker

A native Android wellness journal for logging everyday measurements, viewing
trends, and using educational health calculators. Core tracking is local; Health
Connect and product analytics are optional integrations.

**Release status:** under active verification. A successful build is not proof of
medical accuracy or Play Store approval. See the [current production audit](docs/audit/PLAY_STORE_READINESS_2026-10-05.md)
for verified results, implemented fixes and remaining release gates.

Audit navigation: [readiness report](docs/audit/READINESS_REPORT.md) ·
[feature inventory](docs/audit/FEATURE_AUDIT.md) ·
[exact changes](docs/audit/CHANGE_INVENTORY.md).

## What is implemented

- Manual weight, blood-pressure, hydration and food/calorie logging.
- Calculator history, trends, weekly reports, reminders and home-screen widgets.
- BMI, BMR, waist/hip, hydration, BSA, reference body weight, daily calorie,
  heart-rate-zone and metabolic-indicator calculations.
- Light, dark and system themes; metric/imperial entry in supported calculators.
- PDF/CSV/JSON exports and sharing, with sensitive-data disclosures.
- Optional read-only Health Connect steps and weight.

These are source-implemented capabilities, not a claim that every journey has
passed device testing. The food diary has manual entries and presets, not a
barcode scanner or comprehensive nutrition database. No account, cloud restore,
subscription or advertising flow is implemented. Family tracking is currently disabled because core health records are not
scoped to separate profiles.

## Requirements and local build

- Android 8.0 / API 26 or newer to run the app.
- JDK 17, Android SDK Platform 36, and a compatible Android Studio installation.
- Gradle 9.6.0 via the checked-in wrapper; AGP 9.4.1 and Kotlin 2.2.10.
- Set your SDK location in ignored `local.properties`, or configure the SDK through
  Android Studio. Never commit machine paths or signing credentials.

```sh
./gradlew testDebugUnitTest assembleDebug lintDebug
./gradlew assembleRelease bundleRelease
# Requires a running emulator or connected device:
./gradlew connectedDebugAndroidTest
```

On Windows use `gradlew.bat`. Release packaging without credentials produces an
**unsigned validation artifact**, not an upload-ready release. Supply
`RELEASE_STORE_FILE`, `RELEASE_STORE_PASSWORD`, `RELEASE_KEY_ALIAS`, and
`RELEASE_KEY_PASSWORD` as environment variables or private Gradle properties.
Run `bundlePlayRelease` to verify signing configuration and build a distribution artifact. Do not
put signing material in this repository.

The included Firebase client configuration is not a server credential. To develop
against your own Firebase project, configure your own Android application with
the correct application ID and signing certificates.

## Architecture

Single `:app` module using Kotlin, Jetpack Compose/Material 3, Navigation, Hilt,
Room 2.7.1 (schema v16), DataStore, SharedPreferences, WorkManager and Android alarm APIs.

| Directory | Responsibility |
|---|---|
| `app/src/main/java/.../presentation`, `ui` | Screens, state and ViewModels |
| `.../data` | Persistence, repositories, exports and optional service adapters |
| `.../domain`, `calculator`, `util` | Calculation and tracking policies |
| `.../core/navigation` | Routes and navigation graph |
| `.../notifications`, `notification`, `receiver`, `widget` | Reminders and widgets |
| `app/schemas` | Versioned Room schemas; preserve released migrations |
| `app/src/test`, `app/src/androidTest` | JVM, migration and Compose/device tests |
| `docs/audit` | Current findings, inventory and verification evidence |
| `docs/archive` | Historical reports, not current release approval |

## Privacy and health scope

Core records use Android app-private storage; no database-level encryption is
claimed. Automatic Android backups are disabled. Exports leave the sandbox when
you choose a destination. Optional product analytics is off by default and does
not include raw health measurements or free-form notes. Local deletion does not
delete previously exported copies or information already processed by external
services.

[Privacy policy](https://prem736raj.github.io/Health-Metrics-Tracker/privacy.html)
· [Terms](https://prem736raj.github.io/Health-Metrics-Tracker/terms.html)
· [Data Safety engineering mapping](docs/DATA_SAFETY_MAPPING.md)
· [Medical references](docs/MEDICAL_REFERENCES.md)

The app provides educational estimates, not diagnosis, treatment or prescribing.
Do not use calculator outputs for medication dosing or urgent medical decisions.

## Contributing and security

Read [CONTRIBUTING.md](CONTRIBUTING.md) and [SECURITY.md](SECURITY.md).
No open-source license has been selected by the owner yet. Public source access
does not itself grant redistribution rights; obtain permission before reuse.
The existing `master` default branch is retained. Use focused branches and
reviewed pull requests; do not rewrite public history for cosmetic cleanup.

Screenshots and store metadata must come from a tested release candidate, not
generated mockups. See [release checklist](docs/PRODUCTION_RELEASE_CHECKLIST.md)
and the current audit before publishing.
