package com.example.netdatademo.helper

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.example.netdatademo.MainStateHolder.Companion.TAG
import com.example.netdatademo.appContext
import kotlinx.coroutines.flow.MutableStateFlow


class WifiConnectHelper {

    val MATCH_SSID = "MIX2S"

    private val wifiManager: WifiManager = appContext.getSystemService(WifiManager::class.java)

    private val connectivityManager =
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    val wifiSwitchState = MutableStateFlow(false)
    val deviceFoundState = MutableStateFlow(false)

    private val wifiReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        @SuppressLint("MissingPermission")
        @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "onReceive: $intent")
            if (intent?.action == WifiManager.WIFI_STATE_CHANGED_ACTION) {
                val wifiState =
                    intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
                wifiSwitchState.value = (wifiState == WifiManager.WIFI_STATE_ENABLED)
            } else if (intent?.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                val wifiScanResults = wifiManager.scanResults
                val isDeviceFound = wifiScanResults.any { it.SSID == MATCH_SSID }
                deviceFoundState.value = isDeviceFound
            }
        }
    }

    fun initWifiListen() {
        Log.d(TAG, "initWifiListen: 初始化wifi监听")
        val filters = IntentFilter().apply {
            addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
            addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        }
        ContextCompat.registerReceiver(
            appContext,
            wifiReceiver,
            filters,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    fun connectWifi() {
        Log.d(TAG, "connectToWifi: 连接到wifi")
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    // 创建网络描述符
    private val specifier = WifiNetworkSpecifier.Builder()
        .setSsid(MATCH_SSID)
        // 根据网络安全类型选择对应的方法，例如 WPA2/WPA3
        // 无密码
        // .setWpa2Passphrase(password)
        // .setWpa3Passphrase(password) // 如果是 WPA3
        // .setIsHiddenSsid(true) // 如果是隐藏网络
        .build()

    // 创建网络请求
    private val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .setNetworkSpecifier(specifier)
        // 一般用于互联网连接，但如果您只是连接本地设备，可能不需要
        // .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()


    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            // 成功连接到指定的 Wi-Fi 网络
//            connectivityManager.bindProcessToNetwork(network)
            Log.d(TAG, "onAvailable: 成功连接到指定的 Wi-Fi 网络")
        }

        override fun onUnavailable() {
            super.onUnavailable()
            // 用户拒绝连接请求或连接失败
            Log.d(TAG, "onUnavailable: 用户拒绝连接请求或连接失败")
        }

        // ... 其他回调方法，如 onLost 等
    }

    fun releaseWifiListen() {
        Log.d(TAG, "releaseWifiListen: 释放wifi监听")
        appContext.unregisterReceiver(wifiReceiver)
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun startScanWifi() {
        Log.d(TAG, "startScanWifi: 开始扫描wifi")
        wifiManager.startScan()
    }
}