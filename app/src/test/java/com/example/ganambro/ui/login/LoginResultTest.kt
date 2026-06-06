package com.example.ganambro.ui.login

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LoginResultTest {

    @Test
    fun `Sukses carries account identifier`() {
        val result = LoginResult.Sukses("peserta@sekolah.id")
        assertTrue(result is LoginResult.Sukses)
        assertEquals("peserta@sekolah.id", result.akun)
    }

    @Test
    fun `Anonim is singleton object`() {
        val result = LoginResult.Anonim
        assertTrue(result is LoginResult.Anonim)
    }

    @Test
    fun `Sukses and Anonim are distinct`() {
        val sukses: LoginResult = LoginResult.Sukses("test@test.com")
        val anonim: LoginResult = LoginResult.Anonim
        assertTrue(sukses != anonim)
    }
}
