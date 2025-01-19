package com.example.netdatademo.uistate

import com.example.netdatademo.ktor.GithubRepoItem

data class GithubReposState(
    val repos: List<GithubRepoItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    fun toUiState() = GithubReposState(
        repos = repos,
        isLoading = isLoading,
        error = error
    )
}
