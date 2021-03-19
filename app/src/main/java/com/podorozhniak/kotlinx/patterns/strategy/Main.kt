package com.podorozhniak.kotlinx.patterns.strategy

import com.podorozhniak.kotlinx.patterns.strategy.strategy.AdditionalStrategy
import com.podorozhniak.kotlinx.patterns.strategy.strategy.MultiplyStrategy

fun main() {
    val strategyUser = StrategyUser(AdditionalStrategy())

    val x = 12
    val y = 45

    val conditionalValue = (1..10).shuffled().last() % 2 == 0

    if (conditionalValue) {
        strategyUser.strategy.execute(x, y)
    } else {
        strategyUser.strategy = MultiplyStrategy()
        strategyUser.strategy.execute(x, y)
    }
}