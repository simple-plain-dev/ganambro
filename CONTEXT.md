# Ganambro

Aplikasi Kiosk Browser untuk ujian sekolah. Membungkus browser Android agar Peserta hanya bisa mengakses soal ujian dan tidak bisa membuka aplikasi lain.

## Language

**Ujian**:
Satu sesi ujian yang berlangsung di dalam Kiosk Browser. Peserta hanya bisa mengakses URL soal yang sudah ditentukan.
_Avoid_: Exam session, test

**Peserta**:
Siswa yang mengikuti Ujian.
_Avoid_: User, student, examinee

**Pengawas**:
Guru atau petugas yang mengawasi Ujian, membuat Token, dan mengelola akses halaman Pengawas.
_Avoid_: Proctor, admin, supervisor

**Token**:
Kode 6-digit alphanumerik caps yang dibuat Pengawas. Token = 6 karakter pertama alphanumerik dari SHA-256(Salt + timestamp UTC dibulatkan ke durasi time window). Validasi dilakukan lokal tanpa server. Token yang sama berlaku untuk semua Peserta dalam time window yang sama.
_Avoid_: Kode ujian, access code

**Time Window**:
Durasi dalam menit (default 10) di mana Token yang dihasilkan bersifat konstan. Timestamp UTC dibulatkan ke bawah ke kelipatan time window. Dikonfigurasi sebagai build variable `TOKEN_WINDOW_MINUTES`.

**PIN**:
Kode 6-digit numerik statis yang digunakan Pengawas untuk membuka halaman Pengawas. Dikonfigurasi sebagai build variable.
_Avoid_: Password, passcode

**Salt**:
String yang terdiri dari nama sekolah + versi aplikasi + nama aplikasi, disimpan sebagai build variable. Digunakan sebagai input fungsi hash untuk menghasilkan Token.
_Avoid_: Secret key, seed

**Precheck**:
Serangkaian pemeriksaan kondisi perangkat yang berjalan sekuensial di halaman Splash. Urutan: (1) Internet aktif, (2) GPS menyala, (3) Bluetooth mati, (4) Headset tidak terpasang, (5) Volume maksimal. Setiap cek menampilkan status di layar dengan animasi terminal. Jika ada yang gagal, tampilkan pesan spesifik dan tombol keluar — Peserta harus memperbaiki kondisi lalu membuka ulang aplikasi.
_Avoid_: System check, validation

**Kiosk Mode**:
Status perangkat saat Ujian berlangsung. Navigasi sistem diblokir dan deteksi kecurangan aktif.
_Avoid_: Lockdown mode, exam mode

**Deteksi Kecurangan**:
Pemantauan kondisi perangkat selama Ujian. Implementasi:
- **Unpin / meninggalkan app**: deteksi via callback lifecycle Activity (`onPause`, `onStop` + `isInMultiWindowMode`)
- **Volume**: watcher tiap 10 detik via foreground service, memaksa volume ke maksimal
- **Headset**: BroadcastReceiver `ACTION_HEADSET_PLUG` + `AudioManager.isWiredHeadsetOn()`

Aksi saat terpicu:
- Volume turun → auto-koreksi ke maksimal (tanpa popup, tanpa sound)
- Headset terpasang → aplikasi langsung keluar + Warning Sound 1
- Unpin/Split Screen → popup pesan + Warning Sound 1 → kembali ke Menu
_Avoid_: Cheat detection, integrity check

**Warning Sound**:
Suara peringatan yang berbunyi saat kecurangan terdeteksi. Ada dua jenis: Warning Sound 1 (durasi panjang, volume keras — kecurangan terdeteksi) dan Warning Sound 2 (durasi pendek, volume keras — Peserta keluar dari Ujian).
_Avoid_: Alarm, alert

**Splash**:
Halaman awal aplikasi yang menampilkan Precheck dengan animasi seperti terminal. Setelah semua cek berhasil, tampilkan tombol lanjut atau jeda otomatis sebelum pindah ke halaman Login. Jika Precheck gagal, tampilkan tombol keluar.

**Login**:
Halaman otentikasi menggunakan WebView untuk Google Sign-In. Peserta bisa skip untuk masuk sebagai anonim. Halaman Login hanya bisa maju — tidak bisa mundur atau keluar. _Catatan MVP: Google mungkin memblokir OAuth di WebView; ini akan dievaluasi saat implementasi._

**Menu**:
Halaman utama setelah Login. Berisi tombol Ujian (satu tombol menuju EXAM_URL), tombol Petunjuk, tombol Exit, dan tombol Logo.

**EXAM_URL**:
URL Google Sites yang berisi kumpulan link Google Form. Peserta menavigasi antar Form via Sites menggunakan Toolbar Ujian. Dikonfigurasi sebagai build variable.

**Exit**:
Mekanisme keluar dari aplikasi. Ada dua jalur: (1) dari Menu via tombol Exit — langsung keluar tanpa konfirmasi, (2) dari Ujian via toolbar Exit — ketik "exit" untuk konfirmasi, Warning Sound 2 berbunyi, lalu aplikasi keluar.

**Toolbar Ujian**:
Bar navigasi di halaman Ujian berisi empat tombol: Home (kembali ke URL soal awal), Back (mundur satu halaman), Forward (maju satu halaman), Exit (keluar Ujian). Tidak ada URL bar — navigasi terbatas pada tombol-tombol ini.

**Petunjuk**:
Halaman panduan langkah-demi-langkah penggunaan aplikasi untuk Peserta.

**Kiosk Mode**:
Mode imersi yang menyembunyikan system bars (status bar, navigation bar) selama Ujian. Deteksi unpin dilakukan via callback lifecycle Activity, bukan via `startLockTask`.
