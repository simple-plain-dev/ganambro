package com.example.ganambro.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onSkip: () -> Unit,
    // onLogin will be implemented in Slice 3 (Chrome Custom Tab)
) {
    BackHandler { /* blocked — cannot go back to Splash */ }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Ganambro",
            style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = { /* Slice 3 */ },
            enabled = false, // disabled until Slice 3
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Login dengan Google")
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = onSkip,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Lewati")
        }
    }
}
