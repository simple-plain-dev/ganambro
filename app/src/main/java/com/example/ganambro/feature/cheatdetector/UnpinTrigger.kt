package com.example.ganambro.feature.cheatdetector

/**
 * Detects unpin paksa — Peserta exited screen pinning manually (back+overview).
 * FATAL severity: triggers finishAndRemoveTask().
 */
class UnpinTrigger : CheatTrigger {
    override fun onEvent(event: CheatEvent): Severity? {
        return if (event == CheatEvent.UNPIN_PAKSA) Severity.FATAL
        else null // not my event
    }
}
