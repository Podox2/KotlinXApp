package com.podorozhniak.kotlinx.theory.lambdas_anons

fun main() {
    val myClass = MyClass()
    myClass.processListNonLocalReturn()
}

class MyClass {

    val list = listOf(1, 2, -3, 4)

    // non-local return example
    fun processListNonLocalReturn() {
        list.forEach {
            if (it < 0) {
                println("Знайдено від'ємне число: $it. Перериваємо функцію!")
                // non-local return (повернення не з локальної лямбди, а з зовнішньої функції)
                return
            }
            println("Число: $it")
        }
        // Цей рядок НЕ виконається, якщо є від'ємне число
        println("Всі числа додатні")
    }

    // local return example
    fun processListLocalReturn() {
        list.forEach {
            if (it < 0) {
                println("Знайдено від'ємне число: $it")
                // local return: пропускає лише поточну ітерацію
                return@forEach
            }
            println("Число: $it")
        }
        //println("Всі числа додатні")
    }

    // local return example with tag міткою
    fun processListLocalReturnWithTag() {
        list.forEach loop@{
            if (it < 0) {
                println("Знайдено від'ємне число: $it. Перериваємо функцію!")
                // Локальне повернення: пропускає лише поточну ітерацію
                return@loop
            }
            println("Число: $it")
        }
        //println("Всі числа додатні")
    }

}