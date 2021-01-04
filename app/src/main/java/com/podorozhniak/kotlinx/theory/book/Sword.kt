package com.podorozhniak.kotlinx.theory.book

//262 сторінка
class Sword(_name: String) {
    var name = _name
        get() {
            return "The Legendary $field"
        }
        set(value) {
            field = value.toLowerCase().reversed().capitalize()
        }

    init {
        //виклик сеттера
        name = _name
    }


    fun createSword() {
        val sword = Sword("Excalibur")
        println(sword.name)
        sword.name = "Gleipnir"
        println(sword.name)
    }
}
