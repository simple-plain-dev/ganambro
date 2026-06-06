# Issue 4: ExitCoordinator — exit(from:), internal branching, fake WarningSound

## What to build

Modul yang memiliki seluruh behaviour keluar dari aplikasi. Interface: `exit(from: ExitContext): Unit` — internal branching menangani perbedaan:
- `ExitContext.Menu`: langsung keluar (`finishAffinity()`) tanpa konfirmasi
- `ExitContext.Ujian`: dialog konfirmasi ketik "exit" → Warning Sound 2 → keluar

Caller (MenuScreen, Toolbar Ujian) hanya memanggil satu metode. ExitCoordinator bergantung pada WarningSound sebagai adapter.

## Acceptance criteria

- [ ] Interface: `exit(from: ExitContext): Unit`
- [ ] `exit(Menu)` → langsung `finishAffinity()`, tanpa dialog, tanpa suara
- [ ] `exit(Ujian)` → tampilkan dialog "ketik exit untuk keluar"
- [ ] Dialog: ketik "exit" (case-insensitive) → WarningSound.play(WS2) → finishAffinity()
- [ ] Dialog: ketik selain "exit" → tutup dialog, tetap di Ujian
- [ ] Unit test pure: `exit(Menu)` memanggil System.exit tanpa dialog
- [ ] Unit test: `exit(Ujian)` dengan warningSound fake: assert play(WS2) dipanggil lalu exit
- [ ] WarningSound fake adapter digunakan di test

## Blocked by

- #3 (WarningSound)
