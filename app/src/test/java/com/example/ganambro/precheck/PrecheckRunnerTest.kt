package com.example.ganambro.precheck

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PrecheckRunnerTest {

    // --- Fake checkers ---

    private fun passingChecker(name: String) = object : Checker {
        override val name = name
        override suspend fun check(): Boolean = true
        override fun failureReason(): String = "$name failed"
    }

    private fun failingChecker(name: String, reason: String = "$name failed") = object : Checker {
        override val name = name
        override suspend fun check(): Boolean = false
        override fun failureReason(): String = reason
    }

    // --- Tests ---

    @Test
    fun `all checks pass emits InProgress then Passed for each checker`() = runTest {
        val checkers = listOf(
            passingChecker("Internet"),
            passingChecker("GPS"),
            passingChecker("Bluetooth"),
            passingChecker("Headset"),
            passingChecker("Volume"),
        )
        val runner = PrecheckRunner(checkers)

        val results = runner.runChecks().toList()

        assertEquals(10, results.size)
        assertEquals(CheckResult.InProgress("Internet"), results[0])
        assertEquals(CheckResult.Passed("Internet"), results[1])
        assertEquals(CheckResult.InProgress("GPS"), results[2])
        assertEquals(CheckResult.Passed("GPS"), results[3])
        assertEquals(CheckResult.InProgress("Bluetooth"), results[4])
        assertEquals(CheckResult.Passed("Bluetooth"), results[5])
        assertEquals(CheckResult.InProgress("Headset"), results[6])
        assertEquals(CheckResult.Passed("Headset"), results[7])
        assertEquals(CheckResult.InProgress("Volume"), results[8])
        assertEquals(CheckResult.Passed("Volume"), results[9])
    }

    @Test
    fun `execution order follows checker list order`() = runTest {
        val executionOrder = mutableListOf<String>()
        val checkers = listOf(
            object : Checker {
                override val name = "First"
                override suspend fun check(): Boolean { executionOrder.add(name); return true }
                override fun failureReason() = "fail"
            },
            object : Checker {
                override val name = "Second"
                override suspend fun check(): Boolean { executionOrder.add(name); return true }
                override fun failureReason() = "fail"
            },
        )
        val runner = PrecheckRunner(checkers)
        runner.runChecks().toList()

        assertEquals(listOf("First", "Second"), executionOrder)
    }

    @Test
    fun `first failure terminates flow and does not run remaining checkers`() = runTest {
        val executed = mutableListOf<String>()
        val checkers = listOf(
            object : Checker {
                override val name = "Internet"
                override suspend fun check(): Boolean { executed.add(name); return true }
                override fun failureReason() = "fail"
            },
            object : Checker {
                override val name = "GPS"
                override suspend fun check(): Boolean { executed.add(name); return false }
                override fun failureReason() = "GPS is on — turn it off"
            },
            object : Checker {
                override val name = "Bluetooth"
                override suspend fun check(): Boolean { executed.add(name); return true }
                override fun failureReason() = "fail"
            },
        )
        val runner = PrecheckRunner(checkers)

        val results = runner.runChecks().toList()

        // Internet passes, GPS fails, Bluetooth never executes
        assertEquals(4, results.size)
        assertEquals(CheckResult.InProgress("Internet"), results[0])
        assertEquals(CheckResult.Passed("Internet"), results[1])
        assertEquals(CheckResult.InProgress("GPS"), results[2])
        assertEquals(CheckResult.Failed("GPS", "GPS is on — turn it off"), results[3])
        assertEquals(listOf("Internet", "GPS"), executed)
    }

    @Test
    fun `failed check result carries the failure reason`() = runTest {
        val checkers = listOf(
            failingChecker("Bluetooth", "Bluetooth menyala — matikan Bluetooth"),
        )
        val runner = PrecheckRunner(checkers)

        val results = runner.runChecks().toList()

        assertEquals(2, results.size)
        assertEquals(CheckResult.InProgress("Bluetooth"), results[0])
        assertTrue(results[1] is CheckResult.Failed)
        assertEquals("Bluetooth menyala — matikan Bluetooth", (results[1] as CheckResult.Failed).reason)
    }
}
