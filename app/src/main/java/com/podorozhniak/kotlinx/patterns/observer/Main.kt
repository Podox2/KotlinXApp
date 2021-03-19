package com.podorozhniak.kotlinx.patterns.observer

import com.podorozhniak.kotlinx.patterns.observer.observable.ScoreProducerImpl
import com.podorozhniak.kotlinx.patterns.observer.observer.FullInfoObserver
import com.podorozhniak.kotlinx.patterns.observer.observer.SimpleObserver

fun main() {
    val simpleObserver = SimpleObserver()
    val fullInfoObserver = FullInfoObserver()

    val scoreProducer = ScoreProducerImpl()
    scoreProducer.addObserver(simpleObserver)
    scoreProducer.addObserver(fullInfoObserver)
    scoreProducer.notifyObservers(Match(
        result = "Chelsea 2 : 0 Atletico",
        tournament = "Champions League",
    ))
}