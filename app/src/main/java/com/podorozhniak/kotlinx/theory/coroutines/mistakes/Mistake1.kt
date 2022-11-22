package com.podorozhniak.kotlinx.theory.coroutines.mistakes

import kotlinx.coroutines.*

//https://youtu.be/cr5xLjPC4-0
fun main() {
    println("start")
    val idList = mutableListOf<String>()
    (0..9).forEach {
        idList.add(it.toString())
    }
    var userFirstNamesList: List<String>

    runBlocking {
        userFirstNamesList = getUserFirstNames(idList)
        //userFirstNamesList = getUserFirstNamesCorrect(idList)
    }
    userFirstNamesList.forEach {
        println(it)
    }
}

//виклики getFirstName() послідовні
suspend fun getUserFirstNames(userIds: List<String>): List<String> {
    val firstNames = mutableListOf<String>()
    for (id in userIds) {
        val firstName = getFirstName(id)
        firstNames.add(firstName)
        println(firstName)
    }
    return firstNames
}

//виклики getFirstName() паралельні
suspend fun getUserFirstNamesCorrect(userIds: List<String>): List<String> {
    val firstNames = mutableListOf<Deferred<String>>()
    coroutineScope {
        for (id in userIds) {
            val firstName = CoroutineScope(Dispatchers.Default).async {
                getFirstName(id)
            }
            println(firstName) // виведе Deferred об'єкт
            firstNames.add(firstName)
        }
    }
    return firstNames.awaitAll()
}

suspend fun getFirstName(userId: String): String {
    delay(1_000)
    return "First name $userId"
}