package com.example.ganambro.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ganambro.BuildConfig
import com.example.ganambro.feature.token.Token

@Composable
fun MenuPengawasScreen(
    onBackToMenu: () -> Unit,
) {
    BackHandler { /* blocked — must use explicit Kembali button */ }

    var pin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }
    var isUnlocked by remember { mutableStateOf(false) }
    var generatedToken by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!isUnlocked) {
            // ── PIN input phase ──
            Text(
                text = "Menu Pengawas",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            OutlinedTextField(
                value = pin,
                onValueChange = {
                    if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                        pin = it
                        pinError = null
                    }
                },
                label = { Text("PIN 6-digit") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = pinError != null,
            )

            if (pinError != null) {
                Text(
                    text = pinError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (pin == BuildConfig.TEACHER_PIN) {
                        isUnlocked = true
                        pinError = null
                    } else {
                        pinError = "PIN salah"
                        pin = ""
                    }
                },
                enabled = pin.length == 6,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Masuk")
            }
        } else {
            // ── Token generator phase ──
            Text(
                text = "Token Ujian",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            if (generatedToken != null) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                ) {
                    Text(
                        text = generatedToken!!,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }

            Button(
                onClick = {
                    generatedToken = Token.generate(
                        schoolName = BuildConfig.SCHOOL_NAME,
                        appName = BuildConfig.APP_NAME,
                        appVersion = BuildConfig.APP_VERSION,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Generate Token")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onBackToMenu,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Kembali")
        }
    }
}
