package com.example.ganambro.feature.precheck

/**
 * Result of a single precheck — owned by the check module, not by the caller.
 * Callers read [Fail.message] directly to display the error.
 */
sealed class PrecheckResult {
    /** Check passed. */
    data object Pass : PrecheckResult()

    /** Check failed. [message] is human-readable and should be displayed to Peserta. */
    data class Fail(val message: String) : PrecheckResult()
}

/**
 * A single preflight check run before Ujian.
 *
 * Each check owns its failure message — adding a new check requires zero changes
 * in the caller (SplashScreen).
 */
interface Precheck {
    /** Human-readable name, e.g. "Internet", "GPS", "Bluetooth". */
    val name: String

    /** Run the check. Must not throw — return [PrecheckResult.Fail] on failure. */
    suspend fun check(): PrecheckResult
}
