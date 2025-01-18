package com.example.netdatademo.retrofit

data class LogoutBean(
    val `data`: UserData,
    val errorCode: Int,
    val errorMsg: String
)
