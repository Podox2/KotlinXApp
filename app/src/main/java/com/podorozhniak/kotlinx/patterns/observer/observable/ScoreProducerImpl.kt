package com.podorozhniak.kotlinx.patterns.observer.observable

import com.podorozhniak.kotlinx.patterns.observer.Match
import com.podorozhniak.kotlinx.patterns.observer.observer.ScoreObserver

class ScoreProducerImpl : ScoreProducer {

    private val observers = mutableListOf<ScoreObserver>()

    override fun addObserver(observer: ScoreObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: ScoreObserver) {
        observers.remove(observer)
    }

    override fun notifyObservers(match: Match) {
        observers.forEach {
            it.update(match)
        }
    }
}