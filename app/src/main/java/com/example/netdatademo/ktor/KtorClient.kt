package com.example.netdatademo.ktor

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
        client.get("https://api.thecatapi.com/v1/images/search?limit=10")
            .body<List<PicKtorItem>>()
    }

    suspend fun getOneCatImage() = withContext(Dispatchers.IO) {
        client.get("https://api.thecatapi.com/v1/images/search").body<List<PicKtorItem>>()
    }

    suspend fun getGithubRepos(userName: String) = withContext(Dispatchers.IO) {
        client.get("https://api.github.com/users/${userName}/repos")
            .body<List<GithubRepoItem>>()
    }

    suspend fun getMyServerResponse() = withContext(Dispatchers.IO) {
        client.get("https://stepheneasyshot.cn/api/hello").body<String>()
    }

    fun release() {
        client.close()
    }
}