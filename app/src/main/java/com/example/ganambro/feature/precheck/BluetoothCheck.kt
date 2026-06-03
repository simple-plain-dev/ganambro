package com.example.ganambro.feature.precheck

/**
 * Checks that Bluetooth is turned off.
 *
 * Platform dependency (caller supplies): function that returns true when bluetooth is OFF.
 */
class BluetoothCheck(
    private val isBluetoothOff: suspend () -> Boolean,
) : Precheck {
    override val name = "Bluetooth"

    override suspend fun check(): PrecheckResult {
        return if (isBluetoothOff()) PrecheckResult.Pass
        else PrecheckResult.Fail("Bluetooth harus dimatikan")
    }
}
