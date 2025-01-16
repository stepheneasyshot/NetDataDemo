package com.example.netdatademo.di

import com.example.netdatademo.MainStateHolder
import com.example.netdatademo.retrofit.RetroService
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val koinModule = module {

    viewModel { MainStateHolder(get()) }

    viewModelOf(::MainStateHolder)

    factory { RetroService() }
}