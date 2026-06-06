package com.example.ganambro

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ganambro.audio.AndroidWarningSound
import com.example.ganambro.audio.WarningSound
import com.example.ganambro.config.AppConfigFactory
import com.example.ganambro.config.BuildConfigProvider
import com.example.ganambro.exit.ExitContext
import com.example.ganambro.exit.ExitCoordinator
import com.example.ganambro.precheck.PrecheckRunner
import com.example.ganambro.precheck.checkers.BluetoothChecker
import com.example.ganambro.precheck.checkers.GpsChecker
import com.example.ganambro.precheck.checkers.HeadsetChecker
import com.example.ganambro.precheck.checkers.InternetChecker
import com.example.ganambro.precheck.checkers.VolumeChecker
import com.example.ganambro.ui.login.LoginResult
import com.example.ganambro.ui.login.LoginScreen
import com.example.ganambro.ui.menu.MenuScreen
import com.example.ganambro.ui.menu.PetunjukContent
import com.example.ganambro.ui.menu.PetunjukScreen
import com.example.ganambro.ui.pengawas.HiddenAccessTrigger
import com.example.ganambro.ui.pengawas.PengawasScreen
import com.example.ganambro.ui.pengawas.TriggerState
import com.example.ganambro.ui.pengawas.TriggerTarget
import com.example.ganambro.ui.splash.SplashScreen
import com.example.ganambro.ui.token.TokenInputScreen
import com.example.ganambro.ui.ujian.UjianScreen
import com.example.ganambro.token.TokenGenerator
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Build config from BuildConfig
        val appConfig = AppConfigFactory.create(BuildConfigProvider)
        val warningSound = AndroidWarningSound(this)

        setContent {
            GanambroApp(
                activity = this,
                appConfig = appConfig,
                warningSound = warningSound,
            )
        }
    }
}

@Composable
fun GanambroApp(
    activity: Activity,
    appConfig: com.example.ganambro.config.AppConfig,
    warningSound: WarningSound,
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    // HiddenAccessTrigger for Pengawas access
    val hiddenTrigger = remember { HiddenAccessTrigger() }

    // Exit coordinator — confirmation via Compose dialog
    var showExitDialog by remember { mutableStateOf(false) }
    var exitConfirmDeferred by remember { mutableStateOf<CompletableDeferred<Boolean>?>(null) }

    val exitCoordinator = remember {
        ExitCoordinator(
            warningSound = warningSound,
            onExit = { activity.finishAffinity() },
            requestConfirmation = { _ ->
                exitConfirmDeferred = CompletableDeferred()
                showExitDialog = true
                exitConfirmDeferred!!.await()
            },
        )
    }

    // Reusable precheck runner
    val precheckRunner = remember {
        PrecheckRunner(
            listOf(
                InternetChecker(context),
                GpsChecker(context),
                BluetoothChecker(),
                HeadsetChecker(context),
                VolumeChecker(context),
            )
        )
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = "splash") {

            // === Splash ===
            composable("splash") {
                SplashScreen(
                    precheckRunner = precheckRunner,
                    onNavigateToLogin = { navController.navigate("login") { popUpTo("splash") { inclusive = true } } },
                    onExit = { activity.finishAffinity() },
                )
            }

            // === Login ===
            composable("login") {
                LoginScreen(
                    onLoginResult = { _: LoginResult ->
                        navController.navigate("menu") { popUpTo("login") { inclusive = true } }
                    },
                )
            }

            // === Menu ===
            composable("menu") {
                MenuScreen(
                    exitCoordinator = exitCoordinator,
                    onNavigateToTokenInput = { navController.navigate("token_input") },
                    onNavigateToPetunjuk = { content ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("petunjuk", content)
                        navController.navigate("petunjuk")
                    },
                    onLogoTap = {
                        val state = hiddenTrigger.registerClick(TriggerTarget.Logo)
                        if (state is TriggerState.Triggered) {
                            navController.navigate("pengawas")
                        }
                    },
                    onPetunjukTap = {
                        val state = hiddenTrigger.registerClick(TriggerTarget.Petunjuk)
                        if (state is TriggerState.Triggered) {
                            navController.navigate("pengawas")
                            true
                        } else false
                    },
                )
            }

            // === Petunjuk ===
            composable("petunjuk") {
                val content = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<PetunjukContent>("petunjuk")
                    ?: PetunjukContent("Error", emptyList(), "")
                PetunjukScreen(
                    content = content,
                    onBack = { navController.popBackStack() },
                )
            }

            // === Token Input ===
            composable("token_input") {
                TokenInputScreen(
                    onTokenValid = { token ->
                        navController.navigate("ujian") {
                            popUpTo("menu")
                        }
                    },
                    validateToken = { token ->
                        TokenGenerator.validate(token, appConfig.salt, appConfig.tokenWindowMinutes)
                    },
                    onBack = { navController.popBackStack() },
                )
            }

            // === Ujian ===
            composable("ujian") {
                UjianScreen(
                    examUrl = appConfig.examUrl,
                    exitCoordinator = exitCoordinator,
                    onExitApp = {
                        navController.navigate("menu") { popUpTo("menu") { inclusive = true } }
                    },
                )
            }

            // === Pengawas ===
            composable("pengawas") {
                PengawasScreen(
                    correctPin = appConfig.pin,
                    tokenGenerator = {
                        TokenGenerator.generate(appConfig.salt, appConfig.tokenWindowMinutes)
                    },
                    tokenWindowMinutes = appConfig.tokenWindowMinutes,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }

    // Exit confirmation dialog (ExitContext.Ujian)
    if (showExitDialog) {
        var inputText by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = {
                showExitDialog = false
                exitConfirmDeferred?.complete(false)
            },
            title = { Text("Konfirmasi Keluar") },
            text = {
                androidx.compose.foundation.layout.Column {
                    Text("Ketik \"exit\" untuk keluar dari Ujian:", fontFamily = FontFamily.Monospace)
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        singleLine = true,
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val confirmed = inputText.trim().equals("exit", ignoreCase = true)
                    if (confirmed) {
                        warningSound.play(com.example.ganambro.audio.WarningSoundType.WS2)
                    }
                    showExitDialog = false
                    exitConfirmDeferred?.complete(confirmed)
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    exitConfirmDeferred?.complete(false)
                }) {
                    Text("Batal")
                }
            },
        )
    }
}
