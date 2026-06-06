package com.example.ganambro.ui.token

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Halaman input Token 6-digit alphanumeric.
 *
 * Auto-uppercase, filter non-alphanumeric. Validasi via TokenGenerator.
 * Retry unlimited — tidak ada lockout.
 */
@Composable
fun TokenInputScreen(
    onTokenValid: (String) -> Unit,
    validateToken: (String) -> Boolean,
    onBack: () -> Unit,
) {
    val validator = remember { TokenInputValidator() }
    val chars = remember { mutableStateListOf('\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000') }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Masukkan Token",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00FF88),
            fontFamily = FontFamily.Monospace,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Minta Token 6-digit dari Pengawas",
            fontSize = 13.sp,
            color = Color.Gray,
            fontFamily = FontFamily.Monospace,
        )

        Spacer(Modifier.height(32.dp))

        // 6 character input fields
        Row(
            horizontalArrangement = Arrangement.Center,
        ) {
            for (i in 0..5) {
                OutlinedTextField(
                    value = if (chars[i] == '\u0000') "" else chars[i].toString(),
                    onValueChange = { newValue ->
                        val input = newValue.lastOrNull()
                        if (input != null && validator.isValidChar(input)) {
                            chars[i] = validator.normalizeChar(input)
                            errorMessage = null
                            // Auto-advance to next field
                            if (i < 5) {
                                // focus handled by caller via focus requesters (MVP: simple)
                            }
                        } else if (newValue.isEmpty()) {
                            chars[i] = '\u0000'
                            errorMessage = null
                        }
                    },
                    modifier = Modifier.size(56.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        keyboardType = KeyboardType.Ascii,
                    ),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1A1A1A),
                        unfocusedContainerColor = Color(0xFF1A1A1A),
                        focusedIndicatorColor = Color(0xFF00FF88),
                        unfocusedIndicatorColor = Color.Gray,
                    ),
                )
                if (i < 5) Spacer(Modifier.width(8.dp))
            }
        }

        Spacer(Modifier.height(24.dp))

        // Error message
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
            )
            Spacer(Modifier.height(8.dp))
        }

        // Submit button
        Button(
            onClick = {
                val token = validator.buildToken(chars.filter { it != '\u0000' })
                if (token.length == 6) {
                    if (validateToken(token)) {
                        onTokenValid(token)
                    } else {
                        errorMessage = "Token tidak valid — coba lagi"
                    }
                } else {
                    errorMessage = "Masukkan 6 karakter"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            enabled = validator.isComplete(chars.filter { it != '\u0000' }),
        ) {
            Text("Masuk", fontSize = 18.sp)
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color.Gray,
            ),
        ) {
            Text("Kembali ke Menu")
        }
    }
}
