package com.example.netdatademo.ui.pages

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import com.example.netdatademo.MainStateHolder
import com.example.netdatademo.Screen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PicturePage(
    mainStateHolder: MainStateHolder,
    routeDaa: Screen,
    onBackStack: () -> Unit
) {
    BasePage("图片列表", onCickBack = onBackStack) {

        LaunchedEffect(Unit) {
            mainStateHolder.getCatPic()
        }

        val pitureList = mainStateHolder.pitureListStateFlow.collectAsState().value

        Text(
            text = routeDaa.route,
            style = MaterialTheme.typography.bodyMedium
        )

        LazyColumn {
            item {
                FlowRow {
                    pitureList.picAdress.forEach {
                        AsyncImage(
                            model = it.url,
                            contentDescription = "cat pic",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}