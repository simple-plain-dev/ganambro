package com.example.ganambro.cheat

import com.example.ganambro.audio.FakeWarningSound
import com.example.ganambro.audio.WarningSoundType
import com.example.ganambro.kiosk.FakeKioskMode
import com.example.ganambro.kiosk.KioskEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CheatDetectorTest {

    private var exitCalled = false
    private var navigateToMenuCalled = false

    private fun createDetector(
        scope: CoroutineScope,
        kioskMode: FakeKioskMode = FakeKioskMode(),
        warningSound: FakeWarningSound = FakeWarningSound(),
    ) = CheatDetector(
        kioskMode = kioskMode,
        warningSound = warningSound,
        onExit = { exitCalled = true },
        onNavigateToMenu = { navigateToMenuCalled = true },
        scope = scope,
    )

    @Test
    fun `Unpinned event triggers CheatEvent Unpin WS1 and navigates to Menu`() = runTest {
        val kioskMode = FakeKioskMode()
        val warningSound = FakeWarningSound()
        val detector = createDetector(this, kioskMode, warningSound)

        val events = mutableListOf<CheatEvent>()
        val collectJob = launch {
            detector.events.collect { events.add(it) }
        }
        advanceUntilIdle()

        detector.start()
        advanceUntilIdle()

        kioskMode.emit(KioskEvent.Unpinned)
        advanceUntilIdle()

        assertTrue(events.any { it is CheatEvent.Unpin })
        assertEquals(listOf(WarningSoundType.WS1), warningSound.playedTypes)
        assertTrue(navigateToMenuCalled)
        assertFalse(exitCalled)

        detector.stop()
        collectJob.cancel()
    }

    @Test
    fun `SplitScreen event triggers same chain as Unpinned`() = runTest {
        val kioskMode = FakeKioskMode()
        val warningSound = FakeWarningSound()
        val detector = createDetector(this, kioskMode, warningSound)

        val events = mutableListOf<CheatEvent>()
        val collectJob = launch {
            detector.events.collect { events.add(it) }
        }
        advanceUntilIdle()

        detector.start()
        advanceUntilIdle()

        kioskMode.emit(KioskEvent.SplitScreen)
        advanceUntilIdle()

        assertTrue(events.any { it is CheatEvent.Unpin })
        assertEquals(listOf(WarningSoundType.WS1), warningSound.playedTypes)
        assertTrue(navigateToMenuCalled)

        detector.stop()
        collectJob.cancel()
    }

    @Test
    fun `stop tears down KioskMode subscription`() = runTest {
        val kioskMode = FakeKioskMode()
        val warningSound = FakeWarningSound()
        val detector = createDetector(this, kioskMode, warningSound)

        val events = mutableListOf<CheatEvent>()
        val collectJob = launch {
            detector.events.collect { events.add(it) }
        }
        advanceUntilIdle()

        detector.start()
        advanceUntilIdle()

        detector.stop()
        advanceUntilIdle()

        // After stop, emit should not be processed
        kioskMode.emit(KioskEvent.Unpinned)
        advanceUntilIdle()

        assertTrue(events.isEmpty())
        assertFalse(navigateToMenuCalled)

        collectJob.cancel()
    }

    @Test
    fun `CheatEvent Headset is distinct from Unpin`() {
        assertTrue(CheatEvent.Headset is CheatEvent)
        assertTrue(CheatEvent.Unpin is CheatEvent)
        assertFalse(CheatEvent.Headset == CheatEvent.Unpin)
    }
}
