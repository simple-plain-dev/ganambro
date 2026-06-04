# Architecture — Ganambro

## Overview

Ganambro follows a **two-folder separation**: `ui/` and `feature/`. Each is a top-level package under `com.example.ganambro` with a single, non-overlapping responsibility.

```
com.example.ganambro
├── ui/          — Screen-level state and rendering (Android-aware)
└── feature/     — Pure domain logic, zero Android SDK dependency
```

## Layer responsibilities

### `ui/` — ViewModels and Compose components

| Contains | Does NOT contain |
|----------|------------------|
| Jetpack Compose `@Composable` functions | Domain logic (token, system checks, cheat detection, volume) |
| `androidx.lifecycle.ViewModel` subclasses | Business rules |
| Theme, color, and typography configuration | |
| Screen-level `@Preview` functions | |
| `MainActivity.kt` | |

**Rules:**

- ViewModels hold UI state and call into `feature/` modules for logic.
- Composable functions observe ViewModel state — no direct calls to `feature/`.
- Simple screens with only local `remember` state (e.g., MenuPengawasScreen: PIN + single button) may skip ViewModel. ViewModel is warranted when there's async orchestration, navigation-tied state, or multi-step flows.
- Theme code lives inside `ui/theme/` and has no dependency on `feature/`.
- `MainActivity` hosts the NavHost and detects Menu Pengawas gesture at the top level.

### `feature/` — Pure domain logic

| Contains | Does NOT contain |
|----------|------------------|
| Token generation and validation | UI state, Compose, or ViewModels |
| System checker (internet, GPS, bluetooth) | Android `Context` or framework imports |
| Volume manager (set max, restore) | |
| Cheat detector (classify exit trigger) | |

**Rules:**

- Every module in `feature/` is pure Kotlin. **Zero imports from `android.*`.**
- Unit-testable on JVM without Robolectric or Android test runner.
- Callers (`ui/`) pass in platform dependencies as plain Kotlin lambdas or interfaces.

## Plugin pattern

Tiga fitur menggunakan pola registrasi — sehingga menambah atau menghapus cek/trigger tidak butuh mengubah kode pemanggil:

### `precheck/` — PrecheckRegistry

```kotlin
sealed class PrecheckResult {
    data object Pass : PrecheckResult()
    data class Fail(val message: String) : PrecheckResult()
}

interface Precheck {
    val name: String              // "Internet", "GPS", "Bluetooth"
    suspend fun check(): PrecheckResult
}

class PrecheckRegistry {
    fun register(check: Precheck)
    suspend fun runAll(): List<PrecheckResult>
}
```

Setiap cek mengembalikan `Pass` atau `Fail(message)` — error text dimiliki oleh check, bukan oleh caller. SplashScreen cukup membaca `result.message`, tanpa switch-on-name. Nambah cek baru = buat file baru + register, nol perubahan di SplashScreen.

### `gesture/` — MenuPengawasGesture

```kotlin
enum class Target { LOGO, PETUNJUK }
enum class GestureState { IDLE, LOGO_1, LOGO_2, LOGO_3, PETUNJUK_TRIGGERED, UNLOCKED }

class MenuPengawasGesture {
    fun onTap(target: Target): GestureState
}
```

Pure state machine — tidak bergantung pada Composable lifecycle. Testable di JVM. MenuScreen hanya memanggil `gesture.onTap(target)` dan mengamati hasilnya.

### `cheatdetector/` — CheatDetector

```kotlin
enum class Severity { WARNING, FATAL }

interface CheatTrigger {
    fun onEvent(event: CheatEvent): Severity
}

class CheatDetector {
    fun register(trigger: CheatTrigger)
    fun notify(event: CheatEvent): Severity?
}
```

UnpinTrigger, BackGestureTrigger, PauseTrigger masing-masing implement `CheatTrigger`. ModeUjian ViewModel tinggal panggil `cheatDetector.notify(event)`. Nambah trigger curang = buat file baru + register.

## Dependency direction

```
ui/ ──depends on──▶ feature/ (token, precheck, volume, cheatdetector)
```

`feature/` has zero dependency on `ui/` or Android SDK. Arrows go one way only.

## File tree (target MVP)

```
com/example/ganambro/
├── MainActivity.kt
├── ui/
│   ├── theme/
│   │   └── Theme.kt
│   ├── screens/
│   │   ├── SplashScreen.kt
│   │   ├── LoginScreen.kt
│   │   ├── MenuScreen.kt
│   │   ├── PortalUjianScreen.kt
│   │   ├── ModeUjianScreen.kt
│   │   ├── PetunjukScreen.kt
│   │   └── MenuPengawasScreen.kt
│   └── components/
│       ├── WebViewToolbar.kt       # Header toolbar: Home, Back, Forward, Exit
│       └── ChromeTabLauncher.kt    # CustomTabsIntent wrapper
├── feature/
│   ├── token/
│   │   └── Token.kt                # Single module: generate() + validate(input) — 8-char hex
│   ├── precheck/
│   │   ├── Precheck.kt             # Interface: name + suspend check(): PrecheckResult
│   │   ├── PrecheckRegistry.kt     # register(Precheck) + runAll(): List<PrecheckResult>
│   │   ├── InternetCheck.kt
│   │   ├── GpsCheck.kt
│   │   └── BluetoothCheck.kt
│   ├── volume/
│   │   └── VolumeManager.kt        # Set STREAM_ALARM to max, poll every 2s
│   ├── gesture/
│   │   └── MenuPengawasGesture.kt  # Pure state machine: onTap(Target): GestureState
│   └── cheatdetector/
│       ├── CheatTrigger.kt         # Interface: onEvent(CheatEvent): Severity
│       ├── CheatDetector.kt        # register(CheatTrigger) + notify(event)
│       ├── UnpinTrigger.kt
│       ├── BackGestureTrigger.kt
│       └── PauseTrigger.kt
```

