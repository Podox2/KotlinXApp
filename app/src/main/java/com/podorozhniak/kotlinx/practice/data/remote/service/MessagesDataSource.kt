package com.podorozhniak.kotlinx.practice.data.remote.service

import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.Result
import com.podorozhniak.kotlinx.practice.data.remote.model.Message
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit

class MessagesDataSource(retrofit: Retrofit) : MessagesService {
    private val api = retrofit.create(MessagesService::class.java)

    override fun messagesSingle(): Single<List<Message>> = api.messagesSingle()

    override fun messagesDeferred(): Deferred<List<Message>> = api.messagesDeferred()

    override suspend fun messages(): List<Message> = api.messages()

    override suspend fun messagesResult(): Result<List<Message>> = api.messagesResult()

    override fun messagesCall(): Call<List<Message>> = api.messagesCall()
}