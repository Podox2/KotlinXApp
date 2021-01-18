package com.podorozhniak.kotlinx.practice.di

import com.podorozhniak.kotlinx.practice.data.remote.service.MessagesDataSource
import com.podorozhniak.kotlinx.practice.data.remote.service.MessagesService
import org.koin.dsl.module

//тип інтерфейсу має = типу, який хоче репозиторій, наприклад MessagesRepoImpl
val dataSourceModule = module {
    single<MessagesService> {
        MessagesDataSource(
            get()
        )
    }
}