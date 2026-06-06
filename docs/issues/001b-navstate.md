# Issue 1b: NavState — sealed class state machine, transition rules, tests

## What to build

Navigasi menggunakan sealed class `NavState` (Splash, Login, Menu, Ujian, Pengawas) dengan aturan transisi eksplisit. Transisi ilegal (Login→Splash, Menu→Ujian tanpa Token) menjadi impossible by construction.

Aturan transisi:
- Splash → Login atau Exit (tidak bisa ke Menu/Ujian/Pengawas)
- Login → Menu saja (tidak mundur ke Splash)
- Menu → Ujian (harus dengan Token valid) atau Pengawas atau Exit
- Ujian → kembali ke Menu (saat exit atau unpin)
- Pengawas → kembali ke Menu

## Acceptance criteria

- [ ] Sealed class NavState dengan 5 varian
- [ ] Fungsi transisi: `NavState.transitionTo(target): NavState` — mengembalikan state yang sama jika ilegal
- [ ] Unit test: setiap transisi legal bisa dieksekusi
- [ ] Unit test: setiap transisi ilegal mengembalikan state yang sama (Login→Splash, Splash→Menu, Menu→Ujian tanpa token, dll)

## Blocked by

None — can start immediately.
