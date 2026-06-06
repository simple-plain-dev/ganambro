# Issue 3: WarningSound — modul audio, play(type), internal volume invariant

## What to build

Modul yang mengenkapsulasi pemutaran suara peringatan. Interface: `play(type: WarningSoundType): Unit`. Volume maksimal adalah invariant internal modul — MediaPlayer volume diset ke max sebelum play, caller tidak perlu tahu.

Dua jenis:
- `WarningSoundType.WS1` — durasi panjang (kecurangan)
- `WarningSoundType.WS2` — durasi pendek (exit sah)

File audio disertakan sebagai asset aplikasi (raw resource).

Mengacu pada ADR-0002: volume enforcement terpisah antara WarningSound (invariant internal saat play) dan CheatDetector (background enforcement).

## Acceptance criteria

- [ ] Interface: `play(type: WarningSoundType): Unit`
- [ ] `play(WS1)` memilih file audio WS1 yang benar
- [ ] `play(WS2)` memilih file audio WS2 yang benar
- [ ] Volume MediaPlayer diset ke maksimal sebelum play (internal, tanpa caller set)
- [ ] Unit test dengan fake MediaPlayer — assert file selection dan volume
- [ ] Dua file audio (.mp3/.ogg) disertakan di res/raw

## Blocked by

None — can start immediately.
