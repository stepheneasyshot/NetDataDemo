package com.example.netdatademo.ui.pages

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netdatademo.Screen

@Composable
fun MainPage(onNavigateTo: (Screen) -> Unit) {
    LazyColumn(modifier = Modifier.padding(10.dp)) {
        item {
            Text(
                text = "导航页", style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Button(
                onClick = { onNavigateTo(Screen.ArticlePage) },
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                Text(text = "玩安卓文章数据（Retrofit）", style = MaterialTheme.typography.bodyMedium)
            }

            Button(
                onClick = { onNavigateTo(Screen.VideoPage) },
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                Text(text = "播放在线视频（ExoPlayer）", style = MaterialTheme.typography.bodyMedium)
            }

            Button(
                onClick = { onNavigateTo(Screen.PicturePage) },
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                Text(
                    text = "显示在线猫猫图片（Retrofit）",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(
                onClick = { onNavigateTo(Screen.PicturePageKtor) },
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                Text(text = "显示在线猫猫图片（Ktor）", style = MaterialTheme.typography.bodyMedium)
            }

            Button(
                onClick = { onNavigateTo(Screen.GithubReposPage) },
                modifier = Modifier.padding(vertical = 10.dp)
            ){
                Text(text = "Github仓库列表（Ktor）", style = MaterialTheme.typography.bodyMedium)
            }

            Button(
                onClick = { onNavigateTo(Screen.MyServerPage) },
                modifier = Modifier.padding(vertical = 10.dp)
            ){
                Text(text = "我的服务器测试（Ktor）", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}