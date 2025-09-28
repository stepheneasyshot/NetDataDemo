package com.example.netdatademo.ui.pages

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netdatademo.MainStateHolder
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun FileDownloadPage(
    mainStateHolder: MainStateHolder,
    onBackStack: () -> Unit
) {
    BasePage("下载文件", onCickBack = onBackStack) {
        val progress by mainStateHolder.downloadProgressState.asStateFlow().collectAsState()

        Button(
            onClick = { mainStateHolder.downloadDebugManager() }
        ) {
            Text(
                text = "开始下载",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "下载进度：",
                style = MaterialTheme.typography.titleMedium
            )

            LinearProgressIndicator(
                progress = progress.progress,
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
            )

            Text(
                text = "${progress.progress.toInt()}%",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

