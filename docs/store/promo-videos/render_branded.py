"""Replace generic plus-symbol branding in the narrated promo campaign.

Creates an independent set of 11 videos and one ZIP, preserving the original
video and audio renders. Uses the user's provided app artwork, true app UI
screenshots, and the preexisting narrated AAC soundtracks.
"""

from __future__ import annotations

import json
import re
import shutil
import subprocess
import zipfile
from pathlib import Path

from PIL import Image, ImageDraw, ImageChops

from render_promos import CAMPAIGNS, OUTPUT, SCENE_LENGTHS, FPS, W, H
from render_voiceovers import TARGET, TOUR, FFMPEG


BRANDED = TARGET / "branded"
ASSETS = BRANDED / "_assets"
SHORT_STILLS = OUTPUT / "_render_stills"
TOUR_STILLS = TARGET / "_assets"
SOURCE_LOGO = Path(r"C:\Users\prem7\AppData\Local\Temp\codex-clipboard-ea4f1eea-30ba-47e8-9ed6-8dcca3d7618f.png")
COPIED_LOGO = ASSETS / "health-metrics-app-logo.png"


def run(command: list[str]) -> None:
    result = subprocess.run(command, capture_output=True, text=True)
    if result.returncode:
        raise RuntimeError(f"FFmpeg failed: {result.stderr[-6000:]}")


def mark(image: Image.Image, bounds: tuple[int, int, int, int], radius: int, logo: Image.Image) -> None:
    """Draw a white rounded logo tile at the old plus badge bounds."""
    x0, y0, x1, y1 = bounds
    width, height = x1 - x0, y1 - y0
    # A slight internal margin prevents any of the orange / teal artwork from
    # touching the rounded edge, even for the compact 44px feature icon.
    pad = max(3, round(min(width, height) * .065))
    tile = Image.new("RGBA", (width, height), (255, 255, 255, 255))
    art = logo.resize((width - 2 * pad, height - 2 * pad), Image.Resampling.LANCZOS)
    tile.alpha_composite(art, (pad, pad))
    mask = Image.new("L", (width, height), 0)
    ImageDraw.Draw(mask).rounded_rectangle((0, 0, width - 1, height - 1), radius=radius, fill=255)
    tile.putalpha(mask)
    image.paste(tile, (x0, y0), tile)


def load_logo() -> Image.Image:
    if not SOURCE_LOGO.is_file():
        raise FileNotFoundError(f"Supplied app logo not found: {SOURCE_LOGO}")
    original = Image.open(SOURCE_LOGO).convert("RGB")
    # Crop only excess white padding around the running / heart / chart mark.
    difference = ImageChops.difference(original, Image.new("RGB", original.size, "white"))
    bbox = difference.point(lambda v: 255 if v > 30 else 0).getbbox()
    if bbox is None:
        raise RuntimeError("Supplied logo appears blank")
    mid_x = (bbox[0] + bbox[2]) / 2
    mid_y = (bbox[1] + bbox[3]) / 2
    span = max(bbox[2] - bbox[0], bbox[3] - bbox[1]) * 1.10
    left = max(0, min(original.width - span, mid_x - span / 2))
    top = max(0, min(original.height - span, mid_y - span / 2))
    box = (round(left), round(top), round(left + span), round(top + span))
    logo = original.crop(box).convert("RGBA")
    COPIED_LOGO.parent.mkdir(parents=True, exist_ok=True)
    logo.save(COPIED_LOGO)
    return logo


def short_poster(spec: dict, scene: int, logo: Image.Image) -> Path:
    original = SHORT_STILLS / f"{spec['id']}_scene_{scene + 1}.png"
    if not original.exists():
        raise FileNotFoundError(original)
    img = Image.open(original).convert("RGBA")
    if scene < 3:
        mark(img, (64, 67, 118, 121), 15, logo)
        # Secondary branded icon in the caption cards.
        card_bounds = [
            (97, 1559, 141, 1603),
            (618, 1519, 662, 1563),
            (91, 1534, 135, 1578),
        ]
        mark(img, card_bounds[scene], 12, logo)
    else:
        # Hero-size mark shown on the final call-to-action scene.
        mark(img, (427, 262, 652, 487), 58, logo)
    path = ASSETS / f"{spec['id']}_branded_scene_{scene + 1}.png"
    img.convert("RGB").save(path, optimize=True)
    return path


def tour_poster(index: int, logo: Image.Image) -> Path:
    original = TOUR_STILLS / f"tour_scene_{index+1:02d}.jpg"
    if not original.exists():
        raise FileNotFoundError(original)
    img = Image.open(original).convert("RGBA")
    mark(img, (65, 69, 118, 122), 13, logo)
    path = ASSETS / f"branded_tour_scene_{index+1:02d}.jpg"
    img.convert("RGB").save(path, quality=95, subsampling=0, optimize=True)
    return path


