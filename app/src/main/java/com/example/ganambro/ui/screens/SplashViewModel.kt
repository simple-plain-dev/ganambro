package com.example.ganambro.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ganambro.feature.precheck.PrecheckRegistry
import com.example.ganambro.feature.precheck.PrecheckResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SplashUiState(
    val lines: List<TerminalLine> = emptyList(),
    val isComplete: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String? = null,
)

data class TerminalLine(
    val text: String,
    val status: TerminalStatus,
)

enum class TerminalStatus { RUNNING, OK, FAIL }

class SplashViewModel(
    private val precheckRegistry: PrecheckRegistry,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState

    init {
        runChecks()
    }

    private fun runChecks() {
        viewModelScope.launch {
            val checks = listOf("Internet", "GPS", "Bluetooth")
            var hasFailure = false
            var failedMessage: String? = null

            // Run sequential checks with terminal-style delays
            val results = precheckRegistry.runAll()

            for ((index, result) in results.withIndex()) {
                val checkName = checks[index]
                val status = when (result) {
                    is PrecheckResult.Pass -> TerminalStatus.OK
                    is PrecheckResult.Fail -> {
                        hasFailure = true
                        failedMessage = result.message
                        TerminalStatus.FAIL
                    }
                }

                // Typing animation: show RUNNING first
                _uiState.value = _uiState.value.copy(
                    lines = _uiState.value.lines + TerminalLine("> Memeriksa $checkName...", TerminalStatus.RUNNING)
                )
                delay(800) // simulate check time

                // Show result
                _uiState.value = _uiState.value.copy(
                    lines = _uiState.value.lines.dropLast(1) +
                            TerminalLine("> Memeriksa $checkName...", status)
                )
                delay(400)

                if (hasFailure) break
            }

            if (!hasFailure) {
                _uiState.value = _uiState.value.copy(
                    lines = _uiState.value.lines + TerminalLine("> Memulai Ganambro...", TerminalStatus.OK)
                )
                delay(600)
            }

            _uiState.value = _uiState.value.copy(
                isComplete = true,
                hasError = hasFailure,
                errorMessage = failedMessage,
            )
        }
    }
}
