package com.example.ganambro.exit

import com.example.ganambro.audio.FakeWarningSound
import com.example.ganambro.audio.WarningSoundType
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
    ) = ExitCoordinator(
        warningSound = warningSound,
        onExit = { exitCalled = true },
        requestConfirmation = { prompt ->
            lastConfirmPrompt = prompt
            confirmResponse
        },
    )

    @Test
    fun `exit Menu calls onExit immediately without confirmation`() {
        val warningSound = FakeWarningSound()
        val coordinator = createCoordinator(warningSound)

        coordinator.exit(ExitContext.Menu)

        assertTrue(exitCalled)
        assertTrue(warningSound.playedTypes.isEmpty())
        assertEquals(null, lastConfirmPrompt)
    }

    @Test
    fun `exit Ujian requests confirmation with correct prompt`() {
        val coordinator = createCoordinator()

        coordinator.exit(ExitContext.Ujian)

        assertEquals("ketik exit untuk keluar", lastConfirmPrompt)
    }

    @Test
    fun `exit Ujian with confirmation plays WS2 then exits`() {
        val warningSound = FakeWarningSound()
        confirmResponse = true
        val coordinator = createCoordinator(warningSound)

        coordinator.exit(ExitContext.Ujian)

        assertTrue(exitCalled)
        assertEquals(listOf(WarningSoundType.WS2), warningSound.playedTypes)
    }

    @Test
    fun `exit Ujian with rejected confirmation does not exit and does not play sound`() {
        val warningSound = FakeWarningSound()
        confirmResponse = false
        exitCalled = false
        val coordinator = createCoordinator(warningSound)

        coordinator.exit(ExitContext.Ujian)

        assertFalse(exitCalled)
        assertTrue(warningSound.playedTypes.isEmpty())
    }
}
