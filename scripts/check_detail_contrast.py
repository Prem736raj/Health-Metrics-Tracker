"""Independent WCAG check of all literal detail palettes; no Android runtime needed."""
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
source = (ROOT / 'app/src/main/java/com/health/calculator/bmi/tracker/ui/components/detail/DetailTheme.kt').read_text()
features = re.findall(r'^    ([A-Z_]+)\(Color\(0xFF([0-9A-F]{6})\), Color\(0xFF([0-9A-F]{6})\)\)', source, re.M)
assert len(features) == 22, 'New feature must join the contrast audit'

def color(text):
    return tuple(int(text[i:i+2], 16) / 255 for i in (0, 2, 4))

def luminance(c):
    return sum(w * (x / 12.92 if x <= .04045 else ((x + .055) / 1.055) ** 2.4)
               for w, x in zip((.2126, .7152, .0722), c))

def ratio(a, b):
    a, b = sorted((luminance(a), luminance(b)))
    return (b + .05) / (a + .05)

def pair(name):
    values = re.search(r'\b' + name + r' = if \(dark\) Color\(0xFF([0-9A-F]{6})\) else Color\(0xFF([0-9A-F]{6})\)', source)
    assert values, name
    return color(values[2]), color(values[1])

roles = {name: pair(name) for name in ['background', 'surface', 'raised', 'text', 'secondary', 'outline', 'warning', 'success', 'danger']}
assert 'lerp(surface, accent, if (dark) .12f else .08f)' in source
assert 'if (dark) Color(0xFF122219) else Color.White' in source
count = 0
failures = []
for feature, day, night in features:
    for dark, accent_hex in enumerate((day, night)):
        accent = color(accent_hex)
        surface = roles['surface'][dark]
        alpha = .12 if dark else .08
        container = tuple(a * alpha + s * (1-alpha) for a, s in zip(accent, surface))
        assert 'lerp(surface, danger, .08f)' in source
        error_surface = tuple(d * .08 + s * .92 for d, s in zip(roles['danger'][dark], surface))
        surfaces = [roles[n][dark] for n in ['background', 'surface', 'raised']] + [container, error_surface]
        candidates = {n: roles[n][dark] for n in ['text', 'secondary', 'warning', 'success', 'danger']}
        candidates['accent'] = accent
        worst = 100
        for role, fg in candidates.items():
            for i, bg in enumerate(surfaces):
                count += 1
                value = ratio(fg, bg)
                worst = min(worst, value)
                if value < 4.5:
                    failures.append((feature, dark, role, i, value))
        count += 1
        value = ratio(color('122219') if dark else (1, 1, 1), accent)
        if value < 4.5:
            failures.append((feature, dark, 'onAccent', value))
        for bg in surfaces[:3]:
            count += 1
            value = ratio(roles['outline'][dark], bg)
            if value < 3:
                failures.append((feature, dark, 'outline', value))
        print(f'{feature} {"night" if dark else "day"}: lowest text/surface {worst:.3f}:1')
print(f'Checked {count} pairings; {len(failures)} failures')
assert not failures, failures
print('VERDICT: PASS')
