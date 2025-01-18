package com.example.netdatademo.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netdatademo.Screen

@Composable
fun MainPage(onNavigateTo: (Screen) -> Unit) {
    Column(modifier = Modifier.padding(10.dp)) {

        Text(text = "导航页", style = MaterialTheme.typography.titleLarge)

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