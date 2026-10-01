# Contributing

Discuss substantial feature or schema changes in an issue before implementing
them. For bugs, provide Android version, app version, reproducible steps and
expected/actual behavior. Remove personal health data from screenshots and logs.
Report security/privacy vulnerabilities privately using [SECURITY.md](SECURITY.md).

## Development workflow

1. Use a focused branch and keep changes scoped to one fix or feature.
2. Trace navigation, persistence and background consumers before changing behavior.
3. Add regression tests. Use synthetic data, not actual health records.
4. Run `testDebugUnitTest`, `lintDebug`, `assembleDebug`; validate release packaging
   for dependency, manifest, reflection or shrinking changes.
5. Run `connectedDebugAndroidTest` for database and Compose changes. Explicitly
   state when no device was available, rather than claiming runtime verification.
6. Include before/after light and dark screenshots for visual changes, with a
   large-font/small-screen check. Do not include production credentials.

Keep commits descriptive (`fix:`, `test:`, `docs:`, `refactor:` are encouraged).
Use a pull request with the root cause, user impact, tests, migration impact and
known limitations. Never delete a Room migration or enable destructive fallback
for a database version that may have reached users. Check generated schema diffs.

## Health and privacy changes

Link authoritative references for formula/threshold changes, state units and age
assumptions, and test exact boundaries and non-finite/invalid inputs. A formula
test is not clinical validation. Preserve explicit consent and optionality for
cloud integrations; update privacy documentation when data flows change.

Do not add analytics events containing measurements, free text or identifiers.
Do not enable advertising, cloud backup or new health permissions incidentally.
New dependencies need a concrete benefit and compatibility verification.

License and contribution terms remain an owner decision; this document does not
grant a license or require a contributor to transfer ownership.
