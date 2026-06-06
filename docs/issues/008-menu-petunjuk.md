# Issue 8: Menu Screen + Petunjuk — 4 buttons, Exit→ExitCoordinator, PetunjukContent

## What to build

Halaman Menu setelah Login. Berisi 4 tombol:
- **Ujian** — navigasi ke Token Input
- **Petunjuk** — navigasi ke halaman Petunjuk
- **Exit** — panggil `exitCoordinator.exit(ExitContext.Menu)`
- **Logo** — trigger untuk akses Pengawas (HiddenAccessTrigger, diimplementasikan di slice #12)

Halaman Petunjuk: konten dipisahkan dari rendering. `PetunjukContent` data class (judul, langkah-langkah, footer) dibaca dari resource string. PetunjukScreen Composable menerima PetunjukContent sebagai parameter.

Navigasi mundur diblokir dari Menu (tidak bisa kembali ke Login).

## Acceptance criteria

- [ ] Menu screen dengan 4 tombol: Ujian, Petunjuk, Exit, Logo
- [ ] Tombol Exit → panggil exitCoordinator.exit(Menu) — langsung keluar
- [ ] Tombol Ujian → navigasi ke Token Input screen
- [ ] Tombol Petunjuk → navigasi ke Petunjuk screen
- [ ] Tombol Logo → trigger HiddenAccessTrigger (counter logic di #12)
- [ ] Back button/system back tidak mundur dari Menu ke Login
- [ ] PetunjukContent data class: title, steps (list), footer
- [ ] PetunjukContent dibaca dari strings.xml resource
- [ ] PetunjukScreen Composable menerima PetunjukContent sebagai parameter
- [ ] Unit test: PetunjukContent bisa di-render dengan konten fake

## Blocked by

- #4 (ExitCoordinator)