def render_short(spec: dict, images: list[Path]) -> Path:
    source = TARGET / f"{spec['id']}-{spec['slug']}-vo.mp4"
    destination = BRANDED / f"{spec['id']}-{spec['slug']}-logo-vo.mp4"
    if not source.is_file():
        raise FileNotFoundError(source)
    command = [FFMPEG, "-hide_banner", "-loglevel", "error", "-y"]
    for img, scene_duration in zip(images, SCENE_LENGTHS):
        command += ["-loop", "1", "-framerate", str(FPS), "-t", f"{scene_duration:.2f}", "-i", str(img)]
    command += ["-i", str(source)]
    filters = []
    for i, scene_duration in enumerate(SCENE_LENGTHS):
        dx = "(iw-ow)/2+sin(n*0.022)*20" if i % 2 == 0 else "(iw-ow)/2-cos(n*0.019)*20"
        dy = "(ih-oh)/2+cos(n*0.018)*26" if i % 2 else "(ih-oh)/2-sin(n*0.017)*26"
        filters.append(
            f"[{i}:v]scale=1150:2044,crop={W}:{H}:x='{dx}':y='{dy}',"
            f"fps={FPS},format=yuv420p,trim=duration={scene_duration:.2f},"
            f"fade=t=in:st=0:d=0.22,fade=t=out:st={scene_duration - .24:.2f}:d=0.24,"
            f"settb=AVTB,setpts=PTS-STARTPTS[v{i}]"
        )
    filters.append("[v0][v1][v2][v3]concat=n=4:v=1:a=0[outv]")
    command += ["-filter_complex", ";".join(filters), "-map", "[outv]", "-map", "4:a:0",
                "-c:v", "libx264", "-preset", "veryfast", "-crf", "19",
                "-pix_fmt", "yuv420p", "-r", str(FPS), "-c:a", "copy",
                "-movflags", "+faststart", "-t", "12.72", str(destination)]
    run(command)
    print(f"Rendered {destination.name}", flush=True)
    return destination


def render_tour(images: list[Path]) -> Path:
    original = TARGET / "11-health-metrics-tracker-complete-tour.mp4"
    if not original.is_file():
        raise FileNotFoundError(original)
    orig_concat = TOUR_STILLS / "tour_concat.txt"
    # Original durations are authoritative, so the narration timing stays exact.
    durations = [float(value) for value in re.findall(r"^duration ([\d.]+)$", orig_concat.read_text(encoding="utf-8"), re.M)]
    if len(durations) != len(images):
        raise RuntimeError(f"Tour image duration mismatch: {len(durations)} != {len(images)}")
    concat = ASSETS / "branded_tour_concat.txt"
    lines = []
    for img, seconds in zip(images, durations):
        lines.extend([f"file '{img.as_posix()}'", f"duration {seconds:.3f}"])
    lines.append(f"file '{images[-1].as_posix()}'")
    concat.write_text("\n".join(lines) + "\n", encoding="utf-8")
    destination = BRANDED / "11-health-metrics-tracker-complete-tour-logo.mp4"
    command = [FFMPEG, "-hide_banner", "-loglevel", "error", "-y",
               "-f", "concat", "-safe", "0", "-i", str(concat),
               "-i", str(original), "-map", "0:v:0", "-map", "1:a:0",
               "-vf", "fps=24,format=yuv420p", "-c:v", "libx264",
               "-preset", "veryfast", "-crf", "19", "-pix_fmt", "yuv420p",
               "-c:a", "copy", "-r", str(FPS), "-movflags", "+faststart",
               "-t", f"{sum(durations):.3f}", str(destination)]
    run(command)
    print(f"Rendered {destination.name}", flush=True)
    return destination


def package(shorts: list[Path], tour: Path) -> Path:
    manifest = json.loads((TARGET / "voiceover-manifest.json").read_text(encoding="utf-8"))
    for i, path in enumerate(shorts):
        manifest["short_videos"][i]["video"] = path.name
    manifest["complete_tour"]["video"] = tour.name
    manifest["brand_logo"] = COPIED_LOGO.name
    manifest["branding_update"] = "Replaced all generic + symbols used as campaign logos in top bars, feature cards and end slates with the supplied app artwork. Original app screenshots remain untouched."
    manifest["original_audio_preserved"] = True
    mf = BRANDED / "branded-voiceover-manifest.json"
    mf.write_text(json.dumps(manifest, indent=2), encoding="utf-8")
    archive = BRANDED / "Health-Metrics-Tracker-11-Branded-Narrated-Videos.zip"
    with zipfile.ZipFile(archive, "w", compression=zipfile.ZIP_STORED) as z:
        for path in [*shorts, tour, mf, COPIED_LOGO]:
            z.write(path, arcname=path.name)
    if not zipfile.ZipFile(archive).testzip() is None:
        raise RuntimeError("ZIP CRC verification failed")
    print(f"ZIP ready: {archive} ({archive.stat().st_size / 1048576:.2f} MiB)", flush=True)
    return archive


def main() -> None:
    ASSETS.mkdir(parents=True, exist_ok=True)
    logo = load_logo()
    shorts = []
    for spec in CAMPAIGNS:
        shots = [short_poster(spec, i, logo) for i in range(4)]
        shorts.append(render_short(spec, shots))
    images = [tour_poster(i, logo) for i in range(len(TOUR))]
    tour = render_tour(images)
    package(shorts, tour)


if __name__ == "__main__":
    main()
