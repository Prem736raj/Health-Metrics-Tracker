"""Produce narrated copies of the 10 short promos and a 12-screen guided tour.

Requires edge-tts, Pillow, numpy, and imageio-ffmpeg. Original promo assets are
read-only inputs; generated files are written under promo-videos/voiceover.
"""

from __future__ import annotations

import asyncio
import json
import math
import re
import subprocess
import zipfile
from pathlib import Path

import edge_tts
import imageio_ffmpeg
from PIL import Image, ImageDraw, ImageFilter

from render_promos import CAMPAIGNS, OUTPUT, SOURCE, W, H, font, gradient_background

TARGET = OUTPUT / "voiceover"
ASSETS = TARGET / "_assets"
FFMPEG = imageio_ffmpeg.get_ffmpeg_exe()
VOICE = "en-US-AvaNeural"
FPS = 24

# Copy was checked against the named source screens. These are lifestyle
# tracking tools, so avoid any claims about medical diagnosis or accuracy.
SHORT_NARRATION = [
    "Meet Health Metrics Tracker. See your daily wellness overview and keep the habits that matter together.",
    "Build a better hydration routine. Log your water intake, review your goal, and celebrate every small step.",
    "Follow your weight journey with clarity. Record your entries and see how your progress changes over time.",
    "Make food logging part of your day. Keep a diary of meals, calories, and key nutrients in one place.",
    "Keep your blood pressure readings organized. Add a record and revisit previous entries whenever you need them.",
    "Explore practical wellness calculators in Health Metrics Tracker. Choose a tool and review its results in moments.",
    "See your body mass index and its reference range. A simple way to understand one wellness measurement.",
    "Turn recorded blood pressure readings into a visual timeline. Spot changes and review patterns over time.",
    "Take your tracking records with you. Explore available PDF, CSV, and JSON export options inside the app.",
    "Make Health Metrics Tracker fit your routine. Adjust preferences and choose reminders that work for you.",
]

# One genuine original screenshot is displayed in each feature scene, including
# Insights and History (which had been left out of the original short promos).
TOUR = [
    ("INTRODUCTION", "A clearer view of your everyday wellness.", "01-home.png",
     "Meet Health Metrics Tracker, a simple home for your daily wellness routine.", (147, 94, 248)),
    ("WELLNESS DASHBOARD", "Start with the full picture.", "01-home.png",
     "Open the dashboard to see your daily trackers and wellness overview together.", (152, 96, 252)),
    ("WATER TRACKING", "Make every glass count.", "02-water.png",
     "Log your water intake and see how you're progressing toward your daily hydration goal.", (64, 157, 246)),
    ("WEIGHT JOURNEY", "See progress over time.", "03-weight.png",
     "Record your weight, review your history, and understand changes across your journey.", (55, 194, 168)),
    ("FOOD DIARY", "Know your daily intake.", "04-food.png",
     "Keep a food diary with calories and macronutrients to understand your everyday meals.", (247, 145, 104)),
    ("BLOOD PRESSURE LOG", "Organize every reading.", "05-blood-pressure.png",
     "Save blood pressure readings and revisit your previous records in one organized place.", (235, 101, 137)),
    ("WELLNESS CALCULATORS", "Everyday tools in one place.", "06-calculators.png",
     "Explore the available wellness calculators and choose the one that fits your needs.", (172, 111, 255)),
    ("BMI CALCULATOR", "Understand the reference scale.", "07-bmi.png",
     "Calculate your body mass index and view the reference categories alongside your result.", (82, 197, 145)),
    ("PRESSURE TRENDS", "Review the patterns.", "08-pressure-trends.png",
     "Explore charts of your recorded blood pressure readings and follow changes over time.", (105, 148, 248)),
    ("WELLNESS INSIGHTS", "Your check-ins, in context.", "09-insights.png",
     "Explore wellness insights based on your check-ins and the information you've recorded.", (179, 109, 240)),
    ("CALCULATION HISTORY", "Keep previous results close.", "10-history.png",
     "Go back to past calculations and revisit the results you've already saved.", (112, 176, 233)),
    ("EXPORT YOUR DATA", "Keep a copy of your records.", "11-export.png",
     "Explore PDF, CSV, and JSON export options for the records you choose to keep.", (204, 126, 252)),
    ("SETTINGS AND REMINDERS", "Build your own routine.", "12-settings.png",
     "Customize app preferences and manage reminders to make tracking work for you.", (111, 170, 239)),
    ("TRACK YOUR DAY", "Your wellness, one day at a time.", "01-home.png",
     "Health Metrics Tracker. Track your day, review your progress, and stay engaged with your habits.", (147, 94, 248)),
]


