package com.podorozhniak.kotlinx.theory.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.NonCancellable.cancel

// structured concurrency - для інкапсуляції потоків виконання.
// дозволяє прокидувати ексепшени (помилки) з дочірніх потоків в батьківські
// і хендлити їх

// приклад хендлера для батьківської джоби
// такі хендлери не хендлять CancellationException (спеціальний ексепшн, який і так хендлиться джобами)
val handler = CoroutineExceptionHandler { coroutineContext, exception ->
    println("Exception thrown in one of the children: ${exception.message}")
}

// можна хендлити і конкренто для дочірньої джоби
val childHandler = CoroutineExceptionHandler { coroutineContext, exception ->
    println("Child handled exception: ${exception.message}")
}

fun main(): Unit = runBlocking {
    secondJobException()
    //secondJobCancellation()
    //secondJobExceptionSupervisor()
}

// без супервайзера і ексепшн хендлера по дефолту, якщо в якійсь джобі викидується ексепшн, то він прокидується і в батьківську джобу
// а з неї і в решту активних дочірніх джоб. тобто всі ці джоби фейляться

// викидуємо ексепшн в другій джобі
// jobA встигне виконатись
// jobB спеціально фейлимо
// ексепшн прокинеться в батьківську джобу - батьківська джоба зафейлиться
// разом з батьківською зафейляться і інші дочірні джоби - в цьому випадку jobC
suspend fun secondJobException() {
    withContext(Dispatchers.IO) {
        // використання хендлера має фіксити креш апплікухи. але консольна програма все рівно крешиться
        val parentJob = launch(handler) {

            val jobA = launch {
                val resultA = getResultOrException(1)
                println("resultA - $resultA")
            }
            // ця джоба встигне успішно виконатись
            jobA.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultA: $throwable")
                }
            }

            val jobB = launch {
                val resultB = getResultOrException(2)
                println("resultB - $resultB")
            }
            // завершиться через ексепшн - throwable != null
            jobB.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultB: $throwable")
                }
            }

            val jobC = launch {
                val resultC = getResultOrException(3)
                println("resultC - $resultC")
            }
            // зафейлиться через jobB - throwable != null
            jobC.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultC: $throwable")
                }
            }
        }

        // зафейлиться через jobB
        parentJob.invokeOnCompletion { throwable ->
            if (throwable != null) {
                println("Parent job failed: $throwable")
            } else {
                println("Parent job success")
            }
        }
    }
}

suspend fun getResultOrException(number: Int): Int {
    delay(number * 500L)
    // викидуємо ексепшн для другої джоби
    if (number == 2) {
        throw Exception("Error getting result for number $number")
    }
    return number * 2
}
///////////////////////////////////////////////////////////////////////////////////////////////////////
// якщо якась джоба кенселиться (не фейлиться), то це ніяк не впливає на батьківську або дочірні джоби
// CancellationException'и джоби хендлять самі під капотом, тому

// кенсилемо (або викидуємо CancellationException) другу джобу
suspend fun secondJobCancellation() {
    withContext(Dispatchers.IO) {
        val parentJob = launch {

            val jobA = launch {
                val resultA = getResultOrCancellationException(1)
                println("resultA - $resultA")
            }
            // успішно виконується
            jobA.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultA: $throwable")
                }
            }

            // відміняємо джобу після запуску
            val jobB = launch {
                val resultB = getResultOrCancellationException(2)
                // цей метод зробить те ж саме
                //val resultB = getResultOrCancel(2)
                println("resultB - $resultB")
            }
            delay(1_000)
            jobB.cancel()
            // завершиться через cancel - throwable != null
            jobB.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultB: $throwable")
                }
            }

            val jobC = launch {
                val resultC = getResultOrCancellationException(3)
                println("resultC - $resultC")
            }
            // виконається успішно
            jobC.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultC: $throwable")
                }
            }
        }

        // виконається успішно
        parentJob.invokeOnCompletion { throwable ->
            if (throwable != null) {
                println("Parent job failed: $throwable")
            } else {
                println("Parent job success")
            }
        }
    }
}

// відмінити джобу можна типу через виклик cancel()
// але так джоба не відміниться просто під капотом викинеться CancellationException()
// документація каже не юзати цей метод
@OptIn(InternalCoroutinesApi::class)
suspend fun getResultOrCancel(number: Int): Int {
    delay(number * 500L)
    if (number == 2) {
        //не робить нічого! все спрацює успішно
        cancel(CancellationException("Error getting result for number $number"))
    }
    return number * 2
}

// спеціальний вид ексепшенів - CancellationException
// не впливає на інші дочірні і батьківську джобу
// виклик cancel() робить теж саме
suspend fun getResultOrCancellationException(number: Int): Int {
    delay(number * 500L)
    // викидуємо ексепшн для другої джоби
    if (number == 2) {
        throw CancellationException("Error getting result for number $number")
    }
    return number * 2
}
//////////////////////////////////////////////////////////////////////////////////////
// з супервайзером, якщо в якійсь джобі викидується ексепшн, то він не вплине на інші джоби
// потрібно обов'язково використовувати з CoroutineExceptionHandler'ом

suspend fun secondJobExceptionSupervisor() {
    withContext(Dispatchers.IO) {
        // використання хендлера має фіксити креш апплікухи. але консольна програма чомусь все рівно крешиться (13.02.23 не крешиться)
        val parentJob = launch(handler) {
            supervisorScope {

                // ------------ JOB A ------------
                val jobA = launch {
                    val resultA = getResultOrException(1)
                    println("resultA - $resultA")
                }

                // ------------ JOB B ------------
                val jobB = launch {
                    // взагалі можна помістити тіло джоби в try/catch
                    // (але не можна в try запускати нову корутину див. StructuredConcurrency2)
                    // але це поганий підхід, бо треба так робити для всіх дочірніх джоб
                    //try {
                        val resultB = getResultOrException(2)
                        println("resultB - $resultB")
                    /*} catch (e: Exception) {
                        println("exception handled")
                    }*/
                }
                // зафейлиться через ексепшн
                jobB.invokeOnCompletion { throwable ->
                    if (throwable != null) {
                        println("Error getting resultB: $throwable")
                    }
                }

                // ------------ JOB C ------------
                val jobC = launch {
                    val resultC = getResultOrException(3)
                    println("resultC - $resultC")
                }
                // не зафейлиться
                jobC.invokeOnCompletion { throwable ->
                    if (throwable != null) {
                        println("Error getting resultC: $throwable")
                    }
                }
            }
        }

        // не зафейлиться
        parentJob.invokeOnCompletion { throwable ->
            if (throwable != null) {
                println("Parent job failed: $throwable")
            } else {
                println("Parent job success")
            }
        }
    }
}