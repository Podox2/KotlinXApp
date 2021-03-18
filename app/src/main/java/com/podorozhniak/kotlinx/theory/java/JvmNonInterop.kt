package com.podorozhniak.kotlinx.theory.java

class JvmNonInterop(var name: String) {

    var age: Int = 0

    companion object {
        fun getNumber() = 1
    }

    fun defaultParam(number: Int = 10) {
        println(number)
    }
}