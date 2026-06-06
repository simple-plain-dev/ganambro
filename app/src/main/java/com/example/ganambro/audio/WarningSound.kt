package com.example.ganambro.audio

/** Two distinct warning sounds used throughout the application. */
enum class WarningSoundType { WS1, WS2 }

/**
 * Encapsulates playing warning sounds.
 * Volume is always max — an internal invariant of every implementation.
 */
interface WarningSound {
    fun play(type: WarningSoundType)
}
