package com.podorozhniak.kotlinx.practice.di

import com.podorozhniak.kotlinx.practice.base.ViewModelExecutor
import com.podorozhniak.kotlinx.practice.view.network_request.NetworkRequestViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { NetworkRequestViewModel(get(), get()) }
    single {
        ViewModelExecutor()
    }
}
