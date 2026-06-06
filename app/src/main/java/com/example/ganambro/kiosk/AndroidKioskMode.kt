package com.example.ganambro.kiosk

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Android implementation of [KioskMode].
 *
 * - Hides system bars via immersive mode (not [android.app.admin.DevicePolicyManager.startLockTask])
 * - Locks orientation to portrait
 * - Detects unpin / split screen via [Activity] lifecycle callbacks
 */
class AndroidKioskMode(private val activity: Activity) : KioskMode {

    private val eventChannel = Channel<KioskEvent>(Channel.CONFLATED)
    override val events: Flow<KioskEvent> = eventChannel.receiveAsFlow()

    override fun start() {
        // Lock portrait orientation
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Immersive mode — hide status bar + navigation bar
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        val insetsController = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    override fun stop() {
        // Restore orientation
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        // Restore system bars
        val insetsController = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
        insetsController.show(WindowInsetsCompat.Type.systemBars())
    }

    /**
     * Called from [Activity.onPause] — detects Peserta leaving the app.
     */
    fun onActivityPaused() {
        if (activity.isInMultiWindowMode) {
            eventChannel.trySend(KioskEvent.SplitScreen)
        } else {
            eventChannel.trySend(KioskEvent.Unpinned)
        }
    }
}
