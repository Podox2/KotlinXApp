package com.podorozhniak.kotlinx.theory.sequences

/*Note: Java 8 and Scala both have the concept of streams, which is the same as
a Sequence. Kotlin chose to use Sequence as a new class to avoid naming
conflicts when running on a Java 8 JVM and also be able to backport it to older
JVM targets.*/
fun main() {
    //listExample()
    sequenceExample()
}

// виводить filter, filter, filter, map, map, map, forEach, forEach, forEach,
// тобто всі елементи списку спочатку проходять через першу функцію (filter)
// потім через другу і т.д. тому що під капотом ці функції реалізовані
// через forEach. на кожному кроці створюється об'єкт для нового списку.
// тобто такий підхід має два недоліки:
// 1. Memory: New collections are returned at every step.
// 2. Performance: Complete collections are processed at every step
fun listExample() {
    val list = listOf(1, 2, 3)
    list.filter {
        print("filter, ")
        it > 0
    }.map { // 3
        print("map, ")
        it.toString()
    }.forEach { // 4
        print("forEach, ")
    }
}

// sequence - "колекції", які генерують якісь значення.
// sequence - handles the collection of items in a lazy evaluated manner. The items processed in
// a sequence are not evaluated until you access them. They are great at representing collection
// where the size isn’t known in advance, like reading lines from a file.
// виводить filter, map, forEach, filter, map, forEach, filter, map, forEach,
// тобто всі функції проходить не список, а елемент списку
// However, laziness also introduces some overhead, which is undesirable for common
// simple transformations of smaller collections and makes them less performant. It is
// still recommended to use simple Iterables in most of the cases. The benefit of
// using a Sequence is only when there is a large or infinite collection of elements with
// multiple operations, especially if you are filtering items.
fun sequenceExample() {
    val list = listOf(1, 2, 3)
    list.asSequence().filter {
        print("filter, ")
        it > 0
    }.map {
        print("map, ")
    }.forEach {
        print("forEach, ")
    }
}
