package com.example.netdatademo.retrofit

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

const val wananDroidUrl = "https://www.wanandroid.com/"

const val catApiUrl = "https://api.thecatapi.com/"

class RetroService {

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 设置日志级别为BODY，显示请求和响应的正文
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
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


    private val wanAndroiAapi: WanAndroidRepository =
        wananDroidRetrofit.create(WanAndroidRepository::class.java)

    private val catApi: CatApi = catApiRetrofit.create(CatApi::class.java)

    suspend fun getArticleList(page: Int) = withContext(Dispatchers.IO) {
        val response = wanAndroiAapi.getArticleList(page).execute()
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
            for (cookie in cookies) {
                Log.i("loginWanAndroid -> Received Cookie:", cookie)
            }
            val body = response.body()
            Log.i("loginWanAndroid -> Response Body:", body.toString())
            // 验证登录是否成功
            body?.data?.username ?: ""
        } else {
            // 登录失败
            Log.e("loginWanAndroid -> Error:", response.message())
            ""
        }
    }
}

interface WanAndroidRepository {
    @GET("article/list/{page}/json")
    fun getArticleList(@Path("page") page: Int): Call<Article>

    @POST("user/login")
    fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<LoginBean>
}


interface CatApi {
    @GET("v1/images/search?limit=10")
    fun getCatPic(): Call<PicAdress>
}

