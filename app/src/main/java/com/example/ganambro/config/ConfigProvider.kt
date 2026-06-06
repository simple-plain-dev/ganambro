package com.example.ganambro.config

/**
 * Abstraction over build configuration sources.
 * Modules depend on this interface instead of accessing BuildConfig directly.
 */
interface ConfigProvider {
    val schoolName: String
    val appName: String
    val pin: String
    val examUrl: String
    val appVersion: String
    val tokenWindowMinutes: Int get() = 10
}
