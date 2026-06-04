package com.example.ganambro.feature.volume

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VolumeManagerTest {

    @Test
    fun `sets volume to max on start`() = runTest {
        var currentVolume = 3
        val sut = VolumeManager(
            getVolume = { currentVolume },
            getMaxVolume = { 15 },
            setVolume = { currentVolume = it },
            pollIntervalMs = 1000,
        )

        sut.start()

        // Volume should have been set to max by now (start sets it synchronously)
        assertEquals(15, currentVolume)
        sut.restore()
    }

    @Test
    fun `restores original volume after restore`() = runTest {
        var currentVolume = 7
        val sut = VolumeManager(
            getVolume = { currentVolume },
            getMaxVolume = { 15 },
            setVolume = { currentVolume = it },
            pollIntervalMs = 500,
        )

        sut.start()
        assertEquals(15, currentVolume)

        sut.restore()
        assertEquals(7, currentVolume)
    }

    @Test
    fun `restore without start is no-op`() = runTest {
        var currentVolume = 5
        val sut = VolumeManager(
            getVolume = { currentVolume },
            getMaxVolume = { 15 },
            setVolume = { currentVolume = it },
        )

        sut.restore()
        assertEquals(5, currentVolume)
    }
}
