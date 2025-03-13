package com.example.netdatademo.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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

    suspend fun getGithubRepos(userName: String) = withContext(Dispatchers.IO) {
        client.get("https://api.github.com/users/${userName}/repos")
            .body<List<GithubRepoItem>>()
    }

    suspend fun getMuServerResponse(name: String, email: String) = withContext(Dispatchers.IO) {
        client.post("https://stepheneasyshot.cn/user") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("name" to name, "email" to email))
        }.body<String>()
    }

    fun release() {
        client.close()
    }
}

/**
 * ubuntu@VM-8-4-ubuntu:~/myapp$ sudo mysql -p app_db -t
 * Enter password:
 * Reading table information for completion of table and column names
 * You can turn off this feature to get a quicker startup with -A
 *
 * Welcome to the MySQL monitor.  Commands end with ; or \g.
 * Your MySQL connection id is 14
 * Server version: 8.0.41-0ubuntu0.24.04.1 (Ubuntu)
 *
 * Copyright (c) 2000, 2025, Oracle and/or its affiliates.
 *
 * Oracle is a registered trademark of Oracle Corporation and/or its
 * affiliates. Other names may be trademarks of their respective
 * owners.
 *
 * Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.
 *
 * mysql> USE app_db
 * Database changed
 * mysql> SHOW TABLES;
 * +------------------+
 * | Tables_in_app_db |
 * +------------------+
 * | users            |
 * +------------------+
 * 1 row in set (0.01 sec)
 *
 * mysql> SELECT * FROM users;
 * +----+-------------+--------------------------+
 * | id | name        | email                    |
 * +----+-------------+--------------------------+
 * |  1 | John Doe    | john@example.com         |
 * |  3 | Stephen     | zhanfeng990927@gmail.com |
 * |  4 | StephenZyan | zhanfeng990927@g.com     |
 * |  5 | John3       | john@examcom             |
 * |  6 | Jo443       | john@mcom                |
 * +----+-------------+--------------------------+
 * 5 rows in set (0.00 sec)
 *
 * mysql>
 */