/** Compose presentation artwork around genuine app captures without redrawing UI. */
import fs from 'node:fs/promises';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { createRequire } from 'node:module';
import crypto from 'node:crypto';

const require = createRequire('C:/Users/prem7/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/');
const sharp = require('sharp');
const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const out = path.join(root, 'docs/store/listing-2026-10-09');
const screens = JSON.parse(await fs.readFile(path.join(out, 'screens.json'), 'utf8'));
for (const dir of ['polished', 'recommended-8', 'originals']) await fs.mkdir(path.join(out, dir), { recursive: true });
const esc = s => s.replaceAll('&', '&amp;').replaceAll('<', '&lt;').replaceAll('>', '&gt;');
const hash = b => crypto.createHash('sha256').update(b).digest('hex');
const svg = text => Buffer.from(text);
const background = await sharp(path.join(out, 'background-artwork.png')).resize(1080, 1920, { fit: 'cover' }).removeAlpha().png().toBuffer();
const report = [];
const previews = [];
for (const [index, s] of screens.entries()) {
  const source = path.join(out, 'capture-evidence', `${s.capture}.png`);
  const sourceBuffer = await fs.readFile(source);
  const sourceInfo = await sharp(sourceBuffer).metadata();
  if (sourceInfo.width !== 1080 || sourceInfo.height !== 1920) throw Error(`Unexpected capture size: ${source}`);
  const original = path.join(out, 'originals', `${s.id}.png`);
  await sharp(sourceBuffer).removeAlpha().png({ compressionLevel: 9, palette: false }).toFile(original);
  const decodedSource = await sharp(sourceBuffer).removeAlpha().raw().toBuffer();
  const decodedOriginal = await sharp(original).raw().toBuffer();
  if (!decodedSource.equals(decodedOriginal)) throw Error(`Original pixels changed: ${s.id}`);
  const chrome = svg(`<svg width="1080" height="1920" xmlns="http://www.w3.org/2000/svg">
    <defs><filter id="shadow" x="-30%" y="-10%" width="160%" height="130%"><feDropShadow dx="0" dy="15" stdDeviation="20" flood-color="#163e37" flood-opacity=".16"/></filter></defs>
    <rect x="86" y="267" width="908" height="1608" rx="29" fill="#fff" filter="url(#shadow)"/>
    <rect x="86" y="267" width="908" height="1608" rx="29" fill="none" stroke="#d3ddd5" stroke-width="2"/>
    <g font-family="Segoe UI,Arial,sans-serif">
      <text x="540" y="64" text-anchor="middle" font-size="25" font-weight="600" letter-spacing="3.5" fill="#42685d">HEALTH METRICS TRACKER</text>
      <text x="540" y="151" text-anchor="middle" font-size="62" font-weight="700" letter-spacing="-1.8" fill="#124e48">${esc(s.title)}</text>
      <text x="540" y="214" text-anchor="middle" font-size="33" fill="#557068">${esc(s.subtitle)}</text>
      <text x="540" y="1907" text-anchor="middle" font-size="20" letter-spacing=".7" fill="#dcece5">Actual app screens · Sample data</text>
    </g>
  </svg>`);
  const scaled = await sharp(sourceBuffer).resize(900, 1600, { kernel: 'lanczos3' }).removeAlpha().toBuffer();
  const mask = svg('<svg width="900" height="1600" xmlns="http://www.w3.org/2000/svg"><rect width="900" height="1600" rx="25" fill="white"/></svg>');
  const card = await sharp(scaled).ensureAlpha().composite([{ input: mask, blend: 'dest-in' }]).png().toBuffer();
  const target = path.join(out, 'polished', `${s.id}.png`);
  await sharp(background).composite([{ input: chrome }, { input: card, left: 90, top: 271 }]).removeAlpha().png({ compressionLevel: 9, palette: false }).toFile(target);
  if (index < 8) await fs.copyFile(target, path.join(out, 'recommended-8', `${s.id}.png`));
  const m = await sharp(target).metadata();
  const content = await fs.readFile(target);
  const header = content.subarray(0, 33);
  const passes = m.width === 1080 && m.height === 1920 && !m.hasAlpha && header[24] === 8 && header[25] === 2 && content.length < 8 * 1024 * 1024;
  if (!passes) throw Error(`Technical checks failed: ${s.id}`);
  report.push({ id:s.id, title:s.title, recommended:index < 8, dimensions:[m.width,m.height], mode:'RGB, 24-bit PNG', bytes:content.length, sourceSha256:hash(sourceBuffer), finalSha256:hash(content), originalPixelsPreserved:true, passes, altText:s.alt });
  previews.push({ input:await sharp(target).resize(270,480).png().toBuffer(), left:(index%4)*284+22, top:Math.floor(index/4)*502+122 });
  console.log(`Exported ${s.id} (${Math.round(content.length/1024)} KB)`);
}
if (new Set(report.map(s=>s.sourceSha256)).size !== screens.length) throw Error('Duplicate app captures detected');
const previewHeader = svg(`<svg width="1158" height="1650" xmlns="http://www.w3.org/2000/svg"><rect width="1158" height="1650" fill="#e6ece6"/><g font-family="Segoe UI,Arial,sans-serif" fill="#124e48"><text x="22" y="52" font-size="34" font-weight="700">Health Metrics Tracker — Play Store images</text><text x="22" y="91" font-size="23" fill="#557068">12 screens · First 8 recommended · 1080 × 1920 PNG exports</text><text x="22" y="1640" font-size="21" fill="#557068">Actual release QA app captures with synthetic sample entries.</text></g></svg>`);
await sharp(previewHeader).composite(previews).jpeg({ quality:92, chromaSubsampling:'4:4:4' }).toFile(path.join(out, 'preview.jpg'));
await fs.writeFile(path.join(out, 'validation.json'), JSON.stringify({ allPass:report.every(x=>x.passes), count:report.length, recommendedCount:8, uniqueCaptures:screens.length, assets:report }, null, 2));
await fs.writeFile(path.join(out, 'alt-text.csv'), 'filename,recommended,alt_text\n'+report.map(s=>`"${s.id}.png",${s.recommended},"${s.altText.replaceAll('"','""')}"`).join('\n')+'\n');
await fs.writeFile(path.join(out, 'gallery.html'), `<!doctype html><html lang="en"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Health Metrics Tracker — listing images</title><style>body{margin:0;background:#eff2ec;color:#124e48;font-family:system-ui,sans-serif}main{max-width:1500px;margin:auto;padding:40px}h1{font-size:38px;letter-spacing:-1.5px;margin:0 0 14px}p{color:#536b61;line-height:1.6}.grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(250px,1fr));gap:24px}figure{margin:0;padding:16px;background:#fff;border-radius:20px;border:1px solid #dce5dc}img{width:100%;height:auto;border-radius:10px}figcaption{font-size:17px;font-weight:600;margin:14px 0}a{color:#176e5b}small{display:block;color:#65786d;margin:8px 0 12px}</style><main><h1>Health Metrics Tracker</h1><p>12 polished Play Store images. Upload the first eight for the phone listing; the last four are alternatives.<br>All exports are 1080 × 1920, 24-bit RGB PNG. Tap a preview to open the full-resolution image.</p><div class="grid">${screens.map((s,i)=>`<figure><a href="polished/${s.id}.png"><img src="polished/${s.id}.png" alt="${esc(s.alt)}" loading="lazy"></a><figcaption>${i+1}. ${esc(s.title)}</figcaption><small>${i<8?'Recommended upload':'Alternative image'}</small><a href="polished/${s.id}.png" download>Polished PNG</a> &nbsp;·&nbsp; <a href="originals/${s.id}.png" download>Original capture</a></figure>`).join('')}</div><p>Sample data was added to an isolated QA emulator. The app interface was captured from a real minified release QA build; supporting background artwork was generated with the built-in image generation tool.</p></main></html>`);
console.log(`All ${report.length} assets passed format checks; original RGB pixels preserved; no duplicate captures.`);
