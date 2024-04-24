package com.podorozhniak.kotlinx.theory.java

class JvmInterop(@JvmField var name: String) {

    @JvmField
    var age: Int = 0

    companion object {
        @JvmStatic
        fun getNumber() = 1
        @JvmField
        val NONE = ""
        const val CONST_NONE = ""
    }

    @JvmOverloads
    fun defaultParam(number: Int = 10) {
        println(number)
    }
}