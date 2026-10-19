"""Package only reviewed deliverables, excluding intermediate and QA captures."""
from pathlib import Path
import json
import zipfile
import struct

root = Path(__file__).resolve().parents[1]
folder = root / 'docs/store/listing-2026-10-09'
report = json.loads((folder / 'validation.json').read_text(encoding='utf-8'))
assert report['allPass'] and report['count'] == 12 and report['recommendedCount'] == 8
paths = []
for name, count in [('polished',12),('recommended-8',8),('originals',12)]:
    files = sorted((folder / name).glob('*.png'))
    assert len(files) == count
    for file in files:
        data = file.read_bytes()
        assert data[:8] == b'\x89PNG\r\n\x1a\n'
        assert struct.unpack('>II',data[16:24]) == (1080,1920)
        assert data[24:26] == bytes([8,2]), '24-bit RGB PNG required'
        paths.append(file)
for name in ['README.md','preview.jpg','gallery.html','alt-text.csv','validation.json','screens.json','background-artwork.png','provenance.json']:
    paths.append(folder / name)
archive = root / 'docs/store/Health-Metrics-Tracker-Play-Store-Images.zip'
with zipfile.ZipFile(archive,'w',compression=zipfile.ZIP_DEFLATED,compresslevel=6) as bundle:
    for file in paths:
        bundle.write(file,Path('Health-Metrics-Tracker-Play-Store-Images') / file.relative_to(folder))
with zipfile.ZipFile(archive) as bundle:
    assert bundle.testzip() is None
    assert len(bundle.namelist()) == len(paths)
print(json.dumps({'archive':str(archive),'sizeMb':round(archive.stat().st_size/1024**2,2),'files':len(paths),'polished':12,'recommended':8,'originals':12,'zipIntegrity':'passed'},indent=2))
