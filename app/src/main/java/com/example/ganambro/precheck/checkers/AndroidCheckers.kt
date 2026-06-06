package com.example.ganambro.precheck.checkers

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.location.LocationManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.ganambro.precheck.Checker

/**
 * Checks that the device has an active internet connection.
 */
class InternetChecker(private val context: Context) : Checker {
    override val name = "Internet"

    override suspend fun check(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun failureReason() = "Internet tidak tersedia — nyalakan data/WiFi lalu buka ulang aplikasi"
}

/**
 * Checks that GPS/Location is enabled.
 */
class GpsChecker(private val context: Context) : Checker {
    override val name = "GPS"

    override suspend fun check(): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun failureReason() = "GPS mati — nyalakan GPS lalu buka ulang aplikasi"
}

/**
 * Checks that Bluetooth is OFF.
 */
class BluetoothChecker : Checker {
    override val name = "Bluetooth"

    override suspend fun check(): Boolean {
        val adapter = BluetoothAdapter.getDefaultAdapter() ?: return true // no BT = pass
        return !adapter.isEnabled
    }

    override fun failureReason() = "Bluetooth menyala — matikan Bluetooth lalu buka ulang aplikasi"
}

/**
 * Checks that no wired headset is plugged in.
 * Note: Bluetooth headset is covered by [BluetoothChecker].
 */
class HeadsetChecker(private val context: Context) : Checker {
    override val name = "Headset"

    override suspend fun check(): Boolean {
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return !am.isWiredHeadsetOn
    }

    override fun failureReason() = "Headset terpasang — lepaskan headset lalu buka ulang aplikasi"
}

/**
 * Checks that media volume is at maximum.
 */
class VolumeChecker(private val context: Context) : Checker {
    override val name = "Volume"

    override suspend fun check(): Boolean {
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val current = am.getStreamVolume(AudioManager.STREAM_MUSIC)
        return current >= max
    }

    override fun failureReason() = "Volume tidak maksimal — naikkan volume ke maksimal lalu buka ulang aplikasi"
}
