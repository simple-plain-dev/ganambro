package com.example.ganambro.config

data class AppConfig(
    val schoolName: String,
    val appName: String,
    val pin: String,
    val examUrl: String,
    val tokenWindowMinutes: Int,
    val appVersion: String,
) {
    /** Salt dihitung dari komponen build: SCHOOL_NAME + APP_NAME + APP_VERSION */
    val salt: String get() = "$schoolName$appName$appVersion"
}