def run(args: list[str]) -> None:
    p = subprocess.run(args, capture_output=True, text=True)
    if p.returncode:
        raise RuntimeError(f"FFmpeg failed: {' '.join(args[-8:])}\n{p.stderr[-5000:]}")


def duration(path: Path) -> float:
    p = subprocess.run([FFMPEG, "-hide_banner", "-i", str(path)], capture_output=True, text=True)
    match = re.search(r"Duration:\s*(\d+):(\d+):(\d+\.\d+)", p.stderr)
    if not match:
        raise RuntimeError(f"Cannot probe duration: {path}: {p.stderr[-1000:]}")
    hours, minutes, seconds = match.groups()
    return int(hours) * 3600 + int(minutes) * 60 + float(seconds)


async def synthesize(key: str, line: str, *, max_seconds: float | None = None) -> Path:
    path = ASSETS / f"{key}.mp3"
    if path.exists() and path.stat().st_size > 5000:
        if max_seconds is None or duration(path) < max_seconds:
            return path
    for rate in ("-5%", "+10%", "+22%", "+35%"):
        await edge_tts.Communicate(line, voice=VOICE, rate=rate).save(str(path))
        if max_seconds is None or duration(path) <= max_seconds:
            break
    if max_seconds is not None and duration(path) > max_seconds:
        raise RuntimeError(f"Narration is too long for {key}: {duration(path):.2f}s")
    return path


async def create_narration() -> tuple[list[Path], list[Path]]:
    sem = asyncio.Semaphore(3)

    async def limited(key: str, line: str, max_seconds: float | None = None) -> Path:
        async with sem:
            return await synthesize(key, line, max_seconds=max_seconds)

    shorts = await asyncio.gather(*[
        limited(f"short_{i+1:02d}", line, 12.1) for i, line in enumerate(SHORT_NARRATION)
    ])
    tour = await asyncio.gather(*[
        limited(f"tour_{i+1:02d}", record[3]) for i, record in enumerate(TOUR)
    ])
    return list(shorts), list(tour)


def mix_short(spec: dict, mp3: Path) -> Path:
    origin = OUTPUT / f"{spec['id']}-{spec['slug']}.mp4"
    result = TARGET / f"{spec['id']}-{spec['slug']}-vo.mp4"
    if result.exists() and result.stat().st_size > 250_000:
        return result
    af = ("[0:a]volume=0.24,aresample=48000[bg];"
          "[1:a]adelay=170|170,volume=1.5,aresample=48000[vo];"
          "[bg][vo]amix=inputs=2:duration=first:normalize=0,"
          "alimiter=limit=0.94[outa]")
    run([FFMPEG, "-hide_banner", "-loglevel", "error", "-y",
         "-i", str(origin), "-i", str(mp3), "-filter_complex", af,
         "-map", "0:v:0", "-map", "[outa]", "-c:v", "copy",
         "-c:a", "aac", "-b:a", "192k", "-movflags", "+faststart",
         "-t", "12.72", str(result)])
    print(f"Narrated {result.name}", flush=True)
    return result


def wrap(draw: ImageDraw.ImageDraw, text: str, size: int, max_width: int, bold=False) -> list[str]:
    lines: list[str] = []
    row = ""
    for word in text.split():
        candidate = (row + " " + word).strip()
        if draw.textbbox((0, 0), candidate, font=font(size, bold=bold))[2] > max_width and row:
            lines.append(row)
            row = word
        else:
            row = candidate
    if row:
        lines.append(row)
    return lines


