package com.example.netdatademo.retrofit

class PicAdress : ArrayList<PicAdressItem>()

data class PicAdressItem(
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)