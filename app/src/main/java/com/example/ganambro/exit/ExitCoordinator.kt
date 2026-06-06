package com.example.ganambro.exit

import com.example.ganambro.audio.WarningSound
import com.example.ganambro.audio.WarningSoundType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Context from which exit is triggered — determines behaviour.
 *
 * - [Menu]: langsung keluar, tanpa konfirmasi, tanpa suara.
 * - [Ujian]: konfirmasi ketik "exit" → Warning Sound 2 → keluar.
 */
enum class ExitContext { Menu, Ujian }

/**
 * Owns all exit behaviour for the application.
 *
 * Delegates the actual system exit to [onExit] and the confirmation dialog
 * to [requestConfirmation], keeping the module pure and testable.
 */
class ExitCoordinator(
    private val warningSound: WarningSound,
    private val onExit: () -> Unit,
    private val requestConfirmation: suspend (prompt: String) -> Boolean,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
) {

    fun exit(from: ExitContext) {
        when (from) {
            ExitContext.Menu -> onExit()
            ExitContext.Ujian -> {
                scope.launch {
                    val confirmed = requestConfirmation("ketik exit untuk keluar")
                    if (confirmed) {
                        warningSound.play(WarningSoundType.WS2)
                        onExit()
                    }
                }
            }
        }
    }
}
