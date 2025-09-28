package com.example.netdatademo.ktor

import android.content.ContentResolver
import android.net.Uri
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.yield
import java.io.IOException
import kotlin.math.max
import kotlin.math.min

class FileDownloadManager(val ktorClient: KtorClient) {

    suspend fun downloadFileWithProgress(
        url: String,
        fileUri: Uri,
        resolver: ContentResolver
    ): Flow<DownloadProgress> = flow {
        var bytesReceived = 0L
        val progressUpdateThreshold = 512 * 1024L
        var lastUpdateBytes = 0L

        resolver.openOutputStream(fileUri)?.use { outputStream ->
            val response: HttpResponse = ktorClient.client.get(url)
            val contentLength = response.contentLength() ?: 0L
            val channel = response.bodyAsChannel()

            // 使用更小的缓冲区，并根据内存情况动态调整
            var bufferSize = 8192
            val buffer = ByteArray(bufferSize)

            while (!channel.isClosedForRead) {
                // 检查内存压力，动态调整缓冲区大小
                if (isMemoryPressureHigh()) {
                    bufferSize = max(1024, bufferSize / 2)
                }

                val bytesRead = channel.readAvailable(buffer, 0, min(buffer.size, bufferSize))
                if (bytesRead < 0) break

                outputStream.write(buffer, 0, bytesRead)
                // ⭐ 关键修复：定期刷新输出流，释放内存缓冲区
                if (bytesReceived % (1024 * 1024) == 0L) { // 每1MB刷新一次
                    outputStream.flush()
                }

                bytesReceived += bytesRead

                if (bytesReceived - lastUpdateBytes >= progressUpdateThreshold) {
                    emit(DownloadProgress(bytesReceived, contentLength))
                    lastUpdateBytes = bytesReceived
                    yield()

                    // ⭐ 在发射进度后强制GC，防止内存累积
                    if (bytesReceived % (10 * 1024 * 1024) == 0L) { // 每10MB尝试GC一次
                        System.gc()
                    }
                }
            }

            // ⭐ 最终刷新确保所有数据写入文件
            outputStream.flush()

            if (bytesReceived != lastUpdateBytes) {
                emit(DownloadProgress(bytesReceived, contentLength))
            }

        } ?: throw IOException("Failed to open output stream")
    }.flowOn(Dispatchers.IO)

    // 简单的内存压力检测
    private fun isMemoryPressureHigh(): Boolean {
        val runtime = Runtime.getRuntime()
        val usedMemory = runtime.totalMemory() - runtime.freeMemory()
        val memoryUsage = usedMemory.toDouble() / runtime.maxMemory()
        return memoryUsage > 0.7 // 内存使用超过70%认为有压力
    }

    data class DownloadProgress(
        val bytesDownloaded: Long,
        val totalBytes: Long
    ) {
        val progress: Float
            get() = if (totalBytes > 0) (bytesDownloaded.toFloat() / totalBytes) * 100 else 0f
    }
}