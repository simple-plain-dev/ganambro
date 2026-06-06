package com.example.ganambro.nav

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NavStateTest {

    // --- Legal transitions ---

    @Test
    fun `Splash can transition to Login`() {
        val result = NavState.Splash.transitionTo(NavState.Login)
        assertSame(NavState.Login, result)
    }

    @Test
    fun `Splash can transition to Exit`() {
        val result = NavState.Splash.transitionTo(NavState.Exit)
        assertSame(NavState.Exit, result)
    }

    @Test
    fun `Login can transition to Menu`() {
        val result = NavState.Login.transitionTo(NavState.Menu)
        assertSame(NavState.Menu, result)
    }

    @Test
    fun `Menu can transition to Ujian with valid token`() {
        val ujian = NavState.Ujian("ABC123")
        val result = NavState.Menu.transitionTo(ujian)
        assertEquals(ujian, result)
    }

    @Test
    fun `Menu can transition to Pengawas`() {
        val result = NavState.Menu.transitionTo(NavState.Pengawas)
        assertSame(NavState.Pengawas, result)
    }

    @Test
    fun `Menu can transition to Exit`() {
        val result = NavState.Menu.transitionTo(NavState.Exit)
        assertSame(NavState.Exit, result)
    }

    @Test
    fun `Ujian can transition to Menu`() {
        val result = NavState.Ujian("ABC123").transitionTo(NavState.Menu)
        assertSame(NavState.Menu, result)
    }

    @Test
    fun `Pengawas can transition to Menu`() {
        val result = NavState.Pengawas.transitionTo(NavState.Menu)
        assertSame(NavState.Menu, result)
    }

    // --- Illegal transitions ---

    @Test
    fun `Login cannot transition back to Splash`() {
        val result = NavState.Login.transitionTo(NavState.Splash)
        assertSame(NavState.Login, result)
    }

    @Test
    fun `Splash cannot transition directly to Menu`() {
        val result = NavState.Splash.transitionTo(NavState.Menu)
        assertSame(NavState.Splash, result)
    }

    @Test
    fun `Splash cannot transition directly to Ujian`() {
        val result = NavState.Splash.transitionTo(NavState.Ujian("ABC123"))
        assertSame(NavState.Splash, result)
    }

    @Test
    fun `Login cannot transition directly to Ujian`() {
        val result = NavState.Login.transitionTo(NavState.Ujian("ABC123"))
        assertSame(NavState.Login, result)
    }

    @Test
    fun `Menu cannot transition to Ujian without token`() {
        val result = NavState.Menu.transitionTo(NavState.Ujian(""))
        assertSame(NavState.Menu, result)
    }

    @Test
    fun `Menu cannot transition to Ujian with blank token`() {
        val result = NavState.Menu.transitionTo(NavState.Ujian("   "))
        assertSame(NavState.Menu, result)
    }

    @Test
    fun `Ujian cannot transition to Splash`() {
        val ujian = NavState.Ujian("ABC123")
        val result = ujian.transitionTo(NavState.Splash)
        assertEquals(ujian, result)
    }

    @Test
    fun `Pengawas cannot transition directly to Ujian`() {
        val result = NavState.Pengawas.transitionTo(NavState.Ujian("ABC123"))
        assertTrue(result is NavState.Pengawas)
    }

    @Test
    fun `Login cannot transition to Pengawas`() {
        val result = NavState.Login.transitionTo(NavState.Pengawas)
        assertSame(NavState.Login, result)
    }
}