## Screen navigation flow

```
Splash ──▶ Login ──▶ Menu ──┬──▶ PortalUjian ──▶ ModeUjian
                             ├──▶ Petunjuk
                             └──▶ MenuPengawas (hidden gesture)
```

| Screen | Trigger | Behavior |
|--------|---------|----------|
| Splash | App launch | Sequential checks (internet, GPS perm, bluetooth). Fail → error + exit. Pass → Login. |
| Login | Auto from Splash | Chrome Custom Tab (accounts.google.com) or Skip button. Both go to Menu. |
| Menu | From Login | Buttons: Portal Ujian, Petunjuk, Exit. Hidden gesture for Menu Pengawas. |
| PortalUjian | From Menu | Token input → validate via `TokenValidator` → ModeUjian on success. |
| ModeUjian | From PortalUjian | `startLockTask()`, WebView with header toolbar, `FLAG_KEEP_SCREEN_ON`, CheatDetector active. |
| Petunjuk | From Menu | Scrollable instructions. |
| MenuPengawas | Gesture from Menu | Tap logo 3× → tap Petunjuk 1× → navigasi ke MenuPengawasScreen (PIN 6-digit, lalu Token generator). Kembali eksplisit ke Menu. |

### Exit flows

| Jalur | Trigger | Warning Sound | Outcome |
|-------|---------|---------------|---------|
| **Normal** | Exit button → dialog ketik "exit" | Warning 1 (singkat) | `stopLockTask()` → kembali ke Menu |
| **Curang** | Unpin paksa, gesture back, `onPause()` | Warning 2 (sirine, panjang) | Unpin paksa → `finishAndRemoveTask()`. Lainnya → Warning 2 saja |

## Token design

```
Token = first 8 chars of SHA-256(salt + roundedTimestamp)
salt  = SCHOOL_NAME + APP_NAME + APP_VERSION
roundedTimestamp = (epochSeconds / 600) * 600   // floor to 10-minute window
```

- Single module `Token` dengan dua operation: `generate(): String` dan `validate(input: String): Boolean`.
- Hasil: 8 karakter uppercase hex (0-9 A-F) — cukup untuk dibacakan lisan atau ditulis di papan tulis.
- Rounding logic, salt assembly, SHA-256, dan truncation adalah implementation detail — caller tidak pernah melihatnya.
- Token valid untuk unlimited uses dalam 10-minute window yang sama.
- Pengawas membuat token baru ketika window berganti.
- `validate()` menerima token window saat ini saja — tanpa grace period.

## BuildConfig fields

Semua variabel build-time diatur di `app/build.gradle.kts` sebagai `BuildConfig` fields:

| Field | Contoh | Digunakan oleh |
|-------|--------|---------------|
| `SCHOOL_NAME` | `"SMA Negeri 1 Jakarta"` | Salt token |
| `APP_NAME` | `"Ganambro"` | Salt token |
| `APP_VERSION` | `BuildConfig.VERSION_NAME` | Salt token |
| `TEACHER_PIN` | `"202606"` | Menu Pengawas password |
| `URL_PORTAL_UJIAN` | `"https://sites.google.com/..."` | ModeUjian WebView home URL |

## AndroidManifest permissions

| Permission | Kebutuhan | Tipe |
|------------|-----------|------|
| `INTERNET` | WebView, Chrome Custom Tab, cek koneksi | Normal |
| `ACCESS_NETWORK_STATE` | Cek status internet | Normal |
| `ACCESS_FINE_LOCATION` | Cek GPS | **Runtime** — dialog di Splash |
| `BLUETOOTH` / `BLUETOOTH_CONNECT` | Cek bluetooth (Android <12 / 12+) | Normal |

## Key decisions

See [docs/adr/](docs/adr/) for recorded architecture decisions.

- [ADR-0002](docs/adr/0002-two-folder-ui-feature.md) — Two-folder separation (`ui/`, `feature/`)
- ~~ADR-0001~~ — Superseded

## Navigation for AI

When exploring this codebase:

1. Start at `CONTEXT.md` for the domain glossary.
2. Read `docs/ARCHITECTURE.md` (this file) for layer boundaries and dependency rules.
3. Read `docs/adr/` for decisions that constrain the architecture.
4. Read `docs/FEATURE-MAP.md` to map features to UI/feature/resource files.
5. `feature/` is pure Kotlin — testable on JVM. `ui/` is Android surface.
