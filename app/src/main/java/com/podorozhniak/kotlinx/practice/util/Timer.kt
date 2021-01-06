package com.podorozhniak.kotlinx.practice.util

import java.util.concurrent.TimeUnit

object Timer {
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var difference: Long = 0

    fun start() {
        startTime = System.nanoTime()
    }

    fun stop(): Long {
        endTime = System.nanoTime()
        difference = endTime - startTime
        difference = TimeUnit.MILLISECONDS.convert(
            difference,
            TimeUnit.NANOSECONDS
        )
        return difference
    }

}