package com.podorozhniak.kotlinx.practice.view.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class StateViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val pageList = mutableListOf<String>()

    fun addPage(page: String) {
        pageList.add(page)
    }

    override fun getItemCount(): Int = pageList.size

    override fun createFragment(position: Int): Fragment =
        PageRecyclerMockFragment.newInstance(pageList[position])

}