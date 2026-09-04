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

// crossinline: вбудовує код лямбди + забороняє non-local return (захищає від спроб завершити зовнішню функцію з іншого контексту).
// Коли inline-функція викликає передану лямбду в іншому контексті виконання (наприклад, усередині анонімного об'єкта,
// іншого лямбда-виразу або Runnable/Thread), неможливо виконати direct return із зовнішньої функції, адже цей код може запуститися пізніше або в іншому потоці.
// Без crossinline компілятор Kotlin просто не дозволить вам викликати лямбду в такому контексті.
// Оголошуємо inline-функцію з crossinline
inline fun doLater(crossinline action: () -> Unit) {
    // Runnable створює новий контекст (інший об'єкт)
    val task = Runnable {
        action() // Код action() вбудовується сюди (inline працює!)
    }
    task.run()
}

fun main2() {
    println("Початок")

    doLater {
        println("Виконуємо дію")

        // return // ❌ ПОМИЛКА: Компілятор забороняє звичайний return!
        return@doLater // ✅ OK: Можна використовувати тільки локальний return
    }

    println("Кінець") // Цей рядок обов'язково виконається
}