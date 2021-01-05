package com.podorozhniak.kotlinx.practice.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentHomeBinding
import com.podorozhniak.kotlinx.practice.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(){
    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun onCreateViewBinding(view: View) = FragmentHomeBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCl.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCLFinalsFragment())
        }
    }
}



