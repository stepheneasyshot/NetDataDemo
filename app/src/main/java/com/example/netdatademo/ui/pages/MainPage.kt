package com.example.netdatademo.ui.pages

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.netdatademo.ElseData
import com.example.netdatademo.ArticleData
import com.example.netdatademo.PictureData

@Composable
fun MainPage(onNavigateTo: (Class<*>) -> Unit) {
    BasePage("导航页") {

        Button(onClick = { onNavigateTo(ArticleData::class.java) }) {
            Text(text = "go to RetroArticle", style = MaterialTheme.typography.bodyMedium)
        }

        Button(onClick = { onNavigateTo(ElseData::class.java) }) {
            Text(text = "go to RetorElse", style = MaterialTheme.typography.bodyMedium)
        }

        Button(onClick = { onNavigateTo(PictureData::class.java) }) {
            Text(text = "go to Picture", style = MaterialTheme.typography.bodyMedium)
        }
    }
}