package com.podorozhniak.kotlinx.theory.oop

//val name можна отримати, не можна зміннити
//var isMarried можна отримати і змінити
//відразу прописується основний конструктор ( (val name: String) )
//можна задавати поля по замовчуванню
class Person private constructor(val name: String) {
    //ініціалізувати щось при створенні об'єкту
    init{
        println("init")
    }

    //другорядний конструктор, якщо є основний, то треба його викликати
    constructor(name: String, isMarried: Boolean = false) : this(name) {
        println("secondary 1")
    }

    constructor(name: String, isMarried: Boolean = false, age: Int = 10) : this(name, isMarried) {
        println("secondary 2")
    }

}