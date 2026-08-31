package com.podorozhniak.kotlinx.theory.coroutines.mistakes

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds

//https://youtu.be/cr5xLjPC4-0
fun main() {
    println("start")
    runBlocking {
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            getUserFirstNames(listOf("1", "2", "3", "4", "5"))
        }.join()
    }
}

//виклики getFirstName() послідовні
suspend fun getUserFirstNamesWrong(userIds: List<String>): List<String> {
    val firstNames = mutableListOf<String>()
    for (id in userIds) {
        val firstName = getFirstName(id)
        firstNames.add(firstName)
        println(firstName)
    }
    return firstNames
}

//виклики getFirstName() паралельні
suspend fun getUserFirstNames(userIds: List<String>): List<String> {
    val firstNames = mutableListOf<Deferred<String>>()
    coroutineScope {
        for (id in userIds) {
            val firstName = async {
                getFirstName(id)
            }
            firstNames.add(firstName)
        }
    }
    return firstNames.awaitAll()
}

//виклики getFirstName() паралельні
suspend fun getUserFirstNamesByLaunch(userIds: List<String>): List<String> {
    val firstNames = mutableListOf<String>()
    coroutineScope {
        val jobs = userIds.mapIndexed { index, userId ->
            launch {
                firstNames[index] = getFirstName(userId)
            }
        }
        jobs.joinAll()
    }
    return firstNames
}

suspend fun getFirstName(userId: String): String {
    delay(1000.milliseconds)
    println("First name $userId")
    return "First name $userId"
}