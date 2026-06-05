# Token ujian divalidasi secara lokal, tanpa server backend

Aplikasi tidak memiliki backend server. Token Ujian dihasilkan dan divalidasi secara lokal di setiap perangkat menggunakan formula deterministik: `SHA-256(Salt + timestamp UTC dibulatkan ke time window)`, diambil 6 karakter pertama alphanumerik uppercase.

Ini berarti Pengawas dan Peserta cukup menjalankan aplikasi yang sama — tidak perlu koneksi internet ke server pusat untuk validasi token.

**Mengapa bukan server-based:**

- MVP ditargetkan untuk sekolah tanpa infrastruktur server
- Satu build aplikasi per sekolah (Salt berbeda) sudah cukup sebagai isolasi
- Time window membatasi masa berlaku token tanpa perlu menyimpan state
- Tidak ada single point of failure — ujian tetap bisa dimulai meski jaringan server pusat bermasalah

**Konsekuensi:**

- Token bisa diprediksi oleh siapa pun yang tahu Salt dan algoritma hash-nya — Salt harus dijaga sebagai rahasia build
- Tidak ada audit log token terpusat — tidak bisa melacak siapa yang generate token kapan
- Tidak ada mekanisme revoke token individual — token berlaku penuh selama time window-nya
