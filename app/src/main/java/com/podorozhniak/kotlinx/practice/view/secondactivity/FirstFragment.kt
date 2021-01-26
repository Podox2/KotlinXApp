package com.podorozhniak.kotlinx.practice.view.secondactivity

import android.os.Bundle
import android.view.View
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentFirstBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import androidx.fragment.app.setFragmentResultListener

class FirstFragment : BaseFragment<FragmentFirstBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_first

    override fun onCreateViewBinding(view: View) = FragmentFirstBinding.bind(view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //аналогічно працює і для нав компонента
        setFragmentResultListener("requestKey") { key, bundle ->
            val result = bundle.getString("bundleKey")
            binding.openSecondFragment.text = result ?: "second fragment"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            openSecondFragment.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .add(R.id.container, SecondFragment()).addToBackStack("tag").commit()
            }
        }
    }
}

