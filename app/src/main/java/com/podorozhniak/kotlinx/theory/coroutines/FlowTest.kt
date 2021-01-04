package com.podorozhniak.kotlinx.theory.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main(args: Array<String>) = runBlocking<Unit> {
    //foo().forEach { value -> println(value) }
    launch {
        for (k in 1..3) {
            println("Im not blocked")
            delay(200)
        }
    }
    fooFlow().collect { value -> println(value) }
}


suspend fun foo(): List<Int> {
    delay(1000)
    return listOf(1, 2, 3)
}

fun fooFlow(): Flow<Int> = flow {
    // flow builder
    for (i in 1..3) {
        delay(100) // pretend we are doing something useful here
        emit(i) // emit next value
    }
}

