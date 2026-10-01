# Play Console Data Safety review guide

Updated: 20 September 2026. **Draft for owner verification; not pre-approved
Yes/No answers and not evidence from a signed production build.**

The previous guide incorrectly treated on-device storage as Play “collection,”
assumed Firebase crash uploads without an implemented Crashlytics integration,
and implied local deletion removes service-side data. Those conclusions are
superseded by this implementation-based review.

## Definitions to apply

Play's collection definition concerns transmission off the device, including
SDK transmission. Solely on-device processing is not collection under that
definition. Optional transmission still needs review; ephemeral processing is
not an automatic exemption from answering the form. Sharing/service-provider
and user-initiated-transfer exceptions depend on actual handling and terms.
[Google Play Data Safety guidance](https://support.google.com/googleplay/android-developer/answer/10787469).

Do not copy another app's answers. Final declarations must cover the actual
release artifact, SDKs, enabled services, processors and retention settings.

## Source-backed data-flow worksheet

| Data / feature | Observed implementation | Owner review required |
|---|---|---|
| Core weight/BP/water/food/calculation logs | Stored in Room, DataStore or SharedPreferences; not included in allowlisted product analytics parameters | Local storage alone does not establish off-device collection; review user export paths separately |
| Profile name/photo/preferences | Local profile and app-private image files | Confirm no additional distribution-specific upload SDKs; do not classify a local photo as uploaded |
| Health Connect steps / weight | Optional, feature-led read permissions; manual weight copy can become a local weight record | Health Connect declarations and permitted use |
| Product analytics | Default-off; fixed allowlisted product events, no raw health measurements in event parameters | App interactions, SDK identifiers and technical/derived data under the configured Analytics settings |
| Crash logs | MainActivity writes a private local diagnostic file; no Crashlytics SDK declaration found in app Gradle | Do not claim automatic Firebase Crashlytics uploads; check Play diagnostics and any future crash SDK independently |
| Reports/exports | PDF/CSV/JSON/image shared via scoped URI and user-selected target | Review user-initiated-transfer treatment; app cannot delete copies another app received |
| Accounts, payments, ads | No implemented account, billing or ad flow found; advertising permissions removed | Confirm final merged manifest and business configuration before declaring absence |

The absence of a particular Android permission is not proof that an SDK cannot
derive a data category. Review SDK disclosures, not just manifest permissions.
[Firebase Android disclosure reference](https://firebase.google.com/docs/android/play-data-disclosure).

## Security and deletion answers are not automatic

Cleartext traffic is disabled and Firebase APIs use TLS, but the final
“encrypted in transit” answer requires checking all off-device paths in the
release configuration. Android-private storage is not app-managed database
encryption.

In-app clear/reset controls delete local records. They do not prove deletion of
service telemetry, backups held by a recipient or previously exported files. Do
not assert a server-side deletion mechanism without verifying the service
terms/configuration and the actual owner support process.

## Before submitting

- [ ] Inspect the final merged release manifest and resolved SDK inventory.
- [ ] Validate actual network/consent behavior with analytics off/on.
- [ ] Review Firebase Analytics processing/retention settings.
- [ ] Publish the updated privacy policy and verify its public URL in a browser.
- [ ] Confirm applicable health-data/message/interaction/identifier categories.
- [ ] Confirm collection purposes, optionality, sharing exceptions and deletion answers.
- [ ] Complete the separate health-app and Health Connect declarations.
- [ ] Retain screenshots/evidence of the final owner-approved console responses.

References: [engineering data-flow mapping](DATA_SAFETY_MAPPING.md),
[current audit](audit/PRODUCTION_AUDIT_2026-09-19.md),
[Health apps declaration](https://support.google.com/googleplay/android-developer/answer/14738291).
