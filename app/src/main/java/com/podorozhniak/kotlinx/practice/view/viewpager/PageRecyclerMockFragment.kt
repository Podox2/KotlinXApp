package com.podorozhniak.kotlinx.practice.view.viewpager

import android.os.Bundle
import android.view.View
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentPageMockBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment

private const val ARG_PARAM = "param1"

class PageRecyclerMockFragment : BaseFragment<FragmentPageMockBinding>() {
    private var pageNumber: String? = null

    override val layoutId: Int
        get() = R.layout.fragment_page_mock


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pageNumber = it.getString(ARG_PARAM)
        }
    }

    override fun onCreateViewBinding(view: View) = FragmentPageMockBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvPageNumber.text = pageNumber
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            PageRecyclerMockFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, param1)
                }
            }
    }
}