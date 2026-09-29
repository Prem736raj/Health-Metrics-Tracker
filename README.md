# Health Metrics Tracker

Health Metrics Tracker is an Android wellness and health-metrics app focused on local tracking, informational calculators, and privacy-conscious workflows.

## Highlights

- Track common wellness and body metrics locally on Android.
- Use informational calculators for areas such as BMI, hydration, blood pressure, body composition, and related trends.
- Optional Firebase Analytics is consent-gated and kept separate from signing and other private credentials.
- Release signing credentials and secret material are intentionally kept outside version control.

## Build and verify

Use the committed Gradle wrapper:

```bash
./gradlew test
./gradlew lintRelease
./gradlew assembleDebug
./gradlew assembleRelease
./gradlew bundleRelease
```

On Windows, use `gradlew.bat` instead of `./gradlew`.

## Release and security notes

The repository contains detailed operational documentation:

- [Release status](docs/RELEASE_STATUS.md)
- [Production release checklist](docs/PRODUCTION_RELEASE_CHECKLIST.md)
- [Security operations](docs/SECURITY_OPERATIONS.md)

Before publishing, complete the remaining device, accessibility, signing, optional Firebase Analytics, privacy/data-safety, and Play Console checks documented in those files.

## Disclaimer

Health Metrics Tracker provides informational wellness tools. It is not a substitute for professional medical advice, diagnosis, or treatment.
