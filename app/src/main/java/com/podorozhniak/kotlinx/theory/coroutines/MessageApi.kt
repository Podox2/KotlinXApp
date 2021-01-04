package com.podorozhniak.kotlinx.theory.coroutines

import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET

interface MessageApi{

    @GET("messages1.json")
    fun messagesSingle(): Single<List<Message>>


    @GET("messages1.json")
    fun messagesDeferred(): Deferred<List<Message>>


    @GET("messages1.json")
    fun messagesCall(): Call<List<Message>>
}