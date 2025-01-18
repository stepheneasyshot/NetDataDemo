package com.example.netdatademo.retrofit

import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.netdatademo.MainStateHolder.Companion.TOKEN_KEY
import com.example.netdatademo.helper.DataStoreHelper
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = runBlocking {
            DataStoreHelper.getData(stringPreferencesKey(TOKEN_KEY), "")
        }
        if (token.isNotEmpty()) {
            val newRequest = originalRequest.newBuilder()
                .addHeader("Cookie", "loginUserName=zhanfeng")
                .addHeader("Cookie", "loginUserPassword=abcd1234@")
                .build()
            Log.d("AuthInterceptor", "intercept: $newRequest")
            return chain.proceed(newRequest)
        }

        return chain.proceed(originalRequest)
    }
}