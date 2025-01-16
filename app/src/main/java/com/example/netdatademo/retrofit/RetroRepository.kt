package com.example.netdatademo.retrofit

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val baseUrl = "https://www.wanandroid.com/"

class RetroService {

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()

    val api = retrofit.create(RetroRepository::class.java)

    suspend fun getArticleList(page: Int) = withContext(Dispatchers.IO) {
        val data = api.getArticleList(page).execute()
        data.body()?.data?.datas ?: listOf()
    }
}

interface RetroRepository {
    @GET("article/list/{page}/json")
    fun getArticleList(@Path("page") page: Int): Call<Article>
}

