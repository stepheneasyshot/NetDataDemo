package com.example.netdatademo.retrofit

import retrofit2.Call
import retrofit2.http.GET

interface CatApi {
    @GET("v1/images/search?limit=10")
    fun getCatPic(): Call<PicAdress>
}