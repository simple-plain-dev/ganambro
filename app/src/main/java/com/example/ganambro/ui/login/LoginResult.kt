package com.example.ganambro.ui.login

/** Outcome of the login screen. */
sealed class LoginResult {
    data class Sukses(val akun: String?) : LoginResult()
    data object Anonim : LoginResult()
}
