package com.example.ganambro.ui.splash

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ganambro.precheck.CheckResult
import com.example.ganambro.precheck.PrecheckRunner

/**
 * Splash screen showing Precheck progress with terminal-like animation.
 *
 * Observes [PrecheckRunner.runChecks] Flow — does not know about Android APIs.
 * Shows a "Lanjut" button when all checks pass, or "Keluar" when one fails.
 */
@Composable
fun SplashScreen(
    precheckRunner: PrecheckRunner,
    onNavigateToLogin: () -> Unit,
    onExit: () -> Unit,
) {
    val context = LocalContext.current
    val logLines = remember { mutableStateListOf<DisplayLine>() }
    var showContinue by remember { mutableStateOf(false) }
    var showExit by remember { mutableStateOf(false) }
    var lastFailedReason by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        precheckRunner.runChecks().collect { result ->
            when (result) {
                is CheckResult.InProgress -> {
                    logLines.add(DisplayLine("⏳ Memeriksa ${result.checkName}...", Color.White))
                }
                is CheckResult.Passed -> {
                    // Replace the InProgress line for this checker
                    val idx = logLines.indexOfLast { it.text.contains(result.checkName) }
                    if (idx >= 0) logLines[idx] = DisplayLine("✅ ${result.checkName}: OK", Color.Green)
                }
                is CheckResult.Failed -> {
                    val idx = logLines.indexOfLast { it.text.contains(result.checkName) }
                    if (idx >= 0) logLines[idx] = DisplayLine("❌ ${result.checkName}: GAGAL", Color.Red)
                    logLines.add(DisplayLine("   → ${result.reason}", Color.Yellow))
                    lastFailedReason = result.reason
                    showExit = true
                }
            }
        }
        // Flow completed successfully (all passed)
        if (!showExit) {
            logLines.add(DisplayLine("", Color.White))
            logLines.add(DisplayLine("✅ Semua pengecekan berhasil!", Color.Green))
            showContinue = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0C0C))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Text(
                text = "Ganambro Precheck",
                color = Color(0xFF00FF88),
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace,
            )
            Text(
                text = "──────────────────────────────",
                color = Color.Gray,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
            )
            Spacer(Modifier.height(8.dp))

            // Terminal log lines
            logLines.forEach { line ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                ) {
                    Text(
                        text = line.text,
                        color = line.color,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Continue button (on success)
            AnimatedVisibility(visible = showContinue) {
                Button(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00FF88),
                        contentColor = Color.Black,
                    ),
                ) {
                    Text("Lanjut →")
                }
            }

            // Exit button (on failure)
            AnimatedVisibility(visible = showExit) {
                Column {
                    Button(
                        onClick = onExit,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White,
                        ),
                    ) {
                        Text("Keluar")
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Perbaiki masalah di atas, lalu buka ulang aplikasi.",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                    )
                }
            }
        }
    }
}

/** A single line rendered in the terminal output. */
data class DisplayLine(val text: String, val color: Color)
