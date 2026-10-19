"""Inspect native ELF alignment and bundle signing markers without external packages.

Run against the final APK/AAB. This complements zipalign, jarsigner/apksigner,
bundletool and Play's pre-launch report; it is not an approval or a device test.
"""
import argparse
import json
import struct
import zipfile
from pathlib import Path


def load_alignments(data):
    if data[:4] != b'\x7fELF':
        raise ValueError('Native library is not ELF')
    endian = '<' if data[5] == 1 else '>'
    if data[4] == 2:
        offset = struct.unpack_from(endian + 'Q', data, 32)[0]
        stride, count = struct.unpack_from(endian + 'HH', data, 54)
        alignment_offset, alignment_type = 48, 'Q'
    elif data[4] == 1:
        offset = struct.unpack_from(endian + 'I', data, 28)[0]
        stride, count = struct.unpack_from(endian + 'HH', data, 42)
        alignment_offset, alignment_type = 28, 'I'
    else:
        raise ValueError('Unsupported ELF class')
    return [
        struct.unpack_from(endian + alignment_type, data, offset + i * stride + alignment_offset)[0]
        for i in range(count)
        if struct.unpack_from(endian + 'I', data, offset + i * stride)[0] == 1
    ]


def inspect(path):
    with zipfile.ZipFile(path) as artifact:
        libraries = []
        for name in artifact.namelist():
            if not name.endswith('.so'):
                continue
            alignments = load_alignments(artifact.read(name))
            required = '/arm64-v8a/' in name or '/x86_64/' in name
            libraries.append({
                'path': name,
                'load_segment_alignments': alignments,
                'requires_16kb': required,
                'passes_16kb_elf_check': bool(alignments) and all(a >= 16384 for a in alignments) if required else None,
            })
        signatures = [n for n in artifact.namelist() if n.upper().startswith('META-INF/') and n.upper().endswith('.SF')]
        return {
            'artifact': str(path.resolve()),
            'bytes': path.stat().st_size,
            'bundle_has_jar_signature_marker': bool(signatures) if path.suffix == '.aab' else None,
            'native_libraries': libraries,
            'all_required_libraries_pass_16kb_elf_check': all(lib['passes_16kb_elf_check'] for lib in libraries if lib['requires_16kb']),
            'limitations': 'ELF alignment only. APK ZIP alignment, signature validity, Play signing identity and device behavior need separate checks.',
        }


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('artifact', type=Path)
    parser.add_argument('--require-signed-bundle', action='store_true')
    args = parser.parse_args()
    result = inspect(args.artifact)
    print(json.dumps(result, indent=2))
    passed = result['all_required_libraries_pass_16kb_elf_check']
    if args.require_signed_bundle:
        passed = passed and args.artifact.suffix == '.aab' and result['bundle_has_jar_signature_marker']
    raise SystemExit(0 if passed else 1)
