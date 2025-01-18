package com.example.netdatademo.ui.pages

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.netdatademo.Screen

@Composable
fun MainPage(onNavigateTo: (Screen) -> Unit) {
    BasePage("导航页") {

        Button(onClick = { onNavigateTo(Screen.ArticlePage) }) {
            Text(text = "玩安卓首页文章列表", style = MaterialTheme.typography.bodyMedium)
        }

        Button(onClick = { onNavigateTo(Screen.VideoPage) }) {
            Text(text = "播放在线视频", style = MaterialTheme.typography.bodyMedium)
        }

        Button(onClick = { onNavigateTo(Screen.PicturePage) }) {
            Text(text = "显示在线猫猫图片", style = MaterialTheme.typography.bodyMedium)
        }
    }
}