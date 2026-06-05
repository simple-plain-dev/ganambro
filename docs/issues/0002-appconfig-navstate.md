# Issue 2: AppConfig + NavState + tests

## What to build

Dua modul fondasi:

**AppConfig** — membaca semua build variable (SCHOOL_NAME, APP_NAME, PIN, EXAM_URL, TOKEN_WINDOW_MINUTES) satu kali di startup dari BuildConfig, merakitnya menjadi data class typed, dan menyediakan Salt yang sudah dihitung dari SCHOOL_NAME + APP_NAME + versionName. Modul-modul lain menerima AppConfig sebagai dependency — tidak pernah mengakses BuildConfig langsung.

**NavState** — sealed class yang merepresentasikan state navigasi aplikasi: Splash, Login, Menu, Ujian, Pengawas. Transisi antar state diatur oleh aturan eksplisit: Splash hanya bisa ke Login atau Exit; Login hanya bisa ke Menu (tidak mundur); Menu ke Ujian harus dengan Token valid; Ujian kembali ke Menu saat exit atau unpin. Transisi ilegal impossible by construction.

**End-to-end:** saat aplikasi start → AppConfig dibangun dari BuildConfig → di-inject ke NavState dan modul lainnya. Navigasi antar layar menggunakan sealed class enum, bukan string routes. Setiap transisi dicek validitasnya di satu tempat.

## Acceptance criteria

- [ ] AppConfig data class berisi semua field yang dibutuhkan (salt, pin, examUrl, tokenWindowMinutes) bertipe typed, bukan string mentah
- [ ] Salt dihitung dari SCHOOL_NAME + APP_NAME + versionName (versionName dari PackageInfo/build.gradle)
- [ ] TOKEN_WINDOW_MINUTES default 10 jika tidak diset di BuildConfig
- [ ] NavState sealed class dengan varian: Splash, Login, Menu, Ujian, Pengawas
- [ ] Fungsi transisi `next(from: NavState, event: NavEvent): NavState` menangani semua transisi legal
- [ ] Transisi ilegal (Login→Splash, Menu→Ujian tanpa token valid) return state yang sama atau throw
- [ ] Test JUnit5 untuk AppConfig: Salt dihitung benar, default value berlaku
- [ ] Test JUnit5 untuk NavState: setiap transisi legal berhasil, transisi ilegal gagal

## Blocked by

None — can start immediately.
