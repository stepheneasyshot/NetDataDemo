package com.example.netdatademo.uistate

import com.example.netdatademo.retrofit.CollectedArticleDataX
import com.example.netdatademo.retrofit.DataX

data class ArticleState(
    val articleList: List<DataX> = listOf(),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    fun toUiState() = ArticleState(
        articleList = articleList,
        isLoading = isLoading,
        error = error
    )
}

data class CollectedArticleState(
    val articleList: List<CollectedArticleDataX> = listOf(),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    fun toUiState() = CollectedArticleState(
        articleList = articleList,
        isLoading = isLoading,
        error = error
    )
}