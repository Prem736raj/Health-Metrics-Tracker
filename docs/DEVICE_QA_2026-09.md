# Device and accessibility QA matrix

Status: **Owner/device verification required**
Prepared: 2026-09-11
Updated: 2026-09-18

This is a reproducible checklist for the signed release candidate. A Windows
Gradle build proves compilation and unit behavior but does not prove rendering,
permissions, TalkBack, OEM scheduling or Health Connect provider behavior.

## Required device matrix

| Profile | API | Form factor | Theme/font | Required flows | Result |
| --- | ---: | --- | --- | --- | --- |
| Small phone | 26/29 | 320–360dp wide | Light, 1.0x/1.5x | onboarding, Home, Track, every calculator, History, Profile | Pending |
| Current Android phone | 33/34 | 360–411dp wide | Light/dark, 1.0x/1.5x/2.0x | first action, water result, BMI slider, AI retry, exports | Pending |
| New Android phone | 35/36 | 360–411dp wide | Light/dark, TalkBack | Health Connect, reminders, reboot/time change, clear data | Pending |
| Large/tablet layout | 34+ | 600dp+ | Light/dark, 1.5x | hubs, charts, calculator inputs/results, split/expanded layouts | Pending |

## CI emulator coverage (automated)

The `instrumented-tests` CI job runs on API 34 (google_apis, x86_64) and
covers Room migration tests (13→14, 14→15, 15→16, full chain 13→16) and
the destructive-fallback safety gate. Results are uploaded as CI artifacts.

## Route and regression checklist

- [ ] Cold start, process death and restore do not show a blank/unknown route.
- [ ] Water Needs input → Calculate → result renders, back navigation works,
  and an unavailable result shows a recovery state rather than an empty page.
- [ ] History title, back and actions share one compact top row.
- [ ] BMI slider/live preview never overlaps the weight or height controls.
- [ ] WHR instructions use the rib-to-iliac midpoint and widest hip landmark.
- [ ] BP values at 179/119, 180/119, 179/120 and 180/120 show the same severe
  repeat/escalation boundary in result, recommendation and widget copy.
- [ ] Heart-rate zones show talk-test guidance and no fixed duration prescription.
- [ ] AI send, offline failure, retry and clear conversation do not duplicate a
  user bubble or expose raw context.
- [ ] Health Connect denial/unavailability leaves manual tracking usable and no
  write permission is requested.
- [ ] Reminder enablement asks for notifications only when needed; reboot,
  timezone/DST and notification denial are handled.
- [ ] CSV, JSON, PDF, image and weekly reports show provenance, selected scope
  and informational/non-diagnostic disclosure.

## Accessibility and visual checks

- [ ] TalkBack labels identify values, units, actions and charts without relying
  on color or emoji.
- [ ] Every primary action and icon action has at least a 48dp target.
- [ ] 1.5x and 2.0x font scales do not clip input labels, result values,
  bottom navigation or dialogs.
- [ ] Light/dark contrast remains readable for hero, metric, list, loading and
  error components.
- [ ] Landscape, split-screen and tablet widths do not create horizontal
  clipping or inaccessible controls.
- [ ] Screenshot review covers Home, Track, Calculators, Insights, Profile,
  Water result, BMI result and one report screen.

## TalkBack testing script

Run these on an API 35/36 device with TalkBack enabled:

### 1. Home screen
- Swipe right through all elements. Verify each metric card announces its
  value, unit, and status (e.g., "Water intake: 1.5 liters of 2.5 liter goal.
  60% complete.").
- Verify the bottom navigation announces tab names, not icon file names.
- Verify the wellness score announces "Wellness Score consistency indicator:
  72 out of 100. Status: Good" (not just "72").

### 2. BMI Calculator
- Navigate to BMI. Verify the slider announces its current value and category.
- Adjust weight with fine-tune buttons. Verify each button announces its
  action (e.g., "Decrease weight by 0.1 kg").
- Calculate. Verify the result announces BMI value and category.
- Verify the category chip does not rely solely on color.

### 3. Blood Pressure
- Enter 180/120. Verify the result announces "Markedly elevated" and the
  recommendation section is focusable.
- Verify the widget BP badge announces the same category.

### 4. AI Assistant
- Open AI coach. Verify the disclosure is announced.
- Send a message. Verify the assistant response is focusable and readable.
- Trigger a retry. Verify no duplicate user bubble is announced.

### 5. Water Tracker
- Log water. Verify the progress update is announced.
- Open the widget. Verify it announces intake, goal, and percentage.

### 6. Exports
- Generate a weekly report. Verify the report header and disclaimer are
  announced.

## Font scale testing script

Run on API 33/34 device:

1. Set font scale to 1.5x (Settings → Display → Font size → Large)
2. Walk through: Home → Track → each calculator → History → Profile
3. Verify no text is clipped, overlapped, or truncated
4. Verify bottom navigation labels remain visible
5. Verify dialog buttons are fully visible and tappable
6. Repeat at 2.0x font scale

## Health Connect testing script

Run on API 35/36 device with Health Connect installed:

### Setup
1. Install Google Fit or Samsung Health
2. Record at least 2 days of steps and 1 weight entry
3. Open Health Metrics Tracker

### Permission grant flow
1. Navigate to Settings → Health Connect
2. Grant Steps permission
3. Return to Home — verify daily steps appear
4. Navigate back to Settings → Health Connect
5. Grant Weight permission
6. Verify latest weight appears on the weight tracking screen

### Permission denial flow
1. Revoke Steps permission in Health Connect settings
2. Return to app — verify manual step entry still works
3. Verify no crash or empty screen

### Multiple providers
1. If available, install a second provider (e.g., both Fit and Samsung Health)
2. Record steps in both
3. Verify aggregated total is reasonable (no double-counting)

### Edge cases
- [ ] Process death while Health Connect permission dialog is showing
- [ ] Health Connect app not installed (API 26–33 without the module)
- [ ] Health Connect installed but no data recorded

## Reminder/widget lifecycle testing script

### Reboot test
1. Enable a BP reminder for 5 minutes from now
2. Enable a water reminder
3. Add a water widget to the home screen
4. Reboot the device
5. Wait for the reminder time
6. Verify the notification arrives
7. Verify the widget shows current data (not stale/error)

### Timezone test
1. Enable a reminder for 30 minutes from now
2. Change timezone forward by 2 hours (Settings → Date & time)
3. Verify the reminder fires at the correct wall-clock time in the new timezone
4. Change timezone back
5. Verify the next occurrence recalculates

### Doze test (API 23+)
1. Enable a reminder
2. Put device into Doze: `adb shell dumpsys deviceidle force-idle`
3. Wait past the reminder time
4. Exit Doze: `adb shell dumpsys deviceidle unforce`
5. Verify the reminder fires within a reasonable window

### Notification denial test
1. Deny notification permission (API 33+)
2. Enable a reminder in the app
3. Verify the app shows a message about notifications being denied
4. Verify no crash occurs

## Evidence record

- Build commit: fill with the signed release-candidate SHA.
- Device/emulator IDs: fill during owner QA.
- Failed flows and screenshots: attach to the release issue; do not put health
  values or personal data in public tickets.
- CI emulator tests: check `instrumented-test-reports` artifact from the latest
  CI run for Room migration test results.
