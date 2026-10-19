"""Check the repository's PNG listing candidates against Play's technical rules.

This does not approve artwork, claims, final listing previews or device coverage.
Reference: https://support.google.com/googleplay/android-developer/answer/9866151
"""
from pathlib import Path
import json
import struct


def png_info(path):
    header = path.read_bytes()[:33]
    if header[:8] != b'\x89PNG\r\n\x1a\n' or header[12:16] != b'IHDR':
        raise ValueError(f'Not a PNG with an IHDR header: {path}')
    width, height, depth, color = struct.unpack('>IIBB', header[16:26])
    return {'path': str(path), 'width': width, 'height': height,
            'bit_depth': depth, 'color_type': color, 'bytes': path.stat().st_size}


def inspect(root):
    entries = []
    icon = png_info(root / 'docs/play_store_icon_512.png')
    icon['passes'] = (icon['width'], icon['height']) == (512, 512) and icon['bit_depth'] == 8 and icon['color_type'] == 6 and icon['bytes'] <= 1024 * 1024
    icon['requirement'] = '512x512, 32-bit PNG with alpha, maximum 1024 KB'
    entries.append(icon)
    feature = png_info(root / 'docs/store/feature-graphic-1024x500.png')
    feature['passes'] = (feature['width'], feature['height']) == (1024, 500) and feature['bit_depth'] == 8 and feature['color_type'] == 2
    feature['requirement'] = '1024x500, 24-bit PNG without alpha'
    entries.append(feature)
    screenshots = sorted((root / 'docs/store/screenshots').glob('*.png'))
    for path in screenshots:
        screenshot = png_info(path)
        short, long = sorted([screenshot['width'], screenshot['height']])
        screenshot['passes'] = screenshot['bit_depth'] == 8 and screenshot['color_type'] == 2 and short >= 320 and long <= 3840 and long <= short * 2
        screenshot['requirement'] = '24-bit PNG without alpha; each side 320..3840 px; long side at most twice the short side'
        entries.append(screenshot)
    return {'assets': entries, 'screenshots': len(screenshots),
            'passes_all_technical_checks': len(screenshots) >= 2 and all(entry['passes'] for entry in entries),
            'limitations': 'Technical file checks only. Review current UI, marketing claims and the final Console preview separately.'}


if __name__ == '__main__':
    result = inspect(Path(__file__).resolve().parents[1])
    print(json.dumps(result, indent=2))
    raise SystemExit(0 if result['passes_all_technical_checks'] else 1)
