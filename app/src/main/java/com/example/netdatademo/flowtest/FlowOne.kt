package com.example.netdatademo.flowtest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

val TAG = "FlowOne".apply {
    Log.i(this, "init")
}


val flow = flow<Int> {
    repeat(5) {
        delay(500)
        emit(it)
    }
}

fun startCollect() {
    CoroutineScope(Dispatchers.IO).launch {
        delay(3000L)
        Log.i(TAG, "startCollect")
        flow.collect {
            Log.i(TAG, "FlowOne collect $it")
        }
        flow.collect {
            Log.i(TAG, "FlowOne collect twice $it")
        }
    }
}

fun mapTest() {
    CoroutineScope(Dispatchers.IO).launch {
        flowOf(1, 2, 3, 4, 5).map {
            it + 1
        }.collectLatest {
            Log.i(TAG, "mapTest collect $it")
        }
    }
}

fun filterTest() {
    CoroutineScope(Dispatchers.IO).launch {
        flowOf(3, 6, 9, 11, 14).filter {
            it % 3 == 0
        }.collectLatest {
            Log.i(TAG, "filterTest collect $it")
        }
    }
}

fun onEachTest() {
    CoroutineScope(Dispatchers.IO).launch {
        flowOf(1, 2, 3, 4, 5).onEach {
            Log.i(TAG, "onEachTest onEach $it")
        }.map { it + 10 }.collect {
            Log.i(TAG, "onEachTest collect $it")
        }
    }
}

@OptIn(FlowPreview::class)
fun debounceTest() {
    CoroutineScope(Dispatchers.IO).launch {
        flow {
            emit(1)
            delay(100)
            emit(2)
            delay(1000)
            emit(3)
            delay(100)
            emit(4)
            delay(100)
            emit(5)
        }.debounce(500).collectLatest {
            Log.i(TAG, "debouneTest collect $it")
        }
    }
}

@OptIn(FlowPreview::class)
fun sampleTest() {
    CoroutineScope(Dispatchers.IO).launch {
        flow {
            emit(1)
            delay(150)
            emit(2)
            delay(150)
            emit(3)
            delay(150)
            emit(4)
            delay(150)
            emit(5)
        }.sample(200).collect {
            Log.i(TAG, "debouneTest collect $it")
        }
    }
}

fun reduceTest() {
    CoroutineScope(Dispatchers.IO).launch {
        val totalResult = flowOf(1, 2, 3, 4, 5).reduce { acc, value ->
            acc * value
        }
        Log.i(TAG, "reduceTest collect $totalResult")
    }
}

fun foldTest() {
    CoroutineScope(Dispatchers.IO).launch {
        val totalResult = flowOf(1, 2, 3, 4, 5).fold(10) { acc, value ->
            acc * value
        }
        Log.i(TAG, "foldTest collect $totalResult")
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun flatMapConcatTest() {
    CoroutineScope(Dispatchers.IO).launch {
        flowOf(1, 2, 3).flatMapConcat {
            flowOf("a$it", "b$it")
        }.collect {
            Log.i(TAG, "flatMapConcatTest collect $it")
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun flatMapMergeTest() {
    CoroutineScope(Dispatchers.IO).launch {
        flowOf(300, 200, 100)
            .flatMapMerge {
                flow {
                    delay(it.toLong())
                    emit("a$it")
                    emit("b$it")
                }
            }
            .collect {
                Log.i(TAG, "flatMapMergeTest collect $it")
            }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun flatMapLatestTest() {
    CoroutineScope(Dispatchers.IO).launch {
        flow {
            emit(1)
            delay(150)
            emit(2)
            delay(50)
            emit(3)
        }.flatMapLatest {
            flow {
                delay(100)
                emit("$it")
            }
        }.collect {
            Log.i(TAG, "flatMapLatestTest collect $it")
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun zipTest() {
    CoroutineScope(Dispatchers.IO).launch {
        flowOf(1, 2, 3, 4, 5)
            .zip(flowOf("a", "b", "c", "d")) { a, b ->
                "$a+$b"
            }
            .collect {
                Log.i(TAG, "zipTest collect $it")
            }
    }
}

fun zipTest2() {
    CoroutineScope(Dispatchers.IO).launch {
        val start = System.currentTimeMillis()
        val flow1 = flow {
            delay(3000)
            emit("a")
        }
        val flow2 = flow {
            delay(2000)
            emit(1)
        }
        flow1.zip(flow2) { a, b ->
            a + b
        }.collect {
            val end = System.currentTimeMillis()
            Log.i(TAG, "Time cost: ${end - start}ms")
        }
    }
}

fun bufferTest() {
    CoroutineScope(Dispatchers.IO).launch {
        flow {
            emit(1)
            delay(1000)
            emit(2)
            delay(1000)
            emit(3)
        }.onEach {
            Log.i(TAG, "bufferTest onEach $it")
        }.buffer().collect {
            delay(1000)
            Log.i(TAG, "bufferTest collect $it")
        }
    }
}

fun conflateTest() {
    CoroutineScope(Dispatchers.IO).launch {
        flow {
            repeat(7) {
                delay(100)
                emit(it)
            }
        }.conflate().collectLatest {
            Log.i(TAG, "conflateTest collect start handle $it")
            delay(210)
            Log.i(TAG, "conflateTest collect end handle $it")
        }
    }
}

fun testLiveData() {
    val liveData = MutableLiveData<Int>()
    liveData.value = 1
    liveData.value = 2
    liveData.value = 3
    liveData.value = 4
    liveData.value = 5
}

