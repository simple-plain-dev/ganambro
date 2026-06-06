package com.example.ganambro.config

object AppConfigFactory {
    fun create(provider: ConfigProvider): AppConfig = AppConfig(
        schoolName = provider.schoolName,
        appName = provider.appName,
        pin = provider.pin,
        examUrl = provider.examUrl,
        tokenWindowMinutes = provider.tokenWindowMinutes,
        appVersion = provider.appVersion,
    )
}
