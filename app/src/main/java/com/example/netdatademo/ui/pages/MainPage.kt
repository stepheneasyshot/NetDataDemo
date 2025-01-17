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
            Text(text = "go to RetroArticle", style = MaterialTheme.typography.bodyMedium)
        }

        Button(onClick = { onNavigateTo(Screen.ElsePage) }) {
            Text(text = "go to RetorElse", style = MaterialTheme.typography.bodyMedium)
        }

        Button(onClick = { onNavigateTo(Screen.PicturePage) }) {
            Text(text = "go to Picture", style = MaterialTheme.typography.bodyMedium)
        }
    }
}