package com.podorozhniak.kotlinx.patterns.observer.observer

import com.podorozhniak.kotlinx.patterns.observer.Match

interface ScoreObserver {
    fun update(match: Match)
}