"""Capture actual Android app screens via ADB; never synthesizes app UI."""
from pathlib import Path
import argparse
import json
import io
import subprocess
import time
import xml.etree.ElementTree as ET
from PIL import Image, ImageStat

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'docs/store/listing-2026-10-09'
ADB = r'C:\Users\prem7\AppData\Local\Android\Sdk\platform-tools\adb.exe'
BASE = [ADB, '-P', '5037', '-s', 'emulator-5554']
ACTIVITY = 'com.healthmetrics.tracker/com.health.calculator.bmi.tracker.MainActivity'

def adb(*args, binary=False):
    result = subprocess.run(BASE + list(args), capture_output=True, timeout=40, check=True)
    return result.stdout if binary else result.stdout.decode('utf-8', errors='replace')

def observe(name='current', quick=False):
    OUT.mkdir(parents=True, exist_ok=True)
    (OUT / 'capture-evidence').mkdir(exist_ok=True)
    png = OUT / 'capture-evidence' / f'{name}.png'
    adb('shell', 'am', 'broadcast', '-a', 'com.android.systemui.demo', '--es', 'command', 'clock', '--es', 'hhmm', '0941')
    adb('shell', 'am', 'broadcast', '-a', 'com.android.systemui.demo', '--es', 'command', 'battery', '--es', 'level', '100', '--es', 'plugged', 'false')
    adb('shell', 'am', 'broadcast', '-a', 'com.android.systemui.demo', '--es', 'command', 'notifications', '--es', 'visible', 'false')
    if quick:
        foreground = adb('shell', 'dumpsys', 'window')
        focus = [line.strip() for line in foreground.splitlines() if 'mCurrentFocus' in line or 'mFocusedApp' in line]
        assert any('com.healthmetrics.tracker' in line for line in focus), focus
        time.sleep(2)
        for attempt in range(16):
            pixels = adb('exec-out', 'screencap', '-p', binary=True)
            with Image.open(io.BytesIO(pixels)) as image:
                spread = max(ImageStat.Stat(image.convert('RGB').crop((0,240,1080,1790))).stddev)
            if spread > 4:
                break
            time.sleep(2)
        else:
            raise RuntimeError('App content has not rendered; refusing to save a blank capture')
        png.write_bytes(pixels)
        evidence = {'image':str(png),'foreground':focus,'capturedAtUtc':time.strftime('%Y-%m-%dT%H:%M:%SZ',time.gmtime())}
        (OUT / 'capture-evidence' / f'{name}.json').write_text(json.dumps(evidence,indent=2),encoding='utf-8')
        print(json.dumps(evidence),flush=True)
        return png, None
    adb('shell', 'uiautomator', 'dump', '/sdcard/listing-window.xml')
    xml = adb('shell', 'cat', '/sdcard/listing-window.xml')
    (OUT / 'capture-evidence' / f'{name}.xml').write_text(xml, encoding='utf-8')
    tree = ET.fromstring(xml)
    time.sleep(1)
    png.write_bytes(adb('exec-out', 'screencap', '-p', binary=True))
    nodes = [{'text': n.get('text') or n.get('content-desc'), 'bounds': n.get('bounds'),
              'class': n.get('class'), 'clickable': n.get('clickable')}
             for n in tree.iter('node') if n.get('text') or n.get('content-desc')]
    print(json.dumps({'image': str(png), 'nodes': nodes}, ensure_ascii=True), flush=True)
    return png, tree

if __name__ == '__main__':
    p = argparse.ArgumentParser()
    p.add_argument('--route')
    p.add_argument('--name', default='current')
    p.add_argument('--tap', nargs=2)
    p.add_argument('--text')
    p.add_argument('--key')
    p.add_argument('--batch', action='store_true')
    p.add_argument('--quick', action='store_true')
    args = p.parse_args()
    if args.batch:
        for screen in json.loads((OUT / 'screens.json').read_text(encoding='utf-8')):
            if not screen['route']:
                continue
            adb('shell', 'am', 'force-stop', 'com.healthmetrics.tracker')
            adb('shell', 'am', 'start', '-n', ACTIVITY, '--es', 'navigate_to', screen['route'])
            time.sleep(5)
            if screen.get('captureAction', {}).get('tap'):
                adb('shell', 'input', 'tap', *[str(x) for x in screen['captureAction']['tap']])
                time.sleep(1)
            observe(screen['capture'], quick=True)
        raise SystemExit(0)
    if args.route:
        adb('shell', 'am', 'force-stop', 'com.healthmetrics.tracker')
        print(adb('shell', 'am', 'start', '-n', ACTIVITY, '--es', 'navigate_to', args.route), flush=True)
        time.sleep(5)
    if args.tap:
        adb('shell', 'input', 'tap', *args.tap)
        time.sleep(0.8)
    if args.text:
        adb('shell', 'input', 'text', args.text)
    if args.key:
        adb('shell', 'input', 'keyevent', args.key)
        time.sleep(0.8)
    observe(args.name, args.quick)
