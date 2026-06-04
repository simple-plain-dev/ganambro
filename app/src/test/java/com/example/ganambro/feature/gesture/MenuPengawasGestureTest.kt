package com.example.ganambro.feature.gesture

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class MenuPengawasGestureTest {

    @Test
    fun `three logo taps advance through LOGO_1 LOGO_2 LOGO_3`() {
        val gesture = MenuPengawasGesture()

        assertEquals(GestureState.LOGO_1, gesture.onTap(Target.LOGO))
        assertEquals(GestureState.LOGO_2, gesture.onTap(Target.LOGO))
        assertEquals(GestureState.LOGO_3, gesture.onTap(Target.LOGO))
    }

    @Test
    fun `tap Petunjuk in IDLE resets to IDLE`() {
        val gesture = MenuPengawasGesture()
        assertEquals(GestureState.IDLE, gesture.onTap(Target.PETUNJUK))
    }

    @Test
    fun `tap Petunjuk before 3 logo taps resets to IDLE`() {
        val gesture = MenuPengawasGesture()

        gesture.onTap(Target.LOGO) // LOGO_1
        gesture.onTap(Target.LOGO) // LOGO_2
        assertEquals(GestureState.IDLE, gesture.onTap(Target.PETUNJUK)) // wrong → reset
    }

    @Test
    fun `LOGO_3 plus PETUNJUK transitions to PETUNJUK_TRIGGERED then UNLOCKED`() {
        val gesture = MenuPengawasGesture()

        gesture.onTap(Target.LOGO) // LOGO_1
        gesture.onTap(Target.LOGO) // LOGO_2
        gesture.onTap(Target.LOGO) // LOGO_3
        assertEquals(GestureState.PETUNJUK_TRIGGERED, gesture.onTap(Target.PETUNJUK))
        assertEquals(GestureState.UNLOCKED, gesture.onTap(Target.LOGO)) // any tap → UNLOCKED
    }

    @Test
    fun `UNLOCKED stays UNLOCKED on any tap`() {
        val gesture = MenuPengawasGesture()

        repeat(3) { gesture.onTap(Target.LOGO) }
        gesture.onTap(Target.PETUNJUK) // PETUNJUK_TRIGGERED
        gesture.onTap(Target.LOGO) // UNLOCKED

        assertEquals(GestureState.UNLOCKED, gesture.onTap(Target.LOGO))
        assertEquals(GestureState.UNLOCKED, gesture.onTap(Target.PETUNJUK))
    }

    @Test
    fun `reset returns state to IDLE`() {
        val gesture = MenuPengawasGesture()

        repeat(3) { gesture.onTap(Target.LOGO) }
        gesture.onTap(Target.PETUNJUK) // PETUNJUK_TRIGGERED
        gesture.onTap(Target.LOGO) // UNLOCKED

        gesture.reset()
        // After reset, first logo tap starts from scratch → LOGO_1, not UNLOCKED
        assertEquals(GestureState.LOGO_1, gesture.onTap(Target.LOGO))
    }

    @Test
    fun `extra logo tap at LOGO_3 stays LOGO_3`() {
        val gesture = MenuPengawasGesture()

        repeat(3) { gesture.onTap(Target.LOGO) } // LOGO_3
        assertEquals(GestureState.LOGO_3, gesture.onTap(Target.LOGO)) // stays LOGO_3
    }

    @Test
    fun `full gesture sequence IDLE to UNLOCKED`() {
        val gesture = MenuPengawasGesture()

        assertEquals(GestureState.LOGO_1, gesture.onTap(Target.LOGO))
        assertEquals(GestureState.LOGO_2, gesture.onTap(Target.LOGO))
        assertEquals(GestureState.LOGO_3, gesture.onTap(Target.LOGO))
        assertEquals(GestureState.PETUNJUK_TRIGGERED, gesture.onTap(Target.PETUNJUK))
        assertEquals(GestureState.UNLOCKED, gesture.onTap(Target.LOGO))
    }
}
