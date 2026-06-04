package com.example.ganambro.feature.precheck

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class GpsCheckTest {

    @Test
    fun `returns Pass when GPS is on`() = runTest {
        val check = GpsCheck { true }
        assertInstanceOf(PrecheckResult.Pass::class.java, check.check())
    }

    @Test
    fun `returns Fail when GPS is off`() = runTest {
        val check = GpsCheck { false }
        val result = check.check()
        assertInstanceOf(PrecheckResult.Fail::class.java, result)
        assertEquals("GPS harus diaktifkan", (result as PrecheckResult.Fail).message)
    }

    @Test
    fun `name is GPS`() {
        assertEquals("GPS", GpsCheck { true }.name)
    }
}
