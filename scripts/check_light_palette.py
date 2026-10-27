"""Protect all pre-existing light constants and the complete light scheme."""
from pathlib import Path
import subprocess, re
root = Path(__file__).resolve().parents[1]
base = "81271c5"
folder = "app/src/main/java/com/health/calculator/bmi/tracker/ui/theme/"
def previous(file):
    return subprocess.check_output(["git", "show", f"{base}:{folder}{file}"], cwd=root).decode("utf-8")
def current(file):
    return (root / folder / file).read_text(encoding="utf-8")
def scheme(text):
    return text.split("private val LightColorScheme = lightColorScheme(", 1)[1].split("\n)", 1)[0]
assert scheme(previous("Theme.kt")) == scheme(current("Theme.kt")), "LightColorScheme changed"
def light_constants(text):
    return dict(re.findall(r"val (\w*Light) = ([^\n]+)", text))
assert light_constants(previous("Color.kt")) == light_constants(current("Color.kt")), "Light colour values changed"
# Non-theme-specific legacy feature colours must also remain unchanged.
old = dict(re.findall(r"val (\w+) = ([^\n]+)", previous("Color.kt")))
new = dict(re.findall(r"val (\w+) = ([^\n]+)", current("Color.kt")))
for name, value in old.items():
    if "Dark" not in name:
        assert new[name] == value, f"Shared/light colour changed: {name}"
print("VERDICT: PASS -- light scheme and every existing shared/light constant unchanged")
