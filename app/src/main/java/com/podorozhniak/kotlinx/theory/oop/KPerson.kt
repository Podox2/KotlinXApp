package com.podorozhniak.kotlinx.theory.oop

class KPerson(private var name: String?, var age: Int) {
    private var height: Int = 0
    var width: Int = 0
        get() = field.plus(1)
        set(width) {
            if (width != 0)
                field = width
        }

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        if (!name.isEmpty())
            this.name = name
    }

    fun getHeight(): Int {
        return if (!name!!.isEmpty())
            height
        else
            0
    }

    fun setHeight(height: Int) {
        if (height != 0)
            this.height = height
    }
}