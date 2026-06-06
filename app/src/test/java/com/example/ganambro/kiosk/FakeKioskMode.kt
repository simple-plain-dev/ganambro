package com.example.ganambro.kiosk

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Test double for [KioskMode].
 * Records [start]/[stop] calls and allows emitting events from test code.
 */
class FakeKioskMode : KioskMode {
    var isActive: Boolean = false
        private set

    private val _events = MutableSharedFlow<KioskEvent>(replay = 1, extraBufferCapacity = 5)
    override val events: Flow<KioskEvent> = _events.asSharedFlow()

    override fun start() {
        isActive = true
    }

    override fun stop() {
        isActive = false
    }

    suspend fun emitUnpinned() {
        _events.emit(KioskEvent.Unpinned)
    }

    suspend fun emit(event: KioskEvent) {
        _events.emit(event)
    }
}
