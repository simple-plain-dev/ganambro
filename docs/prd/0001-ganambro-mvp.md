# PRD: Ganambro MVP — Kiosk Browser Ujian Sekolah

> Istilah domain yang digunakan dalam dokumen ini — **Peserta, Pengawas, Token, Precheck, Kiosk Mode, Deteksi Kecurangan, Warning Sound, Situs Ujian** dan lainnya — didefinisikan di [CONTEXT.md](../../CONTEXT.md). Keputusan arsitektural yang mendasarinya dicatat di [ADR-0001](../adr/0001-local-token-no-backend.md).

## Problem Statement

Sekolah menyelenggarakan ujian berbasis Google Form tetapi tidak memiliki cara untuk mencegah Peserta membuka aplikasi lain, mencari jawaban di internet, atau berkomunikasi via headset. Browser biasa tidak bisa mengunci perangkat ke satu halaman ujian. Dibutuhkan aplikasi wrapper browser yang mengunci perangkat selama Ujian, mendeteksi kecurangan, dan mudah digunakan oleh Peserta maupun Pengawas — tanpa memerlukan server backend.

## Solution

Ganambro adalah aplikasi Android Kiosk Browser. Peserta membuka aplikasi → melewati Precheck (GPS, Bluetooth, headset, volume, internet) → login (opsional) → masuk ke Menu → Pengawas memberikan Token 6-digit → Peserta memasukkan Token → WebView membuka Situs Ujian (Google Sites) yang berisi kumpulan Google Form. Selama Ujian, perangkat dalam Kiosk Mode, Deteksi Kecurangan aktif, dan navigasi terbatas pada Toolbar Ujian. Tidak ada backend — Token divalidasi secara lokal via hashing deterministik.

## User Stories

### Precheck & Splash
1. Sebagai Peserta, saya ingin aplikasi mengecek kondisi perangkat saya sebelum ujian (internet, GPS, Bluetooth, headset, volume), agar saya tahu perangkat saya siap atau tidak.
2. Sebagai Peserta, saya ingin melihat status setiap cek satu per satu seperti animasi terminal, agar saya bisa mengikuti prosesnya secara jelas.
3. Sebagai Peserta, jika Precheck gagal (misal Bluetooth menyala), saya ingin diberi tahu secara spesifik apa yang salah dan tombol untuk keluar, agar saya bisa memperbaikinya dan membuka ulang aplikasi.
4. Sebagai Peserta, jika semua Precheck berhasil, saya ingin tombol untuk melanjutkan ke Login, agar saya tidak langsung masuk tanpa konfirmasi.

### Login
5. Sebagai Peserta, saya ingin login dengan akun Google saya di WebView, agar identitas saya tercatat (jika dibutuhkan).
6. Sebagai Peserta, saya ingin bisa skip login dan masuk sebagai anonim, agar saya tetap bisa mengikuti ujian meskipun tidak ingat password Google.
7. Sebagai Peserta, saya tidak ingin bisa mundur dari halaman Login ke Splash, agar alur aplikasi tetap terkendali.

### Menu
8. Sebagai Peserta, saya ingin melihat halaman Menu dengan tombol Ujian, Petunjuk, Exit, dan Logo, agar saya bisa memilih tindakan selanjutnya.
9. Sebagai Peserta, saya ingin membaca panduan penggunaan di halaman Petunjuk, agar saya paham cara menggunakan aplikasi.
10. Sebagai Peserta, saya ingin keluar dari aplikasi langsung dari halaman Menu tanpa konfirmasi, agar cepat selesai setelah ujian.

### Ujian — Token & Masuk
11. Sebagai Peserta, saya ingin diminta memasukkan Token 6-digit alphanumerik caps dari Pengawas sebelum masuk Ujian, agar hanya Peserta yang sah yang bisa mengakses soal.
12. Sebagai Peserta, jika Token yang saya masukkan salah, saya ingin diberi tahu dan bisa mencoba lagi, agar saya tidak terkunci karena typo.
13. Sebagai Peserta, setelah Token valid, saya ingin langsung diarahkan ke halaman soal (Google Sites) di WebView, agar saya bisa segera mengerjakan.

