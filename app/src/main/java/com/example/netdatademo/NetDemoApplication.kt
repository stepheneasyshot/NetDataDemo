package com.example.netdatademo

import android.app.Application
import com.example.netdatademo.di.koinModule
import com.example.netdatademo.flowtest.flatMapLatestTest
import com.example.netdatademo.flowtest.flatMapMergeTest
import com.example.netdatademo.helper.DataStoreHelper
import org.koin.core.context.startKoin

class NetDemoApplication : Application() {

    companion object {
        lateinit var instance: NetDemoApplication
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(koinModule)
        }
        DataStoreHelper.init(appContext)

        flatMapLatestTest()
    }
}

val appContext = NetDemoApplication.instance.applicationContext