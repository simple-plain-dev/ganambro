# Issue 1: TokenGenerator module + tests

## What to build

Implementasi modul **TokenGenerator** — modul deep murni tanpa dependensi Android. Menghasilkan Token 6-digit alphanumerik caps dari Salt dan timestamp UTC yang dibulatkan ke kelipatan Time Window. Modul ini dipanggil dari dua tempat: halaman Pengawas (generate Token) dan halaman Ujian (validasi Token).

**End-to-end:** TokenGenerator menerima Salt dan durasi Time Window → menghitung timestamp UTC saat ini → membulatkan ke bawah ke kelipatan Time Window → menghasilkan Token deterministik. Validasi Token dilakukan dengan membandingkan input Peserta terhadap hasil generate dengan parameter yang sama.

Token yang sama harus dihasilkan dalam satu Time Window, dan berbeda di Time Window berikutnya.

Mengacu pada ADR-0001: validasi dilakukan lokal tanpa server.

## Acceptance criteria

- [ ] `generate(salt: String, timeWindowMinutes: Int): String` menghasilkan Token 6 karakter alphanumerik uppercase
- [ ] `validate(token: String, salt: String, timeWindowMinutes: Int): Boolean` mengembalikan true hanya untuk Token yang valid
- [ ] Token yang di-generate dalam Time Window yang sama (timestamp dibulatkan sama) bersifat identik
- [ ] Token berbeda saat Time Window berganti (timestamp melintasi batas pembulatan)
- [ ] Timestamp menggunakan UTC, bukan waktu lokal
- [ ] Test JUnit5 mencakup: konsistensi dalam satu window, perbedaan antar window, batas window (boundary), token invalid, token dengan casing berbeda
- [ ] Modul zero dependensi Android — bisa dijalankan di JUnit5 tanpa Robolectric/Android runner

## Blocked by

None — can start immediately.
