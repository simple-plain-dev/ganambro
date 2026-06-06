# Issue 11b: CheatDetector — volume watcher, headset detection, response, WarningSound adapter

## What to build

Modul coordinator yang memantau kondisi perangkat selama Ujian. Interface: `start()` / `stop()` dan `Flow<CheatEvent>`.

Tiga mekanisme deteksi dikoordinasikan:
1. **Volume watcher**: foreground service yang enforce volume maksimal setiap 10 detik (background enforcement)
2. **Headset detection**: BroadcastReceiver — jika headset terpasang → emit CheatEvent.Headset → WarningSound.play(WS1) → exit
3. **Unpin/Split**: menerima event dari KioskMode → emit CheatEvent.Unpin → popup peringatan + WarningSound.play(WS1) → kembali ke Menu

CheatDetector bergantung pada WarningSound sebagai adapter. Mengacu pada ADR-0002: background volume enforcement adalah tanggung jawab CheatDetector, terpisah dari invariant internal WarningSound.

## Acceptance criteria

- [ ] Interface: `start()`, `stop()`, `Flow<CheatEvent>`
- [ ] Volume watcher: setiap 10 detik force volume ke maksimal
- [ ] Headset terpasang → CheatEvent.Headset → WarningSound.play(WS1) → exit
- [ ] Unpin/Split terdeteksi → CheatEvent.Unpin → popup peringatan → WarningSound.play(WS1) → navigasi ke Menu
- [ ] Unit test: fake DetectorService + fake WarningSound
- [ ] Unit test: event Flow emission benar (Headset, Unpin)
- [ ] Unit test: WarningSound.play(WS1) dipanggil saat Headset terdeteksi
- [ ] Unit test: WarningSound.play(WS1) dipanggil saat Unpin terdeteksi
- [ ] Unit test: proper teardown saat stop() dipanggil

## Blocked by

- #3 (WarningSound)
- #11a (KioskMode — untuk menerima unpin events)
