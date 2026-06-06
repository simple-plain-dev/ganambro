# Issue 2: TokenGenerator — pure Kotlin, SHA-256, 6-char extract

## What to build

Token generation dan validation sebagai modul murni tanpa dependensi Android. Formula: `SHA-256(Salt + timestamp UTC dibulatkan ke time window)` → ambil 6 karakter pertama alphanumerik uppercase.

Interface:
- `generate(salt: String, timeWindowMinutes: Int): String`
- `validate(token: String, salt: String, timeWindowMinutes: Int): Boolean`

Mengacu pada ADR-0001: validasi lokal tanpa server. Token berlaku penuh selama Time Window.

## Acceptance criteria

- [ ] Token konsisten dalam satu time window yang sama
- [ ] Token berbeda antar time window yang berbeda
- [ ] Token di perbatasan time window: timestamp tepat di batas menghasilkan token yang sama dengan window sebelumnya
- [ ] SHA-256 6 karakter extraction: hanya alphanumerik uppercase (A-Z, 0-9)
- [ ] Token selalu uppercase
- [ ] `validate()`: token valid → true, token invalid → false
- [ ] Timestamp UTC boundary: generate di akhir window UTC vs awal window berikutnya menghasilkan token berbeda
- [ ] 7+ unit test passing di JUnit5

## Blocked by

None — can start immediately.
