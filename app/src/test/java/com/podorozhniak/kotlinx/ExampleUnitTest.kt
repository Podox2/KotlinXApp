package com.podorozhniak.kotlinx

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun data_class_test() {
        val notDataClass1 = NotDataClass("1", 2)
        val notDataClass2 = NotDataClass("1", 2)
        val dataClass1 = DataClass("1", 2)
        val dataClass2 = DataClass("1", 2)
        println(notDataClass1 == notDataClass2)
        println(notDataClass1 === notDataClass2)
        println(dataClass1 == dataClass2) // true так як data класи реалізовують hashCode i equals
        println(dataClass1 === dataClass2)
    }
}


class NotDataClass (val id: String, val number: Int)

data class DataClass (val id: String, val number: Int)