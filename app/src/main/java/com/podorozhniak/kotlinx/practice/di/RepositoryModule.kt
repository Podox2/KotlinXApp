package com.podorozhniak.kotlinx.practice.di

import com.podorozhniak.kotlinx.practice.data.remote.repository.MessagesRepo
import com.podorozhniak.kotlinx.practice.data.remote.repository.MessagesRepoImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<MessagesRepo> {
        MessagesRepoImpl(
            get()
        )
    }

    //single { provideMessagesRepo(get()) }
}

    /*fun provideMessagesRepo(messagesDataSource: MessagesDataSource): MessagesRepoImpl {
        return MessagesRepoImpl(messagesDataSource)
    }*/
