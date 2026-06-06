package com.example.ganambro.audio

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WarningSoundTest {

    @Test
    fun `fake WarningSound records played type`() {
        val fake = FakeWarningSound()
        fake.play(WarningSoundType.WS1)
        assertEquals(listOf(WarningSoundType.WS1), fake.playedTypes)
    }

    @Test
    fun `fake WarningSound tracks multiple plays in order`() {
        val fake = FakeWarningSound()
        fake.play(WarningSoundType.WS1)
        fake.play(WarningSoundType.WS2)
        fake.play(WarningSoundType.WS1)
        assertEquals(
            listOf(WarningSoundType.WS1, WarningSoundType.WS2, WarningSoundType.WS1),
            fake.playedTypes
        )
    }

    @Test
    fun `fake WarningSound can be reset`() {
        val fake = FakeWarningSound()
        fake.play(WarningSoundType.WS1)
        fake.reset()
        assertTrue(fake.playedTypes.isEmpty())
    }

    @Test
    fun `WarningSoundType enum has WS1 and WS2`() {
        assertEquals(2, WarningSoundType.entries.size)
        assertTrue(WarningSoundType.entries.contains(WarningSoundType.WS1))
        assertTrue(WarningSoundType.entries.contains(WarningSoundType.WS2))
    }
}
