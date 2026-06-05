# Issue 7: CheatDetector + Warning Sound

## What to build

Modul **CheatDetector** yang mengoordinasikan tiga mekanisme Deteksi Kecurangan selama Ujian. Interface: `start()` / `stop()` dan `Flow<CheatEvent>`. Semua downstream actions (memutar Warning Sound, menampilkan popup, navigasi ke Menu, exit aplikasi) ditangani satu kali dari Flow.

**Tiga mekanisme deteksi:**
1. **Volume watcher** — foreground service yang tiap 10 detik memeriksa volume. Jika turun → force ke maksimal (tanpa popup, tanpa sound)
2. **Headset detection** — BroadcastReceiver ACTION_HEADSET_PLUG + AudioManager.isWiredHeadsetOn(). Jika headset terpasang → emit CheatEvent.Headset
3. **Unpin/Split detection** — lifecycle callback Activity (onPause + onStop + isInMultiWindowMode). Jika Peserta meninggalkan app atau split screen → emit CheatEvent.Unpin

**Response terhadap CheatEvent:**
- Volume turun → auto-koreksi ke maksimal (tanpa popup, tanpa sound)
- CheatEvent.Headset → Warning Sound 1 diputar → aplikasi finish()
- CheatEvent.Unpin → popup peringatan + Warning Sound 1 diputar → navigasi ke Menu

**Warning Sound assets** — dua file audio (.mp3/.wav) dalam res/raw. Untuk development: gunakan placeholder dari Android system tone (RingtoneManager). Aset asli disediakan user nanti.

## Acceptance criteria

- [ ] CheatDetector.start() memulai semua mekanisme deteksi
- [ ] CheatDetector.stop() menghentikan semua mekanisme dan membersihkan resource
- [ ] Volume watcher: cek tiap 10 detik via foreground service, force max jika turun
- [ ] Headset detection: ketika headset terpasang → emit CheatEvent.Headset
- [ ] Unpin detection: ketika onPause/onStop + not in multiwindow → emit CheatEvent.Unpin
- [ ] Split screen detection: ketika isInMultiWindowMode true → emit CheatEvent.Unpin
- [ ] CheatEvent.Headset → Warning Sound 1 diputar → finish()
- [ ] CheatEvent.Unpin → popup peringatan "Kecurangan terdeteksi" → Warning Sound 1 → navigasi ke Menu
- [ ] Volume turun → force ke max (no popup, no sound, no event emission to UI)
- [ ] Warning Sound playback tidak terpengaruh oleh volume device (pakai STREAM_ALARM atau langsung max volume)
- [ ] Test CheatDetector dengan fake DetectorService: assert event emission yang benar
- [ ] Foreground service menampilkan notification saat berjalan (Android requirement)

## Blocked by

- Issue 6: Ujian (CheatDetector.start() dipanggil saat masuk Ujian, stop() saat keluar)
