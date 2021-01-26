package com.podorozhniak.kotlinx.practice.view.secondactivity

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentSecondBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment

class SecondFragment : BaseFragment<FragmentSecondBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_second

    override fun onCreateViewBinding(view: View) = FragmentSecondBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val result = "result"
        setFragmentResult("requestKey", bundleOf("bundleKey" to result))
    }
}

