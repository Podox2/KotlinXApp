package com.podorozhniak.kotlinx.theory

//коваріантність від дочірнього до базового
//контрваріантність від базового до дочірнього

open class Person {

    open fun method() {
        println("it's a person")
    }
}

class Student : Person() {

    override fun method() {
        println("it's a student")
    }
}

fun main() {
    //val list: Collection<Student> = mutableListOf<Person>()
    val list2: MutableList<Person> = mutableListOf<Person>()
    list2.add(Person())
    list2.add(Student())
    list2.forEach {
        it.method()
    }
}
