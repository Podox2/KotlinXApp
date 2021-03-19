package com.podorozhniak.kotlinx.patterns.strategy.strategy

class MultiplyStrategy : Strategy {
    override fun execute(x: Int, y: Int) = println(x * y)
}