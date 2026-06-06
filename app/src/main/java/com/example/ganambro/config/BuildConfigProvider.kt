package com.example.ganambro.config

/**
 * Production [ConfigProvider] that reads from [BuildConfig].
 * One build = one school — values are baked at compile time.
 */
object BuildConfigProvider : ConfigProvider {
    override val schoolName: String = com.example.ganambro.BuildConfig.SCHOOL_NAME
    override val appName: String = com.example.ganambro.BuildConfig.APP_NAME
    override val pin: String = com.example.ganambro.BuildConfig.PIN
    override val examUrl: String = com.example.ganambro.BuildConfig.EXAM_URL
    override val appVersion: String = com.example.ganambro.BuildConfig.VERSION_NAME
    override val tokenWindowMinutes: Int = com.example.ganambro.BuildConfig.TOKEN_WINDOW_MINUTES
}
