package com.example.ganambro.exit

import com.example.ganambro.audio.FakeWarningSound
import com.example.ganambro.audio.WarningSoundType
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ExitCoordinatorTest {

    private var exitCalled = false
    private var lastConfirmPrompt: String? = null
    private var confirmResponse: Boolean = true

    private fun createCoordinator(
        warningSound: FakeWarningSound = FakeWarningSound(),
        scope: kotlinx.coroutines.CoroutineScope,
    ) = ExitCoordinator(
        warningSound = warningSound,
        onExit = { exitCalled = true },
        requestConfirmation = { prompt ->
            lastConfirmPrompt = prompt
            confirmResponse
        },
        scope = scope,
    )

    @Test
    fun `exit Menu calls onExit immediately without confirmation`() = runTest {
        val warningSound = FakeWarningSound()
        val coordinator = createCoordinator(warningSound, this)

        coordinator.exit(ExitContext.Menu)

        assertTrue(exitCalled)
        assertTrue(warningSound.playedTypes.isEmpty())
        assertEquals(null, lastConfirmPrompt)
    }

    @Test
    fun `exit Ujian requests confirmation with correct prompt`() = runTest {
        val coordinator = createCoordinator(scope = this)
        coordinator.exit(ExitContext.Ujian)
        advanceUntilIdle()
        assertEquals("ketik exit untuk keluar", lastConfirmPrompt)
    }

    @Test
    fun `exit Ujian with confirmation plays WS2 then exits`() = runTest {
        val warningSound = FakeWarningSound()
        confirmResponse = true
        val coordinator = createCoordinator(warningSound, this)

        coordinator.exit(ExitContext.Ujian)
        advanceUntilIdle()

        assertTrue(exitCalled)
        assertEquals(listOf(WarningSoundType.WS2), warningSound.playedTypes)
    }

    @Test
    fun `exit Ujian with rejected confirmation does not exit and does not play sound`() = runTest {
        val warningSound = FakeWarningSound()
        confirmResponse = false
        exitCalled = false
        val coordinator = createCoordinator(warningSound, this)

        coordinator.exit(ExitContext.Ujian)
        advanceUntilIdle()

        assertFalse(exitCalled)
        assertTrue(warningSound.playedTypes.isEmpty())
    }
}
