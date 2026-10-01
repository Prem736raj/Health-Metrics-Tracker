# Market positioning and dependency review

Research retrieved 19–20 September 2026. Competitor descriptions are their own
published claims, not a hands-on evaluation or endorsement. No market-share,
revenue, clinical accuracy or representative complaint-frequency claim is made.

## Product positioning

The implemented core is manual weight/BP/water/food logging, educational
calculators, local history and shareable exports. Health Connect reads and
Firebase AI are optional integrations, not the foundation of daily logging.

Recommended positioning: **a local-first daily measurement journal with clear
trends, useful exports and transparent educational estimates**. Intended audience:
adults recording their own measurements who want a straightforward diary, not a
diagnosis, coaching subscription, nutrition database or full medical record.
This is a product hypothesis that needs user interviews, not validated demand.

| Competitor/category | Published strength | Implication for this app |
|---|---|---|
| [Samsung Health](https://www.samsung.com/us/apps/samsung-health/) | Connected activity, sleep and nutrition ecosystem | Do not compete on wearable breadth; make manual/offline entry reliable |
| [Cronometer](https://cronometer.com/) | Detailed nutrition tracking and verified food data | Our manually entered foods/presets are not an equivalent database; describe them honestly |
| [MyFitnessPal](https://support.myfitnesspal.com/hc/en-us/articles/360032625951-MyFitnessPal-Premium-features) | Faster food logging, barcode/meal tools and expanded nutrition controls | Logging convenience is expected; adding a scanner without a reliable database is not useful |
| [Health Diary by MedM](https://play.google.com/store/apps/details?hl=en&id=com.medm.app.health) | Multi-metric diary, device connectivity and sharing | Closest broad diary comparison; emphasize understandable trends and user-controlled exports |
| [WaterMinder](https://play.google.com/store/apps/details?hl=en_US&id=com.funnmedia.waterminder) | Quick cups, reminders, widgets and hydration history | Water logging should take very few actions; elaborate education/gamification should not obstruct it |

### Review signals, not prevalence estimates

- MyFitnessPal's displayed April 2026 reviews describe diary ordering and losing
  an in-progress logging flow; another reviewer says a recipe-save issue was
  subsequently fixed. These are examples of friction, not proof of current
  defects on every device. [Review listing](https://play.google.com/store/apps/details?hl=en_US&id=com.myfitnesspal.android).
- A September 2026 MedM reviewer wanted more useful summaries of imported data;
  a July review requested custom ordering and objected to paid connectivity.
  Positive reviews praise useful exports. Product inference: prioritize clear
  history/trends and transparent scope over simply accumulating readings.
  [Review listing](https://play.google.com/store/apps/details?hl=en&id=com.medm.app.health).
- WaterMinder's August/July 2026 reviews include subscription/history-access and
  cancellation concerns, while another praises simple reminders. Product
  inference: explain pricing and access before commitment; preserve access to
  a person's own logs. This is not a claim that all users dislike subscriptions.
  [Review listing](https://play.google.com/store/apps/details?hl=en_US&id=com.funnmedia.waterminder).

Differentiation should be trust and low-friction ownership: understandable
estimates, no diagnosis claims, useful exports, no surprise advertising and no
mandatory account for the implemented local diary. Do not market the whole app
as network-free: optional AI and SDK activity require accurate disclosure.

The full KEEP/IMPROVE/MERGE/REDESIGN/OPTIONAL/REMOVE inventory and missing-feature
priorities are in [FEATURE_AUDIT.md](FEATURE_AUDIT.md). No speculative competitor
feature was implemented during this audit.

## Dependency and SDK review

Source: `gradle/libs.versions.toml`, root/app Gradle files and wrapper properties.
No dependency versions were upgraded in this audit. A larger version number
does not establish a relevant bug fix or a vulnerability in the current version.

| Component | Current | Available / decision | Reason and breaking risk |
|---|---|---|---|
| Android SDK | min 26 / compile 36 / target 36 | Retain | Current target requirement is API 36; device behavior still needs testing |
| Java/JVM | 17 | Retain | Matches the existing AGP/CI build contract |
| Gradle | 8.13 | Retain, distribution checksum added | Supply-chain integrity; no toolchain migration |
| AGP | 8.13.2 | Newest not certified here; retain | Major AGP migration can affect Kotlin, DSL, R8 and plugins |
| Kotlin / Compose compiler | 2.0.21 | Newest not certified here; retain | Compiler and KSP must be upgraded together with a tested matrix |
| KSP | 2.0.21-1.0.26 | Retain with Kotlin | Generated Room/Hilt code compatibility is a gating risk |
| Compose BOM | 2024.11.00 | Stable 2026.09.00 observed | Candidate separate upgrade; UI behavior and compiler compatibility need regression tests |
| Room | 2.7.1 | Stable 2.8.5 observed | Candidate patch-family migration separately; verify compiler and real migrations first |
| Lifecycle | 2.8.7 | Stable 2.11.0 observed | Candidate lifecycle/API upgrade; test state restoration and Flow collection |
| Navigation Compose | 2.8.5 | Stable 2.10.1 observed | Do not combine a Navigation 3 redesign with correctness repairs |
| WorkManager | 2.10.0 | Stable 2.11.2 observed | Candidate scheduler update; rerun delay/reboot/permission tests |
| Health Connect | 1.1.0 | Stable 1.1.0 observed | Keep stable rather than adopting 1.2 prerelease |
| Core / Activity / AppCompat | 1.15.0 / 1.9.3 / 1.7.0 | 1.19.0 / 1.13.0 / 1.8.0 observed | Batch only compatible AndroidX changes with edge-to-edge regression tests |
| DataStore | 1.1.1 | Newest not certified here; retain | Persistence changes need failure/migration tests |
| Hilt / navigation adapter | 2.51.1 / 1.2.0 | Newest not certified here; retain | Compiler/codegen coupling; avoid adding a second injection system |
| Coil | 2.7.0 | Retain pending dedicated migration review | Major-version package/API migration is not a safe incidental cleanup |
| Firebase BOM | 34.18.0 | Retain | Optional AI/analytics/App Check require project-side runtime verification |
| Google Services plugin | 4.5.0 | Retain | Public Firebase client config is not a private signing/server key |
| Gson / desugaring | 2.10.1 / 2.1.4 | Retain; targeted advisory audit still needed | No resolved-graph vulnerability clearance claimed |
| JUnit / AndroidX JUnit / Espresso | 4.13.2 / 1.2.1 / 3.6.1 | Retain for current suite | Upgrade tests after stable baseline, not to inflate tool count |

Primary version sources: [Compose BOM](https://developer.android.com/develop/ui/compose/bom),
[Room](https://developer.android.com/jetpack/androidx/releases/room),
[Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle),
[Navigation](https://developer.android.com/jetpack/androidx/releases/navigation),
[WorkManager](https://developer.android.com/jetpack/androidx/releases/work),
[Health Connect](https://developer.android.com/jetpack/androidx/releases/health-connect),
[AndroidX versions](https://developer.android.com/jetpack/androidx/versions).

The version catalog and Compose BOM already provide useful centralization.
Material extended icons have an explicit version; review alignment during the
Compose upgrade rather than removing all icons blindly. No third-party chart
framework migration is justified by the current audit. The next dependency pass
should inventory the resolved transitive graph and targeted advisories; this
document is not a vulnerability scan certificate.

A read-only OSV API query on 20 September returned no advisory IDs for these exact
Maven coordinates/versions: Gson 2.10.1, Coil 2.7.0, Room runtime 2.7.1,
Hilt Android 2.51.1 and WorkManager runtime 2.10.0. This narrow result does not
cover transitive dependencies, unreported vulnerabilities or configuration flaws.
Source: [OSV query API](https://google.github.io/osv.dev/api/).

## Current Play policy implications

The configured target SDK 36 matches the published requirement for new apps and
updates effective 31 August 2026. This is a configuration check, not a Play
submission or approval. [Target API policy](https://support.google.com/googleplay/android-developer/answer/11926878).

Health-related functionality requires the applicable declaration and accurate
privacy/medical-functionality disclosures. The owner must confirm legal and
regulatory applicability, Data Safety, service retention and real SDK behavior.
The source cannot complete or attest to console forms.
[Health content policy](https://support.google.com/googleplay/android-developer/answer/16679511),
[Health apps declaration](https://support.google.com/googleplay/android-developer/answer/14738291),
[Health Connect use](https://support.google.com/googleplay/android-developer/answer/12991134).

Firebase's current AI Logic guidance says App Check enforcement will be required
from 2 November 2026. Validate a production attestation provider with the signed
app now; a debug emulator or a public Firebase client configuration does not
prove production attestation works. Do not disable enforcement to make a test
request succeed. [Firebase App Check for AI Logic](https://firebase.google.com/docs/ai-logic/app-check).
