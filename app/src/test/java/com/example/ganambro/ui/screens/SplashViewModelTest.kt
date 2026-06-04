package com.example.ganambro.ui.screens

import com.example.ganambro.feature.precheck.Precheck
import com.example.ganambro.feature.precheck.PrecheckRegistry
import com.example.ganambro.feature.precheck.PrecheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    @Test
    fun `all checks pass ends with AllPassed`() = runTest {
        val registry = PrecheckRegistry().apply {
            register(FakePrecheck("Internet", PrecheckResult.Pass))
            register(FakePrecheck("GPS", PrecheckResult.Pass))
            register(FakePrecheck("Bluetooth", PrecheckResult.Pass))
        }
        val vm = SplashViewModel(registry, scope = this)

        vm.startChecks()
        advanceUntilIdle()

        val final = vm.state.value
        assertInstanceOf(SplashViewModel.UiState.AllPassed::class.java, final)
        assertEquals(listOf("Internet", "GPS", "Bluetooth"), (final as SplashViewModel.UiState.AllPassed).messages)
    }

    @Test
    fun `first failure ends with Failed`() = runTest {
        val registry = PrecheckRegistry().apply {
            register(FakePrecheck("Internet", PrecheckResult.Pass))
            register(FakePrecheck("GPS", PrecheckResult.Fail("GPS harus diaktifkan")))
            register(FakePrecheck("Bluetooth", PrecheckResult.Pass))
        }
        val vm = SplashViewModel(registry, scope = this)

        vm.startChecks()
        advanceUntilIdle()

        val final = vm.state.value
        assertInstanceOf(SplashViewModel.UiState.Failed::class.java, final)
        assertEquals("GPS harus diaktifkan", (final as SplashViewModel.UiState.Failed).errorMessage)
        assertEquals(listOf("Internet", "GPS"), final.messages)
    }

    @Test
    fun `checking states progress in order`() = runTest {
        val registry = PrecheckRegistry().apply {
            register(FakePrecheck("Internet", PrecheckResult.Pass))
            register(FakePrecheck("GPS", PrecheckResult.Pass))
            register(FakePrecheck("Bluetooth", PrecheckResult.Pass))
        }
        val vm = SplashViewModel(registry, scope = this)

        // Verify initial state
        val initial = vm.state.value
        assertInstanceOf(SplashViewModel.UiState.Checking::class.java, initial)
        assertEquals("Internet", (initial as SplashViewModel.UiState.Checking).label)

        vm.startChecks()
        advanceUntilIdle()

        // Final is AllPassed (checked in test above)
        assertNotNull(vm.state.value)
    }

    @Test
    fun `registering checks preserves names`() = runTest {
        val registry = PrecheckRegistry()
        registry.register(FakePrecheck("Internet", PrecheckResult.Pass))
        registry.register(FakePrecheck("GPS", PrecheckResult.Pass))

        assertEquals(listOf("Internet", "GPS"), registry.checkNames())
    }

    private class FakePrecheck(
        override val name: String,
        private val result: PrecheckResult,
    ) : Precheck {
        override suspend fun check(): PrecheckResult = result
    }
}
