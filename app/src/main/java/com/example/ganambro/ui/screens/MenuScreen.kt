package com.example.ganambro.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun MenuScreen(
    onPortalUjian: () -> Unit,
    onPetunjuk: () -> Unit,
    onLogoClick: () -> Unit = {}, // gesture hook for Slice 2
    onPetunjukButtonClick: () -> Unit = {}, // gesture hook for Slice 2
) {
    val activity = LocalContext.current as Activity

    BackHandler { activity.finishAffinity() }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Logo — tappable for hidden gesture (Slice 2)
        Text(
            text = "Ganambro",
            style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 48.dp),
        )

        Button(onClick = onPortalUjian, modifier = Modifier.fillMaxWidth()) {
            Text("Portal Ujian")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            onPetunjukButtonClick()
            onPetunjuk()
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Petunjuk")
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = { activity.finishAffinity() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Keluar")
        }
    }
}
