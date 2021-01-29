package com.podorozhniak.kotlinx.practice.view.viewpager

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentViewPagerBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment

class ViewPagerFragment : BaseFragment<FragmentViewPagerBinding>() {

    private val recViewPagerAdapter = RecViewPagerAdapter()
    private val recViewPagerAdapterViewBinding = RecViewPagerAdapterViewBinding()
    //if uses constructor with Fragment as parameter should be initialized after creating view
    private lateinit var stateViewPagerAdapter: StateViewPagerAdapter

    override val layoutId: Int
        get() = R.layout.fragment_view_pager

    override fun onCreateViewBinding(view: View) = FragmentViewPagerBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateViewPagerAdapter = StateViewPagerAdapter(this)
        addMockPages()
        binding.apply {
            viewPager.adapter = recViewPagerAdapter
            //viewPager.adapter = recViewPagerAdapterViewBinding
            //viewPager.adapter = stateViewPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = "OBJECT ${(position + 1)}"
            }.attach()
        }
    }

    private fun addMockPages() {
        recViewPagerAdapter.addPage("1")
        recViewPagerAdapter.addPage("2")
        recViewPagerAdapter.addPage("3")

        recViewPagerAdapterViewBinding.addPage("1")
        recViewPagerAdapterViewBinding.addPage("2")
        recViewPagerAdapterViewBinding.addPage("3")

        stateViewPagerAdapter.addPage("1")
        stateViewPagerAdapter.addPage("2")
        stateViewPagerAdapter.addPage("3")
    }
}