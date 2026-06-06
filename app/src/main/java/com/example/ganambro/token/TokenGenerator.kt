package com.example.ganambro.token

import java.security.MessageDigest

/**
 * Pure Kotlin token generator — no Android dependencies.
 *
 * Formula: SHA-256(salt + floorDiv(utcMillis, windowMs)) → first 6 hex chars uppercase.
 * Tokens are consistent within a Time Window and change when the window advances.
 *
 * See ADR-0001: local token validation, no backend.
 */
object TokenGenerator {

    fun generate(salt: String, timeWindowMinutes: Int): String =
        generateAt(salt, timeWindowMinutes, System.currentTimeMillis())

    internal fun generateAt(salt: String, timeWindowMinutes: Int, currentTimeMillis: Long): String {
        val windowMs = timeWindowMinutes * 60 * 1000L
        val windowTimestamp = (currentTimeMillis / windowMs) * windowMs
        val input = salt + windowTimestamp.toString()
        val digest = MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
            .take(6)
            .uppercase()
    }

    fun validate(token: String, salt: String, timeWindowMinutes: Int): Boolean =
        validateAt(token, salt, timeWindowMinutes, System.currentTimeMillis())

    internal fun validateAt(
        token: String,
        salt: String,
        timeWindowMinutes: Int,
        currentTimeMillis: Long,
    ): Boolean {
        val expected = generateAt(salt, timeWindowMinutes, currentTimeMillis)
        return token == expected
    }
}
