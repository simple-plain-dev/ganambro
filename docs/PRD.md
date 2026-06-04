# PRD: Ganambro — MVP Ujian Shell

## Problem Statement

Sekolah membutuhkan aplikasi ujian berbasis Google Forms yang terkunci (kiosk) untuk mencegah Peserta mengakses aplikasi lain atau keluar dari Ujian. Aplikasi harus menjadi wrapper ringan — tidak membangun ulang sistem ujian, melainkan mengunci browser ke Google Sites/Forms yang sudah ada. Pengawas harus bisa membuat Token yang berlaku 10 menit; Peserta harus memasukkan Token untuk memulai Ujian.

## Solution

Ganambro adalah shell Android yang membungkus Google Sites dalam WebView dengan screen pinning. Sebelum Ujian dimulai, aplikasi mengecek prasyarat sistem (internet, GPS, bluetooth) di layar Splash. Peserta bisa login Google via Chrome Custom Tab (opsional) lalu masuk ke Menu. Dari Menu, Peserta bisa melihat Petunjuk, masuk Portal Ujian dengan Token dari Pengawas, atau Pengawas bisa mengakses Menu Pengawas lewat gesture tersembunyi untuk membuat Token. Selama Mode Ujian, aplikasi memaksa volume maksimal, mendeteksi upaya curang (unpin paksa, gesture back, loss of focus), dan memutar warning sound.

## User Stories

### Splash & Precheck

1. Sebagai Peserta, saya ingin aplikasi mengecek internet, GPS, dan bluetooth sebelum ujian dimulai, agar saya tahu perangkat saya siap.
2. Sebagai Peserta, saya ingin melihat teks loading bergaya terminal saat pengecekan berlangsung, agar saya tahu progresnya.
3. Sebagai Peserta, jika GPS tidak diizinkan, saya ingin melihat pesan error yang jelas dan tombol untuk keluar, agar saya bisa memperbaiki setelan dan mencoba lagi.

### Login

4. Sebagai Peserta, saya ingin login ke akun Google saya lewat Chrome Custom Tab, agar Google Form nanti langsung mengenali saya tanpa login ulang.
5. Sebagai Peserta, saya ingin bisa melewati login jika saya tidak punya akun Google atau tidak ingin login, agar saya tetap bisa mengikuti ujian.

### Menu Utama

6. Sebagai Peserta, saya ingin melihat menu dengan tombol Portal Ujian, Petunjuk, dan Keluar, agar saya bisa memilih tindakan selanjutnya.
7. Sebagai Pengawas, saya ingin mengakses menu tersembunyi lewat gesture tap logo 3× lalu tap Petunjuk 1×, agar Peserta tidak bisa menemukan pembuat Token.

### Portal Ujian

8. Sebagai Peserta, saya ingin memasukkan Token yang diberikan Pengawas, agar saya bisa masuk ke Mode Ujian.
9. Sebagai Peserta, jika Token saya tidak valid atau sudah kadaluarsa, saya ingin melihat pesan "Token tidak valid atau sudah kadaluarsa. Silakan minta Token baru ke Pengawas.", agar saya tahu harus minta Token baru.

### Mode Ujian

9a. Sebagai Peserta, saya ingin layar tetap menyala (tidak mati) selama Mode Ujian, agar saya bisa fokus mengerjakan tanpa menyentuh layar hanya untuk mencegah screen-off.
9b. Sebagai Peserta, orientasi layar harus dikunci portrait selama Mode Ujian, agar tampilan konsisten.

