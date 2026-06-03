package com.example.ganambro.feature.volume

import kotlinx.coroutines.*

/**
 * Manages STREAM_ALARM volume — sets to max and polls to enforce it.
 *
 * Pure Kotlin module. Platform dependencies (get/set volume) are injected via constructor.
 * Caller (ui/) provides Android AudioManager wrappers.
 */
class VolumeManager(
    private val getVolume: () -> Int,
    private val getMaxVolume: () -> Int,
    private val setVolume: (Int) -> Unit,
    private val pollIntervalMs: Long = 2000L,
) {
    private var scope: CoroutineScope? = null
    private var originalVolume: Int? = null

    /**
     * Save current volume, set to max, start polling.
     */
    fun start() {
        originalVolume = getVolume()
        setVolume(getMaxVolume())
        scope = CoroutineScope(Dispatchers.Default).apply {
            launch {
                while (isActive) {
                    if (getVolume() < getMaxVolume()) {
                        setVolume(getMaxVolume())
                    }
                    delay(pollIntervalMs)
                }
            }
        }
    }

    /**
     * Stop polling and restore original volume.
     */
    fun restore() {
        scope?.cancel()
        scope = null
        originalVolume?.let { setVolume(it) }
        originalVolume = null
    }
}
