package com.example.netdatademo

import android.app.Application
import com.example.netdatademo.di.koinModule
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
    }
}

val appContext = NetDemoApplication.instance.applicationContext