package com.example.ganambro.precheck

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/** Result of a single precheck step. */
sealed class CheckResult {
    data class InProgress(val checkName: String) : CheckResult()
    data class Passed(val checkName: String) : CheckResult()
    data class Failed(val checkName: String, val reason: String) : CheckResult()
}

/**
 * Adapter interface for individual device checks.
 * Hides Android API specifics behind a single [check] call.
 */
interface Checker {
    val name: String
    suspend fun check(): Boolean
    fun failureReason(): String
}

/**
 * Runs a sequential pipeline of [Checker]s, emitting [CheckResult] via a [Flow].
 * Execution stops at the first failure — remaining checkers are skipped.
 */
class PrecheckRunner(private val checkers: List<Checker>) {

    suspend fun runChecks(): Flow<CheckResult> = flow {
        for (checker in checkers) {
            emit(CheckResult.InProgress(checker.name))
            if (checker.check()) {
                emit(CheckResult.Passed(checker.name))
            } else {
                emit(CheckResult.Failed(checker.name, checker.failureReason()))
                return@flow
            }
        }
    }
}
