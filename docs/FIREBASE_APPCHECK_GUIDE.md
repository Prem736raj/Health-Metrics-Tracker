# Firebase App Check Setup Guide

Updated: 2026-09-18

This guide covers production App Check configuration with Play Integrity for
the Health Metrics Tracker release build.

## Prerequisites

- A Firebase project with the Android app registered (`com.healthmetrics.tracker`)
- A release signing keystore with known SHA-256 fingerprint
- Access to Firebase Console and Google Cloud Console

---

## Step 1: Get the Release SHA-256 Fingerprint

```bash
keytool -list -v -keystore /path/to/health-metrics-release.jks \
  -alias health-metrics-key
```

Copy the `SHA256:` fingerprint value. It looks like:
```
SHA256: AB:CD:EF:12:34:56:...
```

---

## Step 2: Register the Fingerprint in Firebase

1. Open [Firebase Console](https://console.firebase.google.com)
2. Select your project → Project Settings → Your Apps
3. Find the Android app (`com.healthmetrics.tracker`)
4. Click "Add fingerprint"
5. Paste the SHA-256 fingerprint (colons are OK)
6. Save

> If you use Play App Signing, also add Play's upload and app signing
> certificate fingerprints. Find these in Play Console → Setup → App signing.

---

## Step 3: Register Play Integrity as App Check Provider

1. In Firebase Console → App Check (left sidebar under Build)
2. Click on your Android app
3. Select **Play Integrity** as the attestation provider
4. Click "Save"

> **Debug builds**: The app already includes `firebase-appcheck-debug` as a
> `debugImplementation` dependency. For emulator/CI testing, use the debug
> App Check provider. See the Firebase docs for the debug token flow.

---

## Step 4: Enable App Check Enforcement

> **Warning**: Do NOT enforce App Check until you have verified that the
> release build successfully obtains tokens. Premature enforcement will
> block all AI requests.

### Verification Before Enforcement

1. Build a release APK with the signing keystore
2. Install on a physical device
3. Open the AI Wellness Assistant
4. Send a test message
5. Verify the response arrives (App Check token was accepted)
6. Check Firebase Console → App Check → Metrics for the token request

### Enable Enforcement

1. In Firebase Console → App Check → your product (e.g., Vertex AI)
2. Toggle enforcement to "Enforced"
3. Monitor the metrics dashboard for 24–48 hours
4. Watch for unexpected "invalid token" errors

---

## Step 5: Verify in the App

### Code Architecture (Already Implemented)

The app uses build-variant-specific App Check providers:

- **Release**: `firebase-appcheck-playintegrity` (real attestation)
- **Debug**: `firebase-appcheck-debug` (development token)

This is configured in `HealthCalculatorApp.kt`:
```kotlin
Firebase.initialize(this)
AppCheckInitializerImpl().initialize(this)
```

### What to Verify

| Check | Expected Result |
| --- | --- |
| AI message on release build | Response arrives without error |
| AI message on debug build | Response arrives using debug token |
| AI message with network off | Graceful error, no crash |
| App Check metrics in Firebase | Token requests visible |
| After enforcement enabled | Release builds still work |
| After enforcement enabled | Unsigned/modified APKs are rejected |

---

## Step 6: Production Monitoring

After enabling enforcement:

1. **Firebase Console → App Check → Metrics**
   - Monitor "Verified" vs "Unverified" request counts
   - Unverified requests from legitimate users indicate a configuration problem

2. **Play Console → Android Vitals**
   - Watch for ANR/crash spikes related to network timeouts
   - App Check token acquisition adds ~100-500ms to cold start

3. **Known Limitations**
   - Play Integrity may not work on rooted devices or custom ROMs
   - Some enterprise-managed devices may fail attestation
   - The app degrades gracefully: AI features show an error, other features
     continue working

---

## Troubleshooting

| Problem | Solution |
| --- | --- |
| "App Check token request failed" on release build | Verify SHA-256 fingerprint matches the signing key |
| Works on debug but not release | Debug uses a different App Check provider; verify release fingerprint |
| "No matching app check provider" | Verify `firebase-appcheck-playintegrity` is in release dependencies |
| Token works but AI request fails | Check Firebase AI / Vertex AI service is enabled in your project |
| Pre-launch report shows App Check errors | Pre-launch uses Google's test devices; they may not pass Play Integrity |
