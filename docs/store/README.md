# Play listing artwork

> 6 October 2026 audit: the feature graphic was visually reviewed for feature
> and health claims. Both screenshots were refreshed from the final minified
> release QA copy after the layout fixes. File-format checks passed. Review the
> actual Play-signed build and Console preview before selecting release artwork. See the
> [current readiness audit](../audit/PLAY_STORE_READINESS_2026-10-05.md).

`feature-graphic-1024x500.png` is the local candidate feature graphic. It is a
1024×500, 24-bit PNG (867,028 bytes) prepared on 26 September 2026. It has no
medical claim, personal health reading, promotional price, or text. A human
release owner should review it in the Play Console preview before upload.

The graphic was made with the built-in image generation tool and resized from
its 1794×876 output using high-quality bicubic sampling. Prompt:

> A polished, calm abstract wellness illustration for a personal health journal:
> warm ivory backdrop and restrained deep teal, sea green, muted blue and clay;
> one flowing path linking a water droplet, weight-scale dial and rising trend
> motif, anchored by an outlined heart. Wide landscape composition with safe
> margins. No text, numbers, logo, medical cross, people, body silhouette,
> fabricated readings or watermark.

Two 1080×1920 phone screenshots from the API 34 audit emulator are
available for owner review:

- `screenshots/01-home-api34-dark.png` — Home with the honest empty-score state.
- `screenshots/02-weight-api34-dark.png` — Weight statistics with two synthetic
  measurements (70.5 kg on 1 October and 71.2 kg on 6 October 2026).
  No real person's data was used.

These are **candidates**, not approved Play artwork. They show the minified
release APK in dark mode on one emulator, using a separate QA copy signed with
the standard Android debug certificate. They are not evidence of upload-key or
Play signing. Captures were converted to 24-bit PNG without alpha; RGB pixels
were preserved exactly, with no cropping, compositing or content edits.
`tools/verify_store_assets.py` checks dimensions, PNG format, aspect ratio and
minimum screenshot count. Review the exact final listing preview and capture
light-mode/other-device variants as needed before upload. The Home screenshot
does not imply the optional Health Connect connection is active; it says
“Connect.” The weight screenshot shows a partial trend card below the fold,
reflecting the actual scroll position rather than a composited mockup.

The 82 foreground-checked navigation screenshots under local `scratch/` are QA
evidence, not listing assets: their 1080×2400 size exceeds Play's 2:1 limit.
The applicable technical requirements are in
[Google Play's preview-asset documentation](https://support.google.com/googleplay/android-developer/answer/9866151).
