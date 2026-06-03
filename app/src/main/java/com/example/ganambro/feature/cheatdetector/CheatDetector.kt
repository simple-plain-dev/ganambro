package com.example.ganambro.feature.cheatdetector

/** Severity of a detected cheat event. */
enum class Severity {
    /** Warning only — play sound, log, but don't exit. */
    WARNING,

    /** Fatal — play sound + finishAndRemoveTask(). */
    FATAL,
}

/** Events that can be detected during Mode Ujian. */
enum class CheatEvent {
    /** Peserta exited screen pinning manually (back+overview). */
    UNPIN_PAKSA,

    /** Peserta used system back gesture / back button. */
    GESTURE_BACK,

    /** Activity onPause() fired — app lost focus. */
    ON_PAUSE,
}

/**
 * A single cheat trigger registered with [CheatDetector].
 * Returns [Severity] for events it handles, null for events it ignores.
 */
interface CheatTrigger {
    fun onEvent(event: CheatEvent): Severity?
}

/**
 * Registry of cheat triggers. Call [notify] with each event;
 * the detector returns the first non-null [Severity], or null if no trigger matched.
 */
class CheatDetector {
    private val triggers = mutableListOf<CheatTrigger>()

    /** Register a trigger. Order matters — first match wins. */
    fun register(trigger: CheatTrigger) {
        triggers.add(trigger)
    }

    /**
     * Notify all registered triggers of [event].
     * @return the first matching [Severity], or null if no trigger cares about this event.
     */
    fun notify(event: CheatEvent): Severity? {
        for (trigger in triggers) {
            trigger.onEvent(event)?.let { return it }
        }
        return null
    }
}