10. Sebagai Peserta, saya ingin Google Sites/Forms terbuka dalam WebView fullscreen, agar saya bisa mengerjakan ujian.
11. Sebagai Peserta, saya ingin toolbar browser (Home, Back, Forward, Exit) tersedia di header WebView, agar saya bisa bernavigasi di dalam portal ujian.
12. Sebagai Peserta, saya ingin tombol Home mengarahkan ke URL portal ujian utama, agar saya tidak tersesat.
13. Sebagai Peserta, saya ingin aplikasi mencegah saya keluar dari Mode Ujian tanpa prosedur Exit, agar ujian tetap adil.
14. Sebagai Peserta, jika saya mencoba unpin paksa atau gesture back, saya ingin aplikasi mendeteksinya sebagai kecurangan dan memutar warning sound keras.
15. Sebagai Peserta, jika ada panggilan masuk atau alarm saat Mode Ujian, saya ingin aplikasi mendeteksi loss of focus dan memutar warning sound.
16. Sebagai Pengawas, jika Peserta unpin paksa, saya ingin aplikasi langsung keluar dan menghapus diri dari recent apps, agar Peserta harus minta Token baru.

### Exit Normal

17. Sebagai Peserta, saya ingin keluar dari Mode Ujian dengan menekan tombol Exit, lalu mengetik "exit" di dialog konfirmasi, agar tidak keluar tidak sengaja.
18. Sebagai Peserta, setelah konfirmasi Exit, saya ingin mendengar warning sound singkat lalu kembali ke Menu, agar saya tahu exit berhasil tanpa harus unpin manual.

### Volume Maksimal

19. Sebagai Pengawas, saya ingin volume media Peserta selalu di set maksimal selama aplikasi berjalan, agar warning sound selalu terdengar.
20. Sebagai Peserta, saya ingin volume saya kembali normal setelah keluar dari aplikasi.

### Menu Pengawas (Generate Token)

21. Sebagai Pengawas, saya ingin masuk ke Menu Pengawas dengan PIN 6-digit setelah gesture tersembunyi, agar hanya Pengawas yang bisa membuat Token.
22. Sebagai Pengawas, saya ingin menekan tombol "Generate Token" untuk membuat Token baru, agar saya bisa memberikannya ke Peserta.
23. Sebagai Pengawas, Token yang saya buat harus berlaku selama 10 menit, agar keamanannya terjaga.
24. Sebagai Pengawas, saya ingin Token tetap bisa dipakai berkali-kali dalam 10 menit yang sama, agar beberapa Peserta bisa masuk dengan Token yang sama.

### Petunjuk

25. Sebagai Peserta, saya ingin membaca halaman petunjuk penggunaan aplikasi, agar saya tidak bingung saat ujian.
26. Sebagai Peserta, saya ingin Petunjuk menjelaskan cara mematikan alarm, menonaktifkan mode senyap/getar, cara keluar normal dari Mode Ujian, dan bahwa Token berlaku 10 menit dari Pengawas, agar ujian tidak terganggu.

### Navigasi & Back Stack

27. Sebagai Peserta, saya tidak bisa menggunakan system back di sebagian besar layar (Login, Petunjuk, PortalUjian, MenuPengawas), agar saya tidak mundur ke layar sebelumnya tanpa izin.
28. Sebagai Peserta, saya hanya bisa kembali dari Petunjuk dan MenuPengawas lewat tombol "Kembali" eksplisit, agar navigasi terkontrol.
29. Sebagai Peserta, Portal Ujian adalah jalan satu arah — tidak ada tombol kembali ke Menu, saya harus memasukkan Token yang benar atau keluar aplikasi.

### Konfigurasi Sekolah

30. Sebagai admin TI sekolah, saya ingin mengatur nama sekolah, nama aplikasi, versi, PIN Pengawas, dan URL portal ujian di build config sebelum build APK, agar satu kode bisa dipakai untuk banyak sekolah.

## Implementation Decisions

### Arsitektur

- **Two-folder separation**: `ui/` (Android-aware, ViewModels + Compose) dan `feature/` (pure Kotlin, zero Android dependency).
- Dependency satu arah: `ui/` → `feature/`. `feature/` tidak boleh mengimpor `android.*`.
- `feature/` diorganisir granular per fitur: `token/`, `precheck/`, `volume/`, `gesture/`, `cheatdetector/`.
- Tiga modul menggunakan pola registrasi (plugin): PrecheckRegistry, CheatDetector, MenuPengawasGesture.

