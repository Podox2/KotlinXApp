package com.podorozhniak.kotlinx.practice.view.viewpager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.podorozhniak.kotlinx.R

class RecViewPagerAdapter : RecyclerView.Adapter<RecViewPagerAdapter.PagerVH>() {

    private val pageList = mutableListOf<String>()

    fun addPage(page: String) {
        pageList.add(page)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.fragment_page_mock, parent, false))


    override fun onBindViewHolder(holder: PagerVH, position: Int) {
        holder.bind(pageList[position])
    }

    override fun getItemCount(): Int = pageList.size

    class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var tvPageNumber: TextView

        fun bind(pageNumber: String) {
            tvPageNumber = itemView.findViewById(R.id.tv_page_number)
            tvPageNumber.text = pageNumber
        }
    }
}
