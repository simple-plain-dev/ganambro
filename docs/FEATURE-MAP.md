# Feature Map — Ganambro

Memetakan setiap fitur MVP ke file UI, feature, resource, dan BuildConfig yang terkait.

---

## Fitur 1 — Splash & Pengecekan Sistem

| Layer | File | Keterangan |
|-------|------|------------|
| UI | `ui/screens/SplashScreen.kt` | Sequential terminal-style text, tampilkan `result.message` untuk error |
| Feature | `feature/precheck/Precheck.kt` | Interface: `name: String`, `suspend check(): PrecheckResult` |
| Feature | `feature/precheck/PrecheckResult.kt` | Sealed: `Pass`, `Fail(message: String)` |
| Feature | `feature/precheck/PrecheckRegistry.kt` | `register(Precheck)` + `runAll(): List<PrecheckResult>` |
| Feature | `feature/precheck/InternetCheck.kt` | Cek koneksi internet, kembalikan `Fail("Internet harus aktif")` |
| Feature | `feature/precheck/GpsCheck.kt` | Cek GPS aktif, kembalikan `Fail("GPS harus diaktifkan")` |
| Feature | `feature/precheck/BluetoothCheck.kt` | Cek bluetooth mati, kembalikan `Fail("Bluetooth harus dimatikan")` |
| Resource | `res/values/strings.xml` | Pesan error diambil dari `Fail.message` — string resource redundant, bisa dihapus |
| Resource | `res/values/colors.xml` | Terminal green `#00FF00` pada background hitam |
| AndroidManifest | `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_FINE_LOCATION`, `BLUETOOTH`/`BLUETOOTH_CONNECT` |

**Flow:** Cek sequential → gagal: tampilkan `result.message` + tombol keluar → keluar. Sukses: auto-navigate ke Login.

---

## Fitur 2 — Login via Chrome Custom Tab

| Layer | File | Keterangan |
|-------|------|------------|
| UI | `ui/screens/LoginScreen.kt` | Dua tombol: "Login dengan Google" dan "Lewati" |
| UI | `ui/components/ChromeTabLauncher.kt` | Wrapper `CustomTabsIntent`, buka `accounts.google.com`, auto-close setelah selesai |
| Resource | `res/values/strings.xml` | Label tombol login dan skip |

**Flow:** Login → Custom Tab → auto-close → Menu. Skip → Menu.

---

## Fitur 3 — Menu Utama

| Layer | File | Keterangan |
|-------|------|------------|
| UI | `ui/screens/MenuScreen.kt` | 3 tombol: Portal Ujian, Petunjuk, Keluar + logo + panggil `gesture.onTap()` |
| Feature | `feature/gesture/MenuPengawasGesture.kt` | Pure state machine: `onTap(Target): GestureState` |
| Resource | `res/drawable/ic_logo.xml` | Logo sekolah (vector drawable) |
| Resource | `res/values/strings.xml` | Label: "Portal Ujian", "Petunjuk", "Keluar" |

**Gesture:** tap logo → `gesture.onTap(LOGO)`, tap Petunjuk → `gesture.onTap(PETUNJUK)`. State `UNLOCKED` → muncul PIN dialog.

---

## Fitur 4 — Portal Ujian (Entry Token)

| Layer | File | Keterangan |
|-------|------|------------|
| UI | `ui/screens/PortalUjianScreen.kt` | Input field token (8-char hex) + tombol "Masuk Ujian" |
| Feature | `feature/token/Token.kt` | `generate(): String`, `validate(input: String): Boolean` — menghasilkan 8-char hex dari 8 digit pertama SHA-256 |
| Resource | `res/values/strings.xml` | Placeholder "Masukkan token", error "Token tidak valid" |

**Salt source:** `BuildConfig.SCHOOL_NAME + BuildConfig.APP_NAME + BuildConfig.APP_VERSION`

---

## Fitur 5 — Mode Ujian (Kiosk + WebView)

