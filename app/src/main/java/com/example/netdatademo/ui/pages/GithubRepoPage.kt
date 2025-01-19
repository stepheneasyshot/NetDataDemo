package com.example.netdatademo.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.netdatademo.MainStateHolder

@Composable
fun GithubRepoPage(
    mainStateHolder: MainStateHolder,
    onBackStack: () -> Unit
) {
    BasePage("Github仓库列表", onCickBack = onBackStack) {
        val githubRepoList = mainStateHolder.githubReposListStateFlow.collectAsState()
        val userName by remember { mutableStateOf("QCuncle") }

        LaunchedEffect(Unit) {
            mainStateHolder.getGithubRepos(userName)
        }

        Text(text = "用户名：${userName}", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(githubRepoList.value.repos) {
                Text(
                    text = it.full_name,
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

