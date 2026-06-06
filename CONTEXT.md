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
Modul yang memiliki screen state perangkat selama Ujian. Tanggung jawab: menyembunyikan system bars (mode imersi), mengunci orientasi potret. Tidak menangani volume atau suara — itu milik CheatDetector dan Warning Sound.
_Avoid_: Lockdown mode, exam mode

**CheatDetector**:
Modul coordinator yang memantau kondisi perangkat selama Ujian. Interface: `start()` / `stop()` dan `Flow<CheatEvent>`. Mencakup tiga pemicu:
- **Volume turun**: volume dipaksa kembali ke maksimal secara otomatis (CheatDetector memiliki background volume enforcement)
- **Headset terpasang**: aplikasi langsung keluar + Warning Sound 1
- **Unpin / Split Screen**: Peserta meninggalkan aplikasi atau membuka layar terpisah → popup peringatan + Warning Sound 1 → kembali ke Menu
CheatDetector bergantung pada Warning Sound sebagai adapter untuk memutar suara.
_Avoid_: Cheat detection, integrity check

**Warning Sound**:
Modul yang mengenkapsulasi pemutaran suara peringatan. Interface: `play(type: WarningSoundType)` — selalu diputar di volume maksimal (invariant internal modul). Dua jenis:
- **Warning Sound 1**: durasi panjang, volume keras — dipicu CheatDetector saat kecurangan terdeteksi
- **Warning Sound 2**: durasi pendek, volume keras — dipicu ExitCoordinator saat Peserta keluar dari Ujian
_Avoid_: Alarm, alert

**Splash**:
Halaman awal aplikasi yang menampilkan Precheck dengan animasi seperti terminal. Setelah semua cek berhasil, tampilkan tombol lanjut atau jeda otomatis sebelum pindah ke halaman Login. Jika Precheck gagal, tampilkan tombol keluar.

**Login**:
Halaman otentikasi menggunakan WebView untuk Google Sign-In. Peserta bisa skip untuk masuk sebagai anonim. Halaman Login hanya bisa maju — tidak bisa mundur atau keluar. _Catatan MVP: Google mungkin memblokir OAuth di WebView; ini akan dievaluasi saat implementasi._

**Menu**:
Halaman utama setelah Login. Berisi tombol Ujian (menuju Situs Ujian), tombol Petunjuk, tombol Exit, dan tombol Logo.

**Situs Ujian**:
Halaman Google Sites yang menjadi landing page Ujian, berisi kumpulan link Google Form. Peserta menavigasi antar Form menggunakan Toolbar Ujian.

**ExitCoordinator**:
Modul yang memiliki seluruh behaviour keluar dari aplikasi. Interface: `exit(from: ExitContext)` — internal branching menangani perbedaan antara:
- **Menu**: langsung keluar tanpa konfirmasi
- **Ujian**: dialog konfirmasi ketik "exit" → Warning Sound 2 → keluar
Caller (MenuScreen, Toolbar Ujian) hanya memanggil satu metode.
_Avoid_: Exit handler, quit manager

**Toolbar Ujian**:
Bar navigasi di halaman Ujian berisi empat tombol: Home (kembali ke URL soal awal), Back (mundur satu halaman), Forward (maju satu halaman), Exit (keluar Ujian — didelegasikan ke ExitCoordinator). Tidak ada URL bar — navigasi terbatas pada tombol-tombol ini.

**Petunjuk**:
Halaman panduan langkah-demi-langkah penggunaan aplikasi untuk Peserta. Konten dipisahkan dari rendering: `PetunjukContent` data class (judul, langkah-langkah, footer) dibaca dari resource; PetunjukScreen menerima konten sebagai parameter.

**HiddenAccessTrigger**:
Modul pure Kotlin yang mengenkapsulasi state machine akses tersembunyi ke halaman Pengawas. Interface: `registerClick(target: TriggerTarget): TriggerState` — melacak sekuens klik (Logo 3x lalu Petunjuk 1x), reset jika klik lain di antaranya. State: Idle, Counting(logoCount), Triggered.
_Avoid_: Secret knock, hidden menu detector

**Anonim**:
Status Peserta yang memilih skip di halaman Login. Peserta Anonim tidak tertaut akun Google manapun; Google Form tidak mengenali identitasnya.
_Avoid_: Guest, anonymous user
