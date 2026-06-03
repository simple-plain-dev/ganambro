package com.example.ganambro.feature.cheatdetector

/**
 * Detects system back gesture or back button press during Mode Ujian.
 * WARNING severity: plays warning sound but does not exit.
 */
class BackGestureTrigger : CheatTrigger {
    override fun onEvent(event: CheatEvent): Severity? {
        return if (event == CheatEvent.GESTURE_BACK) Severity.WARNING
        else null
    }
}
