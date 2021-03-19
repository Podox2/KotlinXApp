package com.podorozhniak.kotlinx.patterns.observer.observer

import com.podorozhniak.kotlinx.patterns.observer.Match

class FullInfoObserver : ScoreObserver {
    override fun update(match: Match) {
        print(match)
    }
}