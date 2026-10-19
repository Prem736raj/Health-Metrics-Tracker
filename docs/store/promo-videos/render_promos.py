"""Render screenshot-faithful Health Metrics Tracker promotional videos.

Uses the original Play Store screenshots without generatively altering any UI text.
Runs with Pillow, NumPy and imageio_ffmpeg on the project workstation.
"""

from __future__ import annotations

import json
import math
import subprocess
import wave
from pathlib import Path

import imageio_ffmpeg
import numpy as np
from PIL import Image, ImageDraw, ImageFilter, ImageFont


ROOT = Path(__file__).resolve().parents[3]
SOURCE = ROOT / "docs/store/Health-Metrics-Tracker-Play-Store-Images/Health-Metrics-Tracker-Play-Store-Images/originals"
OUTPUT = Path(__file__).resolve().parent
STILLS = OUTPUT / "_render_stills"
W, H = 1080, 1920
FPS = 24
SCENE_LENGTHS = (3.3, 3.3, 3.3, 3.0)
TRANSITION = 0.5
TOTAL = sum(SCENE_LENGTHS)

FONT_REGULAR = "C:/Windows/Fonts/segoeui.ttf"
FONT_BOLD = "C:/Windows/Fonts/segoeuib.ttf"
FONT_LIGHT = "C:/Windows/Fonts/segoeuil.ttf"

# The verified screenshots for Insights and History contain grammatical mistakes
# in the app UI. They are excluded here to preserve zero-error visible captions.
CAMPAIGNS = [
    dict(id="01", slug="your-wellness-overview", title=("YOUR WELLNESS", "IN FOCUS"), sub="Daily habits. One clear view.",
         screen="01-home.png", support="02-water.png", hook="Start with the big picture", detail="View daily trackers together", tone=(137, 79, 244), signal="Wellness overview"),
    dict(id="02", slug="hydration-habits", title=("MAKE HYDRATION", "A HABIT"), sub="A little progress, every day.",
         screen="02-water.png", support="01-home.png", hook="Log every glass with ease", detail="See your water progress", tone=(65, 155, 241), signal="Water tracking"),
    dict(id="03", slug="weight-journey", title=("SEE YOUR", "PROGRESS"), sub="Your weight history, made visual.",
         screen="03-weight.png", support="01-home.png", hook="Record your weight", detail="Explore changes over time", tone=(56, 194, 166), signal="Weight tracking"),
    dict(id="04", slug="food-diary", title=("FOOD LOGGING,", "SIMPLIFIED"), sub="Understand your daily intake.",
         screen="04-food.png", support="01-home.png", hook="Track calories and macros", detail="Keep a simple food diary", tone=(255, 137, 96), signal="Food diary"),
    dict(id="05", slug="blood-pressure", title=("EVERY READING", "MATTERS"), sub="Your blood pressure log, organized.",
         screen="05-blood-pressure.png", support="08-pressure-trends.png", hook="Record your readings", detail="Review previous entries", tone=(237, 94, 134), signal="Blood pressure log"),
    dict(id="06", slug="wellness-calculators", title=("TOOLS FOR", "EVERYDAY LIFE"), sub="Helpful wellness calculators, together.",
         screen="06-calculators.png", support="07-bmi.png", hook="Explore health calculators", detail="Find the tool you need", tone=(172, 111, 255), signal="Health calculators"),
    dict(id="07", slug="understand-your-bmi", title=("UNDERSTAND", "YOUR BMI"), sub="Clear results and reference ranges.",
         screen="07-bmi.png", support="06-calculators.png", hook="View your BMI result", detail="Explore the reference scale", tone=(85, 193, 145), signal="BMI calculator"),
    dict(id="08", slug="visualize-bp-trends", title=("SPOT THE", "PATTERNS"), sub="Your recorded readings, visualized.",
         screen="08-pressure-trends.png", support="05-blood-pressure.png", hook="Follow blood pressure trends", detail="Compare readings over time", tone=(114, 149, 251), signal="Blood pressure trends"),
    dict(id="09", slug="export-your-records", title=("YOUR RECORDS,", "READY TO GO"), sub="Export your data when you need it.",
         screen="11-export.png", support="05-blood-pressure.png", hook="Choose PDF, CSV or JSON", detail="Keep a copy of your records", tone=(214, 126, 255), signal="Data export"),
    dict(id="10", slug="personalize-your-app", title=("MAKE IT", "YOURS"), sub="Set up tracking your way.",
         screen="12-settings.png", support="02-water.png", hook="Choose your preferences", detail="Set reminders for your routine", tone=(112, 169, 241), signal="App settings"),
]


