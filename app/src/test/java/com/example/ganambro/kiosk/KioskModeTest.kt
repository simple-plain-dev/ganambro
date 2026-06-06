package com.example.ganambro.kiosk

import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KioskModeTest {

    @Test
    fun `fake KioskMode start marks as active`() {
        val kiosk = FakeKioskMode()
        assertFalse(kiosk.isActive)
        kiosk.start()
        assertTrue(kiosk.isActive)
    }

    @Test
    fun `fake KioskMode stop marks as inactive`() {
        val kiosk = FakeKioskMode()
        kiosk.start()
        kiosk.stop()
        assertFalse(kiosk.isActive)
    }

    @Test
    fun `can emit Unpinned event through flow`() = runTest {
        val kiosk = FakeKioskMode()
        kiosk.start()

        val events = mutableListOf<KioskEvent>()
        val collectJob = launch {
            kiosk.events.collect { events.add(it) }
        }
        advanceUntilIdle()

        kiosk.emitUnpinned()
        advanceUntilIdle()
        collectJob.cancel()

        assertTrue(events.any { it is KioskEvent.Unpinned })
    }

    @Test
    fun `can emit SplitScreen event through flow`() = runTest {
        val kiosk = FakeKioskMode()
        kiosk.start()

        val events = mutableListOf<KioskEvent>()
        val collectJob = launch {
            kiosk.events.collect { events.add(it) }
        }
        advanceUntilIdle()

        kiosk.emit(KioskEvent.SplitScreen)
        advanceUntilIdle()
        collectJob.cancel()

        assertTrue(events.any { it is KioskEvent.SplitScreen })
    }

    @Test
    fun `events are distinct sealed classes`() {
        assertTrue(KioskEvent.Unpinned is KioskEvent)
        assertTrue(KioskEvent.SplitScreen is KioskEvent)
    }
}
