package com.example.ganambro.audio

/**
 * Test double for [WarningSound]. Records every play() call for assertion.
 */
class FakeWarningSound : WarningSound {
    val playedTypes = mutableListOf<WarningSoundType>()

    override fun play(type: WarningSoundType) {
        playedTypes.add(type)
    }

    fun reset() {
        playedTypes.clear()
    }
}
