package com.example.ganambro.cheat

import com.example.ganambro.audio.WarningSound
import com.example.ganambro.audio.WarningSoundType
import com.example.ganambro.kiosk.KioskEvent
import com.example.ganambro.kiosk.KioskMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/** Events emitted by [CheatDetector] when a cheating condition is detected. */
sealed class CheatEvent {
    /** Headset terpasang selama Ujian — aplikasi langsung keluar. */
    data object Headset : CheatEvent()

    /** Peserta meninggalkan aplikasi / split screen — kembali ke Menu. */
    data object Unpin : CheatEvent()
}

/**
 * Coordinator that monitors device conditions during Ujian.
 *
 * Subscribes to [KioskMode] events and reacts:
 * - Unpin / SplitScreen → [CheatEvent.Unpin] → WS1 → navigasi ke Menu
 * - Headset detection → [CheatEvent.Headset] → WS1 → exit
 *
 * Volume enforcement is a background concern handled separately.
 * See ADR-0002: volume enforcement seam between WarningSound and CheatDetector.
 */
class CheatDetector(
    private val kioskMode: KioskMode,
    private val warningSound: WarningSound,
    private val onExit: () -> Unit,
    private val onNavigateToMenu: () -> Unit,
    private val scope: CoroutineScope,
) {
    private val _events = MutableSharedFlow<CheatEvent>(extraBufferCapacity = 5)
    val events: Flow<CheatEvent> = _events.asSharedFlow()

    private var watchJob: Job? = null

    /** Start monitoring [KioskMode] events. Safe to call multiple times. */
    fun start() {
        stop()
        watchJob = scope.launch {
            kioskMode.events.collect { event ->
                when (event) {
                    is KioskEvent.Unpinned, is KioskEvent.SplitScreen -> {
                        _events.emit(CheatEvent.Unpin)
                        warningSound.play(WarningSoundType.WS1)
                        onNavigateToMenu()
                    }
                }
            }
        }
    }

    /** Stop monitoring. Idempotent. */
    fun stop() {
        watchJob?.cancel()
        watchJob = null
    }
}
