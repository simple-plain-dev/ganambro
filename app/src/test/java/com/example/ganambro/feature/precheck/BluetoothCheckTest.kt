package com.example.ganambro.feature.precheck

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class BluetoothCheckTest {

    @Test
    fun `returns Pass when bluetooth is off`() = runTest {
        val check = BluetoothCheck { true }
        assertInstanceOf(PrecheckResult.Pass::class.java, check.check())
    }

    @Test
    fun `returns Fail when bluetooth is on`() = runTest {
        val check = BluetoothCheck { false }
        val result = check.check()
        assertInstanceOf(PrecheckResult.Fail::class.java, result)
        assertEquals("Bluetooth harus dimatikan", (result as PrecheckResult.Fail).message)
    }

    @Test
    fun `name is Bluetooth`() {
        assertEquals("Bluetooth", BluetoothCheck { true }.name)
    }
}
