package com.podorozhniak.kotlinx.practice.view.timer

const val DEFAULT_TIME = 3

object TimerContract {

    data class TimerState(
        val timeLeft: Int = DEFAULT_TIME,
        val isTimerRunning: Boolean = false,
        val hasTimeLeft: Boolean = false,
    )

    sealed interface TimerEvent {
        data object Finished : TimerEvent
    }

}