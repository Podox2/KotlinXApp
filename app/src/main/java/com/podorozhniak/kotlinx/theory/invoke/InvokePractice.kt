package com.podorozhniak.kotlinx.theory.invoke

val lambda = {
    println("lambda")
}

val lambdaWithArgument: (String) -> Unit = { text: String ->
    println(text)
}

fun main() {
    // () == .invoke()
    lambda()
    lambda.invoke()

    // з аргументами те ж саме
    lambdaWithArgument("string to print")
    lambdaWithArgument.invoke("string to print")

    // оверрайднутий оператор в класі
    val invokeExample = InvokeExample()
    invokeExample()
    invokeExample.invoke()
}

// invoke це те ж саме, що і (). в котліні можна оверрайднути цей оператор
class InvokeExample {

    operator fun invoke() {
        println("invoke in InvokeExample")
    }
}