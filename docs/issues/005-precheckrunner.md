# Issue 5: PrecheckRunner — Flow<CheckResult>, 5 checker adapters, tests

## What to build

Lima pengecekan (Internet, GPS, Bluetooth, Headset, Volume) dienkapsulasi dalam PrecheckRunner dengan interface: `suspend fun runChecks(): Flow<CheckResult>`. Setiap cek adalah adapter internal yang menyembunyikan Android API spesifik — tapi untuk modul ini, semua checker adalah fake adapter.

Pengecekan berjalan sekuensial sesuai urutan: Internet → GPS → Bluetooth → Headset → Volume. Jika satu gagal, Flow selesai dengan `CheckResult.Failed(reason)`.

## Acceptance criteria

- [ ] Interface: `suspend fun runChecks(): Flow<CheckResult>`
- [ ] Lima fake checker adapter untuk unit test
- [ ] Unit test: urutan eksekusi sesuai (Internet, GPS, Bluetooth, Headset, Volume)
- [ ] Unit test: Flow emission berurutan — setiap cek emit satu CheckResult
- [ ] Unit test: satu gagal → Flow selesai dengan Failed, cek berikutnya tidak dijalankan
- [ ] Unit test: semua sukses → Flow selesai dengan Passed

## Blocked by

None — can start immediately.
