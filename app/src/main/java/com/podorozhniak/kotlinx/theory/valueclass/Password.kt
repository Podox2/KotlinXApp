package com.podorozhniak.kotlinx.theory.valueclass

/*Sometimes it is necessary for business logic to create a wrapper around some type.
However, it introduces runtime overhead due to additional heap allocations. Moreover,
if the wrapped type is primitive, the performance hit is terrible, because primitive
types are usually heavily optimized by the runtime,
while their wrappers don't get any special treatment.

спосіб створити тип-обгортку навколо одного значення без додаткових витрат пам'яті на
створення повноцінного об'єкта під час виконання програми
також можна тоді написати функцію, яка буде приймати саме тип Password, а не String*/
@JvmInline
value class Password(val value: String) {

    /*Inline (value) classes support some functionality of regular classes. In particular,
     they are allowed to declare properties and functions, and have the init block*/
    init {
        require(value.isNotEmpty()) { }
    }

    val length: Int
        get() = value.length

    fun hack() {
        println("password is - $value")
    }
}


fun main() {
    // No actual instantiation of class 'Password' happens
    // At runtime 'securePassword' contains just 'String'
    val securePassword = Password("433463")
}