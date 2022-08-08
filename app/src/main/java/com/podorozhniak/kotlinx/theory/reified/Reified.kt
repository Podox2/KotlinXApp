package com.podorozhniak.kotlinx.theory.reified

import android.content.Context
import android.content.Intent
import com.podorozhniak.kotlinx.practice.view.fragment_result_api.SecondActivity

// коли ми юзаємо дженеріки, то під час виконання втрачається тип дженеріка. Тобто List<String>
// стає просто типом List. В Kotlin'і є спосіб зберегти цей тип. Для цього треба використовувати
// ключове слово reified. А його можна використовувати тільки для inline функцій

// fun <reified T> isANotInline(value: Any) = value is T // помилка - треба inline
// inline fun <T> isANotReified(value: Any) = value is T // помилка - тип стриається, треба reified
// робоча функція
inline fun <reified T> isA(value: Any) = value is T

fun main() {
    // якщо задебажити, то буде видно тільки тип ArrayList, а не ArrayList<String>
    val list: ArrayList<String> = arrayListOf("a", "b", "c")
    println(isA<String>(list)) // false
    println(isA<List<String>>(list)) // true
    println(isA<ArrayList<String>>(list)) // true

    println(isA<String>("a")) // true
    println(isA<String>(1)) // false
}

// юзаємо тип T як аргумент
inline fun <reified T> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}