# Issue 13: MainActivity + NavGraph wiring + AndroidManifest permissions

## What to build

Final integration: menyatukan semua screen dalam NavHost dengan NavState, wire-up semua modul, dan konfigurasi AndroidManifest.

- MainActivity sebagai single-activity host
- NavGraph dengan NavState-based routing
- Semua modul di-inject ke screen yang membutuhkan
- AndroidManifest: permissions (INTERNET, ACCESS_FINE_LOCATION, BLUETOOTH, ACCESS_NOTIFICATION_POLICY), screenOrientation=portrait, wake lock
- CheatDetector di-start saat masuk Ujian, di-stop saat keluar Ujian

## Acceptance criteria

- [ ] MainActivity dengan NavHost Compose
- [ ] NavGraph: semua screen terdaftar dengan rute NavState
- [ ] Transisi navigasi sesuai aturan NavState (dari #1b)
- [ ] AppConfig di-inject ke semua modul yang membutuhkan
- [ ] AndroidManifest: izin runtime dideklarasikan
- [ ] AndroidManifest: screenOrientation=portrait di <activity>
- [ ] KioskMode.start() dipanggil saat masuk Ujian
- [ ] CheatDetector.start() dipanggil saat masuk Ujian, stop() saat keluar
- [ ] Full flow smoke test: Splash → Precheck → Login → Menu → Token Input → Ujian → CheatDetector → Exit

## Blocked by

- #6 (Splash Screen)
- #7 (Login Screen)
- #8 (Menu + Petunjuk)
- #9 (Token Input)
- #10 (Ujian Screen)
- #11b (CheatDetector)
- #12 (Pengawas Screen)
