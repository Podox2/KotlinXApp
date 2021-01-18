package com.podorozhniak.kotlinx.practice.data.remote.repository

import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.Result
import com.podorozhniak.kotlinx.practice.data.remote.model.Message
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.Call

interface MessagesRepo {
    fun messagesSingle(): Single<List<Message>>

    fun messagesDeferred(): Deferred<List<Message>>

    suspend fun messages(): List<Message>

    suspend fun messagesResult(): Result<List<Message>>

    fun messagesCall(): Call<List<Message>>
}