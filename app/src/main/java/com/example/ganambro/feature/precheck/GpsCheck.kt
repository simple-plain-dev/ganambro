package com.example.ganambro.feature.precheck

/**
 * Checks that GPS / location services are enabled.
 *
 * Platform dependency (caller supplies): function that returns true when location is on.
 */
class GpsCheck(
    private val isGpsOn: suspend () -> Boolean,
) : Precheck {
    override val name = "GPS"

    override suspend fun check(): PrecheckResult {
        return if (isGpsOn()) PrecheckResult.Pass
        else PrecheckResult.Fail("GPS harus diaktifkan")
    }
}
