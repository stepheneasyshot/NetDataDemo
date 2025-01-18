package com.example.netdatademo.retrofit

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetroService {

    companion object {
        const val wananDroidUrl = "https://www.wanandroid.com/"

        const val catApiUrl = "https://api.thecatapi.com/"
    }

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 设置日志级别为BODY，显示请求和响应的正文
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(AuthInterceptor())
        .cookieJar(LoginCookieJar())
        .build()

    private val wananDroidRetrofit = Retrofit.Builder()
        .baseUrl(wananDroidUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()

    private val catApiRetrofit = Retrofit.Builder()
        .baseUrl(catApiUrl)
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()


    private val wanAndroiAapi: WanAndroidApi =
        wananDroidRetrofit.create(WanAndroidApi::class.java)

    private val catApi: CatApi = catApiRetrofit.create(CatApi::class.java)

    suspend fun getArticleList(page: Int) = withContext(Dispatchers.IO) {
        val response = wanAndroiAapi.getMainPageArticleList(page).execute()
        response.body()?.data?.datas ?: listOf()
    }

    suspend fun getCatPic() = withContext(Dispatchers.IO) {
        val response = catApi.getCatPic().execute()
        response.body() ?: PicAdress()
    }

    suspend fun loginWanAndroid(username: String, password: String) = withContext(Dispatchers.IO) {
        val response = wanAndroiAapi.login(username, password).execute()
        if (response.isSuccessful) {
            val cookies = response.headers().values("Set-Cookie")
            var token = ""
            for (cookie in cookies) {
                Log.i("loginWanAndroid -> Received Cookie:", cookie)
                if (cookie.contains("token_pass")) {
                    token = cookie.split(";")[0].split("=")[1]
                    Log.i("loginWanAndroid -> token:", token)
                }
            }
            val body = response.body()
            Log.i("loginWanAndroid -> Response Body:", body.toString())
            // 返回token和用户名
            Pair(token, body?.data?.username ?: "")
        } else {
            // 登录失败
            Log.e("loginWanAndroid -> Error:", response.message())
            null
        }
    }

    suspend fun getCollectArticleList() = withContext(Dispatchers.IO) {
        val response = wanAndroiAapi.getLoginArticleList().execute()
        response.body()?.data?.datas?: listOf()
    }
}