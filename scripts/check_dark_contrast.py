"""WCAG contrast validation against the actual Kotlin dark token declarations."""
from pathlib import Path
import re
src = (Path(__file__).resolve().parents[1] / "app/src/main/java/com/health/calculator/bmi/tracker/ui/theme/Color.kt").read_text(encoding="utf-8")
colors = {name: int(rgb, 16) for name, rgb in re.findall(r"val (\w+) = Color\(0xFF([0-9A-Fa-f]{6})\)", src)}
def lum(rgb):
    channels = [(rgb >> shift & 255) / 255 for shift in (16, 8, 0)]
    channels = [x / 12.92 if x <= .04045 else ((x + .055) / 1.055) ** 2.4 for x in channels]
    return sum(x * w for x, w in zip(channels, (.2126, .7152, .0722)))
def contrast(a, b):
    a, b = sorted((lum(colors[a]), lum(colors[b])))
    return (b + .05) / (a + .05)
pairs = []
for surface in ("BackgroundDark", "Level1", "Level2", "Level3"):
    for text in ("Text", "SecondaryText", "DisabledText", "Accent"):
        pairs.append((text, surface, 4.5))
    pairs.append(("FunctionalOutline", surface, 3))
    pairs.append(("WarningOutline", surface, 3))
for role in ("Primary", "Secondary", "Tertiary", "Error", "PrimaryContainer", "SecondaryContainer", "TertiaryContainer", "ErrorContainer", "Background", "Surface", "SurfaceVariant", "InverseSurface"):
    pairs.append(("On" + role + "Dark" if role != "InverseSurface" else "InverseOnSurfaceDark", role + "Dark", 4.5))
failures = []
for fg, bg, threshold in pairs:
    ratio = contrast(fg, bg)
    print(f"{fg}/{bg}: {ratio:.2f}:1 (requires {threshold}:1)")
    if ratio < threshold:
        failures.append(f"{fg}/{bg} = {ratio:.2f}")
if failures:
    raise SystemExit("VERDICT: FAIL -- " + "; ".join(failures))
print(f"VERDICT: PASS -- {len(pairs)} dark text and functional boundary pairings")