def font(size: int, bold: bool = False, light: bool = False) -> ImageFont.FreeTypeFont:
    return ImageFont.truetype(FONT_BOLD if bold else FONT_LIGHT if light else FONT_REGULAR, size)


def gradient_background(accent: tuple[int, int, int], seed: int) -> Image.Image:
    yy, xx = np.mgrid[0:H, 0:W].astype(np.float32)
    base = np.array((10, 13, 28), np.float32)
    arr = np.empty((H, W, 3), np.uint8)
    # Two wide, restrained blooms give each campaign its own color identity.
    glow = np.exp(-(((xx - 850) / 620) ** 2 + ((yy - 950) / 850) ** 2) * 1.45)
    lower = np.exp(-(((xx - 100) / 640) ** 2 + ((yy - 1840) / 580) ** 2) * 1.8)
    wave = (np.sin(yy / 460 + seed * .3) + 1) * 0.5
    for c in range(3):
        arr[..., c] = np.clip(base[c] + glow * (accent[c] - base[c]) * 0.24 + lower * accent[c] * 0.065 + wave * 3, 0, 255).astype(np.uint8)
    return Image.fromarray(arr, "RGB").convert("RGBA")


def rounded(draw, bounds, radius, fill, outline=None, width=1):
    draw.rounded_rectangle(bounds, radius=radius, fill=fill, outline=outline, width=width)


def add_device(canvas: Image.Image, screenshot: Path, x: int, y: int, width: int,
               tilt: float = 0, glow=(145, 88, 255), dim: float = 1.0) -> None:
    """Render the screenshot unchanged inside a premium, gently tilted bezel."""
    s = Image.open(screenshot).convert("RGB")
    inner_w = width - 24
    inner_h = round(inner_w * 16 / 9)
    total_w, total_h = width, inner_h + 28
    body = Image.new("RGBA", (total_w + 60, total_h + 60), (0, 0, 0, 0))
    shadow = Image.new("RGBA", body.size, (0, 0, 0, 0))
    sd = ImageDraw.Draw(shadow)
    sd.rounded_rectangle((30, 30, 30 + total_w, 30 + total_h), radius=50, fill=(0, 0, 0, 230))
    shadow = shadow.filter(ImageFilter.GaussianBlur(29))
    body.alpha_composite(shadow)
    d = ImageDraw.Draw(body)
    d.rounded_rectangle((30, 30, 30 + total_w, 30 + total_h), radius=45, fill=(23, 23, 37, 255), outline=(*glow, 115), width=2)
    sw = s.resize((inner_w, inner_h), Image.Resampling.LANCZOS).convert("RGBA")
    corner_mask = Image.new("L", sw.size, 0)
    md = ImageDraw.Draw(corner_mask)
    md.rounded_rectangle((0, 0, inner_w, inner_h), radius=30, fill=255)
    sw.putalpha(corner_mask)
    body.alpha_composite(sw, (42, 43))
    if dim < 1.0:
        overlay = Image.new("RGBA", body.size, (10, 11, 24, int((1 - dim) * 170)))
        body.alpha_composite(overlay)
    if tilt:
        body = body.rotate(tilt, resample=Image.Resampling.BICUBIC, expand=True)
    canvas.alpha_composite(body, (x, y))


def typographic_header(canvas: Image.Image, spec: dict, scene: int, accent: tuple) -> None:
    d = ImageDraw.Draw(canvas)
    # Editorial top bar and wordmark.
    rounded(d, (64, 67, 118, 121), 15, (*accent, 255))
    d.line((77, 95, 106, 95), fill="white", width=6)
    d.line((91, 81, 91, 109), fill="white", width=6)
    d.text((138, 77), "HEALTH METRICS", font=font(30, bold=True), fill=(242, 241, 251))
    d.text((138, 110), "TRACKER", font=font(21), fill=(180, 175, 197))
    rounded(d, (823, 73, 1019, 121), 22, (36, 34, 60, 255), outline=(139, 132, 166, 255), width=2)
    d.text((847, 85), f"FILM {spec['id']} / 10", font=font(24, bold=True), fill=(228, 225, 245))
    d.line((64, 157, 1016, 157), fill=(220, 214, 255, 48), width=2)


