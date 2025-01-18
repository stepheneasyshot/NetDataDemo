package com.example.netdatademo.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WanAndroidApi {
    // 获取首页文章列表
    @GET("article/list/{page}/json")
    fun getMainPageArticleList(@Path("page") page: Int): Call<Article>

    // 登录
    @POST("user/login")
    fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<LoginBean>

    // 获取登陆后的文章列表
    @GET("lg/collect/list/0/json")
    fun getLoginArticleList(): Call<CollectedArticle>

//    https://www.wanandroid.com/user/logout/json
    // 退出登录
    @GET("user/logout/json")
    fun logout(): Call<LogoutBean>
}