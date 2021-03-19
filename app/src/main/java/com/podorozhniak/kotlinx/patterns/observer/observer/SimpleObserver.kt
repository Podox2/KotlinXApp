package com.podorozhniak.kotlinx.patterns.observer.observer

import com.podorozhniak.kotlinx.patterns.observer.Match

class SimpleObserver : ScoreObserver {
    override fun update(match: Match) {
        println(match.result)
    }
}