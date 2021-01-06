package com.podorozhniak.kotlinx.practice.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.getLastVisibleItemPosition(): Int {
    return (this.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition() + 1
}

fun RecyclerView.getFirstVisibleItemPosition(): Int {
    return (this.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
}