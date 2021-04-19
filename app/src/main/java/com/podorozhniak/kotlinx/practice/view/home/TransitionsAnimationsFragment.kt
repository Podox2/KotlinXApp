package com.podorozhniak.kotlinx.practice.view.home

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentTransitionsAnimationsBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import com.podorozhniak.kotlinx.practice.extensions.onClick

class TransitionsAnimationsFragment : BaseFragment<FragmentTransitionsAnimationsBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_transitions_animations

    override fun onCreateViewBinding(view: View) = FragmentTransitionsAnimationsBinding.bind(view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*enterTransition = MaterialFade()
        exitTransition = MaterialFade()
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, /* forward= */ false)*/
        enterTransition = MaterialFadeThrough()
    }

    /*
        * --------------           --------------
        * |            |           |            |
        * |            |           |            |
        * | fragment A | --------->| fragment B |
        * |            |           |            |
        * |            |           |            |
          --------------           --------------*/
    /*
    Enter anim — fragment B showing anim
    Exit anim — fragment A hiding anim
    Pop exit anim — fragment B hiding anim
    Pop enter anim — fragment A showing anim
    * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnSlidesUp.onClick {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_up)
                    .setExitAnim(R.anim.slide_down)
                    .setPopExitAnim(R.anim.slide_down)
                    .setPopEnterAnim(R.anim.slide_up)
                    .build()
                navigate(navOptions)
            }
            btnSlidesUpOffset.onClick {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_up_offset)
                    .setExitAnim(R.anim.slide_down)
                    .setPopExitAnim(R.anim.slide_down)
                    .setPopEnterAnim(R.anim.slide_up_offset)
                    .build()
                navigate(navOptions)
            }
            btnSlides.onClick {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right_to_left)
                    .setExitAnim(R.anim.slide_out_to_left_side)
                    .setPopExitAnim(R.anim.slide_out_to_right_side)
                    .setPopEnterAnim(R.anim.slide_in_left_to_right)
                    .build()
                navigate(navOptions)
            }
            btnCards.onClick {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right_to_left)
                    .setExitAnim(R.anim.wait)
                    .setPopExitAnim(R.anim.slide_out_to_right_side)
                    .setPopEnterAnim(R.anim.wait)
                    .build()
                navigate(navOptions)
            }
            btnIosLike.onClick {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right_to_left)
                    .setExitAnim(R.anim.slide_out_to_right_side_little)
                    .setPopExitAnim(R.anim.slide_out_to_right_side)
                    .setPopEnterAnim(R.anim.slide_in_from_left_side_little)
                    .build()
                navigate(navOptions)
            }
            btnFades.onClick {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(R.anim.fade_in)
                    .setExitAnim(R.anim.fade_out)
                    .setPopExitAnim(R.anim.fade_out)
                    .setPopEnterAnim(R.anim.fade_in)
                    .build()
                navigate(navOptions)
            }
            btnDiagonal.onClick {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(R.anim.diagonal_in)
                    .setExitAnim(R.anim.wait)
                    .setPopExitAnim(R.anim.diagonal_out)
                    .setPopEnterAnim(R.anim.wait)
                    .build()
                navigate(navOptions)
            }
            btnRotate.onClick {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(R.anim.rotate)
                    .setExitAnim(R.anim.wait)
                    .build()
                navigate(navOptions)
            }
            btnPulse.onClick {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(R.anim.pulse)
                    .setExitAnim(R.anim.wait)
                    .build()
                navigate(navOptions)
            }
        }
    }

    private fun navigate(navOptions: NavOptions) =
        findNavController().navigate(
            TransitionsAnimationsFragmentDirections.actionTransitionsAnimationsFragmentToToOpenFragment(),
            navOptions
        )

}

