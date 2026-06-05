# Issue 6: Ujian — Token entry + WebView + Toolbar + Kiosk Mode

## What to build

Halaman Ujian lengkap: input Token → validasi → WebView memuat Situs Ujian → Toolbar Ujian (Home, Back, Forward, Exit) → Kiosk Mode (imersi, potret). Ini adalah slice terbesar yang menyatukan seluruh pengalaman Ujian.

**Alur end-to-end:**
1. Peserta klik Ujian di Menu → screen input Token muncul (6 text field untuk 6 digit alphanumerik caps)
2. Peserta memasukkan Token dari Pengawas → validasi via TokenGenerator
3. Jika Token salah → pesan error + bisa retry
4. Jika Token valid → WebView muncul, memuat Situs Ujian (EXAM_URL dari AppConfig)
5. Toolbar di atas WebView: Home (kembali ke URL awal), Back (mundur halaman), Forward (maju halaman), Exit (keluar Ujian)
6. Kiosk Mode aktif: system bars disembunyikan (immersive mode), orientasi dikunci potret
7. Exit dari Ujian: klik Exit → dialog ketik "exit" → jika cocok → Warning Sound 2 → finish()
8. Internet putus saat Ujian: tampilkan pesan error + tombol keluar

**Toolbar** — tidak ada URL bar. Navigasi terbatas pada 4 tombol. Home = `webView.loadUrl(examUrl)`, Back = `webView.goBack()`, Forward = `webView.goForward()`.

**Kiosk Mode** — `windowInsetsController.hide(statusBars + navigationBars)` + `requestedOrientation = SCREEN_ORIENTATION_PORTRAIT`.

## Acceptance criteria

- [ ] Screen input Token: 6 text field alphanumerik caps, auto-advance ke field berikutnya
- [ ] Token divalidasi via TokenGenerator.validate() — pesan error jika salah, bisa retry unlimited
- [ ] Setelah Token valid → WebView muncul, memuat Situs Ujian dengan loading indicator
- [ ] Toolbar 4 tombol (Home, Back, Forward, Exit) di atas WebView, tidak ada URL bar
- [ ] Home → loadUrl ke EXAM_URL awal
- [ ] Back/Forward → goBack/goForward, tombol disabled jika tidak ada history
- [ ] Exit → dialog konfirmasi dengan text field ketik "exit" (case-insensitive), tombol OK/Cancel
- [ ] Konfirmasi "exit" benar → Warning Sound 2 diputar → finish()
- [ ] Konfirmasi "exit" salah → pesan error di dialog
- [ ] Kiosk Mode: system bars tersembunyi, orientasi terkunci potret
- [ ] Internet putus (onReceivedError WebView) → tampilkan pesan error + tombol keluar
- [ ] Back button Android tidak bereaksi (tidak bisa mundur ke Menu)

## Blocked by

- Issue 1: TokenGenerator (validasi Token)
- Issue 2: AppConfig + NavState (EXAM_URL, navigasi)
- Issue 5: Menu + Exit (harus bisa diakses dari Menu)
