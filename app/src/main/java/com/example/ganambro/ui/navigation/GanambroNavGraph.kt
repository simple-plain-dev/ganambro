package com.example.ganambro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ganambro.feature.precheck.PrecheckRegistry
import com.example.ganambro.ui.screens.LoginScreen
import com.example.ganambro.ui.screens.MenuScreen
import com.example.ganambro.ui.screens.SplashScreen
import com.example.ganambro.ui.screens.SplashViewModel

@Composable
fun GanambroNavGraph(
    navController: NavHostController,
    precheckRegistry: PrecheckRegistry,
    onExitApp: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Route.Splash.path,
    ) {
        composable(Route.Splash.path) {
            SplashScreen(
                viewModel = SplashViewModel(precheckRegistry),
                onNavigateToLogin = {
                    navController.navigate(Route.Login.path) {
                        popUpTo(Route.Splash.path) { inclusive = true }
                    }
                },
                onExit = onExitApp,
            )
        }

        composable(Route.Login.path) {
            LoginScreen(
                onSkip = {
                    navController.navigate(Route.Menu.path) {
                        popUpTo(Route.Login.path) { inclusive = true }
                    }
                },
            )
        }

        composable(Route.Menu.path) {
            MenuScreen(
                onPortalUjian = {
                    navController.navigate(Route.PortalUjian.path)
                },
                onPetunjuk = {
                    navController.navigate(Route.Petunjuk.path)
                },
            )
        }

        composable(Route.Petunjuk.path) {
            // Placeholder — Slice 4
            androidx.compose.material3.Text("Petunjuk (Slice 4)")
        }

        composable(Route.PortalUjian.path) {
            // Placeholder — Slice 5a
            androidx.compose.material3.Text("Portal Ujian (Slice 5a)")
        }

        composable(Route.MenuPengawas.path) {
            // Placeholder — Slice 2
            androidx.compose.material3.Text("Menu Pengawas (Slice 2)")
        }

        composable(Route.ModeUjian.path) {
            // Placeholder — Slice 5b
            androidx.compose.material3.Text("Mode Ujian (Slice 5b)")
        }
    }
}
