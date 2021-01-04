package com.podorozhniak.kotlinx.theory.oop

//за замовчування всі класи final
//щоб дозволити наслідувати клас треба написати
//open
open class ClickableImpl : Clickable {

    //методи також final по дефолту
    open fun doSomething(){}


    //override = @Override
    //перевизначені методи по дефолту open
    final override fun click() = println("click")

}