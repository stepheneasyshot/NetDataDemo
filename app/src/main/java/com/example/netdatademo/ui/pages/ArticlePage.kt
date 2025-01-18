package com.example.netdatademo.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
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
        val userName = mainStateHolder.userStateFlow.collectAsState()

        LaunchedEffect(Unit) {
            mainStateHolder.getArticleList(0)
            mainStateHolder.loginWanAndroid()
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.AccountCircle, contentDescription = "AccountCircle")
            Text(text = userName.value.userName.ifEmpty {
                "未登录"
            }, style = MaterialTheme.typography.bodyLarge)
        }

        if (userName.value.userName.isNotEmpty()) {

            Text(
                text = "账户收藏文章列表",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                items(listOf<String>("assasv测试", "sevgwebw测试", "sefvgwebew测试")) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(1f)
                            .clip(RoundedCornerShape(10))
                            .background(MaterialTheme.colorScheme.onBackground)
                            .padding(5.dp)
                    )
                }
            }
        }

        Text(
            text = "首页文章列表",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            items(articleData.value.articleList) { it ->
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth(1f)
                        .clip(RoundedCornerShape(10))
                        .background(MaterialTheme.colorScheme.onBackground)
                        .padding(5.dp)
                )
            }
        }
    }
}