package com.example.ganambro.feature.gesture

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MenuPengawasGestureTest {

    @Test
    fun `full happy path reaches UNLOCKED`() {
        val gesture = MenuPengawasGesture()

        assertEquals(GestureState.LOGO_1, gesture.onTap(Target.LOGO))
        assertEquals(GestureState.LOGO_2, gesture.onTap(Target.LOGO))
        assertEquals(GestureState.LOGO_3, gesture.onTap(Target.LOGO))
        assertEquals(GestureState.PETUNJUK_TRIGGERED, gesture.onTap(Target.PETUNJUK))
        assertEquals(GestureState.UNLOCKED, gesture.onTap(Target.PETUNJUK)) // subsequent tap confirms
    }

    @Test
    fun `wrong tap resets to IDLE from LOGO_1`() {
        val gesture = MenuPengawasGesture()
        gesture.onTap(Target.LOGO) // LOGO_1
        val result = gesture.onTap(Target.PETUNJUK) // wrong tap → IDLE

        assertEquals(GestureState.IDLE, result)
    }

    @Test
    fun `wrong tap resets to IDLE from LOGO_2`() {
        val gesture = MenuPengawasGesture()
        gesture.onTap(Target.LOGO) // LOGO_1
        gesture.onTap(Target.LOGO) // LOGO_2
        val result = gesture.onTap(Target.PETUNJUK) // wrong → IDLE

        assertEquals(GestureState.IDLE, result)
    }

    @Test
    fun `LOGO_3 + LOGO stays at LOGO_3`() {
        val gesture = MenuPengawasGesture()
        gesture.onTap(Target.LOGO) // LOGO_1
        gesture.onTap(Target.LOGO) // LOGO_2
        gesture.onTap(Target.LOGO) // LOGO_3
        val result = gesture.onTap(Target.LOGO) // extra LOGO → stays LOGO_3

        assertEquals(GestureState.LOGO_3, result)
    }

    @Test
    fun `LOGO_3 + PETUNJUK transitions to PETUNJUK_TRIGGERED`() {
        val gesture = MenuPengawasGesture()
        gesture.onTap(Target.LOGO)
        gesture.onTap(Target.LOGO)
        gesture.onTap(Target.LOGO)

        assertEquals(GestureState.PETUNJUK_TRIGGERED, gesture.onTap(Target.PETUNJUK))
    }

    @Test
    fun `UNLOCKED is idempotent`() {
        val gesture = MenuPengawasGesture()
        gesture.onTap(Target.LOGO)
        gesture.onTap(Target.LOGO)
        gesture.onTap(Target.LOGO)
        gesture.onTap(Target.PETUNJUK)
        gesture.onTap(Target.PETUNJUK) // UNLOCKED

        val result = gesture.onTap(Target.LOGO) // still UNLOCKED

        assertEquals(GestureState.UNLOCKED, result)
    }

    @Test
    fun `reset returns to IDLE from any state`() {
        val gesture = MenuPengawasGesture()
        gesture.onTap(Target.LOGO)
        gesture.onTap(Target.LOGO)
        gesture.onTap(Target.LOGO) // LOGO_3

        gesture.reset()

        assertEquals(GestureState.IDLE, gesture.onTap(Target.PETUNJUK)) // should stay IDLE
    }

    @Test
    fun `reset clears partially completed sequence`() {
        val gesture = MenuPengawasGesture()
        gesture.onTap(Target.LOGO)
        gesture.onTap(Target.LOGO) // LOGO_2

        gesture.reset()

        // Start fresh
        assertEquals(GestureState.LOGO_1, gesture.onTap(Target.LOGO))
    }
}
