# Issue 10: Ujian Screen + Toolbar — WebView Situs Ujian, navigasi tombol, error handling

## What to build

Halaman Ujian yang memuat Situs Ujian (Google Sites) di WebView fullscreen. Di atas WebView: Toolbar Ujian dengan 4 tombol:
- **Home** — kembali ke URL Situs Ujian awal (EXAM_URL)
- **Back** — `webView.goBack()`
- **Forward** — `webView.goForward()`
- **Exit** — panggil `exitCoordinator.exit(ExitContext.Ujian)`

Tidak ada URL bar — navigasi terbatas pada tombol-tombol ini.

Error handling (user story #31): jika internet putus saat Ujian, tampilkan pesan error + tombol keluar.

## Acceptance criteria

- [ ] WebView memuat EXAM_URL dari AppConfig
- [ ] Toolbar di atas WebView: Home, Back, Forward, Exit
- [ ] Tombol Home → loadUrl(EXAM_URL)
- [ ] Tombol Back → webView.goBack()
- [ ] Tombol Forward → webView.goForward()
- [ ] Tombol Exit → exitCoordinator.exit(Ujian) — dialog konfirmasi dari ExitCoordinator
- [ ] Tidak ada URL bar — hanya toolbar
- [ ] WebView error (internet putus) → tampilkan pesan "Koneksi internet terputus" + tombol "Keluar"
- [ ] UI test: toolbar button interactions

## Blocked by

- #4 (ExitCoordinator)
