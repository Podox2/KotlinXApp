package com.podorozhniak.kotlinx.practice.di

import com.podorozhniak.kotlinx.practice.base.ViewModelExecutor
import org.koin.dsl.module

val viewModelsModule = module {
    single {
        ViewModelExecutor()
    }
}
