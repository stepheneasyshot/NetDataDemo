package com.example.netdatademo.ui.pages

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.netdatademo.MainStateHolder
import com.example.netdatademo.PictureData

@Composable
fun PicturePage(
    mainStateHolder: MainStateHolder,
    retroArticle: PictureData?,
    onBackStack: () -> Unit
) {
    BasePage("图片列表") {
       Button(onClick = { onBackStack() }) {
           Text(
               text = "返回",
               style = MaterialTheme.typography.bodyMedium
           )
       }
    }
}