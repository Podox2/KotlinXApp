package com.podorozhniak.kotlinx.theory.coroutines.mistakes

import kotlinx.coroutines.*
import kotlin.random.Random

fun main() {
    runBlocking {
        doSomething()
    }
}

suspend fun doSomething() {
    val job = CoroutineScope(Dispatchers.Default).launch {
        var random = Random.nextInt(10)
        //без перевірки isActive курутина не зупиниться після виклику cancel
        while (random != 5 && isActive) {
            // ensureActive() робить те саме, що і перевірка isActive + викидає CancellationException
            ensureActive()
            delay(500)
            random = Random.nextInt(10)
            println(random)
        }
    }
    delay(3_000)
    job.cancel()
}