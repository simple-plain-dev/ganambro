package com.example.ganambro.nav

/**
 * Sealed class state machine for application navigation.
 * Transition rules are encoded in [transitionTo] — illegal transitions return the same state.
 */
sealed class NavState {
    data object Splash : NavState()
    data object Login : NavState()
    data object Menu : NavState()
    data class Ujian(val token: String) : NavState()
    data object Pengawas : NavState()
    data object Exit : NavState()

    /**
     * Attempt a transition to [target].
     * Returns [target] if the transition is legal, otherwise returns `this` unchanged.
     */
    fun transitionTo(target: NavState): NavState = when (this) {
        is Splash -> when (target) {
            is Login, is Exit -> target
            else -> this
        }
        is Login -> when (target) {
            is Menu -> target
            else -> this
        }
        is Menu -> when (target) {
            is Ujian -> if (target.token.isNotBlank()) target else this
            is Pengawas, is Exit -> target
            else -> this
        }
        is Ujian -> when (target) {
            is Menu -> target
            else -> this
        }
        is Pengawas -> when (target) {
            is Menu -> target
            else -> this
        }
        is Exit -> this // terminal state — no transitions out
    }
}
