package com.example.netdatademo.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.netdatademo.MainStateHolder
import com.example.netdatademo.ElseData

@Composable
fun ElsePage(
    mainStateHolder: MainStateHolder,
    elseData: ElseData?,
    onBackStack: () -> Unit
) {
    BasePage(title = "ElsePage") {
        Column {
            Button(onClick = { onBackStack() }) {
                Text(text = "ElsePage Content Text")
            }
        }
    }
}
