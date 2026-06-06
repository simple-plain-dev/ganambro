# Issue 7: Login Screen — WebView Google Sign-In, skip, only-forward

## What to build

Halaman Login menggunakan WebView yang membuka Google Sign-In. Peserta bisa skip untuk masuk sebagai anonim (`LoginResult.Anonim`). Halaman Login tidak memiliki tombol mundur — hanya bisa maju ke Menu.

Navigasi: Login Screen → setelah auth berhasil atau skip → emit LoginResult → NavState bertransisi ke Menu.

Catatan MVP: Google mungkin memblokir OAuth di WebView — jika terjadi, Login akan difungsikan hanya sebagai skip-able screen. Risiko diterima.

## Acceptance criteria

- [ ] WebView memuat halaman Google Sign-In
- [ ] Tombol "Skip" — melewati login, emit LoginResult.Anonim
- [ ] Setelah login berhasil (atau skip), navigasi ke Menu
- [ ] Tidak ada tombol back/mundur dari Login ke Splash
- [ ] Jika WebView gagal muat OAuth, tampilkan pesan dan tetap ada tombol Skip
- [ ] LoginResult sealed class: Sukses(akun) | Anonim

## Blocked by

None — can start immediately.
