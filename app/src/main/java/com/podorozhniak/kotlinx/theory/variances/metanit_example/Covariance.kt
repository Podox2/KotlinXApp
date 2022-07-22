package com.podorozhniak.kotlinx.theory.variances.metanit_example

//коваріантність
// в дженеріках можна використовувати дочірні типи
class Covariance {
    interface SomeInterface<out T : Parent> {
        // метод може вертати тип
        fun someMethod(): T
        //fun sendMessage(message: T) // помилка
    }

    open class Parent(val text: String)
    class Child(text: String) : Parent(text)

    fun test(parent: SomeInterface<Parent>, child: SomeInterface<Child>) {
        val interfaceParent: SomeInterface<Parent> = child
        //val interfaceChild: SomeInterface<Child> = parent // помилка

        //в список можна додати об'єкт типу SomeInterface<Child> // помилка
        val parentList = mutableListOf<SomeInterface<Parent>>()
        parentList.add(parent)
        parentList.add(child)

        val childList = mutableListOf<SomeInterface<Child>>()
        //childList.add(parent) // помилка
        childList.add(child)
    }
}