def poster(i: int, item: tuple) -> Path:
    title, subtitle, screenshot, _narration, accent = item
    path = ASSETS / f"tour_scene_{i+1:02d}.jpg"
    if path.exists() and path.stat().st_size > 100_000:
        return path
    # This premium background matches the existing campaign identity.
    canvas = gradient_background(accent, i + 22)
    draw = ImageDraw.Draw(canvas)
    for y in range(620, 1640, 70):
        for x in range(60, 1040, 70):
            draw.ellipse((x, y, x + 3, y + 3), fill=(241, 237, 255, 31))
    draw.rounded_rectangle((65, 69, 118, 122), radius=13, fill=(*accent, 255))
    draw.line((78, 94, 105, 94), fill="white", width=6)
    draw.line((92, 81, 92, 108), fill="white", width=6)
    draw.text((142, 73), "HEALTH METRICS", font=font(34, bold=True), fill=(249, 247, 255))
    draw.text((142, 115), "TRACKER", font=font(25), fill=(186, 180, 205))
    draw.line((66, 168, 1014, 168), fill=(218, 208, 250, 85), width=2)

    title_font = 83 if len(title) < 22 else 69
    title_lines = wrap(draw, title, title_font, 950, bold=True)
    while len(title_lines) > 2 and title_font > 48:
        title_font -= 5
        title_lines = wrap(draw, title, title_font, 950, bold=True)
    ty = 219
    for ln in title_lines:
        draw.text((67, ty), ln, font=font(title_font, bold=True), fill=(250, 249, 255))
        ty += title_font + 6
    subtitle_y = max(ty + 13, 345)
    for ln in wrap(draw, subtitle, 35, 940):
        draw.text((70, subtitle_y), ln, font=font(35, light=True), fill=(209, 206, 233))
        subtitle_y += 43

    # Source images are composited as unchanged pixels: no synthetic UI text.
    sw, sh = 650, 1156
    sx, sy = 215, 473
    shadow = Image.new("RGBA", canvas.size, (0, 0, 0, 0))
    sd = ImageDraw.Draw(shadow)
    sd.rounded_rectangle((sx-17, sy-16, sx+sw+17, sy+sh+17), 45, fill=(0, 0, 0, 190))
    canvas.alpha_composite(shadow.filter(ImageFilter.GaussianBlur(33)))
    draw.rounded_rectangle((sx-15, sy-15, sx+sw+15, sy+sh+15), 39,
                           fill=(25, 24, 47, 255), outline=(*accent, 210), width=4)
    original = Image.open(SOURCE / screenshot).convert("RGB")
    original = original.resize((sw, sh), Image.Resampling.LANCZOS).convert("RGBA")
    mask = Image.new("L", (sw, sh), 0)
    ImageDraw.Draw(mask).rounded_rectangle((0, 0, sw-1, sh-1), 25, fill=255)
    original.putalpha(mask)
    canvas.alpha_composite(original, (sx, sy))

    draw.rounded_rectangle((72, 1667, 1007, 1753), 23,
                           fill=(36, 32, 58, 245), outline=(*accent, 140), width=2)
    tag = "THE COMPLETE FEATURE TOUR" if i in (0, len(TOUR)-1) else subtitle.upper()
    if len(tag) > 38:
        tag = "YOUR WELLNESS, YOUR ROUTINE"
    tw = draw.textbbox((0, 0), tag, font=font(30, bold=True))[2]
    draw.text(((W-tw)//2, 1695), tag, font=font(30, bold=True), fill=(246, 244, 255))
    draw.text((71, 1821), "TRACK  /  REVIEW  /  UNDERSTAND", font=font(26, bold=True), fill=(237, 235, 249))
    draw.text((778, 1822), f"{i+1:02d} / {len(TOUR):02d}", font=font(28), fill=(*accent, 255))
    draw.line((70, 1790, 1010, 1790), fill=(229, 222, 247, 70), width=2)
    canvas.convert("RGB").save(path, quality=92, subsampling=0, optimize=True)
    return path


def make_tour_audio(mp3s: list[Path], lengths: list[float]) -> Path:
    wav = ASSETS / "tour_narration.wav"
    if wav.exists() and wav.stat().st_size > 100_000:
        return wav
    # Pad each line to its matching image segment, keeping speech synchronized.
    cmd = [FFMPEG, "-hide_banner", "-loglevel", "error", "-y"]
    for file in mp3s:
        cmd += ["-i", str(file)]
    filters = []
    for i, seconds in enumerate(lengths):
        filters.append(f"[{i}:a]aresample=48000,adelay=260|260,"
                       f"apad,atrim=duration={seconds:.3f},asetpts=PTS-STARTPTS[a{i}]")
    filters.append("".join(f"[a{i}]" for i in range(len(mp3s))) +
                   f"concat=n={len(mp3s)}:v=0:a=1[voice]")
    run(cmd + ["-filter_complex", ";".join(filters), "-map", "[voice]",
               "-c:a", "pcm_s16le", str(wav)])
    return wav


def tour_video(mp3s: list[Path]) -> tuple[Path, float]:
    path = TARGET / "11-health-metrics-tracker-complete-tour.mp4"
    voice_lengths = [duration(p) for p in mp3s]
    # 1 second of visual rest around each spoken line; never trim speech.
    lengths = [round(max(5.8, x + 1.4), 3) for x in voice_lengths]
    whole = round(sum(lengths), 3)
    images = [poster(i, item) for i, item in enumerate(TOUR)]
    audio = make_tour_audio(mp3s, lengths)
    concat = ASSETS / "tour_concat.txt"
    lines = []
    for img, seconds in zip(images, lengths):
        lines.extend([f"file '{img.as_posix()}'", f"duration {seconds:.3f}"])
    lines.append(f"file '{images[-1].as_posix()}'")
    concat.write_text("\n".join(lines) + "\n", encoding="utf-8")
    if path.exists() and path.stat().st_size > 1_000_000:
        return path, whole
    # One source input supplies the photo sequence, a second provides the
    # original campaign's instrumental bed, and the third is synchronized TTS.
    mix = ("[1:a]volume=0.18,aresample=48000[bed];"
           "[2:a]volume=1.45,aresample=48000[narr];"
           "[bed][narr]amix=inputs=2:duration=first:normalize=0,"
           "alimiter=limit=0.94[mix]")
    run([FFMPEG, "-hide_banner", "-loglevel", "error", "-y",
         "-f", "concat", "-safe", "0", "-i", str(concat),
         "-stream_loop", "-1", "-i", str(OUTPUT / "01-your-wellness-overview.mp4"),
         "-i", str(audio), "-filter_complex", mix,
         "-map", "0:v:0", "-map", "[mix]", "-vf", "fps=24,format=yuv420p",
         "-c:v", "libx264", "-preset", "veryfast", "-crf", "19",
         "-pix_fmt", "yuv420p", "-c:a", "aac", "-b:a", "192k",
         "-r", str(FPS), "-movflags", "+faststart", "-t", str(whole), str(path)])
    print(f"Rendered complete tour: {path.name} ({whole:.1f}s)", flush=True)
    return path, whole


def package(shorts: list[Path], tour: Path, length: float) -> None:
    manifest = {
        "voice": VOICE,
        "resolution": "1080x1920",
        "fps": FPS,
        "short_videos": [
            {"video": p.name, "narration": script, "source": f"{s['id']}-{s['slug']}.mp4"}
            for p, script, s in zip(shorts, SHORT_NARRATION, CAMPAIGNS)
        ],
        "complete_tour": {
            "video": tour.name,
            "duration_seconds": length,
            "screenshots": [record[2] for record in TOUR],
            "feature_screenshots_unique": len({record[2] for record in TOUR}),
            "scenes": [{"title": r[0], "image": r[2], "narration": r[3]} for r in TOUR],
        },
    }
    (TARGET / "voiceover-manifest.json").write_text(json.dumps(manifest, indent=2), encoding="utf-8")
    archive = TARGET / "Health-Metrics-Tracker-11-Narrated-Videos.zip"
    with zipfile.ZipFile(archive, "w", compression=zipfile.ZIP_STORED) as z:
        for p in [*shorts, tour, TARGET / "voiceover-manifest.json"]:
            if not p.exists() or p.stat().st_size < 1000:
                raise RuntimeError(f"Missing output: {p}")
            z.write(p, arcname=p.name)
    print(f"Packaged: {archive} ({archive.stat().st_size / 1048576:.1f} MiB)", flush=True)


async def main():
    TARGET.mkdir(parents=True, exist_ok=True)
    ASSETS.mkdir(parents=True, exist_ok=True)
    shorts_audio, tour_audio = await create_narration()
    print("Neural narration synthesized and checked for duration.", flush=True)
    shorts = [mix_short(s, mp3) for s, mp3 in zip(CAMPAIGNS, shorts_audio)]
    full, total = tour_video(tour_audio)
    package(shorts, full, total)


if __name__ == "__main__":
    asyncio.run(main())
