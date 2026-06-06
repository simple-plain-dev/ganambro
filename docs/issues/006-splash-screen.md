# Issue 6: Splash Screen — terminal UI + PrecheckRunner wire-up + Android checkers

## What to build

Halaman Splash menampilkan Precheck dengan animasi terminal. PrecheckRunner yang sudah dibuat (#5) di-wire ke Android checker adapters yang sesungguhnya (ConnectivityManager, LocationManager, BluetoothAdapter, AudioManager).

SplashScreen mengobservasi `Flow<CheckResult>` dari PrecheckRunner — tidak tahu Android API apa pun. Setiap CheckResult ditampilkan sebagai baris teks animasi terminal.

- Jika ada yang gagal: tampilkan pesan spesifik + tombol keluar (restart aplikasi untuk retry)
- Jika semua sukses: tampilkan tombol "Lanjut" → navigasi ke Login

## Acceptance criteria

- [ ] Lima Android checker adapter: InternetChecker, GpsChecker, BluetoothChecker, HeadsetChecker, VolumeChecker
- [ ] Setiap checker implement interface internal yang sama dengan fake di PrecheckRunner
- [ ] Animasi terminal: baris teks muncul satu per satu dengan typing effect
- [ ] Gagal: tampilkan pesan spesifik (misal "Bluetooth menyala — matikan Bluetooth lalu buka ulang aplikasi")
- [ ] Gagal: tombol "Keluar" — finishAffinity()
- [ ] Sukses: tombol "Lanjut" — navigasi ke Login
- [ ] Izin runtime diminta saat Precheck berjalan (GPS, Bluetooth, DND/volume)

## Blocked by

- #5 (PrecheckRunner)
