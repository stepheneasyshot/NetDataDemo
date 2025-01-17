package com.example.netdatademo.ui.pages

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.netdatademo.MainStateHolder
import com.example.netdatademo.Screen

@Composable
fun PicturePage(
    mainStateHolder: MainStateHolder,
    routeDaa: Screen,
    onBackStack: () -> Unit
) {
    BasePage("图片列表") {
        Text(
            text = routeDaa.route,
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = { onBackStack() }) {
            Text(
                text = "返回",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}