# Issue 11a: KioskMode — immersion, orientasi lock, unpin detection

## What to build

Modul yang memiliki screen state perangkat selama Ujian. Interface: `start()` / `stop()`.

Tanggung jawab:
- Menyembunyikan system bars (mode imersi, bukan `startLockTask`)
- Mengunci orientasi potret (user story #32)
- Mendeteksi Peserta meninggalkan aplikasi (unpin/split screen) via lifecycle callbacks → mengirim event ke CheatDetector

Tidak menangani volume atau suara.

## Acceptance criteria

- [ ] Interface: `start()` — aktifkan immersi + orientasi lock
- [ ] Interface: `stop()` — kembalikan ke normal
- [ ] System bars (status bar, navigation bar) tersembunyi dalam mode imersi
- [ ] Orientasi layar terkunci potret (portrait) selama KioskMode aktif
- [ ] Deteksi unpin: `onPause` + `onStop` → emit event
- [ ] Deteksi split screen: `isInMultiWindowMode` → emit event
- [ ] Unit test dengan fake Activity lifecycle

## Blocked by

None — can start immediately.
