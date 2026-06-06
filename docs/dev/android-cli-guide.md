# Android CLI Development Guide

Panduan command-line untuk development Ganambro tanpa Android Studio.

## Prasyarat

Android SDK sudah terpasang di `C:\Users\SAFRILTH\scoop\apps\android-clt\current`.  
Set environment variable sebelum menjalankan perintah:

```bash
export ANDROID_SDK_ROOT="C:/Users/SAFRILTH/scoop/apps/android-clt/current"
```

## Daftar Perintah

### AVD Manager (mengelola emulator)

```bash
# Lihat AVD yang tersedia
"$ANDROID_SDK_ROOT/cmdline-tools/latest/bin/avdmanager.bat" list avd

# Lihat target/system image yang terpasang
"$ANDROID_SDK_ROOT/cmdline-tools/latest/bin/avdmanager.bat" list target

# Buat AVD baru (contoh)
"$ANDROID_SDK_ROOT/cmdline-tools/latest/bin/avdmanager.bat" create avd \
  -n "Pixel_6_API_35" \
  -k "system-images;android-35;google_apis_playstore;x86_64" \
  -d "pixel_6"
```

### SDK Manager (mengelola SDK packages)

```bash
# Lihat package yang terpasang
"$ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager.bat" --list

# Install system image (contoh)
"$ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager.bat" \
  "system-images;android-35;google_apis_playstore;x86_64"
```

### Emulator (menjalankan emulator)

```bash
# Jalankan emulator dengan AVD tertentu
"$ANDROID_SDK_ROOT/emulator/emulator.exe" -avd Medium_Phone_30 &

# Dengan opsi performa
"$ANDROID_SDK_ROOT/emulator/emulator.exe" \
  -avd Medium_Phone_30 \
  -no-boot-anim \
  -netdelay none \
  -netspeed full &
```

### ADB (Android Debug Bridge)

```bash
# Tunggu emulator siap
adb wait-for-device

# Cek status boot
adb shell getprop sys.boot_completed    # output "1" = siap

# List device
adb devices

# Install APK
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Uninstall
adb uninstall com.example.ganambro

# Buka aplikasi
adb shell am start -n com.example.ganambro/.MainActivity

# Hentikan aplikasi
adb shell am force-stop com.example.ganambro

# Screenshot
adb exec-out screencap -p > screenshot.png

# Lihat log (real-time, filter Ganambro)
adb logcat -s Ganambro

# Lihat log (dump terakhir)
adb logcat -d -t 50
```

### Layout Inspection (tanpa Android Studio)

```bash
# Dump layout hierarchy sebagai XML
adb shell uiautomator dump

# Pull layout file dari device (gunakan MSYS_NO_PATHCONV di Git Bash)
MSYS_NO_PATHCONV=1 adb pull /sdcard/window_dump.xml layout.xml

# Prettify XML untuk dibaca manual
python -c "
import xml.dom.minidom as md
dom = md.parse('layout.xml')
print(dom.toprettyxml(indent='  '))
" > layout-pretty.xml

# Cari teks/node spesifik
grep -oP 'text="[^"]*"' layout.xml
grep -oP 'bounds="\[[^\]]+\]"' layout.xml

# Klik elemen di koordinat tertentu (dari bounds)
adb shell input tap 540 562

# Klik elemen berdasarkan teks (Android 12+)
adb shell input keyevent KEYCODE_TAB  # navigasi focus
```

### Debugging Crash

```bash
# Lihat stack trace crash terakhir
adb logcat -d -s AndroidRuntime:E | grep -A30 "FATAL"

# Lihat log spesifik app
adb logcat -d --pid=$(adb shell pidof com.example.ganambro)

# Clear log dan lihat real-time
adb logcat -c && adb logcat -s AndroidRuntime:E
```

## Workflow Build & Run Lengkap

```bash
# 1. Set SDK path
export ANDROID_SDK_ROOT="C:/Users/SAFRILTH/scoop/apps/android-clt/current"

# 2. Build
cd E:/dev/ujian/ganambro
./gradlew :app:assembleDebug

# 3. Jalankan emulator (jika belum)
"$ANDROID_SDK_ROOT/emulator/emulator.exe" -avd Medium_Phone_30 -no-boot-anim &

# 4. Tunggu boot
adb wait-for-device && adb shell getprop sys.boot_completed

# 5. Install & jalankan
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.example.ganambro/.MainActivity

# 6. Screenshot
adb exec-out screencap -p > screenshot.png
```
