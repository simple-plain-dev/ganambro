package com.example.ganambro.feature.cheatdetector

/**
 * Detects Activity onPause() during Mode Ujian.
 * Triggered by incoming calls, alarms, or screen-off (mitigated by FLAG_KEEP_SCREEN_ON).
 * WARNING severity: plays warning sound but does not exit.
 */
class PauseTrigger : CheatTrigger {
    override fun onEvent(event: CheatEvent): Severity? {
        return if (event == CheatEvent.ON_PAUSE) Severity.WARNING
        else null
    }
}
