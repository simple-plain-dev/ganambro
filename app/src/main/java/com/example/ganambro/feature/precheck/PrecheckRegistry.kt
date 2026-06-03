package com.example.ganambro.feature.precheck

/**
 * Collects registered [Precheck] modules and runs them in order.
 *
 * Adding a new check (e.g. BatteryCheck) requires:
 * 1. Create the check file implementing [Precheck]
 * 2. Register it — zero changes to SplashScreen or any other file.
 */
class PrecheckRegistry {
    private val checks = mutableListOf<Precheck>()

    /** Register a check to be run when [runAll] is called. */
    fun register(check: Precheck) {
        checks.add(check)
    }

    /**
     * Run all registered checks sequentially.
     * Stops at the first failure.
     *
     * @return list of [PrecheckResult] — each entry corresponds to one check.
     */
    suspend fun runAll(): List<PrecheckResult> {
        val results = mutableListOf<PrecheckResult>()
        for (check in checks) {
            val result = check.check()
            results.add(result)
            if (result is PrecheckResult.Fail) break // stop at first failure
        }
        return results
    }
}
