package com.podorozhniak.kotlinx.theory.oop

/*data class реалізує
пару функций equals()/hashCode(),
функцию toString() в форме "User(name=John, age=42)",
компонентные функции componentN(), которые соответствуют свойствам, в соответствии с порядком их объявления,
функцию copy() - copy shallow (поверхневе)
 */
data class DataClass(
    val id: String
)