# Changelog

Changes below are development changes, not an assertion that a release has shipped.

## Unreleased — September 2026 production audit

- Guard spreadsheet exports against formula injection and carriage-return corruption.
- Make calculator-history integrity checks non-destructive.
- Correct Health Connect latest-weight ordering and privacy-rationale entry points.
- Preserve coroutine cancellation in Health Connect operations.
- Remove unused advertising permissions inherited from optional analytics.
- Clarify optional AI health-context transfer and local deletion limits.
- Improve onboarding scrolling, text sizing, theme colors and state preservation.
- Correct calendar-day streak logic across DST and repeated logs.
- Retain food history beyond 90 days, share food state across screens, prevent
  rapid-entry ID collisions and make weekly summaries include today reactively.
- Pause food-log writes if stored JSON cannot be read, preserving original
  preferences and warning on the Food Log screen instead of showing empty totals.
- Correct overview lookup keys/routes and category-color precedence.
- Report export failures honestly; preserve cancellation and bound worker retries.
- Make bulk history cleanup transactional and handle weight/storage failures.
- Preserve weight precision while editing, share decimal parsing with the dialog,
  and make the dialog scrollable/keyboard-aware.
- Use current-locale, thread-safe date formatting; keep exports during ordinary
  cache cleanup and correct storage accounting off the UI thread.
- Align BMR form validation with the existing calculation input contract.
- Replace unsupported Data Safety answers and stale checked release gates with
  evidence-based owner review steps.
- Add regression/device tests and harden CI permissions, concurrency and wrapper checksum.
- Add repository entry documentation and archive historical progress reports.

See [production audit](docs/audit/PRODUCTION_AUDIT_2026-09-19.md) for exact verified
results and remaining work. Existing versionName `1.0.1` / versionCode `2` are
unchanged; the owner must assign a new code if versionCode 2 has already shipped.
