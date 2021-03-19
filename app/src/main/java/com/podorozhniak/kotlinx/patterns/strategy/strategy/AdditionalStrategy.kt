package com.podorozhniak.kotlinx.patterns.strategy.strategy

class AdditionalStrategy : Strategy {
    override fun execute(x: Int, y: Int) = println(x + y)
}