def wrap_copy(draw: ImageDraw.Draw, value: str, max_width: int, size=39) -> list[str]:
    tokens = value.split()
    lines, current = [], ""
    for token in tokens:
        attempt = (current + " " + token).strip()
        if draw.textbbox((0, 0), attempt, font=font(size))[2] > max_width and current:
            lines.append(current)
            current = token
        else:
            current = attempt
    if current:
        lines.append(current)
    return lines


def draw_copy(canvas: Image.Image, spec: dict, scene: int, accent: tuple) -> None:
    d = ImageDraw.Draw(canvas)
    if scene == 0:
        headline = spec["title"]
        sub = spec["sub"]
    elif scene == 1:
        headline = (spec["hook"].upper(),)
        sub = "Built around your daily routine."
    else:
        headline = (spec["detail"].upper(),)
        sub = "Clarity in every check-in."
    d.text((70, 204), "A BETTER VIEW OF YOUR DAY", font=font(24, bold=True), fill=(*accent, 255), stroke_width=0)
    head_size = 98 if scene == 0 else 81
    all_lines = []
    for line in headline:
        upper = line.upper()
        words = upper.split()
        row = ""
        for word in words:
            next_row = (row + " " + word).strip()
            if d.textbbox((0, 0), next_row, font=font(head_size, bold=True))[2] > 950 and row:
                all_lines.append(row)
                row = word
            else:
                row = next_row
        if row:
            all_lines.append(row)
    while len(all_lines) > 3:
        head_size -= 5
        all_lines = [" ".join(headline)]
    top = 259
    for ln in all_lines:
        d.text((63, top), ln, font=font(head_size, bold=True), fill=(250, 249, 255), stroke_width=0)
        top += head_size + 14
    sy = max(top + 8, 480)
    for line in wrap_copy(d, sub, 925, size=39):
        d.text((70, sy), line, font=font(39, light=True), fill=(205, 206, 225))
        sy += 54
    d.line((70, sy + 27, 234, sy + 27), fill=(*accent, 255), width=8)


def bottom_band(canvas: Image.Image, spec: dict, scene: int, accent: tuple) -> None:
    d = ImageDraw.Draw(canvas)
    d.line((68, 1768, 1010, 1768), fill=(240, 240, 250, 47), width=2)
    d.text((70, 1804), "TRACK  /  REVIEW  /  UNDERSTAND", font=font(25, bold=True), fill=(236, 233, 248))
    d.text((70, 1848), "Your wellness, one day at a time.", font=font(30), fill=(175, 172, 199))
    for i in range(3):
        rounded(d, (885 + 45 * i, 1813, 919 + 45 * i, 1820), 3,
                (*accent, 255) if i == scene else (110, 108, 143, 165))


def label_card(canvas: Image.Image, title: str, accent: tuple, x: int, y: int, width=405):
    shadow = Image.new("RGBA", canvas.size, (0, 0, 0, 0))
    ds = ImageDraw.Draw(shadow)
    ds.rounded_rectangle((x + 11, y + 17, x + width + 11, y + 151), radius=34, fill=(0, 0, 0, 170))
    shadow = shadow.filter(ImageFilter.GaussianBlur(20))
    canvas.alpha_composite(shadow)
    d = ImageDraw.Draw(canvas)
    rounded(d, (x, y, x + width, y + 144), 31, (32, 32, 52, 245), outline=(*accent, 175), width=3)
    rounded(d, (x + 26, y + 24, x + 70, y + 68), 12, (*accent, 255))
    d.text((x + 39, y + 28), "+", font=font(30, bold=True), fill="white")
    for i, line in enumerate(wrap_copy(d, title, width - 110, size=33)[:2]):
        d.text((x + 91, y + 30 + i * 39), line, font=font(33, bold=i == 0), fill=(247, 246, 253))


