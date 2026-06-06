package com.example.ganambro.ui.token

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TokenInputValidatorTest {

    private val validator = TokenInputValidator()

    @Test
    fun `valid alphanumeric input is accepted`() {
        assertTrue(validator.isValidChar('A'))
        assertTrue(validator.isValidChar('Z'))
        assertTrue(validator.isValidChar('0'))
        assertTrue(validator.isValidChar('9'))
    }

    @Test
    fun `non-alphanumeric input is rejected`() {
        assertFalse(validator.isValidChar('!'))
        assertFalse(validator.isValidChar(' '))
        assertFalse(validator.isValidChar('.'))
        assertFalse(validator.isValidChar('-'))
    }

    @Test
    fun `lowercase letters are converted to uppercase`() {
        assertEquals('A', validator.normalizeChar('a'))
        assertEquals('Z', validator.normalizeChar('z'))
        assertEquals('5', validator.normalizeChar('5'))
    }

    @Test
    fun `6 characters all filled returns true`() {
        assertTrue(validator.isComplete(listOf('A', 'B', 'C', '1', '2', '3')))
    }

    @Test
    fun `less than 6 characters returns false`() {
        assertFalse(validator.isComplete(listOf('A', 'B', 'C', '1', '2')))
        assertFalse(validator.isComplete(listOf()))
    }

    @Test
    fun `buildToken joins chars into uppercase string`() {
        val chars = listOf('a', 'B', 'c', '1', 'X', '9')
        assertEquals("ABC1X9", validator.buildToken(chars))
    }
}
