package com.podorozhniak.kotlinx

import org.junit.Assert
import org.junit.Test

class DataClassTest {

    @Test
    fun data_class_test() {
        val notDataClass1 = NotDataClass("1", 2)
        val notDataClass2 = NotDataClass("1", 2)
        val dataClass1 = DataClass("1", 2)
        val dataClass2 = DataClass("1", 2)

        Assert.assertFalse(notDataClass1 == notDataClass2)
        Assert.assertFalse(notDataClass1 === notDataClass2)
        Assert.assertFalse(dataClass1 === dataClass2)

        // true because data classes implement hashCode and equals
        Assert.assertTrue(dataClass1 == dataClass2)
        Assert.assertEquals(dataClass1, dataClass2)
        Assert.assertEquals(dataClass1.id, notDataClass2.id)
    }
}


class NotDataClass (val id: String, val number: Int)

data class DataClass (val id: String, val number: Int)