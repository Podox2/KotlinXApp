package com.podorozhniak.kotlinx.theory.coroutines

import kotlinx.coroutines.*

// viewModelScope - SupervisorScope, що логічно (якийсь зафейлений реквест, зроблений у в'ю моделі, не повинен впливати на інші)

// коротко по корутинах
// взагалі не ловимо ексепшени - аплікуха крешиться, джоби фейляться
// обробляємо креші (try/catch або coroutine exception handler) - аплікуха не крешиться, джоби фейляться
// обробляємо креші і використовуємо supervisor scope - аплікуха не крешиться, джоби не фейляться
fun main(): Unit = runBlocking {
    exceptionLogicInCoroutines()
    // coroutine exception handler + supervisor
    //simpleApproach()
    //handlerApproach()
    //supervisorAndHandlerApproach()
}

suspend fun exceptionLogicInCoroutines() {
    withContext(Dispatchers.IO) {
        // цей код виконається успішно
        // ловимо креші всередині тіла корутини
        launch {
            try {
                throw Exception()
            } catch (e: Exception) {
                println("e caught")
            }
        }

        // цей код крешнеться
        // цей try не хендлить креші, які викидуються в дочірній джобі
        try {
            val childJob = launch {
                // цей експшен не буде оброблений
                throw Exception()
            }
            // ось цей ексепшн буде оброблений
            throw Exception()
        } catch (e: Exception) {
            println("e caught")
        }

        ////////////////////////////////////////////////
        // якщо юзати .launch{} код нижче має статись креш, але чомусь це не так
        //CoroutineScope(Dispatchers.IO).launch {

        // не має бути крешу, без await() async не викине ексепшн
        CoroutineScope(Dispatchers.IO).async {
            val s = async {
                delay(1_000)
                // цей ексепшн викинеться, коли ми викликаємо await()
                throw Exception("error")
                "Result"
            }
            // без цього рядка (виклику await()) ексепшн не викинеться
            println(s.await())
        }
    }
}

// без ніякої додаткової логіки
// аплікуха крешнеться
// друга дочірня і батьківська джоби зафейляться, бо в першій дочірній ексепшн
suspend fun simpleApproach() {
    withContext(Dispatchers.IO) {
        val parentJob = launch {
            launch {
                delay(400)
                throw Exception()
            }
            launch {
                delay(1000)
                println("Second job is finished")
            }
        }
        parentJob.invokeOnCompletion { throwable ->
            if (throwable != null) {
                println("parentJob received throwable")
            } else {
                println("nice")
            }
        }
    }
}

// з coroutine exception handler
// аплікуха не крешнеться
// друга дочірня і батьківська джоби зафейляться
// !! в консольній аплікусі воно працює чомусь не так (взагалі нічого не виводиться в консолі). це зв'язано зі скоупами скоріш за все
// якщо, наприклад, в фрагменті (див. CoroutineFragment.handlerApproach()) написати цей код
// без withContext (тому що не має контекста, який можна переключити) все працює правильно
suspend fun handlerApproach() {
    withContext(Dispatchers.IO) {
        val parentJobWithExcHandler = CoroutineScope(Dispatchers.Default + handler).launch {
            launch {
                delay(400)
                throw Exception("some exc")
            }
            launch {
                delay(1000)
                println("Second job is finished")
            }
        }
        parentJobWithExcHandler.invokeOnCompletion { throwable ->
            if (throwable != null) {
                println("parentJob received throwable")
            } else {
                println("nice")
            }
        }
    }
}

// з coroutine exception handler + supervisor scope
// аплікуха не крешнеться
// друга дочірня і батьківська джоби успішно завершать роботу
suspend fun supervisorAndHandlerApproach() {
    withContext(Dispatchers.IO) {
        val superVisorParentJobWithExcHandler = launch(handler) {
            supervisorScope {
                launch {
                    delay(400)
                    throw Exception("some exc")
                }
                launch {
                    delay(1000)
                    println("Second job is finished")
                }
            }
        }
        superVisorParentJobWithExcHandler.invokeOnCompletion { throwable ->
            if (throwable != null) {
                println("parentJob received throwable")
            } else {
                println("nice")
            }
        }
    }
}