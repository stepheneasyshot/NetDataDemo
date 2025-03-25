package com.example.netdatademo.ui.pages

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.netdatademo.MainStateHolder
import java.util.Locale

@Composable
fun MyServerPage(
    mainStateHolder: MainStateHolder,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onBackStack: () -> Unit,
) {
    BasePage("个人服务器测试", onCickBack = onBackStack) {

        LaunchedEffect(Unit) {
            mainStateHolder.getMyServerResponse()
        }

        val myResponse = mainStateHolder.myServerResponseStateFlow.collectAsState().value

        LaunchedEffect(myResponse) {
            if (myResponse.isNotEmpty()) {
                mainStateHolder.speak(myResponse, Locale.US)
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(text = myResponse)
        }

        DisposableEffect(lifecycleOwner) {
            Log.i("MyServerPage", "MyServerPage ${lifecycleOwner.lifecycle.currentState}")
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_STOP) {
                    // 当 Activity 退到后台时，Lifecycle 会触发 ON_STOP 事件
                    mainStateHolder.stopSpeech()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                mainStateHolder.stopSpeech()
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}