package com.podorozhniak.kotlinx.practice.view.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.podorozhniak.kotlinx.databinding.FragmentPageMockBinding

class RecViewPagerAdapterViewBinding : RecyclerView.Adapter<RecViewPagerAdapterViewBinding.PagerViewBindingVH>() {

    private val pageList = mutableListOf<String>()

    fun addPage(page: String) {
        pageList.add(page)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewBindingVH {
        val itemBinding = FragmentPageMockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewBindingVH(itemBinding)
    }

    override fun onBindViewHolder(holder: PagerViewBindingVH, position: Int) {
        holder.bind(pageList[position])
    }

    override fun getItemCount(): Int = pageList.size

    class PagerViewBindingVH(private val itemBinding: FragmentPageMockBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(pageNumber: String) {
            itemBinding.apply {
                tvPageNumber.text = pageNumber
            }
        }
    }
}
