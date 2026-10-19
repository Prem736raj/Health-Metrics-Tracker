"""Add synthetic listing fixtures only to the dedicated local audit emulator."""
from pathlib import Path
import datetime as dt
import sqlite3
import subprocess
from capture_listing import adb, ROOT

work = ROOT / 'scratch/listing-sample-data'
work.mkdir(exist_ok=True)
remote = '/data/data/com.healthmetrics.tracker/databases/health_calculator_database'
adb('shell', 'am', 'force-stop', 'com.healthmetrics.tracker')
assert 'uid=0(root)' in adb('shell', 'id'), 'Dedicated rooted audit emulator required'
owner = adb('shell', 'stat', '-c', '%u:%g', remote).strip()
for suffix in ['', '-wal', '-shm']:
    adb('pull', remote + suffix, str(work / ('health_calculator_database' + suffix)))
db = sqlite3.connect(work / 'health_calculator_database')
db.execute('PRAGMA wal_checkpoint(TRUNCATE)')
backup = work / ('before-listing-fixtures-' + dt.datetime.now().strftime('%Y%m%d-%H%M%S') + '.sqlite')
db.execute("VACUUM INTO ?", (str(backup),))
today = dt.datetime.fromisoformat(adb('shell', 'date', '+%Y-%m-%dT00:00:00%z').strip())
sample_note = 'Sample listing data'
for day, weight in enumerate([71.3, 71.1, 71.2, 71.0, 71.1, 71.0, 71.2]):
    date = today - dt.timedelta(days=6-day) + dt.timedelta(hours=8)
    stamp = int(date.timestamp()*1000)
    if not db.execute('SELECT 1 FROM weight_entries WHERE note=? AND dateMillis=?', (sample_note,stamp)).fetchone():
        db.execute('INSERT INTO weight_entries(weightKg,dateMillis,note,source) VALUES(?,?,?,?)', (weight,stamp,sample_note,'MANUAL'))
    for index in range(6 if day==6 else 8):
        waterstamp = stamp + index * 55*60*1000
        if not db.execute('SELECT 1 FROM water_intake_log WHERE note=? AND timestamp=?',(sample_note,waterstamp)).fetchone():
            db.execute('INSERT INTO water_intake_log(amountMl,timestamp,note) VALUES(?,?,?)',(250,waterstamp,sample_note))
    systolic, diastolic, pulse = [(117,75,68),(118,76,70),(116,74,67),(119,77,71),(118,76,69),(117,75,68),(118,76,70)][day]
    if not db.execute('SELECT 1 FROM blood_pressure_readings WHERE note=? AND measurementTimestamp=?',(sample_note,stamp)).fetchone():
        values = {'systolic':systolic,'diastolic':diastolic,'pulse':pulse,'category':'OPTIMAL','riskLevel':'LOW',
                  'arm':'LEFT','position':'SITTING','timeOfDay':'MORNING','measurementTimestamp':stamp,'note':sample_note,
                  'isPartOfAverage':0,'averageGroupId':None,'isAveragedResult':0,'readingsInAverage':1,
                  'pulsePressure':systolic-diastolic,'meanArterialPressure':(systolic+2*diastolic)/3,
                  'onMedication':0,'medicationName':'','createdAt':stamp}
        db.execute('INSERT INTO blood_pressure_readings('+','.join(values)+') VALUES('+','.join('?' for _ in values)+')',list(values.values()))
db.commit()
db.execute('PRAGMA wal_checkpoint(TRUNCATE)')
assert db.execute('PRAGMA integrity_check').fetchone()[0]=='ok'
db.close()
for suffix in ['', '-wal', '-shm']:
    local = work / ('health_calculator_database'+suffix)
    if local.exists():
        adb('push', str(local), remote+suffix)
        adb('shell','chown',owner,remote+suffix)
        adb('shell','chmod','660',remote+suffix)
    elif suffix:
        # Remove only stale SQLite sidecars for the database replaced above.
        adb('shell','rm','-f',remote+suffix)
print('Synthetic fixtures added; original emulator database backed up under scratch/listing-sample-data.')
