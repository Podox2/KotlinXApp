package com.podorozhniak.kotlinx.practice.generics

class Test<T: DataClass>(val someData: T) {

    fun doStuff(data: T) {

    }
}

fun main() {
    val child = Child()
    val test = Test(someData = child)
    test.doStuff(child)
}

open class DataClass

class Child : DataClass()