### Ujian — Browser & Toolbar
14. Sebagai Peserta, saya ingin melihat toolbar dengan tombol Home, Back, Forward, dan Exit di atas WebView, agar saya bisa bernavigasi dalam Google Sites tanpa URL bar.
15. Sebagai Peserta, saya ingin tombol Home mengembalikan saya ke halaman awal Google Sites, agar setelah selesai satu mata pelajaran saya bisa memulai mata pelajaran lain tanpa keluar aplikasi.
16. Sebagai Peserta, saya ingin tombol Back dan Forward berfungsi seperti navigasi browser biasa, agar saya bisa mundur/maju antar halaman.
17. Sebagai Peserta, ketika saya klik Exit di Ujian, saya harus mengetik "exit" sebagai konfirmasi, agar saya tidak keluar secara tidak sengaja.
18. Sebagai Peserta, setelah konfirmasi exit, saya ingin mendengar Warning Sound 2 lalu aplikasi keluar, agar exit sah terdengar oleh Pengawas.

### Kiosk Mode & Deteksi Kecurangan
19. Sebagai Peserta, selama Ujian saya tidak boleh melihat status bar dan navigation bar Android, agar saya fokus pada soal.
20. Sistem harus memaksa volume perangkat ke maksimal setiap 10 detik selama Ujian, agar Warning Sound selalu terdengar jika terjadi kecurangan.
21. Sistem harus mendeteksi headset terpasang selama Ujian — jika terdeteksi, aplikasi langsung keluar dan membunyikan Warning Sound 1.
22. Sistem harus mendeteksi Peserta meninggalkan aplikasi (tekan Home/unpin Kiosk) — jika terdeteksi, tampilkan popup peringatan, bunyikan Warning Sound 1, dan kembalikan Peserta ke Menu.
23. Sistem harus mendeteksi split screen — jika terdeteksi, tampilkan popup peringatan, bunyikan Warning Sound 1, dan kembalikan Peserta ke Menu.

### Pengawas
24. Sebagai Pengawas, saya ingin akses ke halaman Pengawas tersembunyi — dengan mengklik Logo 3x lalu Petunjuk 1x secara berurutan dari Menu, agar Peserta tidak bisa mengaksesnya secara tidak sengaja.
25. Sebagai Pengawas, saya harus memasukkan PIN 6-digit numerik untuk membuka halaman Pengawas, agar hanya petugas sah yang bisa generate Token.
26. Sebagai Pengawas, saya ingin melihat Token yang sedang aktif di layar, agar saya bisa memberikannya ke Peserta.
27. Sebagai Pengawas, saya ingin melihat countdown mundur ke time window berikutnya, agar saya tahu kapan Token akan berubah.
28. Sebagai Pengawas, setelah time window habis, saya harus klik "Generate" lagi untuk mendapatkan Token baru, agar saya sadar Token sudah berganti.

### Token — Mekanisme
29. Sebagai Pengawas, Token yang saya generate harus sama untuk semua Peserta dalam time window yang sama, agar satu Token cukup untuk seluruh kelas.
30. Sebagai Pengawas, Token harus berganti otomatis setiap time window (default 10 menit), agar Token kadaluarsa secara alami tanpa server.

### Penanganan Error
31. Sebagai Peserta, jika internet putus saat Ujian, saya ingin melihat pesan error dan tombol keluar, agar saya tahu apa yang terjadi dan bisa segera keluar.
32. Sebagai Peserta, layar aplikasi harus terkunci orientasi potret selama Ujian, agar tampilan tidak berubah saat perangkat dimiringkan.

## Implementation Decisions

### Token module — deep, pure domain
Token generation dan validation diimplementasikan sebagai modul murni tanpa dependensi Android. Interface: `generate(salt: String, timeWindowMinutes: Int): String` dan `validate(token: String, salt: String, timeWindowMinutes: Int): Boolean`. Salt dihitung dari komponen build.

Mengacu pada ADR-0001: validasi dilakukan lokal tanpa server. Token berlaku penuh selama Time Window.

