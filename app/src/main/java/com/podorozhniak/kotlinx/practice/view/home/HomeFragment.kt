package com.podorozhniak.kotlinx.practice.view.home

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentHomeBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import com.podorozhniak.kotlinx.practice.di.appContext
import com.podorozhniak.kotlinx.practice.extensions.*
import com.podorozhniak.kotlinx.practice.util.MemoryManager
import com.podorozhniak.kotlinx.practice.util.Screen
import com.podorozhniak.kotlinx.practice.view.MainActivity

class HomeFragment : BaseFragment<FragmentHomeBinding>(){
    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun onCreateViewBinding(view: View) = FragmentHomeBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClFinals.onClick {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCLFinalsFragment())
        }
        binding.btnClCountdown.onClick {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCLCountdownFragment())
        }
        binding.btnChangeStatusBarColor.onClick {
            (requireActivity() as MainActivity).animationStatusBarColor(
                R.color.white,
                R.color.purple_500
            )
        }
        binding.btnGetDisplaySizes.onClick {
            getDisplaySizes()
        }
        binding.btnMemory.onClick {
            checkMemories()
        }
        binding.btnTest.onClick {
        }
    }

    private fun getDisplaySizes() {
        log("density - ${Screen.getDisplayDensityViaContext(appContext)}")
        log("density2 - ${Screen.getDisplayDensity()}")

        log("width")
        val widthInPx = (requireActivity() as MainActivity).widthScreenInPx()
        val widthInDp = (requireActivity() as MainActivity).widthScreenInDp()
        log("in px - $widthInPx")
        log("in dp - $widthInDp")

        log("height")
        val heightInPx = (requireActivity() as MainActivity).heightScreenInPx()
        val heightInDp = (requireActivity() as MainActivity).heightScreenInDp()
        log("in px - $heightInPx")
        log("in dp - $heightInDp")
    }

    private fun checkMemories(){
        log("total memory - ${MemoryManager.getTotalInternalMemorySize()}")
        log("available memory - ${MemoryManager.getAvailableInternalMemorySize()}")
        MemoryManager.checkMemory((appContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager))
    }
}



