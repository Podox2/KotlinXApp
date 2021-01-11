package com.podorozhniak.kotlinx.practice.view.home

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentRippleBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import com.podorozhniak.kotlinx.practice.extensions.onClick

class RipplesFragment : BaseFragment<FragmentRippleBinding>(){
    override val layoutId: Int
        get() = R.layout.fragment_ripple

    override fun onCreateViewBinding(view: View) = FragmentRippleBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            ivClose.onClick {}
        }
    }
}

