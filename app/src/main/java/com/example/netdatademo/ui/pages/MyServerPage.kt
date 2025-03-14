package com.example.netdatademo.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.netdatademo.MainStateHolder

@Composable
fun MyServerPage(
    mainStateHolder: MainStateHolder,
    onBackStack: () -> Unit
) {
    BasePage("个人服务器测试", onCickBack = onBackStack) {

        LaunchedEffect(Unit) {
            mainStateHolder.getMyServerResponse()
        }

        val myResponse = mainStateHolder.myServerResponseStateFlow.collectAsState().value

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(text = myResponse)
        }
    }
}