### Precheck pipeline
Lima pengecekan (Internet, GPS, Bluetooth, Headset, Volume) dienkapsulasi dalam PrecheckRunner dengan interface tunggal: `suspend fun runChecks(): Flow<CheckResult>`. Setiap cek adalah adapter internal yang menyembunyikan Android API spesifik (ConnectivityManager, LocationManager, BluetoothAdapter, AudioManager). SplashScreen hanya mengobservasi Flow — tidak tahu API apa pun. Pengecekan berjalan sekuensial sesuai urutan.

### CheatDetector coordinator
Tiga mekanisme deteksi (volume watcher via foreground service, headset detection via BroadcastReceiver, unpin/split detection via lifecycle callbacks) dikoordinasikan dalam satu modul dengan interface: `start()` / `stop()` dan `Flow<CheatEvent>`. CheatDetector bergantung pada Warning Sound sebagai adapter untuk memutar suara saat kecurangan terdeteksi. Semua downstream actions (memutar Warning Sound, menampilkan popup, navigasi ke Menu, exit aplikasi) ditangani satu kali dari Flow — bukan direplikasi di setiap mekanisme.

### Navigasi sebagai state machine
Navigasi tidak menggunakan string routes langsung — melainkan sealed class NavState (Splash, Login, Menu, Ujian, Pengawas) dengan aturan transisi eksplisit. Transisi ilegal (Login→Splash, Menu→Ujian tanpa Token) menjadi impossible by construction. Aturan: Splash hanya bisa ke Login atau Exit; Login hanya ke Menu (tidak mundur); Menu ke Ujian harus dengan Token valid; Ujian kembali ke Menu saat exit atau unpin.

### AppConfig module
Semua build variable (SCHOOL_NAME, APP_NAME, PIN, EXAM_URL, TOKEN_WINDOW_MINUTES) dibaca dari BuildConfig satu kali di startup, dirakit menjadi AppConfig data class, dan diinject ke modul-modul yang membutuhkan. Modul tidak pernah mengakses BuildConfig langsung.

### Login via WebView
Login menggunakan WebView yang membuka Google Sign-In. Peserta bisa skip untuk masuk sebagai anonim. Halaman Login tidak memiliki tombol mundur — hanya bisa maju ke Menu. Catatan: Google mungkin memblokir OAuth di WebView; risiko diterima untuk MVP.

### Kiosk Mode — immersion
Modul yang memiliki screen state perangkat. Menyembunyikan system bars (mode imersi, bukan `startLockTask`), mengunci orientasi potret. Tidak menangani volume atau suara — itu milik CheatDetector dan Warning Sound. Deteksi unpin via `onPause` + `onStop` + `isInMultiWindowMode` — hasilnya dikirim ke CheatDetector sebagai event.

### Warning Sound
Modul yang mengenkapsulasi pemutaran suara peringatan. Interface: `play(type: WarningSoundType): Unit` — selalu diputar di volume maksimal. Dua file audio disertakan sebagai asset aplikasi: Warning Sound 1 (durasi panjang, volume keras — dipicu CheatDetector) dan Warning Sound 2 (durasi pendek, volume keras — dipicu ExitCoordinator).

### ExitCoordinator
Modul yang memiliki seluruh behaviour keluar dari aplikasi. Interface: `exit(from: ExitContext): Unit` — internal branching:
- `ExitContext.Menu`: langsung keluar tanpa konfirmasi
- `ExitContext.Ujian`: dialog konfirmasi ketik "exit" → Warning Sound 2 → keluar
Caller (MenuScreen, Toolbar Ujian) hanya memanggil satu metode.

### Halaman Pengawas — akses tersembunyi
Trigger: **HiddenAccessTrigger** — modul pure Kotlin dengan interface `registerClick(target: TriggerTarget): TriggerState`, melacak sekuens klik Logo 3x lalu Petunjuk 1x secara berurutan di Menu. Counter reset jika ada klik lain di antaranya. Setelah state Triggered, tampilkan input PIN 6-digit. Setelah PIN benar, halaman Pengawas menampilkan Token aktif + countdown mundur. Pengawas harus klik Generate lagi setelah time window habis.

### Build variables
| Variabel | Digunakan untuk |
|---|---|
| SCHOOL_NAME | Komponen Salt |
| APP_NAME | Komponen Salt |
| PIN | Akses halaman Pengawas |
| EXAM_URL | Situs Ujian yang dimuat di WebView Ujian |
| TOKEN_WINDOW_MINUTES | Durasi Time Window dalam menit (default 10) |

