package com.podorozhniak.kotlinx.theory.variances.metanit_example

//інваріантність
// типи Parent і Child знаходяться в одній ієрархії класів
// інтуїтивно можна подумати, що і типи List<Parent> і List<Child> зв'язані
// але це не так. Всі контейнери в джаві і більшість(?) в котліні інваріантні
class Invariance {

    interface SomeInterface<T : Parent> {
        fun someMethod(): T
        fun sendMessage(message: T)
    }

    open class Parent
    class Child : Parent()

    fun test(parent: SomeInterface<Parent>, child: SomeInterface<Child>) {
        //val interfaceParent: SomeInterface<Parent> = child // помилка
        //val interfaceChild: SomeInterface<Child> = parent // помилка

        val parentList = mutableListOf<SomeInterface<Parent>>()
        parentList.add(parent)
        //parentList.add(child) // помилка

        val childList = mutableListOf<SomeInterface<Child>>()
        //childList.add(parent) // помилка
        childList.add(child)
    }
}