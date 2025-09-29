package com.example.netdatademo.ui.pages

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netdatademo.MainStateHolder

@Composable
fun WifiSearchPage(
    mainStateHolder: MainStateHolder,
    onBackStack: () -> Unit
) {
    BasePage("搜索WiFi", onCickBack = onBackStack) {

        val wifiState by mainStateHolder.wifiState.collectAsState()
        val deviceFoundState by mainStateHolder.wifiDeviceFoundState.collectAsState()

        LaunchedEffect(Unit) {
            mainStateHolder.initWifiListen()
        }

        DisposableEffect(Unit) {
            onDispose {
                mainStateHolder.stopWifiListen()
            }
        }

        Text(
            text = "Wifi开关状态：${if (wifiState) "已开启" else "已关闭"}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Button(
            onClick = { mainStateHolder.startScanWifi() },
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            Text(
                text = "搜索",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Text(
            text = "搜索状态：${if (deviceFoundState) "已搜索到MIX2S" else "未搜索到设备"}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        if (deviceFoundState) {
            Button(
                onClick = { mainStateHolder.connectWifi() },
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Text(
                    text = "连接",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

    }
}

