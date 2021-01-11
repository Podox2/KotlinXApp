package com.podorozhniak.kotlinx.practice.base

import android.os.Bundle
import android.view.View
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.DialogFullscreenBinding
import com.podorozhniak.kotlinx.practice.extensions.onClick

class FullscreenDialogFragment : BaseFullScreenFragmentDialog<DialogFullscreenBinding>() {

    override val layoutId: Int
        get() = R.layout.dialog_fullscreen

    override fun onCreateViewBinding(view: View) = DialogFullscreenBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnButton.onClick {
                tvText.text = "WOAW!"
            }
        }
    }
}