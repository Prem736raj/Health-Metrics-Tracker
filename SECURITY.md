# Security and privacy reporting

Please do not put vulnerabilities, credentials, health records, or exploit
details in public issues. Contact **prem736raj@gmail.com** (the contact also
published in the privacy policy) with the affected version, impact and minimal
reproduction using synthetic data. A response SLA is not currently promised.

For public discussions, omit logs and screenshots that reveal health values,
notes, AI conversations, provider tokens, signing keys or Firebase debug tokens.
Never send release keystores or passwords as part of a report.

## Scope and limitations

The app uses Android-private local storage and disables automatic Android backup;
it does not promise database-level encryption, medical confidentiality beyond
the implemented controls, or protection on a compromised/unlocked device.
User-directed exports and optional AI processing leave the device. See the
[privacy policy](https://prem736raj.github.io/Health-Metrics-Tracker/privacy.html).

Only the current development branch is actively maintained. Release support
windows and a vulnerability response process must be confirmed by the owner
before a public production launch.

Maintainers: follow [security operations](docs/SECURITY_OPERATIONS.md), preserve
evidence privately, rotate any genuinely exposed credentials, and communicate
affected versions and mitigations without publishing sensitive user information.
