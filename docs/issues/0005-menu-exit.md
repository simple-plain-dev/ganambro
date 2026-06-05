# Issue 5: Menu + Exit from Menu

## What to build

Halaman Menu utama setelah Login. Berisi empat tombol: **Ujian** (menuju input Token lalu Situs Ujian), **Petunjuk** (halaman panduan statis), **Exit** (keluar aplikasi), dan **Logo** (bagian dari trigger tersembunyi ke halaman Pengawas — diimplementasikan di Issue 8).

Halaman **Petunjuk** adalah Composable statis berisi panduan langkah-demi-langkah penggunaan aplikasi — tidak ada logika kompleks, cukup text content.

Exit dari Menu: tombol Exit langsung keluar dari aplikasi tanpa konfirmasi. Ini berbeda dengan Exit dari Ujian (yang memerlukan konfirmasi ketik "exit" — diimplementasikan di Issue 6).

**End-to-end:** dari Login → MenuScreen muncul dengan 4 tombol → Peserta klik Ujian → navigasi ke token entry screen (Issue 6). Peserta klik Petunjuk → halaman panduan muncul → bisa kembali ke Menu. Peserta klik Exit → finish() langsung.

## Acceptance criteria

- [ ] MenuScreen menampilkan 4 tombol: Ujian, Petunjuk, Exit, Logo
- [ ] Tombol Ujian → navigasi ke screen input Token (bagian dari Issue 6)
- [ ] Tombol Petunjuk → navigasi ke halaman Petunjuk (Composable text content)
- [ ] Halaman Petunjuk memiliki tombol kembali ke Menu
- [ ] Tombol Exit → finish() tanpa konfirmasi
- [ ] Tombol Logo terlihat (fungsionalitas klik diimplementasikan di Issue 8)
- [ ] Back button Android di Menu tidak bereaksi (tidak bisa mundur ke Login)

## Blocked by

- Issue 2: AppConfig + NavState (membutuhkan NavState untuk navigasi)
