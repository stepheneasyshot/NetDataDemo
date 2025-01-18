package com.example.netdatademo.ui.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.netdatademo.MainStateHolder
import com.example.netdatademo.Screen


@Composable
fun ArticlePage(
    mainStateHolder: MainStateHolder,
    retroArticle: Screen,
    onBackStack: () -> Unit
) {
    BasePage("文章列表", onCickBack = onBackStack) {
        val articleData = mainStateHolder.articleListStateFlow.collectAsState()

        LaunchedEffect(Unit) {
            mainStateHolder.getArticleList(0)
        }

        Text(text = retroArticle.route, style = MaterialTheme.typography.bodyMedium)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(1f)
        ) {
            items(articleData.value.articleList) { it ->
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}