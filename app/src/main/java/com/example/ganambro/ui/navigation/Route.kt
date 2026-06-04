package com.example.ganambro.ui.navigation

/**
 * Route definitions for the Ganambro navigation graph.
 */
sealed class Route(val path: String) {
    data object Splash : Route("splash")
    data object Login : Route("login")
    data object Menu : Route("menu")
    data object Petunjuk : Route("petunjuk")
    data object PortalUjian : Route("portal_ujian")
    data object MenuPengawas : Route("menu_pengawas")
    data object ModeUjian : Route("mode_ujian")
}
