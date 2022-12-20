package com.podorozhniak.kotlinx.theory.sam

// фішка в тому, що в Java, якщо інтерфейс має 1 метод, то його можна реалізувати через лямбду
// в Kotlin'і для цього треба помітити такий інтерфейс словом fun
fun main() {
    val samImpl = SamKtImpl()

    // Kotlin реалізації
    // реалізація через object
    val samByObject = object : SamKt {
        override fun saySomething(): String {
            return "said something"
        }
    }
    // реалізація через лямбду (для цього і потрібні SAM)
    val samByLambda = SamKt { "said something" }


    // реалізація через object. не можна реалізувати через лямбду
    // студія не підказує, що можна конвертувати в лямбду
    val notSamByObject = object : NotSamKt {
        override fun saySomething(): String {
            return "said something"
        }
    }
    // реалізація через лямбду неможлива (бо інтерфейс не помічений fun)
    //val notSamByLambda = NotSamKt { "said something" }


    // Java реалізації
    val samJByObject = object: SamJ {
        override fun saySomething(): String {
            return "said something"
        }
    }
    val samJByLambda = SamJ { "said something" }
}