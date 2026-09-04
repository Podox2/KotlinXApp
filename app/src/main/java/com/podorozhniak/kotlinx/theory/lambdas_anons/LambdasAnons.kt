package com.podorozhniak.kotlinx.theory.lambdas_anons

fun main() {
    val lambdasAnons = LambdasAnons()
    val text = "Original text"
    println("main is running")
    lambdasAnons.printLength(text)
    text.printLengthExt()
    doPrint(lambdasAnons.printLengthLambda, text)
    doPrint(lambdasAnons.printLengthLambdaReceiver, text)
    doPrintReceiver(lambdasAnons.printLengthLambda, text)
    lambdasAnons.test()
    println("main is finished")
}

fun doPrint(printTask: (String) -> Unit, textToPrint: String) {
    printTask(textToPrint)
}

fun doPrintReceiver(printTask: (String).() -> Unit, textToPrint: String) {
    printTask(textToPrint)
}

// extension функція
fun String.printLengthExt() {
    println(length)
}

class LambdasAnons {

    fun test() {
        printLengthLambda("qweqe")
        printLengthLambdaReceiver("wqe")
    }

    // звичайна функція
    fun printLength(text: String) {
        println(text.length)
    }

    // функціональний тип з параметром реалізований через лямбду
    val printLengthLambda: (String) -> Unit = { text: String ->
        println(text.length)
    }

    // функціональний тип з ресівером реалізований через лямбду
    // до об'єкту типу String можна звертатись без this
    val printLengthLambdaReceiver: String.() -> Unit = {
        println(/*this.*/length)
    }

    // функціональний тип з параметром і ресівером реалізований через лямбду
    val printLengthLambdaParamReceiver: String.(String) -> Unit = { text: String ->
        println(text.length)
        println(length)
    }

    // функціональний тип з параметром реалізований через анонімну функцію
    val printLengthAnonFun: (String) -> Unit = fun(text: String) {
        println(text.length)
    }

    // функціональний тип з ресівером реалізований через анонімну функцію
    val printLengthAnonFunReceiver: String.() -> Unit = fun String.() {
        println(length)
    }

    // !!! АЛЕ в Kotlin типи String.() -> Unit та (String) -> Unit є взаємозамінними (сумісними) під час присвоєння та передачі параметрів.
    // Під капотом для JVM обидва ці типи компілюються в один і той самий інтерфейс — Function1<String, Unit>.
    // тому в сигнатурі може бути параметр, а реалізація через ресівер
    val printLengthAnonFun2: (String) -> Unit = fun String.() {
        println(length)
    }

    // в сигнатурі може бути ресівер, а реалізація через параметр
    val printLengthAnonFunReceiver2: String.() -> Unit = fun(text: String) {
        println(text.length)
    }

    // функціональний тип з параметром і ресівером реалізований через анонімну функцію
    val printLengthAnonFunParamReceiver: String.(String) -> Unit = fun String.(text: String) {
        println(text.length)
        println(length)
    }
}