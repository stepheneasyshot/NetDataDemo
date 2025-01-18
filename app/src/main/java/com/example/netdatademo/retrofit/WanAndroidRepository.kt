package com.example.netdatademo.retrofit

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val wananDroidUrl = "https://www.wanandroid.com/"

const val catApiUrl = "https://api.thecatapi.com/"

class RetroService {

    private val wananDroidRetrofit = Retrofit.Builder()
        .baseUrl(wananDroidUrl)
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()

    private val catApiRetrofit = Retrofit.Builder()
        .baseUrl(catApiUrl)
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()

    private val wanAndroiAapi: WanAndroidRepository = wananDroidRetrofit.create(WanAndroidRepository::class.java)

    private val catApi: CatApi = catApiRetrofit.create(CatApi::class.java)

    suspend fun getArticleList(page: Int) = withContext(Dispatchers.IO) {
        val data = wanAndroiAapi.getArticleList(page).execute()
        data.body()?.data?.datas ?: listOf()
    }

    suspend fun getCatPic() = withContext(Dispatchers.IO) {
        val data = catApi.getCatPic().execute()
        data.body() ?: PicAdress()
    }
}

interface WanAndroidRepository {
    @GET("article/list/{page}/json")
    fun getArticleList(@Path("page") page: Int): Call<Article>
}

interface CatApi {
    @GET("v1/images/search?limit=10")
    fun getCatPic(): Call<PicAdress>
}

