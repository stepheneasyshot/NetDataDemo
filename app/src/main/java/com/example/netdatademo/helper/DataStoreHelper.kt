package com.example.netdatademo.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

/**
 * 谷歌推荐的最新存储方式
 * 协程实现，可以方便地获取存储的结果回调
 */
object DataStoreHelper {
    // 定义一个 DataStore 实例
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store_settings")

    private lateinit var outDataStore: DataStore<Preferences>

    /**
     * 初始化，使get和set不受Context域限制
     */
    fun init(context: Context) {
        outDataStore = context.dataStore
    }

    // 定义一个 suspend 函数，用于从 DataStore 中读取数据
    suspend fun <T> getData(key: Preferences.Key<T>, defaultValue: T): T {
        return (outDataStore.data.first()[key] ?: defaultValue)
    }

    // 定义一个 suspend 函数，用于将数据保存到 DataStore 中
    suspend fun <T> saveData(key: Preferences.Key<T>, value: T) {
        outDataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}