| Layer | File | Keterangan |
|-------|------|------------|
| UI | `ui/screens/ModeUjianScreen.kt` | WebView `startLockTask()` + `FLAG_KEEP_SCREEN_ON` + header toolbar |
| UI | `ui/components/WebViewToolbar.kt` | Header toolbar: Home (`URL_PORTAL_UJIAN`), Back, Forward, Exit |
| Feature | `feature/volume/VolumeManager.kt` | Set `STREAM_ALARM` ke max, polling setiap 2 detik |
| Feature | `feature/cheatdetector/CheatTrigger.kt` | Interface: `onEvent(CheatEvent): Severity` |
| Feature | `feature/cheatdetector/CheatDetector.kt` | `register(CheatTrigger)` + `notify(event)` |
| Feature | `feature/cheatdetector/UnpinTrigger.kt` | Deteksi unpin paksa → FATAL |
| Feature | `feature/cheatdetector/BackGestureTrigger.kt` | Deteksi gesture back → WARNING |
| Feature | `feature/cheatdetector/PauseTrigger.kt` | Deteksi `onPause()` → WARNING |
| Resource | `res/raw/warning_exit.wav` | Warning Sound 1: singkat, keras (exit normal) |
| Resource | `res/raw/warning_force.wav` | Warning Sound 2: sirine panjang, keras (curang) |

**Exit flows:**

| Jalur | Trigger | Sound | Outcome |
|-------|---------|-------|---------|
| Normal | Exit → dialog ketik "exit" → konfirmasi | Warning 1 | `stopLockTask()` → Menu |
| Curang | Unpin paksa | Warning 2 | `finishAndRemoveTask()` → keluar, hilang dari overview |
| Curang | Gesture back | Warning 2 | Warning saja |
| Curang | `onPause()` | Warning 2 | Warning saja |

---

## Fitur 6 — Petunjuk

| Layer | File | Keterangan |
|-------|------|------------|
| UI | `ui/screens/PetunjukScreen.kt` | Scrollable teks petunjuk penggunaan |
| Resource | `res/values/strings.xml` | Semua teks petunjuk dalam string resource |

---

## Fitur 7 — Menu Pengawas (Hidden + Generate Token)

| Layer | File | Keterangan |
|-------|------|------------|
| UI | `ui/screens/MenuPengawasScreen.kt` | Screen dengan 2 fase: (1) PIN 6-digit → (2) Generate Token + tampilan 8-char hex. `remember` state, tanpa ViewModel. |
| Feature | `feature/token/Token.kt` | `generate(): String` — 8-char hex dari 8 digit pertama SHA-256 |
| Feature | `feature/gesture/MenuPengawasGesture.kt` | State machine — `UNLOCKED` memicu navigasi ke screen ini |
| Build | `app/build.gradle.kts` | `TEACHER_PIN` field (6-digit PIN, default `"202606"`) |
| Build | `app/build.gradle.kts` | `SCHOOL_NAME`, `APP_NAME`, `APP_VERSION` (salt) |
| Build | `app/build.gradle.kts` | `URL_PORTAL_UJIAN` (Google Sites URL) |
| Resource | `res/values/strings.xml` | Placeholder PIN, label "Generate Token" |

---

## Fitur 8 — Navigasi & Routing

| Layer | File | Keterangan |
|-------|------|------------|
| UI | `MainActivity.kt` | Single-activity, Compose `NavHost`, routing antar screen |
| Feature | — | Navigasi ditangani seluruhnya di layer `ui/` |

---

## Dependency antar fitur

```
Splash ──▶ Login ──▶ Menu ──┬──▶ PortalUjian ──▶ ModeUjian
                             ├──▶ Petunjuk
                             └──▶ MenuPengawas
```

Semua fitur independen — hanya terhubung melalui navigasi di `ui/`.

## Plugin pattern summary

| Registry | Register method | Interface | Caller |
|----------|----------------|-----------|--------|
| `PrecheckRegistry` | `register(Precheck)` | `check() → PrecheckResult` | SplashScreen |
| `CheatDetector` | `register(CheatTrigger)` | `onEvent(event) → Severity` | ModeUjianScreen |
| `MenuPengawasGesture` | (built-in state machine) | `onTap(target) → GestureState` | MenuScreen |
