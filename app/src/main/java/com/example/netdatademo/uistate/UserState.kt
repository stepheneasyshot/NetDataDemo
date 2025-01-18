package com.example.netdatademo.uistate

data class UserState(
    val userName: String = "",
    val isLoading: Boolean = true,
    val error: String? = null
){
    fun toUiState() = UserState(
        userName = userName,
        isLoading = isLoading,
        error = error
    )
}