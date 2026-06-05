# Issue 3: Splash + Precheck pipeline + tests

## What to build

Halaman Splash dengan Precheck pipeline. Lima pengecekan kondisi perangkat berjalan sekuensial dengan animasi terminal. Jika semua lolos → tombol lanjut ke Login. Jika ada yang gagal → pesan spesifik dan tombol keluar (Peserta harus restart aplikasi untuk retry).

**PrecheckRunner** — modul deep dengan interface tunggal `suspend fun runChecks(): Flow<CheckResult>`. Lima Checker adapter internal: Internet (ConnectivityManager), GPS (LocationManager), Bluetooth (BluetoothAdapter), Headset (AudioManager), Volume (AudioManager). Setiap Checker menyembunyikan Android API spesifik dari SplashScreen.

**SplashScreen** — Composable yang mengobservasi Flow dari PrecheckRunner. Menampilkan setiap CheckResult (Pending → Passed/Failed) dengan animasi terminal baris per baris. Urutan cek: Internet → GPS → Bluetooth → Headset → Volume.

**Izin runtime** — INTERNET, ACCESS_FINE_LOCATION, BLUETOOTH, ACCESS_NOTIFICATION_POLICY diminta saat Precheck berjalan (bukan saat startup).

**End-to-end:** aplikasi dibuka → SplashScreen muncul → PrecheckRunner mulai → satu per satu cek muncul di layar seperti terminal → semua hijau → tombol "Lanjut" muncul → klik → navigasi ke Login. Jika salah satu merah → pesan spesifik + tombol "Keluar" → klik → finish().

## Acceptance criteria

- [ ] PrecheckRunner menjalankan 5 cek sekuensial sesuai urutan
- [ ] Flow mengemisi CheckResult.Pending → CheckResult.Passed (atau CheckResult.Failed(reason)) untuk setiap cek
- [ ] Jika satu cek gagal, Flow tetap lanjut ke cek berikutnya (tidak berhenti di tengah)
- [ ] SplashScreen menampilkan animasi terminal dengan indikator loading/passed/failed per cek
- [ ] Pesan gagal spesifik per cek (contoh: "Bluetooth masih menyala — harap matikan Bluetooth")
- [ ] Tombol "Lanjut" hanya muncul jika semua cek Passed
- [ ] Tombol "Keluar" muncul jika ada yang gagal; klik → finish()
- [ ] Izin runtime diminta inline saat cek terkait berjalan
- [ ] Test PrecheckRunner dengan fake Checker: assert urutan eksekusi, Flow emission, propagasi kegagalan
- [ ] Tidak bisa navigasi mundur dari Splash (back button tidak bereaksi)

## Blocked by

- Issue 2: AppConfig + NavState (membutuhkan NavState untuk navigasi ke Login)
