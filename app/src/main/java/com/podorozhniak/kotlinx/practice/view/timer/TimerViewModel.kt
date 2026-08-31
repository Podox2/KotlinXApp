package com.podorozhniak.kotlinx.practice.view.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class TimerViewModel : ViewModel() {

    private val _state = MutableStateFlow(TimerContract.TimerState())
    val state = _state.asStateFlow()

    private val _event = Channel<TimerContract.TimerEvent>()
    val event = _event.receiveAsFlow()

    private var job: Job? = null

    fun startPauseTimer() {
        if (_state.value.isTimerRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        job = viewModelScope.launch {

            updateIsTimerRunning(true)

            while (_state.value.timeLeft > 0) {
                delay(1000.milliseconds)
                _state.update {
                    it.copy(timeLeft = it.timeLeft - 1)
                }
            }

            updateIsTimerRunning(false)
            _state.update {
                it.copy(hasTimeLeft = true)
            }
            job?.cancel()
        }

        viewModelScope.launch {
            _event.send(TimerContract.TimerEvent.Finished)
        }
    }

    private fun pauseTimer() {
        job?.cancel()
        updateIsTimerRunning(false)
    }

    fun resetTimer() {
        job?.cancel()
        _state.update {
            it.copy(
                timeLeft = DEFAULT_TIME,
                isTimerRunning = false,
                hasTimeLeft = false
            )
        }
    }

    private fun updateIsTimerRunning(isTimerRunning: Boolean) {
        _state.update {
            it.copy(isTimerRunning = isTimerRunning)
        }
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }
}