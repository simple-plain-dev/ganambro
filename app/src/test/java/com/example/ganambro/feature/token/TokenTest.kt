package com.example.ganambro.feature.token

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TokenTest {

    @Test
    fun `generate produces 64-char uppercase hex`() {
        val token = Token.generate("SMAN1", "Ganambro", "1.0", 0L)

        assertEquals(64, token.length, "Token must be 64 characters (SHA-256 hex)")
        assertTrue(token.all { it in '0'..'9' || it in 'A'..'F' }, "Token must be uppercase hex")
    }

    @Test
    fun `generate with same inputs at same timestamp produces identical token`() {
        val a = Token.generate("SMAN1", "Ganambro", "1.0", 100L)
        val b = Token.generate("SMAN1", "Ganambro", "1.0", 100L)

        assertEquals(a, b)
    }

    @Test
    fun `generate rounds timestamp to 10-minute window`() {
        // Seconds 100, 200, 599 all round down to 0 (within window [0, 600))
        val t0 = Token.generate("S", "A", "1", 100L)
        val t1 = Token.generate("S", "A", "1", 200L)
        val t2 = Token.generate("S", "A", "1", 599L)

        assertEquals(t0, t1)
        assertEquals(t0, t2)
    }

    @Test
    fun `generate produces different token across window boundaries`() {
        val inWindow0 = Token.generate("S", "A", "1", 599L)  // rounds to 0
        val inWindow1 = Token.generate("S", "A", "1", 600L)  // rounds to 600

        // These should be different tokens
        assertTrue(inWindow0 != inWindow1, "Tokens across window boundaries must differ")
    }

    @Test
    fun `validate returns true for matching token`() {
        val token = Token.generate("X", "Y", "2", 500L)

        val result = Token.validate(token, "X", "Y", "2", 500L)

        assertEquals(true, result)
    }

    @Test
    fun `validate returns false for token from different window`() {
        val token = Token.generate("X", "Y", "2", 500L) // window [0, 600)

        val result = Token.validate(token, "X", "Y", "2", 700L) // window [600, 1200)

        assertEquals(false, result)
    }

    @Test
    fun `validate returns false for wrong input`() {
        val result = Token.validate("wrong-token", "X", "Y", "2", 0L)

        assertEquals(false, result)
    }

    @Test
    fun `different school produces different token`() {
        val schoolA = Token.generate("SMAN1", "Ganambro", "1.0", 0L)
        val schoolB = Token.generate("SMAN2", "Ganambro", "1.0", 0L)

        assertTrue(schoolA != schoolB, "Different school names must produce different tokens")
    }
}
