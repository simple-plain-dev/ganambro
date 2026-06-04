package com.example.ganambro

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.ActivityInfo
import android.location.LocationManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ganambro.feature.precheck.*
import com.example.ganambro.feature.volume.VolumeManager
import com.example.ganambro.ui.screens.*
import com.example.ganambro.ui.theme.GanambroTheme

class MainActivity : ComponentActivity() {

    private val volumeManager: VolumeManager by lazy {
        val audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        VolumeManager(
            getVolume = { audio.getStreamVolume(AudioManager.STREAM_ALARM) },
            getMaxVolume = { audio.getStreamMaxVolume(AudioManager.STREAM_ALARM) },
            setVolume = { audio.setStreamVolume(AudioManager.STREAM_ALARM, it, 0) },
        )
    }

    private val precheckRegistry: PrecheckRegistry by lazy {
        PrecheckRegistry().apply {
            register(InternetCheck { isInternetAvailable() })
            register(GpsCheck { isGpsEnabled() })
            register(BluetoothCheck { isBluetoothOff() })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        volumeManager.start()

        setContent {
            GanambroTheme {
                val navController = rememberNavController()

                DisposableEffect(Unit) {
                    onDispose { volumeManager.restore() }
                }

                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") {
                        val viewModel = remember { SplashViewModel(precheckRegistry) }
                        SplashScreen(
                            viewModel = viewModel,
                            onNavigateToLogin = {
                                navController.navigate("login") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            },
                            onExit = { finish() },
                        )
                    }

                    composable("login") {
                        LoginScreen(
                            onSkip = {
                                navController.navigate("menu") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                        )
                    }

                    composable("menu") {
                        MenuScreen(
                            onPortalUjian = { /* Slice 5a */ },
                            onPetunjuk = { /* Slice 4 */ },
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        volumeManager.restore()
        super.onDestroy()
    }

    // ── Platform checks ──

    private fun isInternetAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun isGpsEnabled(): Boolean {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun isBluetoothOff(): Boolean {
        val adapter: BluetoothAdapter? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val bm = getSystemService(Context.BLUETOOTH_SERVICE) as android.bluetooth.BluetoothManager
            bm.adapter
        } else {
            @Suppress("DEPRECATION")
            BluetoothAdapter.getDefaultAdapter()
        }
        return adapter == null || !adapter.isEnabled
    }
}
