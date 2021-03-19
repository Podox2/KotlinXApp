package com.podorozhniak.kotlinx.patterns.strategy.strategy

interface Strategy {
    fun execute(x: Int, y: Int)
}