APP_VERSION diambil dari `versionName` di build.gradle.

### Precheck urutan dan perilaku
Urutan: Internet → GPS → Bluetooth → Headset → Volume. Setiap cek menampilkan status di layar dengan animasi terminal. Gagal = pesan spesifik + tombol keluar (harus restart aplikasi untuk retry). Sukses semua = tombol lanjut ke Login.

## Testing Decisions

### Filosofi pengujian
Test hanya external behavior — bukan implementation details. Interface adalah test surface. Callers dan tests melintasi seam yang sama.

### Modul yang diuji
1. **TokenGenerator** — prioritas tertinggi. Pure Kotlin, zero Android, diuji di JUnit5. Test cases: token konsisten dalam satu time window, token berbeda antar time window, token di perbatasan time window, SHA-256 6-char extraction, token uppercase, validasi token valid/invalid, timestamp UTC boundary. Dibangun dan diuji sebelum semua modul lain.

2. **PrecheckRunner** — integrasi internal. Test dengan fake Checker adapters untuk assert: urutan eksekusi, Flow emission berurutan, propagasi kegagalan (satu gagal → Flow selesai dengan Failed), semua sukses → Flow selesai dengan Passed.

3. **CheatDetector** — unit test dengan fake DetectorService dan fake WarningSound. Assert: event Flow emission yang benar (headset terpasang → CheatEvent.Headset, unpin → CheatEvent.Unpin), WarningSound.play(WS1) dipanggil saat headset/unpin terdeteksi, proper teardown saat stop() dipanggil.

4. **AppConfig** — unit test sederhana. Assert: Salt dihitung benar dari komponen, default TOKEN_WINDOW_MINUTES saat tidak diset.

5. **NavState** — unit test pure. Assert: setiap transisi legal bisa dieksekusi, setiap transisi ilegal throws/mengembalikan state yang sama.

6. **WarningSound** — unit test dengan fake MediaPlayer. Assert: `play(WS1)` memilih file audio yang benar, `play(WS2)` memilih file yang benar, volume diset ke maksimal sebelum play.

7. **ExitCoordinator** — unit test pure. Assert: `exit(Menu)` memanggil System.exit tanpa dialog, `exit(Ujian)` memunculkan konfirmasi lalu System.exit + Warning Sound 2.

8. **HiddenAccessTrigger** — unit test pure. Assert: sekuens Logo×3 + Petunjuk×1 → Triggered, klik salah di tengah → reset ke Idle, klik Petunjuk duluan → tetap Idle, Triggered lalu klik lagi → tetap Triggered.

### Tidak diuji (MVP)
Screens UI (Compose) — diuji kemudian, bukan prioritas. Android-specific integrations (actual ConnectivityManager call, actual BroadcastReceiver) — sulit di-test di unit test, akan diverifikasi manual.

## Out of Scope

- Backend server untuk validasi token atau sinkronisasi
- Dukungan multi-sekolah dalam satu build (satu build = satu sekolah)
- Google Sign-In native (Credential Manager) — ditunda ke iterasi berikutnya
- Durasi ujian dan waktu mulai otomatis per mata pelajaran (config per URL)
- Multi-device Pengawas (monitoring real-time status Peserta)
- Camera proctoring
- Logging terpusat atau audit trail token
- Dukungan iOS / platform selain Android
- Deteksi floating apps spesifik — sudah tercakup oleh deteksi lifecycle/unpin
- Keyboard detection / external keyboard blocking

## Further Notes

- Aplikasi ini ditargetkan minSdk 21 (Android 5.0) untuk coverage 99% perangkat sekolah.
- Izin runtime yang diperlukan: INTERNET, ACCESS_FINE_LOCATION (GPS), BLUETOOTH, ACCESS_NOTIFICATION_POLICY (volume/DND override). Aplikasi akan meminta izin saat Precheck berjalan.
- Mode imersi mungkin tidak bekerja sempurna di beberapa vendor Android (Xiaomi, Oppo). Risiko diterima.
- Google OAuth di WebView mungkin diblokir — jika terjadi, Login akan difungsikan hanya sebagai skip-able screen dan identitas Peserta tidak diverifikasi.
