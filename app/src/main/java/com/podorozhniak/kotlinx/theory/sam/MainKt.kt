package com.podorozhniak.kotlinx.theory.sam

// фішка в тому, що в Java, якщо інтерфейс має 1 метод, то його можна реалізувати через лямбду
// в Kotlin'і для цього треба помітити такий інтерфейс словом fun
fun main() {
    val samImpl = SamKtImpl()

    // реалізація через object
    val samByObject = object : SamKt {
        override fun saySomething(): String {
            return "qwe"
        }
    }
    // реалізація через лямбду (для цього і потрібні SAM)
    val samByLambda = SamKt { "wqe" }


    // реалізація через object
    val notSamByObject = object : NotSamKt {
        override fun saySomething(): String {
            return "qwe"
        }
    }
    // реалізація через лямбду неможлива (бо інтерфейс не помічений fun)
    //val notSamByLambda = NotSamKt { "wqe" }


    val samJByObject = object: SamJ {
        override fun saySomething(): String {
            return "qweqwe"
        }
    }
    val samJByLambda = SamJ { "qwe" }
}