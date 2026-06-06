# Android CLI Development Guide

Panduan command-line untuk development Ganambro tanpa Android Studio.

## Prasyarat

- `android` CLI: `C:\Users\SAFRILTH\AppData\AndroidCLI\android` (auto-detected)
- Android SDK: `C:\Users\SAFRILTH\AppData\Local\Android\Sdk`
- Gradle wrapper: `./gradlew` (di root project)

> **Prioritas**: Selalu gunakan `android` CLI sebagai opsi pertama. Fallback ke `adb`/`avdmanager`/`sdkmanager` hanya jika `android` CLI tidak mencakup use-case.

---

## 1. Informasi Environment

```bash
# Versi CLI dan path SDK
android info
# output: sdk: C:\Users\SAFRILTH\AppData\Local\Android\Sdk
#         version: 1.0.15433482
```

---

## 2. Emulator Management (`android emulator`)

```bash
# List AVD yang tersedia
android emulator list

# Jalankan emulator (blocking — return saat emulator siap)
android emulator start Medium_Phone_30

# Jalankan dengan cold boot (tanpa snapshot)
android emulator start Medium_Phone_30 --cold

# Buat AVD baru dari profile
android emulator create --list-profiles          # lihat profile tersedia
android emulator create medium_phone             # buat AVD phone

# Hentikan emulator
android emulator stop Medium_Phone_30

# Hapus AVD
android emulator remove Medium_Phone_30
```

---

## 3. Build & Deploy (`android run`, `android describe`)

```bash
# Build project (via gradlew)
cd E:/dev/ujian/ganambro
./gradlew :app:assembleDebug

# Deploy APK ke emulator/device
android run --apks app/build/outputs/apk/debug/app-debug.apk

# Deploy ke device tertentu
android run --apks app/build/outputs/apk/debug/app-debug.apk --device emulator-5554

# Info project (varian build, path APK)
android describe --project_dir .
# output: app:debug → app\build\outputs\apk\debug\app-debug.apk
```

---

## 4. Layout Inspection (`android layout`)

```bash
# Lihat layout tree — JSON, diformat rapi
android layout --pretty

# Output contoh:
# [
#   { "text": "Ganambro Precheck",       "center": "[280,70]"  },
#   { "text": "✅ Internet: OK",          "center": "[193,176]" },
#   { "text": "✅ Semua pengecekan berhasil!", "center": "[333,416]" },
#   { "interactions": ["clickable","focusable"],
#     "center": "[540,563]" },
#   { "text": "Lanjut →",                "center": "[540,562]" }
# ]

# Cek perubahan sejak dump terakhir (untuk animasi/transisi)
android layout --diff

# Simpan ke file
android layout -o layout.json --pretty
```

---

## 5. Screenshot & UI Element Targeting (`android screen`)

```bash
# Screenshot biasa
android screen capture -o screenshot.png

# Screenshot dengan bounding box bernomor (untuk screen resolve)
android screen capture --annotate -o annotated.png

# Resolve: substitusi koordinat dari annotated screenshot ke string
#   Setiap '#N' diganti dengan koordinat center bounding box label N
#   Contoh: adb shell input tap '#1' → adb shell input tap '540 562'
android screen resolve --screenshot annotated.png --string "adb shell input tap '#1'"
```

---

## 6. SDK Management (`android sdk`)

```bash
# List package terpasang dan tersedia
android sdk list

# Install package
android sdk install "system-images;android-35;google_apis_playstore;x86_64"

# Update semua package
android sdk update

# Remove package
android sdk remove "system-images;android-30;google_apis_playstore;x86"
```

---

## 7. Project Creation (`android create`)

```bash
# Buat project Android baru
android create --help
```

---

## 8. ADB (fallback untuk operasi low-level)

```bash
# Cek device terhubung
adb devices

# Install APK (jika android run tidak digunakan)
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Launch activity
adb shell am start -n com.example.ganambro/.MainActivity

# Force stop
adb shell am force-stop com.example.ganambro

# Tap koordinat (dari android layout output)
adb shell input tap 540 562

# Screenshot (fallback)
adb exec-out screencap -p > screenshot.png

# Log real-time
adb logcat -s AndroidRuntime:E
adb logcat -d --pid=$(adb shell pidof com.example.ganambro)
```

---

## 9. AVD & SDK Manager (fallback CLI tools)

```bash
# Path ke cmdline-tools
AVDMGR="$ANDROID_SDK_ROOT/cmdline-tools/latest/bin/avdmanager.bat"
SDKMGR="$ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager.bat"

# List AVD
"$AVDMGR" list avd

# List target/system images
"$AVDMGR" list target

# Install system image
"$SDKMGR" --install "system-images;android-35;google_apis_playstore;x86_64"

# Buat AVD
"$AVDMGR" create avd -n "Test_Device" -k "system-images;android-35;google_apis_playstore;x86_64" -d "pixel_6"
```

---

## Workflow Lengkap (dengan `android` CLI)

```bash
# 1. Info environment
android info

# 2. Start emulator (blocking sampai siap)
android emulator start Medium_Phone_30 &

# 3. Build
cd E:/dev/ujian/ganambro
./gradlew :app:assembleDebug

# 4. Deploy & run
android run --apks app/build/outputs/apk/debug/app-debug.apk

# 5. Cek layout
android layout --pretty

# 6. Interaksi — dapatkan koordinat dari output layout, lalu:
adb shell input tap <x> <y>

# 7. Screenshot dengan annotasi
android screen capture --annotate -o debug.png

# 8. Debug crash
adb logcat -d -s AndroidRuntime:E | grep "FATAL"
```
