package com.example.netdatademo.di

import com.example.netdatademo.MainStateHolder
import com.example.netdatademo.ktor.FileDownloadManager
import com.example.netdatademo.ktor.KtorClient
import com.example.netdatademo.retrofit.RetroService
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val koinModule = module {

    single { KtorClient() }

    viewModel { MainStateHolder(get(), get(), get()) }

    viewModelOf(::MainStateHolder)

    factory { RetroService() }

    factory { FileDownloadManager(get()) }
}