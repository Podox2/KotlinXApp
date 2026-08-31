package com.podorozhniak.kotlinx.practice.view.timer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.podorozhniak.kotlinx.databinding.ActivityTimerBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimerActivity : AppCompatActivity() {

    private val timerViewModel: TimerViewModel by viewModel()

    private lateinit var binding: ActivityTimerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                timerViewModel.state.collect {
                    binding.tvTimer.text = formatTime(it.timeLeft)
                    binding.btnStart.text = if (it.isTimerRunning) "Pause" else "Start"
                    binding.btnStart.isEnabled = it.hasTimeLeft.not()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                timerViewModel.event.collect {
                    Snackbar.make(
                        binding.root, "Finished", Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.btnStart.setOnClickListener {
            timerViewModel.startPauseTimer()
        }

        binding.btnReset.setOnClickListener {
            timerViewModel.resetTimer()
        }
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return "%02d:%02d".format(minutes, secs)
    }
}