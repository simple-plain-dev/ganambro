# Issue 9: Token Input → Ujian Entry — 6-digit alphanumeric, validasi TokenGenerator

## What to build

Halaman input Token setelah Peserta klik "Ujian" di Menu. Menampilkan 6 input field untuk karakter alphanumerik uppercase, tombol "Masuk", dan pesan error jika Token salah.

Setelah Token valid (divalidasi oleh TokenGenerator), navigasi ke Ujian Screen dengan URL Situs Ujian dari AppConfig.

## Acceptance criteria

- [ ] 6 input field untuk karakter alphanumerik (auto-uppercase, filter non-alphanumerik)
- [ ] Tombol "Masuk" — validasi Token via TokenGenerator.validate()
- [ ] Token valid → navigasi ke Ujian Screen dengan EXAM_URL dari AppConfig
- [ ] Token salah → tampilkan pesan error "Token tidak valid", input tetap bisa diedit
- [ ] Bisa mencoba lagi tanpa batas (tidak ada lockout)
- [ ] UI test: input 6 karakter, klik Masuk, validasi dipanggil

## Blocked by

- #2 (TokenGenerator)
- #8 (Menu Screen — sebagai origin navigasi)
