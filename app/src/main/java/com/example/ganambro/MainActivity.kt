package com.example.ganambro

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.ganambro.precheck.PrecheckRunner
import com.example.ganambro.precheck.checkers.BluetoothChecker
import com.example.ganambro.precheck.checkers.GpsChecker
import com.example.ganambro.precheck.checkers.HeadsetChecker
import com.example.ganambro.precheck.checkers.InternetChecker
import com.example.ganambro.precheck.checkers.VolumeChecker
import com.example.ganambro.ui.splash.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App(activity = this)
        }
    }
}

private enum class Screen { Splash, PlaceholderLogin }

@Composable
fun App(activity: Activity) {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }

    Surface(modifier = Modifier.fillMaxSize()) {
        when (currentScreen) {
            Screen.Splash -> {
                val precheckRunner = PrecheckRunner(
                    listOf(
                        InternetChecker(context),
                        GpsChecker(context),
                        BluetoothChecker(),
                        HeadsetChecker(context),
                        VolumeChecker(context),
                    )
                )
                SplashScreen(
                    precheckRunner = precheckRunner,
                    onNavigateToLogin = { currentScreen = Screen.PlaceholderLogin },
                    onExit = { activity.finishAffinity() },
                )
            }
            Screen.PlaceholderLogin -> PlaceholderScreen("Login Screen (coming soon)")
        }
    }
}

@Composable
private fun PlaceholderScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, color = Color.White)
    }
}
