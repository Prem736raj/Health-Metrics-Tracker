"""Check the dark onboarding's native text/icon roles without Android dependencies."""
from pathlib import Path
import re

root = Path(__file__).resolve().parents[1]
source = (root / "app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/onboarding/ForestOnboardingScreen.kt").read_text(encoding="utf-8")
roles = dict(re.findall(r"val (\w+) = Color\(0xFF([0-9A-Fa-f]{6})\)", source))


def luminance(hex_color):
    channels = [int(hex_color[i:i + 2], 16) / 255 for i in (0, 2, 4)]
    linear = [v / 12.92 if v <= 0.04045 else ((v + 0.055) / 1.055) ** 2.4 for v in channels]
    return sum(v * weight for v, weight in zip(linear, (0.2126, 0.7152, 0.0722)))


def contrast(first, second):
    high, low = sorted((luminance(first), luminance(second)), reverse=True)
    return (high + 0.05) / (low + 0.05)


checks = []
for surface in ("background", "card"):
    for text in ("text", "secondary", "sage"):
        checks.append((f"{text}/{surface}", roles[text], roles[surface], 4.5))
    for icon in ("glow", "cyan", "amber", "peach", "blue"):
        checks.append((f"{icon}/{surface}", roles[icon], roles[surface], 3.0))
for fill in ("B4D69E", "A3C390", "B6D79E"):
    checks.append((f"button ink/{fill}", "08120C", fill, 4.5))
failed = []
for label, foreground, background, minimum in checks:
    ratio = contrast(foreground, background)
    print(f"{label}: {ratio:.2f}:1 (minimum {minimum}:1)")
    if ratio < minimum:
        failed.append(label)
if failed:
    raise SystemExit("VERDICT: FAIL -- " + ", ".join(failed))
print(f"VERDICT: PASS -- {len(checks)} forest text/icon pairings")
