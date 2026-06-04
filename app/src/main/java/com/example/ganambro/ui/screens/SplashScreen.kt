package com.example.ganambro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToLogin: () -> Unit,
    onExit: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        when (val s = state) {
            is SplashViewModel.UiState.AllPassed -> onNavigateToLogin()
            else -> {} // wait
        }
    }

    LaunchedEffect(Unit) {
        viewModel.startChecks()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0C0C))
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopStart),
        ) {
            when (val s = state) {
                is SplashViewModel.UiState.Checking -> {
                    // Show completed checks
                    s.done.forEach { name ->
                        Text(
                            text = "> Memeriksa $name... ✅",
                            color = Color(0xFF00FF00),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                        )
                    }
                    // Show current check
                    Text(
                        text = "> Memeriksa ${s.label}... ⏳",
                        color = Color(0xFFB0B0B0),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                    )
                }
                is SplashViewModel.UiState.AllPassed -> {
                    s.messages.forEach { name ->
                        Text(
                            text = "> Memeriksa $name... ✅",
                            color = Color(0xFF00FF00),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "> Memulai Ganambro...",
                        color = Color(0xFF00FF00),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                    )
                }
                is SplashViewModel.UiState.Failed -> {
                    s.messages.forEach { name ->
                        Text(
                            text = "> Memeriksa $name... ❌",
                            color = Color(0xFFFF4444),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                        )
                    }
                }
            }
        }

        // Error state with exit button
        if (state is SplashViewModel.UiState.Failed) {
            val failed = state as SplashViewModel.UiState.Failed
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = failed.errorMessage,
                    color = Color(0xFFFF4444),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onExit,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF444444)),
                ) {
                    Text("Keluar", fontFamily = FontFamily.Monospace, color = Color.White)
                }
            }
        }
    }
}
