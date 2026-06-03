package com.example.ganambro.feature.gesture

/**
 * Tap targets for the MenuPengawas unlock gesture.
 */
enum class Target { LOGO, PETUNJUK }

/**
 * States of the gesture state machine.
 */
enum class GestureState {
    /** Waiting for first logo tap. */
    IDLE,

    /** Logo tapped 1×. */
    LOGO_1,

    /** Logo tapped 2×. */
    LOGO_2,

    /** Logo tapped 3× — Petunjuk button now "armed". */
    LOGO_3,

    /** Petunjuk tapped while armed — gesture complete. */
    PETUNJUK_TRIGGERED,

    /** Gesture unlocked — caller should show PIN dialog. */
    UNLOCKED,
}

/**
 * Pure state machine for the MenuPengawas hidden gesture.
 *
 * Gesture sequence: tap logo 3× → tap Petunjuk 1× → PIN prompt.
 * Survives Composable recomposition; testable on JVM without Android.
 */
class MenuPengawasGesture {
    private var state: GestureState = GestureState.IDLE

    /**
     * Feed a tap event into the state machine.
     * @return the new state after processing the tap.
     */
    fun onTap(target: Target): GestureState {
        state = when (state) {
            GestureState.IDLE -> if (target == Target.LOGO) GestureState.LOGO_1 else GestureState.IDLE
            GestureState.LOGO_1 -> if (target == Target.LOGO) GestureState.LOGO_2 else GestureState.IDLE
            GestureState.LOGO_2 -> if (target == Target.LOGO) GestureState.LOGO_3 else GestureState.IDLE
            GestureState.LOGO_3 -> if (target == Target.PETUNJUK) GestureState.PETUNJUK_TRIGGERED else if (target == Target.LOGO) GestureState.LOGO_3 else GestureState.IDLE
            GestureState.PETUNJUK_TRIGGERED -> GestureState.UNLOCKED
            GestureState.UNLOCKED -> GestureState.UNLOCKED // idempotent
        }
        return state
    }

    /** Reset the state machine to IDLE. */
    fun reset() {
        state = GestureState.IDLE
    }
}
