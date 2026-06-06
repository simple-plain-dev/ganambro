package com.example.ganambro.ui.pengawas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Halaman Pengawas dengan two-step akses:
 * 1. PIN 6-digit numerik (3× percobaan)
 * 2. Token display dengan countdown real-time
 */
@Composable
fun PengawasScreen(
    correctPin: String,
    tokenGenerator: () -> String,
    tokenWindowMinutes: Int,
    onBack: () -> Unit,
) {
    var pinAttempts by remember { mutableStateOf(0) }
    var isPinCorrect by remember { mutableStateOf(false) }
    var pinError by remember { mutableStateOf<String?>(null) }
    val pinChars = remember { mutableStateListOf('\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000') }

    var currentToken by remember { mutableStateOf("") }
    var countdownSeconds by remember { mutableStateOf(0) }
    var showGenerate by remember { mutableStateOf(false) }

    // Countdown timer
    LaunchedEffect(isPinCorrect, currentToken) {
        if (isPinCorrect) {
            val totalSeconds = tokenWindowMinutes * 60
            countdownSeconds = totalSeconds
            while (countdownSeconds > 0) {
                delay(1000)
                countdownSeconds--
            }
            showGenerate = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (!isPinCorrect) {
            // === PIN Entry ===
            Text(
                text = "Akses Pengawas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFA500),
                fontFamily = FontFamily.Monospace,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Masukkan PIN 6-digit",
                fontSize = 13.sp,
                color = Color.Gray,
                fontFamily = FontFamily.Monospace,
            )
            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.Center) {
                for (i in 0..5) {
                    OutlinedTextField(
                        value = if (pinChars[i] == '\u0000') "" else pinChars[i].toString(),
                        onValueChange = { newValue ->
                            val input = newValue.lastOrNull()
                            if (input != null && input in '0'..'9') {
                                pinChars[i] = input
                                pinError = null
                            } else if (newValue.isEmpty()) {
                                pinChars[i] = '\u0000'
                                pinError = null
                            }
                        },
                        modifier = Modifier.size(52.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 22.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White,
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1A1A1A),
                            unfocusedContainerColor = Color(0xFF1A1A1A),
                            focusedIndicatorColor = Color(0xFFFFA500),
                            unfocusedIndicatorColor = Color.Gray,
                        ),
                    )
                    if (i < 5) Spacer(Modifier.width(6.dp))
                }
            }

            Spacer(Modifier.height(16.dp))

            if (pinError != null) {
                Text(text = pinError!!, color = Color.Red, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    val enteredPin = pinChars.filter { it != '\u0000' }.joinToString("")
                    if (enteredPin == correctPin) {
                        isPinCorrect = true
                        currentToken = tokenGenerator()
                    } else {
                        pinAttempts++
                        if (pinAttempts >= 3) {
                            onBack()
                        } else {
                            pinError = "PIN salah — ${3 - pinAttempts}x lagi"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)),
            ) {
                Text("Masuk", fontSize = 16.sp, color = Color.Black)
            }
        } else {
            // === Token Display ===
            Text(
                text = "Token Aktif",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = FontFamily.Monospace,
            )
            Spacer(Modifier.height(8.dp))

            if (currentToken.isNotEmpty()) {
                Text(
                    text = currentToken,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00FF88),
                    fontFamily = FontFamily.Monospace,
                )
            } else {
                Text(
                    text = "Token kadaluarsa",
                    fontSize = 18.sp,
                    color = Color.Red,
                    fontFamily = FontFamily.Monospace,
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Countdown: ${countdownSeconds / 60}:${(countdownSeconds % 60).toString().padStart(2, '0')}",
                fontSize = 18.sp,
                color = if (countdownSeconds < 60) Color.Red else Color.White,
                fontFamily = FontFamily.Monospace,
            )

            Spacer(Modifier.height(24.dp))

            if (showGenerate) {
                Button(
                    onClick = {
                        currentToken = tokenGenerator()
                        countdownSeconds = tokenWindowMinutes * 60
                        showGenerate = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF88)),
                ) {
                    Text("Generate Token Baru", fontSize = 16.sp, color = Color.Black)
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            ) {
                Text("Kembali ke Menu")
            }
        }
    }
}
