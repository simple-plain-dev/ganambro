package com.example.ganambro.audio

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import com.example.ganambro.R

/**
 * Android implementation of [WarningSound].
 * Plays audio from raw resources at maximum volume — volume enforcement is internal.
 *
 * See ADR-0002: volume max is an invariant of this module, not the caller's responsibility.
 */
class AndroidWarningSound(private val context: Context) : WarningSound {

    override fun play(type: WarningSoundType) {
        val resId = when (type) {
            WarningSoundType.WS1 -> R.raw.warning_sound_1
            WarningSoundType.WS2 -> R.raw.warning_sound_2
        }
        val mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer?.apply {
            setVolume(1f, 1f)
            setOnCompletionListener { it.release() }
            start()
        }
    }
}
