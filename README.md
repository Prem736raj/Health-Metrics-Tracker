# Health Metrics Tracker

Health Metrics Tracker is an Android wellness app for tracking everyday health metrics, using informational calculators, reviewing trends, and optionally connecting selected data from Health Connect.

The app is designed around privacy-conscious, local-first workflows. Its calculators and wellness insights are informational tools and are not a substitute for medical diagnosis, treatment, or professional clinical advice.

## Highlights

- Daily wellness dashboard with weight, water, steps, calories, and recent metrics.
- Ten health and wellness calculators with validation, methodology, limitations, and references.
- Weight and hydration tracking with trends, goals, and history.
- Read-only, feature-scoped Health Connect integration for supported metrics.
- Deterministic local insights before any optional AI interpretation.
- Optional AI wellness assistant with consent-gated context sharing and safety controls.
- Opt-in reminders, weekly summaries, widgets, and retention features.
- Privacy, terms, Play data-safety guidance, release checks, and medical references in `docs/`.

## Tech stack

- Kotlin
- Jetpack Compose + Material 3
- Hilt
- Room
- DataStore
- WorkManager
- Health Connect
- Firebase AI Logic
- Firebase Analytics with consent-gated collection
- Gradle Kotlin DSL

## Requirements

- Android Studio with a recent Android SDK
- Android SDK 36
- JDK 17
- Minimum Android API 26

## Build

Use the committed Gradle wrapper.

On macOS or Linux:

```bash
./gradlew test lintDebug assembleDebug
```

On Windows:

```powershell
.\gradlew.bat test lintDebug assembleDebug
```

Release builds support external signing properties/environment variables and do not require signing credentials for normal debug development.

## Project documentation

Useful project documents include:

- `PHASE_PROGRESS.md` — product-development history and current capability boundaries.
- `docs/MEDICAL_REFERENCES.md` — references used for calculator and health-information work.
- `docs/PRODUCTION_RELEASE_CHECKLIST.md` — release-readiness checks.
- `docs/PLAY_DATA_SAFETY_GUIDE.md` — Play Console data-safety guidance.
- `docs/FIREBASE_APPCHECK_GUIDE.md` — Firebase App Check setup.
- `docs/SECURITY_OPERATIONS.md` — operational security notes.

The hosted privacy and terms pages are available through the repository's GitHub Pages site.

## Safety and scope

Health Metrics Tracker provides estimates, logging tools, trend descriptions, and wellness-oriented information. It does not diagnose conditions, prescribe treatment, or replace a clinician. Health Connect access is optional and read-only for supported features, and AI-related context sharing is opt-in.

## Repository status

The app currently targets Android API 36, supports Android API 26+, and uses application ID `com.healthmetrics.tracker`.
