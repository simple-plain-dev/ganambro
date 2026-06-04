package com.example.ganambro.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ganambro.feature.precheck.PrecheckRegistry
import com.example.ganambro.feature.precheck.PrecheckResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val precheckRegistry: PrecheckRegistry,
    private val scope: CoroutineScope? = null,
) : ViewModel() {

    private val actualScope: CoroutineScope = scope ?: viewModelScope

    sealed class UiState {
        data class Checking(
            val label: String,
            val done: List<String>,
        ) : UiState()

        data class AllPassed(
            val messages: List<String>,
        ) : UiState()

        data class Failed(
            val messages: List<String>,
            val errorMessage: String,
        ) : UiState()
    }

    private val _state = MutableStateFlow<UiState>(
        UiState.Checking(label = "Internet", done = emptyList())
    )
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun startChecks() {
        actualScope.launch {
            val names = precheckRegistry.checkNames()
            val results = precheckRegistry.runAll()
            val passedNames = mutableListOf<String>()

            for ((index, result) in results.withIndex()) {
                passedNames.add(names[index])
                when (result) {
                    is PrecheckResult.Pass -> {
                        val nextIndex = index + 1
                        if (nextIndex < names.size) {
                            _state.value = UiState.Checking(
                                label = names[nextIndex],
                                done = passedNames.toList(),
                            )
                        }
                    }
                    is PrecheckResult.Fail -> {
                        _state.value = UiState.Failed(
                            messages = passedNames,
                            errorMessage = result.message,
                        )
                        return@launch
                    }
                }
            }

            _state.value = UiState.AllPassed(messages = passedNames)
        }
    }
}
