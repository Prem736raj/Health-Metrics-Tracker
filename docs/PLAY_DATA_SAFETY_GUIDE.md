# Play Console Data Safety Form Guide

Updated: 2026-09-18

This document provides exact Yes/No answers for each Play Console Data Safety
question, based on the actual implementation in `docs/DATA_SAFETY_MAPPING.md`
and the current signed release build.

> **Important**: Verify these answers against the actual release artifact before
> submitting. If Firebase products, analytics behavior, or data handling change,
> update this guide accordingly.

---

## Section 1: Data Collection and Sharing

### Does your app collect or share any of the required user data types?

**Answer: Yes**

The app collects health and fitness data (locally stored).

---

## Section 2: Data Types

### 2.1 Location

| Question | Answer | Notes |
| --- | --- | --- |
| Approximate location | No | |
| Precise location | No | |

### 2.2 Personal Info

| Question | Answer | Notes |
| --- | --- | --- |
| Name | No | Profile uses a local display name only, not collected |
| Email address | No | |
| User IDs | No | No accounts |
| Address | No | |
| Phone number | No | |
| Race and ethnicity | No | Ethnicity selection for WHR is local-only preference |
| Political or religious beliefs | No | |
| Sexual orientation | No | |
| Other personal info | No | |

### 2.3 Financial Info

| Question | Answer | Notes |
| --- | --- | --- |
| User payment info | No | |
| Purchase history | No | |
| Credit score | No | |
| Other financial info | No | |

### 2.4 Health and Fitness

| Question | Answer | Notes |
| --- | --- | --- |
| Health info | **Yes** | Weight, BP readings, BMI, water intake, food logs — all stored locally |
| Fitness info | **Yes** | Steps via Health Connect, exercise zone calculations — stored locally |

**Is this data collected, shared, or both?**
- **Collected**: Yes
- **Shared**: No

**Is this data processed ephemerally?**
- No (it is persisted locally for the user's tracking features)

**Is this data required for your app, or can users choose whether it's collected?**
- Required for core features, but users choose which metrics to track

**Why is this data collected?**
- App functionality (wellness tracking and informational calculators)

### 2.5 Messages

| Question | Answer | Notes |
| --- | --- | --- |
| Emails | No | |
| SMS or MMS | No | |
| Other in-app messages | **Yes** | AI chat messages stored locally |

**Is this data shared?** No

**Why is this data collected?** App functionality (AI Wellness Assistant)

### 2.6 Photos and Videos

| Question | Answer | Notes |
| --- | --- | --- |
| Photos | No | |
| Videos | No | |

### 2.7 Audio

| Question | Answer | Notes |
| --- | --- | --- |
| Voice or sound recordings | No | |
| Music files | No | |
| Other audio files | No | |

### 2.8 Files and Docs

| Question | Answer | Notes |
| --- | --- | --- |
| Files and docs | No | Exports are user-initiated and go to user's chosen location |

### 2.9 Calendar

| Question | Answer | Notes |
| --- | --- | --- |
| Calendar events | No | |

### 2.10 Contacts

| Question | Answer | Notes |
| --- | --- | --- |
| Contacts | No | |

### 2.11 App Activity

| Question | Answer | Notes |
| --- | --- | --- |
| App interactions | **Yes** (if analytics opted in) | Screen views, button taps — product analytics only |
| In-app search history | No | |
| Installed apps | No | |
| Other user-generated content | No | |
| Other actions | No | |

**Is this data shared?** No

**Why is this data collected?** Analytics (opt-in only, disabled by default)

### 2.12 Web Browsing

| Question | Answer | Notes |
| --- | --- | --- |
| Web browsing history | No | |

### 2.13 App Info and Performance

| Question | Answer | Notes |
| --- | --- | --- |
| Crash logs | **Yes** | Via Firebase infrastructure |
| Diagnostics | **Yes** | Via Firebase infrastructure |
| Other app performance data | No | |

**Is this data shared?** No (processed by Firebase for the developer)

**Why is this data collected?** Analytics, app functionality

### 2.14 Device or Other IDs

| Question | Answer | Notes |
| --- | --- | --- |
| Device or other IDs | **Yes** | Firebase App Check / Play Integrity uses device attestation |

**Is this data shared?** No

**Why is this data collected?** Security (App Check), app functionality

---

## Section 3: Security Practices

### Is all of the user data collected by your app encrypted in transit?

**Answer: Yes**

All network communication uses HTTPS/TLS. The `network_security_config.xml`
enforces secure connections.

### Do you provide a way for users to request that their data is deleted?

**Answer: Yes**

The app provides in-app clear-data controls. Users can also clear app data
from Android Settings. No server-side user data exists beyond Firebase
service infrastructure.

---

## Section 4: Preview and Submit

Before submitting, verify:

1. The privacy policy URL resolves correctly.
2. Answers match the actual signed release build behavior.
3. Health Connect permissions are declared separately in the Health Connect
   declarations section (not just Data Safety).
4. Analytics collection is confirmed as opt-in / disabled by default.
