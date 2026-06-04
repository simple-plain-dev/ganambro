package com.example.ganambro.feature.precheck

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class PrecheckRegistryTest {

    @Test
    fun `runs all registered checks and collects results`() = runTest {
        val registry = PrecheckRegistry()
        registry.register(FakePrecheck("A", PrecheckResult.Pass))
        registry.register(FakePrecheck("B", PrecheckResult.Pass))

        val results = registry.runAll()

        assertEquals(2, results.size)
        assertInstanceOf(PrecheckResult.Pass::class.java, results[0])
        assertInstanceOf(PrecheckResult.Pass::class.java, results[1])
    }

    @Test
    fun `stops at first failure and returns partial results`() = runTest {
        val registry = PrecheckRegistry()
        registry.register(FakePrecheck("A", PrecheckResult.Pass))
        registry.register(FakePrecheck("B", PrecheckResult.Fail("B failed")))
        registry.register(FakePrecheck("C", PrecheckResult.Pass)) // should not run

        val results = registry.runAll()

        assertEquals(2, results.size) // A + B only, C skipped
        assertInstanceOf(PrecheckResult.Pass::class.java, results[0])
        assertInstanceOf(PrecheckResult.Fail::class.java, results[1])
        val fail = results[1] as PrecheckResult.Fail
        assertEquals("B failed", fail.message)
    }

    // Fake implementation for testing
    private class FakePrecheck(
        override val name: String,
        private val result: PrecheckResult,
    ) : Precheck {
        override suspend fun check(): PrecheckResult = result
    }
}
