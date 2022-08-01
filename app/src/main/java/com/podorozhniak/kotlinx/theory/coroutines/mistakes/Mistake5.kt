package com.podorozhniak.kotlinx.theory.coroutines.mistakes

import kotlinx.coroutines.*

// методи в'ю моделі, які дьоргає фрагмент не повинні бути suspend
// для таких методів в фрагменті прийдеться запустити курутину і якщо відбудеться configuration change
// курутина його не переживе і робота, яку вона виконувала, зупиниться
class SomeFragment {
    private val someViewModel = SomeViewModel()
    private val correctSomeViewModel = CorrectSomeViewModel()

    private val someJob = Job()
    private val someCoroutineScope = CoroutineScope(Dispatchers.Main + someJob)

    fun main() {
        //виклик неможливий не з курутини
        //someViewModel.someMethod()

        //курутина прив'язана до лайфсайкла фрагмента - погано
        someCoroutineScope.launch {
            someViewModel.someMethod()
        }

        //запуститься курутина у в'ю моделі, яка переживе зміну конфігурації
        correctSomeViewModel.someMethod()
    }
}

class SomeViewModel {

    suspend fun someMethod() {
        delay(4_000)
        println("it's working")
    }
}

class CorrectSomeViewModel {

    private val someJob = Job()
    private val someCoroutineScope = CoroutineScope(Dispatchers.IO + someJob)

    fun someMethod() {
        someCoroutineScope.launch {
            delay(4_000)
            println("it's working")
        }
    }
}