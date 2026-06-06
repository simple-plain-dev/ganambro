# Issue 12: Pengawas Screen — HiddenAccessTrigger, PIN, token display + countdown

## What to build

Halaman Pengawas yang diakses melalui hidden trigger dari Menu. Two-step akses:

1. **HiddenAccessTrigger** — modul pure Kotlin, interface: `registerClick(target: TriggerTarget): TriggerState`. Melacak sekuens klik Logo 3x lalu Petunjuk 1x. Reset jika klik lain di antaranya. State: Idle, Counting(logoCount), Triggered.

2. **PIN screen** — setelah Triggered, tampilkan input 6-digit numerik. PIN divalidasi terhadap AppConfig.pin.

3. **Token display** — setelah PIN benar, tampilkan Token aktif (dari TokenGenerator) + countdown mundur ke time window berikutnya. Pengawas harus klik "Generate" lagi setelah time window habis untuk mendapatkan Token baru.

## Acceptance criteria

- [ ] HiddenAccessTrigger: pure Kotlin, interface `registerClick(target): TriggerState`
- [ ] Unit test: sekuens Logo×3 + Petunjuk×1 → Triggered
- [ ] Unit test: klik salah di tengah (misal Logo×2 lalu Exit) → reset ke Idle
- [ ] Unit test: klik Petunjuk duluan → tetap Idle
- [ ] Unit test: Triggered lalu klik lagi → tetap Triggered
- [ ] PIN input: 6-digit numerik, cocokkan dengan AppConfig.pin
- [ ] PIN benar → tampilkan Token display
- [ ] PIN salah → pesan error, 3x percobaan lalu kembali ke Menu
- [ ] Token display: Token aktif dari TokenGenerator + countdown ke window berikutnya
- [ ] Setelah countdown habis → Token hilang, tombol "Generate" muncul
- [ ] Klik Generate → Token baru + countdown restart
- [ ] Back button → kembali ke Menu

## Blocked by

- #2 (TokenGenerator)
- #8 (Menu Screen — HiddenAccessTrigger di-wire ke klik button)
