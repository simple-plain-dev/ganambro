# Issue 8: Pengawas screen

## What to build

Halaman Pengawas yang diakses secara tersembunyi dari Menu. Menampilkan Token yang sedang aktif beserta countdown mundur ke Time Window berikutnya. Pengawas bisa memberikan Token ini ke Peserta.

**Akses tersembunyi:**
Dari Menu, Pengawas harus mengklik **Logo 3x** lalu **Petunjuk 1x** secara berurutan. Jika ada klik lain di antaranya, counter reset. Setelah trigger terpenuhi → muncul dialog input PIN 6-digit numerik.

**PIN & Token:**
PIN divalidasi terhadap AppConfig.pin. Jika PIN benar → halaman Pengawas muncul. Halaman menampilkan:
- Token yang sedang aktif (dari TokenGenerator.generate())
- Countdown mundur ke Time Window berikutnya (real-time, update tiap detik)
- Tombol "Generate" — hanya berfungsi setelah countdown habis (Time Window baru)

**End-to-end:** Pengawas di Menu → klik Logo 3x → klik Petunjuk 1x → dialog PIN muncul → masukkan PIN → halaman Pengawas muncul → Token ditampilkan + countdown → Pengawas beri Token ke Peserta → Peserta masukkan Token di Issue 6.

## Acceptance criteria

- [ ] Trigger: klik Logo 3x lalu Petunjuk 1x secara berurutan — counter reset jika ada klik lain
- [ ] Setelah trigger terpenuhi → dialog input PIN 6-digit muncul
- [ ] PIN divalidasi terhadap AppConfig.pin — pesan error jika salah, bisa retry
- [ ] Setelah PIN benar → halaman Pengawas muncul
- [ ] Token ditampilkan dengan jelas (font besar, monospace)
- [ ] Countdown real-time menampilkan sisa waktu ke Time Window berikutnya (MM:SS)
- [ ] Tombol "Generate" disabled selama countdown > 0
- [ ] Setelah countdown habis → tombol Generate enabled → klik → Token baru dari TokenGenerator.generate()
- [ ] Countdown restart setelah Generate
- [ ] Tombol kembali ke Menu tersedia
- [ ] Tidak ada indikasi visual di Menu bahwa trigger Pengawas sedang dihitung (tersembunyi)
- [ ] Back button Android di halaman Pengawas → kembali ke Menu

## Blocked by

- Issue 1: TokenGenerator (menghasilkan Token)
- Issue 2: AppConfig + NavState (PIN dari AppConfig, navigasi)
- Issue 5: Menu (trigger klik Logo + Petunjuk)
