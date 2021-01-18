package com.podorozhniak.kotlinx.practice.data.remote.service

import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.Result
import com.podorozhniak.kotlinx.practice.data.remote.model.Message
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET

interface MessagesService {
    @GET("messages1.json")
    fun messagesSingle(): Single<List<Message>>

    @GET("messages1.json")
    fun messagesDeferred(): Deferred<List<Message>>

    @GET("messages1.json")
    suspend fun messages(): List<Message>

    @GET("messages1.json")
    suspend fun messagesResult(): Result<List<Message>>

    @GET("messages1.json")
    fun messagesCall(): Call<List<Message>>
}