### Token

- Token = SHA-256(SCHOOL_NAME + APP_NAME + APP_VERSION + roundedTimestamp).
- `roundedTimestamp = (epochSeconds / 600) * 600` — floor ke window 10 menit.
- Satu modul `Token` dengan dua operasi: `generate()` dan `validate(input)`. Rounding, salt, SHA-256 adalah implementation detail.
- Token ditampilkan sebagai uppercase hex (0-9 A-F), 8 karakter (8 digit pertama SHA-256), tanpa tombol Copy/Share.
- Error validasi: "Token tidak valid atau sudah kadaluarsa. Silakan minta Token baru ke Pengawas."

Precision note — the Token module's interface:
```kotlin
object Token {
    fun generate(schoolName, appName, appVersion, timestampSeconds): String
    fun validate(input, schoolName, appName, appVersion, timestampSeconds): Boolean
}
```

### Precheck

- Interface: `Precheck.check()` mengembalikan `PrecheckResult` — sealed class `Pass` atau `Fail(message: String)`.
- Setiap check memiliki pesan error-nya sendiri. Caller (SplashScreen) hanya membaca `Fail.message` tanpa switch-on-name.
- Registry jalan sequential, berhenti di kegagalan pertama.

### Gesture Menu Pengawas

- Pure state machine: `onTap(Target): GestureState`. Target: `LOGO`, `PETUNJUK`. States: `IDLE → LOGO_1 → LOGO_2 → LOGO_3 → PETUNJUK_TRIGGERED → UNLOCKED`.
- Survives Composable recomposition. Tidak ada state di UI layer.

### Cheat Detector

- Tiga trigger: Unpin paksa (FATAL), Gesture back (WARNING), onPause (WARNING).
- Interface `CheatTrigger.onEvent(event): Severity?` — null berarti bukan urusannya.
- Unpin paksa → `finishAndRemoveTask()` (hapus dari recent apps).
- FATAL trigger berasal dari perubahan lock task state; WARNING dari WebView back interception dan Activity lifecycle.

### Mode Ujian

- `startLockTask()` untuk screen pinning.
- `FLAG_KEEP_SCREEN_ON` untuk mencegah layar mati (mengurangi false positive onPause).
- WebView dengan header toolbar (Home, Back, Forward, Exit).
- Exit normal: dialog ketik "exit" → Warning Sound 1 → `stopLockTask()` → kembali ke Menu.

### Volume

- STREAM_ALARM diset ke maksimal (bypass DND di sebagian besar device AOSP).
- Polling setiap 2 detik untuk memastikan volume tetap maksimal.
- Volume asli disimpan dan direstore saat aplikasi keluar.

### BuildConfig

Lima field build-time di `build.gradle.kts`:
- `SCHOOL_NAME`, `APP_NAME`, `APP_VERSION` — komponen salt Token.
- `TEACHER_PIN` — PIN 6-digit untuk Menu Pengawas (default `202606`).
- `URL_PORTAL_UJIAN` — URL Google Sites yang dibuka WebView.

### Warning Sound

- Warning 1 (singkat, keras) — exit normal.
- Warning 2 (sirine, panjang, keras) — semua trigger curang.
- File audio: `res/raw/warning_exit.wav` dan `res/raw/warning_force.wav` (built-in untuk MVP).

### Login

- Chrome Custom Tab membuka `accounts.google.com`.
- Auto-close setelah sesi tertanam.
- Tombol Skip untuk lewati login.
- Tidak ada fallback jika Chrome tidak terinstal (Chrome hampir selalu bawaan di device Indonesia).

### Navigasi & System Back

- System back diblokir di: Login, Petunjuk, PortalUjian, MenuPengawas (hanya tombol eksplisit).
- System back di Menu: `finishAffinity()` — langsung keluar aplikasi.
- System back di ModeUjian: dianggap curang (GESTURE_BACK).
- Petunjuk dan MenuPengawas: tombol "Kembali" eksplisit ke Menu.
- PortalUjian: dead end — tidak ada tombol kembali, hanya input Token.

