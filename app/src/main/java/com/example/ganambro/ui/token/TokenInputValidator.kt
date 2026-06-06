package com.example.ganambro.ui.token

/**
 * Pure Kotlin validation for 6-character alphanumeric token input.
 * Handles input filtering, uppercasing, and completeness check.
 */
class TokenInputValidator {

    /** Check if a single character is valid (A-Z, 0-9). */
    fun isValidChar(char: Char): Boolean =
        char in 'A'..'Z' || char in 'a'..'z' || char in '0'..'9'

    /** Normalize a character to uppercase. */
    fun normalizeChar(char: Char): Char = char.uppercaseChar()

    /** Check if exactly 6 characters are filled. */
    fun isComplete(chars: List<Char>): Boolean =
        chars.size == 6 && chars.all { it != '\u0000' }

    /** Build a 6-char uppercase string from the character list. */
    fun buildToken(chars: List<Char>): String =
        chars.joinToString("") { it.uppercase() }
}
