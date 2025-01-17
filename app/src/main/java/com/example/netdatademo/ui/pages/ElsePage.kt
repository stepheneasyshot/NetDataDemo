package com.example.netdatademo.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.netdatademo.MainStateHolder
import com.example.netdatademo.Screen

@Composable
fun ElsePage(
    mainStateHolder: MainStateHolder,
    elseData: Screen,
    onBackStack: () -> Unit
) {
    BasePage(title = "ElsePage") {
        Column {
            Text(text = elseData.route, style = MaterialTheme.typography.bodyMedium)
            Button(onClick = { onBackStack() }) {
                Text(text = "ElsePage Content Text")
            }
        }
    }
}
