package com.podorozhniak.kotlinx.theory.flat

// flatMap об'єднує списки в один
data class Order(val id: Int, val items: List<String>)

val orders = listOf(
    Order(1, listOf("apple", "banana")),
    Order(2, listOf("cherry"))
)

// map: gives you a list of lists
val itemLists = orders.map { it.items }
// [[apple, banana], [cherry]]

// flatMap: gives you one flat list of all items
val allItems = orders.flatMap { it.items }
// [apple, banana, cherry]

// під капотом відбувається 2 операції - map {} і flatten()
val allItems2 = orders.map { it.items }.flatten()
// [apple, banana, cherry]

fun main() {
    println(itemLists)
    println(allItems)
    println(allItems2)
}
