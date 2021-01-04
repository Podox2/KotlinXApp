package com.podorozhniak.kotlinx.theory.book

fun main(args: Array<String>) {
    println("It's working!")

    //
    println({
        val currentYear = 2019
        "$currentYear is right now"
    }())

    //
    val showYear: () -> String = {
        val currentYear = 2019
        "$currentYear is right now (value)"
    }
    println(showYear())

    //
    val showYearLaconic = {
        val currentYear = 2019
        "$currentYear is right now (value) laconic"
    }
    println(showYearLaconic())

    //
    val showString: (String) -> String = {
        "The string is $it"
    }
    println(showString("2014"))

    //
    val showStringAndNumber: (String, Int) -> String = { s, n ->
        "The string is $s, the number is $n"
    }
    println(showStringAndNumber("woaw", 4))

    //
    val showStringAndNumberLaconic = { s: String, n: Int ->
        "The string is $s, the number is $n laconic"
    }
    println(showStringAndNumberLaconic("woaw", 4))

    //
    println("\n")
    val funForFun = { s: String, n: Int ->
        "$s and $n"
    }

    fun funAsParam(s: String, funAsParam: (String, Int) -> String): Boolean {
        println("Simple string param $s")
        println(funAsParam(s, 10))
        return true
    }
    funAsParam("string", funForFun)

//laconic
    println("\n")
    funAsParamLaconic("string", ::showNumber) { s: String, n: Int ->
        "$s and $n"
    }
}

inline fun funAsParamLaconic(s: String, funReferenceAsParameter: (Int) -> Unit, funAsParam: (String, Int) -> String): Boolean {
    println("Laconic")
    println("Simple string param $s")
    funReferenceAsParameter(2)
    println(funAsParam(s, 10))
    return true
}
fun showNumber(n: Int) = println("$n")