def scene_poster(spec: dict, scene: int, index: int) -> Image.Image:
    accent = spec["tone"]
    canvas = gradient_background(accent, index)
    d = ImageDraw.Draw(canvas)
    # Micro-grid, asymmetrical hero framing, and subtle orbital outlines.
    for row in range(645, 1600, 58):
        for col in range(52, 1060, 58):
            d.ellipse((col, row, col + 3, row + 3), fill=(255, 255, 255, 18))
    d.ellipse((430, 650, 1520, 1740), outline=(*accent, 53), width=2)
    d.ellipse((535, 755, 1415, 1635), outline=(*accent, 39), width=2)
    typographic_header(canvas, spec, scene, accent)
    draw_copy(canvas, spec, scene, accent)
    first = SOURCE / spec["screen"]
    second = SOURCE / spec["support"]
    if scene == 0:
        add_device(canvas, second, 55, 927, 360, tilt=7.5, glow=accent, dim=.88)
        add_device(canvas, first, 327, 600, 600, tilt=-4, glow=accent)
        label_card(canvas, spec["signal"], accent, 71, 1535, 420)
    elif scene == 1:
        add_device(canvas, first, 168, 583, 655, tilt=4, glow=accent)
        label_card(canvas, spec["hook"], accent, 592, 1495, 435)
    else:
        add_device(canvas, first, 40, 760, 405, tilt=7, glow=accent, dim=.85)
        add_device(canvas, second, 450, 598, 520, tilt=-5, glow=accent)
        label_card(canvas, spec["detail"], accent, 65, 1510, 445)
    bottom_band(canvas, spec, scene, accent)
    return canvas.convert("RGB")


def end_poster(spec: dict, index: int) -> Image.Image:
    accent = spec["tone"]
    canvas = gradient_background(accent, index + 23)
    d = ImageDraw.Draw(canvas)
    for offset in range(-220, 1300, 120):
        d.line((offset, 0, offset + 760, 1920), fill=(*accent, 22), width=3)
    rounded(d, (427, 262, 652, 487), 58, (*accent, 255))
    d.line((465, 374, 617, 374), fill="white", width=23)
    d.line((541, 298, 541, 450), fill="white", width=23)
    d.text((157, 588), "HEALTH METRICS", font=font(90, bold=True), fill=(249, 249, 255))
    d.text((338, 708), "TRACKER", font=font(89, bold=True), fill=(249, 249, 255))
    d.line((430, 850, 650, 850), fill=(*accent, 255), width=10)
    d.text((217, 924), "Track your day. See your progress.", font=font(41), fill=(231, 228, 247))
    d.text((311, 997), "YOUR WELLNESS, IN FOCUS", font=font(31, bold=True), fill=(*accent, 255))
    add_device(canvas, SOURCE / spec["screen"], 313, 1130, 400, tilt=-2.2, glow=accent)
    # The bottom is purposefully cropped: a cinematic teaser, not a UI reconstruction.
    rounded(d, (346, 1710, 733, 1793), 41, (*accent, 255))
    d.text((414, 1733), "EXPLORE THE APP", font=font(30, bold=True), fill="white")
    return canvas.convert("RGB")


def audio_track(path: Path, seed: int) -> None:
    """Create short, royalty-free original synthesized ambient music."""
    rate = 44100
    t = np.arange(int(TOTAL * rate), dtype=np.float64) / rate
    beat = np.zeros_like(t)
    roots = [[220.0, 261.63, 329.63, 392.0], [196.0, 246.94, 293.66, 369.99],
             [174.61, 220.0, 261.63, 349.23]][seed % 3]
    chord = np.zeros_like(t)
    for i, freq in enumerate(roots):
        chord += (np.sin(2 * np.pi * freq * t + i * .4) + .25 * np.sin(4 * np.pi * freq * t)) / (7.5 + i)
    chord *= (0.7 + 0.3 * np.sin(2 * np.pi * .13 * t))
    # Soft repeating plucks and rounded low pulse.
    for ix, ts in enumerate(np.arange(.45, TOTAL - .8, .76)):
        pos = int(ts * rate)
        nn = min(int(rate * .52), len(t) - pos)
        tt = np.arange(nn) / rate
        freq = roots[(ix + seed) % len(roots)] * (2 if ix % 4 else 1)
        envelope = np.exp(-tt * 7.3) * (1 - np.exp(-tt * 65))
        beat[pos:pos + nn] += .14 * np.sin(2 * np.pi * freq * tt) * envelope
    bass = .055 * np.sin(2 * np.pi * (roots[0] / 2) * t) * (np.sin(np.pi * (t % .76) / .76) ** 6)
    mix = chord * .25 + beat + bass
    fade = np.minimum(1, t / .9) * np.minimum(1, (TOTAL - t) / 1.3)
    mix = np.tanh(mix * fade * 1.3)
    left = (mix * 32767).astype("<i2")
    right = (np.roll(mix, 131) * 32767).astype("<i2")
    stereo = np.column_stack((left, right)).tobytes()
    with wave.open(str(path), "wb") as wav:
        wav.setnchannels(2)
        wav.setsampwidth(2)
        wav.setframerate(rate)
        wav.writeframes(stereo)


