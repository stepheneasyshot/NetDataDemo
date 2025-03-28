package com.example.netdatademo.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.netdatademo.MainStateHolder
import com.example.netdatademo.Screen

@Composable
fun VideoPage(
    mainStateHolder: MainStateHolder,
    videoData: Screen,
    onBackStack: () -> Unit
) {
    BasePage(title = "视频页", onCickBack = onBackStack) {
        Column {

            val context = LocalContext.current
            val exoPlayer = remember {
                ExoPlayer.Builder(context).build().apply {
                    val mediaItem = MediaItem.fromUri(mainStateHolder.getVideoUrl())
                    setMediaItem(mediaItem)
                    prepare()
                }
            }

            AndroidView(factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                }
            })

            DisposableEffect(Unit) {
                onDispose {
                    exoPlayer.release()
                }
            }
        }
    }
}
