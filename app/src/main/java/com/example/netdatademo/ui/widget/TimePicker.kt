package com.example.netdatademo.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


@Composable
fun TimePicker() {
    val hours = (0..23).toList()
    // 扩展列表，前后各添加 2 个空项
    val extendedHours = List(2) { -1 } + hours + List(2) { -1 }
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = extendedHours.size / 2) // 默认滚动到中间
    val selectedHour by remember { derivedStateOf { calculateCenterItem(lazyListState, extendedHours) } }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemScrollOffset }
            .map { calculateCenterItem(lazyListState, extendedHours) }
            .distinctUntilChanged()
            .collect { }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Hour",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .height(200.dp)
            ) {
                items(extendedHours.size) { index ->
                    val hour = extendedHours[index]
                    if (hour != -1) { // 只显示有效的小时项
                        HourItem(
                            hour = hour,
                            isSelected = hour == selectedHour,
                            selectedHour = selectedHour,
                            onClick = { }
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp) // 空项占位高度
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HourItem(hour: Int, isSelected: Boolean, selectedHour: Int, onClick: () -> Unit) {
    // 计算当前项与选中项的差值
    val difference = kotlin.math.abs(hour - selectedHour)
    // 根据差值动态计算字体大小
    val fontSize = when (difference) {
        0 -> 30.sp // 选中项最大
        1 -> 24.sp // 靠近选中项
        2 -> 20.sp // 次靠近选中项
        else -> 16.sp // 其他项
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (hour < 10) "0$hour" else hour.toString(),
            fontSize = fontSize,
            color = if (isSelected) Color.Blue else Color.Black,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

// 计算当前位于屏幕中部的项
private fun calculateCenterItem(lazyListState: LazyListState, extendedHours: List<Int>): Int {
    val layoutInfo = lazyListState.layoutInfo
    val visibleItems = layoutInfo.visibleItemsInfo
    if (visibleItems.isEmpty()) return -1

    val centerY = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.height / 2
    val centerItem = visibleItems.find {
        it.offset <= centerY && it.offset + it.size >= centerY
    } ?: visibleItems.first()

    return extendedHours[centerItem.index]
}