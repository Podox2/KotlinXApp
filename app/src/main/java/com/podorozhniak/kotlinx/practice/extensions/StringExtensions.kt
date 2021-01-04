package com.podorozhniak.kotlinx.practice.extensions

//функції можна прописувати не в класі
//вони замінюють static функції
fun joinToString(s: String): String{
    return "ss"
}

//функція-розширення для класу String
//String - тип-отримувач (receiver type)
//this - об'єкт-отримувач (receiver object)
//fun String.lastChar(): Char = this.get(this.length - 1)
//можна опустити this
/*
extension функція скомпілиться в функцію, в яку передається об'єкт,
ну і для цього об'єкту не можна достукатись до private полів
* */
fun String.lastChar(): Char = get(length - 1)



