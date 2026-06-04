package com.example.ganambro.feature.precheck

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class InternetCheckTest {

    @Test
    fun `returns Pass when online`() = runTest {
        val check = InternetCheck { true }
        val result = check.check()

        assertInstanceOf(PrecheckResult.Pass::class.java, result)
    }

    @Test
    fun `returns Fail when offline with correct message`() = runTest {
        val check = InternetCheck { false }
        val result = check.check()

        assertInstanceOf(PrecheckResult.Fail::class.java, result)
        assertEquals("Internet harus aktif", (result as PrecheckResult.Fail).message)
    }

    @Test
    fun `name is Internet`() {
        val check = InternetCheck { true }
        assertEquals("Internet", check.name)
    }
}
