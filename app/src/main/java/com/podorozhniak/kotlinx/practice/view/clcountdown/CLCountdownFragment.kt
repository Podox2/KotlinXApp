package com.podorozhniak.kotlinx.practice.view.clcountdown

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentClCountdownBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import com.podorozhniak.kotlinx.practice.di.appContext
import com.podorozhniak.kotlinx.practice.util.getDifference

class CLCountdownFragment : BaseFragment<FragmentClCountdownBinding>(){
    override val layoutId: Int
        get() = R.layout.fragment_cl_countdown

    override fun onCreateViewBinding(view: View) = FragmentClCountdownBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureTextSwitcher()
        showRemainingTime()
    }

    private fun showRemainingTime() {
        val handler = Handler()
        val delay = 1000L
        handler.postDelayed(object : Runnable {
            override fun run() {
                binding.tvRemainingTime.setText(getDifference(appContext))
                handler.postDelayed(this, delay)
            }
        }, delay)
    }

    private fun configureTextSwitcher() {
        binding.tvRemainingTime.setFactory {
            TextView(
                ContextThemeWrapper(
                    requireContext(),
                    R.style.TextSwitcherStyle
                ), null, 0
            )
        }
        val inAnim = AnimationUtils.loadAnimation(
            requireContext(),
            android.R.anim.fade_in
        )
        val outAnim = AnimationUtils.loadAnimation(
            requireContext(),
            android.R.anim.fade_out
        )
        inAnim.duration = 200
        outAnim.duration = 200
        binding.tvRemainingTime.inAnimation = inAnim
        binding.tvRemainingTime.outAnimation = outAnim

    }
}



