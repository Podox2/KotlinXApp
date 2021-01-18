package com.podorozhniak.kotlinx.practice.data.remote.repository

import com.podorozhniak.kotlinx.practice.data.remote.model.Message
import com.podorozhniak.kotlinx.practice.data.remote.service.MessagesService
import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.Result
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.Call

class MessagesRepoImpl(private val messagesDataSource: MessagesService) : MessagesRepo {
    override fun messagesSingle(): Single<List<Message>> = messagesDataSource.messagesSingle()

    override fun messagesDeferred(): Deferred<List<Message>> = messagesDataSource.messagesDeferred()

    override suspend fun messages(): List<Message> {
        //throw IllegalStateException()
        return messagesDataSource.messages()
    }

    override suspend fun messagesResult(): Result<List<Message>> {
        //throw IllegalStateException()
        return messagesDataSource.messagesResult()
    }

    override fun messagesCall(): Call<List<Message>> = messagesDataSource.messagesCall()
}