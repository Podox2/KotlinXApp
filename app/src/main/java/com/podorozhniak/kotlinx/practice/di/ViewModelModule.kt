package com.podorozhniak.kotlinx.practice.di

import com.podorozhniak.kotlinx.practice.view.network_request.NetworkRequestViewModel
import com.podorozhniak.kotlinx.practice.view.timer.TimerViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val viewModelsModule = module {
    viewModel {
        NetworkRequestViewModel(
            messagesRepo = get()
        )
    }

    viewModel { TimerViewModel() }
}
