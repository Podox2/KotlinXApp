package com.podorozhniak.kotlinx.practice.view.home

import android.animation.ObjectAnimator
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialFadeThrough
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentHomeBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import com.podorozhniak.kotlinx.practice.di.appContext
import com.podorozhniak.kotlinx.practice.extensions.*
import com.podorozhniak.kotlinx.practice.util.MemoryManager
import com.podorozhniak.kotlinx.practice.util.Screen
import com.podorozhniak.kotlinx.practice.util.viewhelper.CustomTextFormatter
import com.podorozhniak.kotlinx.practice.util.viewhelper.CustomTextFormatter.Companion.PHONE_PATTERN
import com.podorozhniak.kotlinx.practice.util.viewhelper.CustomTextWatcher
import com.podorozhniak.kotlinx.practice.view.MainActivity

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    companion object {
        private const val SHAKE_ANIMATION_DURATION = 500L
    }

    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun onCreateViewBinding(view: View) = FragmentHomeBinding.bind(view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFade().apply {
            duration = 150L
        }
        exitTransition = MaterialFadeThrough()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            //navigation
            btnClFinals.onClick {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCLFinalsFragment())
            }
            btnClCountdown.onClick {
                /*val navOptions =
                    NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()*/
                findNavController().navigate(R.id.action_homeFragment_to_CLCountdownFragment)
            }
            btnFragmentsTransitionAnimation.onClick {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTransitionsAnimationsFragment())
            }

            //UI changes
            btnChangeStatusBarColor.onClick {
                (requireActivity() as MainActivity).animationStatusBarColor(
                    R.color.white,
                    R.color.purple_500
                )
            }
            btnChangeNavBarColor.onClick {
                (requireActivity() as MainActivity).changeNavBarColor()
            }
            btnHideSystemUi.onClick {
                (requireActivity() as MainActivity).hideSystemUI()
            }
            btnShowFlashAnimation.onClick {
                showFlashAnimation()
            }
            btnShowShakeAnimation.onClick {
                showShakeAnimation()
            }

            //info
            btnGetDisplaySizes.onClick {
                getDisplaySizes()
            }
            btnMemory.onClick {
                checkMemories()
            }
            btnHandler.onClick {
                Handler(Looper.getMainLooper()).postDelayed({
                    toast("from looper")
                }, 3_000)
            }

            //to get real view sizes in px
            btnTest.post {
                log("viewWidthPx = ${binding.btnTest.width}")
                log("viewHeightPx = ${binding.btnTest.height}")
                //converting in dp
                log("viewWidthDp = ${Screen.convertPxToDp(binding.btnTest.width.toFloat())}")
                log("viewHeightDp = ${Screen.convertPxToDp(binding.btnTest.height.toFloat())}")
            }

            //приклад прграмного створення констрейнтів
            /*val constraintLayout: ConstraintLayout = root
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(
                R.id.iv_foreground,
                ConstraintSet.TOP,
                R.id.top_doodle_edge,
                ConstraintSet.TOP,
                0
            )
            constraintSet.applyTo(constraintLayout)*/

            etFirstName.addTextChangedListener(
                CustomTextWatcher(
                    etFirstName,
                    tiFirstName
                ) { etFirstName, tiFirstName ->
                        etFirstName.isPersonNameValid(tiFirstName)
                })

            etPhoneNumber.addTextChangedListener(
                CustomTextFormatter(
                    etPhoneNumber,
                    tiPhoneNumber,
                    PHONE_PATTERN
                ) { etPhoneNumber, tiPhoneNumber ->
                        etPhoneNumber.isPhoneValid(tiPhoneNumber)
                })
        }
    }

    private fun getDisplaySizes() {
        log("density via context - ${Screen.getDisplayDensityViaContext(appContext)}")
        log("density - ${Screen.getDisplayDensity()}")

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

    private fun checkMemories() {
        log("total memory - ${MemoryManager.getTotalInternalMemorySize()}")
        log("available memory - ${MemoryManager.getAvailableInternalMemorySize()}")
        log("${MemoryManager.logMemory()}")
        MemoryManager.checkMemory((appContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager))
    }

    private fun showShakeAnimation() {
        ObjectAnimator
            .ofFloat(
                binding.btnShowShakeAnimation,
                "translationX",
                0f,
                25f,
                -25f,
                25f,
                -25f,
                15f,
                -15f,
                6f,
                -6f,
                0f
            )
            .setDuration(SHAKE_ANIMATION_DURATION)
            .start()
    }
}



