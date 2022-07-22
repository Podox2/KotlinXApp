package com.podorozhniak.kotlinx.theory.variances.metanit_example

//контрваріантність
//  в дженеріках можна виокристовувати базвоий тип
class Contrvariance {
    interface SomeInterface<in T : Parent> {
        //fun someMethod(): T
        fun sendMessage(message: T)
    }

    open class Parent(val text: String)
    class Child(text: String) : Parent(text)

    fun test(parent: SomeInterface<Parent>, child: SomeInterface<Child>) {
        //val interfaceParent: SomeInterface<Parent> = child // помилка
        val interfaceChild: SomeInterface<Child> = parent

        val parentList = mutableListOf<SomeInterface<Parent>>()
        parentList.add(parent)
        //parentList.add(child) // помилка

        //в список можна додати об'єкт типу SomeInterface<Parent>
        val childList = mutableListOf<SomeInterface<Child>>()
        childList.add(parent)
        childList.add(child)
    }
}