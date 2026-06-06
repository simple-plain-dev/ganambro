# Issue 1a: AppConfig — build vars, Salt, data class, tests

## What to build

Modul AppConfig membaca semua build variable dari BuildConfig satu kali di startup, merakitnya menjadi data class, dan meng-inject ke modul lain. Modul tidak pernah mengakses BuildConfig langsung.

Interface: data class `AppConfig(schoolName, appName, pin, examUrl, tokenWindowMinutes, appVersion)` dengan `salt` sebagai derived property dari komponen build.

Salt dihitung dari `SCHOOL_NAME + APP_NAME + APP_VERSION`.

## Acceptance criteria

- [ ] AppConfig data class dengan semua field + derived `salt` property
- [ ] AppConfigFactory membaca dari BuildConfig dan merakit AppConfig
- [ ] Unit test: Salt dihitung benar dari komponen build
- [ ] Unit test: default TOKEN_WINDOW_MINUTES saat tidak diset di BuildConfig
- [ ] Unit test: Salt berbeda antar kombinasi SCHOOL_NAME/APP_NAME/APP_VERSION

## Blocked by

None — can start immediately.
