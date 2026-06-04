package com.example.ganambro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.ganambro.feature.precheck.BluetoothCheck
import com.example.ganambro.feature.precheck.GpsCheck
import com.example.ganambro.feature.precheck.InternetCheck
import com.example.ganambro.feature.precheck.PrecheckRegistry
import com.example.ganambro.feature.volume.VolumeManager
import com.example.ganambro.ui.navigation.GanambroNavGraph
import com.example.ganambro.ui.theme.GanambroTheme

class MainActivity : ComponentActivity() {

    private val volumeManager by lazy {
        VolumeManager(
            getVolume = { 0 },
            getMaxVolume = { 15 },
            setVolume = { /* STREAM_ALARM via AudioManager */ },
        )
    }

    private val precheckRegistry by lazy {
        PrecheckRegistry().apply {
            register(InternetCheck { true }) // TODO: wire real connectivity check
            register(GpsCheck { true })      // TODO: wire real GPS check
            register(BluetoothCheck { true }) // TODO: wire real bluetooth check
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        volumeManager.start()

        setContent {
            GanambroTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()
                    GanambroNavGraph(
                        navController = navController,
                        precheckRegistry = precheckRegistry,
                        onExitApp = { finish() },
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        volumeManager.restore()
        super.onDestroy()
    }
}
