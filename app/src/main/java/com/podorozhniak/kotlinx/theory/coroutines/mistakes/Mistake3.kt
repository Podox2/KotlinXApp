package com.podorozhniak.kotlinx.theory.coroutines.mistakes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

suspend fun doNetworkCall(): Result<String> {
    val result = networkCall()
    return if (result == "Success") {
        Result.success(result)
    } else Result.failure(Exception())
}

//якщо робити кол в мережу з мейн потока, буде помилка
suspend fun networkCall(): String {
    delay(3_000)
    return if (Random.nextBoolean()) "Success" else "Error"
}

//зміна потоку
suspend fun networkCallCorrect(): String {
    return withContext(Dispatchers.IO) {
        delay(3_000)
        if (Random.nextBoolean()) "Success" else "Error"
    }
}