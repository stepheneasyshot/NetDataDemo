package com.example.netdatademo.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun BasePage(title: String, onCickBack: () -> Unit, content: @Composable () -> Unit) {
    Column {
        Row(
            Modifier.padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "back",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clip(RoundedCornerShape(10))
                    .clickable {
                        onCickBack()
                    }
                    .padding(5.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f)
            )
        }
        Spacer(
            Modifier
                .fillMaxWidth(1f)
                .height(2.dp)
                .background(MaterialTheme.colorScheme.onBackground)
        )

        Column(Modifier.padding(10.dp)) {
            content()
        }
    }
}