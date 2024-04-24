package com.podorozhniak.kotlinx.theory.coroutines.mistakes

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import retrofit2.HttpException

//якщо так оброблювати помилки, то батьківська курутина не буде знати про відміну
suspend fun riskyTask() {
    try {
        delay(1_000)
        println("The answer is ${10 / 0}")
    } catch (e: Exception) {
        println("Can't divide")
    }
}

// можна перехоплювати всі помилки, але окремо обробити CancellationException
suspend fun riskyTaskCorrect() {
    try {
        delay(1_000)
        println("The answer is ${10 / 0}")
    } catch (e: Exception) {
        if (e is CancellationException) {
            throw e
        }
        println("Can't divide")
    }
}

// можна вказати конкретний вид помилки
suspend fun riskyTaskCorrect2() {
    try {
        delay(1_000)
        println("The answer is ${10 / 0}")
    } catch (e: HttpException) {
        println("Can't divide")
    }
}