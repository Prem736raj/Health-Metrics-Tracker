# Data Safety mapping

Updated: 2026-09-11

This document maps the Android implementation to the information that must be
reviewed in the Google Play Data safety form. It is an engineering aid, not a
substitute for the release owner's final Play Console declarations.

## Local-only wellness records

The following are stored locally in Room/DataStore for the app's wellness
features and are not included in product analytics event parameters:

- Profile preferences and optional goals
- Weight, blood-pressure, water, calorie/food, step-history and calculator
  history records
- Reminder preferences and report selections
- User-entered notes and locally generated reports/exports

These records support the feature the user selected, are not required for app
exploration, and are removed by the in-app clear-data controls where provided.
Android automatic backup is disabled in the manifest. The app does not expose
cloud backup, QR transfer or cross-device restore.

## Optional Health Connect access

Health Connect is feature-led and read-only:

- Steps: read steps to show optional daily totals, history and comparisons.
- Weight: read weight only when the user explicitly enables that connection.

The app requests no Health Connect write permissions and does not request sleep,
heart-rate or other records pre-emptively. Users can deny or revoke access in
Health Connect settings; the app keeps manual tracking available.

## Optional product analytics

Analytics collection is off by default and is enabled only through the app's
privacy setting. The allowlisted events contain product-flow metadata such as
screen/entry-point labels and fixed outcome categories. Health measurements,
free-form text, notes, names and calculator values are rejected before any
analytics adapter is called.

## Technical services

Optional Firebase Analytics may process standard pseudonymous app/device information
when the user enables product analytics. No health measurement is placed in an
analytics event parameter. The release owner must verify the Firebase Console
retention, processor and Data safety settings before publishing.

## User controls and release review

Before release, verify the following against the signed build and current
Firebase/Play configuration:

1. Privacy disclosure links resolve to the Health Metrics Tracker pages.
2. Health Connect permission screens explain each requested record and denial
   leaves the rest of the app usable.
3. Product analytics is visibly optional and disabled by default.
4. Clear-data behavior removes the local records described above.
5. Play Console declarations match the actual release artifact and any enabled
   Firebase products; do not copy this mapping blindly if configuration changes.
