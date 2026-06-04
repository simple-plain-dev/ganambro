package com.example.ganambro.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ganambro.feature.precheck.PrecheckRegistry

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToLogin: () -> Unit,
    onExit: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isComplete) {
        if (uiState.isComplete && !uiState.hasError) {
            onNavigateToLogin()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0C0C))
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopStart),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            uiState.lines.forEach { line ->
                AnimatedVisibility(visible = true, enter = fadeIn()) {
                    Text(
                        text = when (line.status) {
                            TerminalStatus.RUNNING -> "${line.text} ⏳"
                            TerminalStatus.OK -> "${line.text} ✅"
                            TerminalStatus.FAIL -> "${line.text} ❌"
                        },
                        color = when (line.status) {
                            TerminalStatus.RUNNING -> Color(0xFFB0B0B0)
                            TerminalStatus.OK -> Color(0xFF00FF00)
                            TerminalStatus.FAIL -> Color(0xFFFF4444)
                        },
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }
        }

        // Error state with exit button
        if (uiState.hasError && uiState.isComplete) {
            Column(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = uiState.errorMessage ?: "Prasyarat tidak terpenuhi",
                    color = Color(0xFFFF4444),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                )
                Button(
                    onClick = onExit,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF444444))
                ) {
                    Text("Keluar", fontFamily = FontFamily.Monospace, color = Color.White)
                }
            }
        }
    }
}
