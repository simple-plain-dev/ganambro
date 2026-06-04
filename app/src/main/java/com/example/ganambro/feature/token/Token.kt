package com.example.ganambro.feature.token

import java.security.MessageDigest

/**
 * Single module for Token generation and validation.
 *
 * Rounding logic, salt assembly, and SHA-256 are implementation details —
 * callers never see them.
 *
 * Usage:
 *   val token = Token.generate()
 *   val valid = Token.validate(userInput)
 */
object Token {

    /**
     * Generate a Token valid for the current 10-minute window.
     * Token = first 8 chars of SHA-256(SCHOOL_NAME + APP_NAME + APP_VERSION + roundedTimestamp)
     */
    fun generate(
        schoolName: String,
        appName: String,
        appVersion: String,
        timestampSeconds: Long = System.currentTimeMillis() / 1000,
    ): String {
        val salt = "$schoolName$appName$appVersion"
        val rounded = roundToWindow(timestampSeconds)
        return sha256("$salt$rounded").take(8)
    }

    /**
     * Validate a user-provided token against the current 10-minute window.
     * Returns true if the token matches.
     */
    fun validate(
        input: String,
        schoolName: String,
        appName: String,
        appVersion: String,
        timestampSeconds: Long = System.currentTimeMillis() / 1000,
    ): Boolean {
        return input == generate(schoolName, appName, appVersion, timestampSeconds)
    }

    // ── implementation ──

    private fun roundToWindow(epochSeconds: Long): Long {
        val windowSize = 600L // 10 minutes
        return (epochSeconds / windowSize) * windowSize
    }

    private fun sha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(input.toByteArray())
        return bytes.joinToString("") { "%02X".format(it) }
    }
}
