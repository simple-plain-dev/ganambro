package com.example.ganambro.ui.pengawas

/**
 * Target buttons that participate in the hidden access sequence.
 * [Logo] must be clicked 3×, then [Petunjuk] 1×, in exact order.
 * Any other click resets the counter.
 */
enum class TriggerTarget { Logo, Petunjuk, Exit }

/** State of the hidden access sequence. */
sealed class TriggerState {
    data object Idle : TriggerState()
    data class Counting(val logoCount: Int) : TriggerState()
    data object Triggered : TriggerState()
}

/**
 * Pure-Kotlin state machine for hidden Pengawas access.
 *
 * Sequence: Logo×3 → Petunjuk×1 → Triggered.
 * Any out-of-sequence click resets to Idle (except when already Triggered).
 */
class HiddenAccessTrigger {
    private var logoCount = 0
    var state: TriggerState = TriggerState.Idle
        private set

    fun registerClick(target: TriggerTarget): TriggerState {
        if (state is TriggerState.Triggered) return state

        when (target) {
            TriggerTarget.Logo -> {
                logoCount++
                if (logoCount >= 3) {
                    logoCount = 3
                }
                state = TriggerState.Counting(logoCount)
            }
            TriggerTarget.Petunjuk -> {
                if (logoCount >= 3) {
                    state = TriggerState.Triggered
                } else {
                    reset()
                }
            }
            TriggerTarget.Exit -> {
                reset()
            }
        }
        return state
    }

    fun reset() {
        logoCount = 0
        state = TriggerState.Idle
    }
}
