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
Kode 6-digit alphanumerik caps yang dibuat dari Salt dan waktu. Berlaku untuk semua Peserta selama satu Time Window. Divalidasi secara lokal tanpa server.
_Avoid_: Kode ujian, access code

**Time Window**:
Periode waktu di mana Token yang dihasilkan bersifat konstan. Timestamp dibulatkan ke bawah ke kelipatan durasi window. Di luar periode ini, Token berubah.

**PIN**:
Kode 6-digit numerik statis yang digunakan Pengawas untuk membuka halaman Pengawas.
_Avoid_: Password, passcode

**Salt**:
Nilai tunggal yang unik per build aplikasi, digunakan sebagai input untuk menghasilkan Token dan PIN.
_Avoid_: Secret key, seed

**Precheck**:
Serangkaian pemeriksaan kondisi perangkat yang berjalan sekuensial di halaman Splash. Urutan: (1) Internet aktif, (2) GPS menyala, (3) Bluetooth mati, (4) Headset tidak terpasang, (5) Volume maksimal. Setiap cek menampilkan status di layar dengan animasi terminal. Jika ada yang gagal, tampilkan pesan spesifik dan tombol keluar — Peserta harus memperbaiki kondisi lalu membuka ulang aplikasi.
_Avoid_: System check, validation

**Kiosk Mode**:
Status perangkat saat Ujian berlangsung. System bars disembunyikan (mode imersi), navigasi sistem diblokir, dan Deteksi Kecurangan aktif.
_Avoid_: Lockdown mode, exam mode

**Deteksi Kecurangan**:
Pemantauan kondisi perangkat selama Ujian. Mencakup tiga pemicu:
- **Volume turun**: volume dipaksa kembali ke maksimal secara otomatis
- **Headset terpasang**: aplikasi langsung keluar + Warning Sound 1
- **Unpin / Split Screen**: Peserta meninggalkan aplikasi atau membuka layar terpisah → popup peringatan + Warning Sound 1 → kembali ke Menu
_Avoid_: Cheat detection, integrity check

**Warning Sound**:
Suara peringatan yang berbunyi saat kecurangan terdeteksi. Ada dua jenis: Warning Sound 1 (durasi panjang, volume keras — kecurangan terdeteksi) dan Warning Sound 2 (durasi pendek, volume keras — Peserta keluar dari Ujian).
_Avoid_: Alarm, alert

**Splash**:
Halaman awal aplikasi yang menampilkan Precheck dengan animasi seperti terminal. Setelah semua cek berhasil, tampilkan tombol lanjut atau jeda otomatis sebelum pindah ke halaman Login. Jika Precheck gagal, tampilkan tombol keluar.

**Login**:
Halaman otentikasi menggunakan WebView untuk Google Sign-In. Peserta bisa skip untuk masuk sebagai anonim. Halaman Login hanya bisa maju — tidak bisa mundur atau keluar. _Catatan MVP: Google mungkin memblokir OAuth di WebView; ini akan dievaluasi saat implementasi._

**Menu**:
Halaman utama setelah Login. Berisi tombol Ujian (menuju Situs Ujian), tombol Petunjuk, tombol Exit, dan tombol Logo.

**Situs Ujian**:
Halaman Google Sites yang menjadi landing page Ujian, berisi kumpulan link Google Form. Peserta menavigasi antar Form menggunakan Toolbar Ujian.

**Exit**:
Mekanisme keluar dari aplikasi. Ada dua jalur: (1) dari Menu via tombol Exit — langsung keluar tanpa konfirmasi, (2) dari Ujian via toolbar Exit — ketik "exit" untuk konfirmasi, Warning Sound 2 berbunyi, lalu aplikasi keluar.

**Toolbar Ujian**:
Bar navigasi di halaman Ujian berisi empat tombol: Home (kembali ke URL soal awal), Back (mundur satu halaman), Forward (maju satu halaman), Exit (keluar Ujian). Tidak ada URL bar — navigasi terbatas pada tombol-tombol ini.

**Petunjuk**:
Halaman panduan langkah-demi-langkah penggunaan aplikasi untuk Peserta.

**Anonim**:
Status Peserta yang memilih skip di halaman Login. Peserta Anonim tidak tertaut akun Google manapun; Google Form tidak mengenali identitasnya.
_Avoid_: Guest, anonymous user
