package com.example.netdatademo.uistate

import com.example.netdatademo.retrofit.PicAdress

data class PitureState (
    val picAdress: PicAdress,
    val isLoading: Boolean = true,
    val error: String? = null
){
    fun toUiState() = PitureState(
        picAdress = picAdress,
        isLoading = isLoading,
        error = error
    )
}