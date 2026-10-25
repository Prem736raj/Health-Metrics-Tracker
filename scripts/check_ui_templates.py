"""Regression guard for accidental escaped Kotlin interpolation in UI copy."""
from pathlib import Path
import re
root = Path(__file__).resolve().parents[1]
issues = []
for p in (root / "app/src/main").rglob("*"):
    if p.suffix not in (".kt", ".xml"):
        continue
    for n, line in enumerate(p.read_text(encoding="utf-8").splitlines(), 1):
        if re.search(r"\\\$(?:[A-Za-z_]\w*|\{)", line):
            issues.append(f"{p.relative_to(root)}:{n}: escaped template: {line.strip()}")
if issues:
    raise SystemExit("VERDICT: FAIL -- " + "\n".join(issues))
print("VERDICT: PASS -- no escaped UI templates")
