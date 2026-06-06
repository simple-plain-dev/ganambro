# Volume enforcement: WarningSound vs CheatDetector — seam clarified

Volume enforcement di perangkat memiliki dua concern berbeda yang sebelumnya kabur: (1) suara peringatan harus selalu keras saat diputar, dan (2) volume perangkat harus tetap maksimal sepanjang Ujian. Diputuskan: WarningSound memiliki invariant internal bahwa `play()` selalu memutar di volume maksimal — caller tidak perlu tahu. CheatDetector memiliki background volume enforcement terpisah (periodik setiap 10 detik) untuk memastikan perangkat tetap di volume maksimal meskipun tidak ada suara yang diputar. Ini dua tanggung jawab berbeda di dua modul berbeda — tidak ada leak di seam.

**Considered Options:**
- Volume sepenuhnya milik CheatDetector — WarningSound terima parameter volume. Ditolak karena ExitCoordinator (yang juga memutar Warning Sound 2) akan perlu bergantung pada CheatDetector hanya untuk set volume, menciptakan dependency yang tidak perlu.
- Volume sepenuhnya milik WarningSound, termasuk background enforcement. Ditolak karena background enforcement adalah tanggung jawab Ujian-spesifik (CheatDetector), bukan tanggung jawab pemutar suara generik.
