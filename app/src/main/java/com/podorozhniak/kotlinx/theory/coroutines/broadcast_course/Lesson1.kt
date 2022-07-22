package com.podorozhniak.kotlinx.theory.coroutines.broadcast_course

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

fun main(): Unit = runBlocking {
    repeat(5) {
        launch {
            val result = doWork(it)
            println(result)
        }
    }
}

suspend fun doWork(number: Int): String {
    delay(Random.nextLong(3_000))
    return number.toString()
}