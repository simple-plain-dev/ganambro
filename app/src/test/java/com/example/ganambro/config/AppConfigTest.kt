package com.example.ganambro.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class AppConfigTest {

    // --- AppConfig data class ---

    @Test
    fun `salt computed from schoolName appName appVersion`() {
        val config = AppConfig(
            schoolName = "SMA1",
            appName = "Ganambro",
            pin = "123456",
            examUrl = "https://ujian.sekolah.id",
            tokenWindowMinutes = 10,
            appVersion = "1.0"
        )
        assertEquals("SMA1Ganambro1.0", config.salt)
    }

    @Test
    fun `salt differs across different schoolName`() {
        val config1 = AppConfig("SMA1", "Ganambro", "123456", "url", 10, "1.0")
        val config2 = AppConfig("SMA2", "Ganambro", "123456", "url", 10, "1.0")
        assertNotEquals(config1.salt, config2.salt)
    }

    @Test
    fun `salt differs across different appVersion`() {
        val config1 = AppConfig("SMA1", "Ganambro", "123456", "url", 10, "1.0")
        val config2 = AppConfig("SMA1", "Ganambro", "123456", "url", 10, "2.0")
        assertNotEquals(config1.salt, config2.salt)
    }

    // --- AppConfigFactory ---

    @Test
    fun `factory creates AppConfig with all fields from provider`() {
        val provider = object : ConfigProvider {
            override val schoolName = "SMA1"
            override val appName = "Ganambro"
            override val pin = "123456"
            override val examUrl = "https://ujian.sekolah.id"
            override val appVersion = "1.0"
            override val tokenWindowMinutes = 15
        }
        val config = AppConfigFactory.create(provider)
        assertEquals("SMA1", config.schoolName)
        assertEquals("Ganambro", config.appName)
        assertEquals("123456", config.pin)
        assertEquals("https://ujian.sekolah.id", config.examUrl)
        assertEquals("1.0", config.appVersion)
        assertEquals(15, config.tokenWindowMinutes)
        assertEquals("SMA1Ganambro1.0", config.salt)
    }

    @Test
    fun `factory uses default tokenWindowMinutes 10 when provider does not override`() {
        val provider = object : ConfigProvider {
            override val schoolName = "SMA1"
            override val appName = "Ganambro"
            override val pin = "123456"
            override val examUrl = "url"
            override val appVersion = "1.0"
            // tokenWindowMinutes not overridden → default 10
        }
        val config = AppConfigFactory.create(provider)
        assertEquals(10, config.tokenWindowMinutes)
    }
}