### Isi Petunjuk

Halaman Petunjuk wajib memuat instruksi:
- Matikan alarm sebelum ujian.
- Nonaktifkan mode senyap/getar — warning sound harus terdengar.
- Jangan tinggalkan aplikasi selama ujian.
- Token didapat dari Pengawas, berlaku 10 menit.
- Cara keluar normal: tombol Exit → ketik "exit".
- Kalau keluar paksa, aplikasi akan bunyi keras dan hilang dari recent apps.

### Tampilan Token

- Token ditampilkan sebagai uppercase hex (0-9 A-F), 8 karakter (8 digit pertama SHA-256).
- Teks biasa dalam box — tanpa tombol Copy atau Share. Token disampaikan lisan atau ditulis di papan tulis oleh Pengawas.

### Permission

- `ACCESS_FINE_LOCATION` — runtime permission dialog di Splash. Ditolak → langsung error + tombol keluar (tidak ada retry loop).
- Sisanya normal permissions.

## Testing Decisions

### Prinsip

- Test external behavior, bukan implementation details.
- `feature/` modules pure Kotlin → testable di JVM tanpa Android runner.

### Modul yang diuji

| Modul | Jenis tes | Alasan |
|-------|-----------|--------|
| `Token` | Unit (JVM) | Logic kriptografis — rounding window, salt, SHA-256. Generate + validate dalam satu test. |
| `MenuPengawasGesture` | Unit (JVM) | State machine — semua transisi state harus terverifikasi. |
| `PrecheckRegistry` | Unit (JVM) | Registrasi + sequential run + stop-at-first-fail. |
| `Precheck` (masing-masing) | Unit (JVM) | Masing-masing mengembalikan Pass/Fail yang benar. |
| `CheatDetector` | Unit (JVM) | Register trigger + notify mengembalikan Severity yang benar. |
| `VolumeManager` | Unit (JVM) | Mock get/set volume → verifikasi max + polling + restore. |
| SplashViewModel | Unit (Robolectric/VM) | Orchestration precheck → navigasi Login atau error. |
| PortalUjianViewModel | Unit (VM) | Token validation → navigasi ModeUjian atau error. |
| ModeUjianViewModel | Unit (VM) | Cheat detector notify → warning sound + finishAndRemoveTask. |

## Out of Scope

- **Database lokal**: MVP tidak menyimpan data apapun. Tidak ada Room, DAO, atau migrasi.
- **Backend/server**: Semua validasi Token dilakukan lokal.
- **Login wajib**: Login Google opsional — tidak ada enforcement.
- **Multi-Ujian**: Satu APK = satu URL portal ujian. Tidak ada pemilihan ujian.
- **Timer ujian**: Tidak ada batasan waktu dari sisi aplikasi.
- **Laporan kecurangan**: Event curang hanya memutar suara dan/atau keluar — tidak ada logging atau reporting.
- **Block screenshot/cast**: Tidak ada deteksi screenshot untuk MVP.
- **Kustomisasi UI**: Tema terminal di Splash adalah satu-satunya kustomisasi visual.
- **Update OTA**: APK didistribusikan manual per sekolah.

## Further Notes

- APK dibuild per sekolah dengan mengubah BuildConfig fields. Tidak ada mekanisme konfigurasi runtime.
- PIN Menu Pengawas hardcoded di BuildConfig — akan diganti setiap rilis versi baru.
- Chrome Custom Tab mengandalkan Chrome terinstal di device Peserta (bawaan di device Indonesia). Tanpa Chrome, tombol login tidak muncul — Peserta harus Skip.
- Screen pinning bisa di-unpin manual (back+overview) di device tanpa device owner — inilah alasan Token 10 menit sebagai rem prosedural.
- Semua file di `feature/` ditulis dalam Bahasa Inggris (kode), pesan error untuk Peserta ditulis dalam Bahasa Indonesia.
