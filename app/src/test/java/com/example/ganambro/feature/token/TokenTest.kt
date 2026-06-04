package com.example.ganambro.feature.token

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TokenTest {

    private val school = "SMA N 1"
    private val app = "Ganambro"
    private val version = "1.0"

    @Test
    fun `generate produces consistent uppercase hex within same 10-minute window`() {
        val fixedTimestamp = 1_000_000L

        val token1 = Token.generate(school, app, version, fixedTimestamp)
        val token2 = Token.generate(school, app, version, fixedTimestamp)

        assertEquals(token1, token2, "same inputs → same token")
        assertTrue(token1.all { it in '0'..'9' || it in 'A'..'F' }, "must be uppercase hex")
        assertEquals(8, token1.length, "token is 8 hex chars (first 8 of SHA-256)")
    }

    @Test
    fun `validate accepts token from current window`() {
        val now = 1_000_000L
        val token = Token.generate(school, app, version, now)
        assertTrue(Token.validate(token, school, app, version, now))
    }

    @Test
    fun `validate rejects token from different 10-minute window`() {
        val window1 = 1_000_000L
        val window2 = 1_000_000L + 600 // next window
        val token = Token.generate(school, app, version, window1)
        assertFalse(Token.validate(token, school, app, version, window2))
    }

    @Test
    fun `validate rejects garbage input`() {
        assertFalse(Token.validate("not-a-token", school, app, version, 1_000_000L))
    }

    @Test
    fun `different salt produces different token`() {
        val token1 = Token.generate("School A", app, version, 1_000_000L)
        val token2 = Token.generate("School B", app, version, 1_000_000L)
        assertNotEquals(token1, token2)
    }
}
