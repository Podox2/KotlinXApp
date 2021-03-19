package com.podorozhniak.kotlinx.patterns.builder

/* якщо є об'єкти з великою кількістю полів, які мають дефолтні значення, але їх можна змінити*/
data class Hero(
    val healthPoints: Int,
    val manaPoints: Int,
    val heroClass: String
)

class HeroBuilder {
    private var healthPoints: Int = 1000
    private var manaPoints: Int = 300
    private var heroClass: String = "Wizard"

    fun healthPoints(value: Int): HeroBuilder {
        healthPoints = value
        return this
    }

    fun manaPoints(value: Int): HeroBuilder {
        manaPoints = value
        return this
    }

    fun heroClass(value: String): HeroBuilder {
        heroClass = value
        return this
    }

    fun build(): Hero =
        Hero(
            healthPoints = healthPoints,
            manaPoints = manaPoints,
            heroClass = heroClass
        )
}

fun main() {
    val heroDefault = HeroBuilder()
        .build()

    val heroLittleCustomized = HeroBuilder()
        .heroClass("Archer")
        .build()

    val heroFullyCustomized = HeroBuilder()
        .healthPoints(1300)
        .manaPoints(100)
        .heroClass("Archer")
        .build()

    println(heroDefault)
    println(heroLittleCustomized)
    println(heroFullyCustomized)
}