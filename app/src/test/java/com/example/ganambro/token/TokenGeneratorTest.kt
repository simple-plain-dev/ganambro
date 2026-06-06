package com.example.ganambro.token

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class TokenGeneratorTest {

    // --- generate ---

    @Test
    fun `token is consistent within same time window`() {
        val salt = "SMA1Ganambro1.0"
        // Window-aligned: time=0 is exactly a window boundary
        val windowMs = 10 * 60 * 1000L // 10 minutes

        val token1 = TokenGenerator.generateAt(salt, 10, 0)
        val token2 = TokenGenerator.generateAt(salt, 10, 30_000) // +30 sec, same window
        val token3 = TokenGenerator.generateAt(salt, 10, windowMs - 1) // just before next window

        assertEquals(token1, token2)
        assertEquals(token2, token3)
    }

    @Test
    fun `token differs across different time windows`() {
        val salt = "SMA1Ganambro1.0"
        val windowMs = 10 * 60 * 1000L

        val token1 = TokenGenerator.generateAt(salt, 10, 0)
        val token2 = TokenGenerator.generateAt(salt, 10, windowMs)

        assertNotEquals(token1, token2)
    }

    @Test
    fun `token just before window boundary differs from token in next window`() {
        val salt = "SMA1Ganambro1.0"
        // Window boundary aligned: time=0 is start of window 0
        val windowMs = 10 * 60 * 1000L

        val endOfWindow0 = windowMs - 1   // last ms of window 0
        val startOfWindow1 = windowMs      // first ms of window 1

        val tokenWindow0 = TokenGenerator.generateAt(salt, 10, endOfWindow0)
        val tokenWindow1 = TokenGenerator.generateAt(salt, 10, startOfWindow1)

        assertNotEquals(tokenWindow0, tokenWindow1)
    }

    @Test
    fun `token is always 6 characters`() {
        val token = TokenGenerator.generateAt("salt", 10, System.currentTimeMillis())
        assertEquals(6, token.length)
    }

    @Test
    fun `token contains only alphanumeric uppercase characters`() {
        val token = TokenGenerator.generateAt("salt", 10, System.currentTimeMillis())
        assertTrue(token.all { it in 'A'..'Z' || it in '0'..'9' })
    }

    @Test
    fun `token is always uppercase`() {
        val token = TokenGenerator.generateAt("TestSalt", 10, System.currentTimeMillis())
        assertEquals(token, token.uppercase())
    }

    @Test
    fun `different salt produces different token`() {
        val token1 = TokenGenerator.generateAt("SMA1", 10, 0)
        val token2 = TokenGenerator.generateAt("SMA2", 10, 0)
        assertNotEquals(token1, token2)
    }

    // --- validate ---

    @Test
    fun `validate returns true for valid token`() {
        val salt = "SMA1Ganambro1.0"
        val token = TokenGenerator.generateAt(salt, 10, 0)
        assertTrue(TokenGenerator.validateAt(token, salt, 10, 0))
    }

    @Test
    fun `validate returns false for invalid token`() {
        val salt = "SMA1Ganambro1.0"
        assertFalse(TokenGenerator.validateAt("XXXXXX", salt, 10, 0))
    }

    @Test
    fun `validate returns false for wrong salt`() {
        val token = TokenGenerator.generateAt("SaltA", 10, 0)
        assertFalse(TokenGenerator.validateAt(token, "SaltB", 10, 0))
    }

    @Test
    fun `token from previous window fails validation in current window`() {
        val salt = "SMA1Ganambro1.0"
        val windowMs = 10 * 60 * 1000L

        val tokenWindow0 = TokenGenerator.generateAt(salt, 10, 0)
        assertFalse(TokenGenerator.validateAt(tokenWindow0, salt, 10, windowMs))
    }
}
