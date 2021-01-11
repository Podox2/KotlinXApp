package com.podorozhniak.kotlinx.practice.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.podorozhniak.kotlinx.R

abstract class BaseFullScreenFragmentDialog<Binding : ViewBinding> : DialogFragment() {

    lateinit var binding: Binding
    protected abstract val layoutId: Int

    abstract fun onCreateViewBinding(view: View): Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = onCreateViewBinding(inflater.inflate(layoutId, container, false))
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

}