def render_video(stills: list[Path], wav: Path, destination: Path) -> None:
    ffmpeg = imageio_ffmpeg.get_ffmpeg_exe()
    cmd = [ffmpeg, "-hide_banner", "-loglevel", "error", "-y"]
    for img, duration in zip(stills, SCENE_LENGTHS):
        cmd.extend(["-loop", "1", "-framerate", str(FPS), "-t", f"{duration:.2f}", "-i", str(img)])
    cmd.extend(["-i", str(wav)])
    # Continuous subtle camera drift with brief fade transitions.
    filters = []
    for i, duration in enumerate(SCENE_LENGTHS):
        dx = "(iw-ow)/2+sin(n*0.022)*20" if i % 2 == 0 else "(iw-ow)/2-cos(n*0.019)*20"
        dy = "(ih-oh)/2+cos(n*0.018)*26" if i % 2 else "(ih-oh)/2-sin(n*0.017)*26"
        filters.append(
            f"[{i}:v]scale=1150:2044,crop={W}:{H}:x='{dx}':y='{dy}',"
            f"fps={FPS},format=yuv420p,trim=duration={duration:.2f},"
            f"fade=t=in:st=0:d=0.22,fade=t=out:st={duration - .24:.2f}:d=0.24,"
            f"settb=AVTB,setpts=PTS-STARTPTS[v{i}]"
        )
    filters.append("[v0][v1][v2][v3]concat=n=4:v=1:a=0[outv]")
    cmd.extend(["-filter_complex", ";".join(filters), "-map", "[outv]", "-map", "4:a:0",
                "-c:v", "libx264", "-preset", "veryfast", "-crf", "19", "-pix_fmt", "yuv420p",
                "-r", str(FPS), "-c:a", "aac", "-b:a", "160k", "-movflags", "+faststart",
                "-t", f"{TOTAL:.2f}", str(destination)])
    result = subprocess.run(cmd, capture_output=True, text=True)
    if result.returncode:
        raise RuntimeError(f"FFmpeg failed ({result.returncode}): {result.stderr[-6000:]}")


def main() -> None:
    import argparse

    parser = argparse.ArgumentParser()
    parser.add_argument("--only", type=int, help="Render just one numbered campaign (1-10)")
    parser.add_argument("--stills-only", action="store_true", help="Write scene posters for QA")
    args = parser.parse_args()
    OUTPUT.mkdir(parents=True, exist_ok=True)
    STILLS.mkdir(parents=True, exist_ok=True)
    chosen = [CAMPAIGNS[args.only - 1]] if args.only else CAMPAIGNS
    manifest = []
    for idx, spec in enumerate(chosen):
        poster_paths = []
        for scene in range(4):
            output_png = STILLS / f"{spec['id']}_scene_{scene + 1}.png"
            image = end_poster(spec, int(spec["id"])) if scene == 3 else scene_poster(spec, scene, int(spec["id"]))
            image.save(output_png, optimize=True)
            poster_paths.append(output_png)
        video = OUTPUT / f"{spec['id']}-{spec['slug']}.mp4"
        if not args.stills_only:
            wav = STILLS / f"{spec['id']}_soundtrack.wav"
            audio_track(wav, int(spec["id"]))
            render_video(poster_paths, wav, video)
            wav.unlink(missing_ok=True)
            print(f"Rendered {video.name} ({video.stat().st_size / 1e6:.1f} MB)", flush=True)
        manifest.append({"title": " ".join(spec["title"]), "video": video.name,
                         "screenshots": [spec["screen"], spec["support"]],
                         "duration_seconds": round(TOTAL, 2), "format": f"{W}x{H} H.264 + AAC"})
    (OUTPUT / "manifest.json").write_text(json.dumps(manifest, indent=2), encoding="utf-8")
    if not args.stills_only:
        print(f"Completed {len(chosen)} campaign(s). Output: {OUTPUT}", flush=True)


if __name__ == "__main__":
    main()
