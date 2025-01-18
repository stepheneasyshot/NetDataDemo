package com.example.netdatademo.uistate

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