# CONTEXT — Ganambro

## Project purpose

Ganambro adalah shell/wrapper ujian berbasis Google Form untuk lingkungan sekolah. Menyediakan mode ujian terkunci (kiosk) bagi Peserta dan pembuatan Token bagi Pengawas.

## Domain glossary

| Term | Definition |
|------|------------|
| **Peserta** | Siswa yang mengikuti ujian melalui aplikasi. _Avoid_: siswa, pengguna, user. |
| **Pengawas** | Guru atau admin yang membuat Token ujian melalui Menu Pengawas. _Avoid_: guru, admin. |
| **Ujian** | Sesi pengerjaan Google Form dalam Mode Ujian. _Avoid_: google form, tes, quiz. |
| **Token** | Kode 8-digit hex (8 karakter pertama SHA-256) yang dihasilkan Pengawas, digunakan Peserta untuk memasuki Ujian. Disampaikan lisan atau ditulis di papan tulis — tanpa tombol Copy. |
| **Mode Ujian** | Tampilan layar penuh terkunci (kiosk) yang mencegah Peserta keluar dari Ujian. _Avoid_: kiosk, fullscreen. |
| **Portal Ujian** | Halaman tempat Peserta memasukkan Token untuk memulai Ujian. |
| **Menu Pengawas** | Menu tersembunyi yang hanya dapat diakses oleh Pengawas untuk membuat Token. Diakses melalui gesture → PIN 6-digit → layar Generate Token. _Avoid_: hidden menu. |
| **Salt** | String gabungan `SCHOOL_NAME + APP_NAME + APP_VERSION` yang digunakan sebagai input fungsi hash Token. |
| **Warning Sound** | Audio keras yang diputar saat Mode Ujian dilanggar. Dua jenis: Warning 1 (singkat, exit normal) dan Warning 2 (panjang, curang). |
| **Unpin Paksa** | Peserta keluar dari screen pinning tanpa melalui tombol Exit. Diklasifikasikan sebagai curang. |
| **Gesture** | Urutan ketukan (tap logo 3×, Petunjuk 1×) untuk membuka Menu Pengawas. Diimplementasikan sebagai pure state machine. State UNLOCKED → navigasi ke MenuPengawasScreen (PIN + Token generator). |

## Architecture layer map

| Layer | Package root | Responsibility |
|-------|-------------|----------------|
| UI    | `com.example.ganambro.ui` | ViewModels, Compose components, theme, screens, shared widgets |
| Feature | `com.example.ganambro.feature` | Pure Kotlin domain logic, granular per fitur: `token/`, `precheck/`, `volume/`, `gesture/`, `cheatdetector/` |
