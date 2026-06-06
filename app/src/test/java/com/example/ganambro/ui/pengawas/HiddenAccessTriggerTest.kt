package com.example.ganambro.ui.pengawas

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HiddenAccessTriggerTest {

    @Test
    fun `Logo 3x then Petunjuk 1x triggers`() {
        val trigger = HiddenAccessTrigger()
        assertEquals(TriggerState.Idle, trigger.state)

        trigger.registerClick(TriggerTarget.Logo)
        assertEquals(TriggerState.Counting(1), trigger.state)

        trigger.registerClick(TriggerTarget.Logo)
        assertEquals(TriggerState.Counting(2), trigger.state)

        trigger.registerClick(TriggerTarget.Logo)
        assertEquals(TriggerState.Counting(3), trigger.state)

        trigger.registerClick(TriggerTarget.Petunjuk)
        assertEquals(TriggerState.Triggered, trigger.state)
    }

    @Test
    fun `wrong click in middle resets to Idle`() {
        val trigger = HiddenAccessTrigger()

        trigger.registerClick(TriggerTarget.Logo)
        trigger.registerClick(TriggerTarget.Logo) // 2x Logo
        assertEquals(TriggerState.Counting(2), trigger.state)

        trigger.registerClick(TriggerTarget.Exit) // wrong click
        assertEquals(TriggerState.Idle, trigger.state)
    }

    @Test
    fun `Petunjuk before Logo stays Idle`() {
        val trigger = HiddenAccessTrigger()

        trigger.registerClick(TriggerTarget.Petunjuk)
        assertEquals(TriggerState.Idle, trigger.state)
    }

    @Test
    fun `Triggered stays Triggered on further clicks`() {
        val trigger = HiddenAccessTrigger()
        trigger.registerClick(TriggerTarget.Logo)
        trigger.registerClick(TriggerTarget.Logo)
        trigger.registerClick(TriggerTarget.Logo)
        trigger.registerClick(TriggerTarget.Petunjuk)
        assertEquals(TriggerState.Triggered, trigger.state)

        trigger.registerClick(TriggerTarget.Logo)
        assertEquals(TriggerState.Triggered, trigger.state)

        trigger.registerClick(TriggerTarget.Petunjuk)
        assertEquals(TriggerState.Triggered, trigger.state)

        trigger.registerClick(TriggerTarget.Exit)
        assertEquals(TriggerState.Triggered, trigger.state)
    }

    @Test
    fun `Logo 4x without Petunjuk stays Counting 3`() {
        val trigger = HiddenAccessTrigger()
        trigger.registerClick(TriggerTarget.Logo)
        trigger.registerClick(TriggerTarget.Logo)
        trigger.registerClick(TriggerTarget.Logo)
        trigger.registerClick(TriggerTarget.Logo) // extra Logo
        assertEquals(TriggerState.Counting(3), trigger.state)
    }

    @Test
    fun `can be reset manually`() {
        val trigger = HiddenAccessTrigger()
        trigger.registerClick(TriggerTarget.Logo)
        trigger.registerClick(TriggerTarget.Logo)
        trigger.registerClick(TriggerTarget.Logo)
        trigger.registerClick(TriggerTarget.Petunjuk)
        assertEquals(TriggerState.Triggered, trigger.state)

        trigger.reset()
        assertEquals(TriggerState.Idle, trigger.state)
    }
}
