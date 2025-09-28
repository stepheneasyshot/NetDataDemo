package com.example.netdatademo.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class KtorClient {

    companion object {
        const val TAG = "KtorClient"
    }

    val client = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        // **重点：确保启用了此插件**
        install(HttpRedirect) {
            // 确保 enabled 为 true，这是默认值，但建议检查
            checkHttpMethod = true
            allowHttpsDowngrade = false
        }
        install(HttpTimeout) {
            // 请求超时：整个请求（包括连接、重定向、数据传输）的最长时间
            // 建议将其设置为一个较大的值，例如 60 秒 (1分钟)
            requestTimeoutMillis = 60_000
            // 可选：连接超时（建立连接的时间）
            connectTimeoutMillis = 15_000
        }

        followRedirects = true

        defaultRequest {
            header(HttpHeaders.UserAgent, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
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