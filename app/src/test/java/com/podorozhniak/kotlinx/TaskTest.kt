package com.podorozhniak.kotlinx

import org.junit.Assert
import org.junit.Test

class TaskTest {
    @Test
    fun addition_isCorrect() {
        val array = arrayListOf(1, 4, 1, 2, 2, 3, 1)
        //val array = arrayListOf(2, 2, 1)
        var indexOfNumber = 0

        loop@
        while (indexOfNumber <= array.size) {
            var unique = true
            array.forEachIndexed { index, it ->
                if(indexOfNumber != index && array[indexOfNumber] == it){
                    unique = false
                    return@forEachIndexed
                }
            }

            if(unique) break@loop
            else indexOfNumber++

        }
        Assert.assertEquals(4, array[indexOfNumber])
    }
}