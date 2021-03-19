package com.podorozhniak.kotlinx.patterns.observer.observable

import com.podorozhniak.kotlinx.patterns.observer.Match
import com.podorozhniak.kotlinx.patterns.observer.observer.ScoreObserver

interface ScoreProducer {
    fun addObserver(observer: ScoreObserver)
    fun removeObserver(observer: ScoreObserver)
    fun notifyObservers(match: Match)
}