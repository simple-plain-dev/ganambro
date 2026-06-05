# Issue 4: Login screen

> ⚠️ **HITL** — Google memblokir OAuth di WebView/embedded browser sejak 2021. Perlu diputuskan sebelum implementasi: (a) tetap pakai WebView dan terima risiko diblokir, (b) ganti ke Chrome Custom Tab, atau (c) skip login sepenuhnya untuk MVP dan hanya tampilkan tombol "Lanjutkan sebagai Anonim".

## What to build

Halaman Login menggunakan WebView untuk Google Sign-In. Peserta bisa login dengan akun Google atau skip untuk masuk sebagai **Anonim** (tidak tertaut akun Google manapun). Halaman Login hanya bisa maju — tidak bisa mundur ke Splash dan tidak bisa keluar aplikasi.

**End-to-end:** setelah Splash sukses → navigasi ke LoginScreen → WebView menampilkan halaman Google Sign-In → Peserta login dengan akun Google (atau klik "Skip") → navigasi ke Menu. Jika skip, Peserta masuk sebagai Anonim. Tidak ada tombol back, tidak ada tombol exit.

## Acceptance criteria

- [ ] WebView membuka halaman Google Sign-In saat LoginScreen muncul
- [ ] Tombol "Skip" / "Lanjutkan sebagai Anonim" tersedia dan terlihat jelas
- [ ] Setelah login sukses ATAU klik skip → navigasi ke Menu melalui NavState
- [ ] Tidak ada tombol mundur ke Splash
- [ ] Tidak ada tombol keluar dari aplikasi di halaman ini
- [ ] Back button Android dihiraukan di halaman ini
- [ ] Jika Google memblokir WebView → fallback: tampilkan pesan error dan tombol skip saja

### Pertanyaan HITL yang harus dijawab:

Bagaimana menangani pemblokiran Google OAuth di WebView? Tiga opsi:
1. Tetap pakai WebView — terima risiko. Jika diblokir, Login menjadi skip-only screen.
2. Ganti ke Chrome Custom Tab — sesi Google terbawa, tapi Custom Tab tidak bagian dari app kita.
3. Hapus Login dari MVP — langsung Splash → Menu, semua Peserta Anonim.

## Blocked by

- Issue 2: AppConfig + NavState (membutuhkan NavState untuk navigasi ke Menu)
