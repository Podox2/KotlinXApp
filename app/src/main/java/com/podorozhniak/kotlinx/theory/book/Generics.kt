package com.podorozhniak.kotlinx.theory.book

class LootBox<T : Loot>(vararg item: T) {
    var isOpen: Boolean = false
    private var loot: Array<out T> = item

    operator fun get(index: Int): T? = loot[index].takeIf { isOpen }

    fun fetch(item: Int): T? {
        return loot[item].takeIf { isOpen }
    }

    fun <R> fetch(item: Int, lootModFunction: (T) -> R): R? {
        return lootModFunction(loot[item]).takeIf { isOpen }
    }
}

class Barrel<out T>(val item: T)

open class Loot(val value: Int)
class Fedora(val name: String, value: Int) : Loot(value)
class Coin(value: Int) : Loot(value)

fun main() {
    val lootBoxOne: LootBox<Fedora> = LootBox(
        Fedora("a generic-looking fedora", 15),
        Fedora("a dazzling magenta fedora", 25)
    )
    val lootBoxTwo: LootBox<Coin> = LootBox(Coin(15))

    lootBoxOne.isOpen = true
    lootBoxOne.fetch(1)?.run {
        println("You retrieve $name from the box!")
    }

    val coin = lootBoxOne.fetch(0) {
        Coin(it.value * 3)
    }
    coin?.let { println(it.value) }

    val fedora = lootBoxOne[1]
    fedora?.let { println(it.name) }
    ////

    var fedoraBarrel: Barrel<Fedora> =
        Barrel(Fedora("a generic-looking fedora", 15))
    var lootBarrel: Barrel<Loot> = Barrel(Coin(15))
    lootBarrel = fedoraBarrel
    val myFedora: Fedora = lootBarrel.item
}