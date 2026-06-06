package com.example.ganambro.kiosk

import kotlinx.coroutines.flow.Flow

/** Events emitted by [KioskMode] when the Peserta leaves the app or splits the screen. */
sealed class KioskEvent {
    data object Unpinned : KioskEvent()
    data object SplitScreen : KioskEvent()
}

/**
 * Manages device screen state during Ujian.
 * Responsibilities: immersive mode, portrait lock, unpin/split detection.
 * Does NOT handle volume or sound — those belong to [CheatDetector] and [WarningSound].
 */
interface KioskMode {
    fun start()
    fun stop()
    val events: Flow<KioskEvent>
}
