package com.example.ganambro.feature.precheck

/**
 * Checks that internet connectivity is available.
 *
 * Platform dependency (caller supplies): function that returns true when online.
 */
class InternetCheck(
    private val isOnline: suspend () -> Boolean,
) : Precheck {
    override val name = "Internet"

    override suspend fun check(): PrecheckResult {
        return if (isOnline()) PrecheckResult.Pass
        else PrecheckResult.Fail("Internet harus aktif")
    }
}
