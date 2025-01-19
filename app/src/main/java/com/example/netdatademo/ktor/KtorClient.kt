package com.example.netdatademo.ktor

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class KtorClient {

    companion object {
        const val TAG = "KtorClient"
    }

    private val client = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun getCatPicture() = withContext(Dispatchers.IO) {
        val data =
            client.get("https://api.thecatapi.com/v1/images/search?limit=10")
                .body<List<PicKtorItem>>()
        Log.i(TAG, "getCatPicture->  $data")
        data
    }

    fun release() {
        client.close()
    }
}