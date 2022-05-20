package com.podorozhniak.kotlinx.practice.flow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentFlowBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FlowFragment : BaseFragment<FragmentFlowBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_flow

    override fun onCreateViewBinding(view: View): FragmentFlowBinding {
        return FragmentFlowBinding.bind(view)
    }

    private val flowViewModel: FlowViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        subscribeToObservables()
    }

    private fun setOnClickListeners() {
        binding.apply {
            btnLiveData.setOnClickListener {
                flowViewModel.updateLiveData()
            }
            btnEvent.setOnClickListener {
                flowViewModel.updateEventLiveData()
            }
            btnStateFlow.setOnClickListener {
                flowViewModel.updateStateFlow()
            }
            btnSharedFlow.setOnClickListener {
                flowViewModel.updateSharedFlow()
            }
            btnFlow.setOnClickListener {
                subscribeToFlow()
            }
        }
    }

    private fun subscribeToObservables() {
        //при повороті екрану обробить значення і відобразиться тост
        flowViewModel.textLiveData.observe(viewLifecycleOwner) {
            binding.tvLiveData.text = it
            //Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        }

        //при повороті екрану значення вже буде оброблене до цього і тому тост не відобразиться
        flowViewModel.textEventLivaData.observe(viewLifecycleOwner) {
            binding.tvEvent.text = it.getContentIfNotHandled()
            it.getContentIfNotHandled()?.let { text ->
                Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()
            }
        }

        //потрібно юзати launchWhenStarted
        //можна робити всякі операції з flow (н-д, map)
        //при повороті екрану flow заемітить значення, воно обробиться і відобразиться тост
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            flowViewModel.textStateFlow
                .map { text -> "$text was mapped" }
                .collectLatest {
                    binding.tvStateFlow.text = it
                    //Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                }
        }

        //при повороті екрану тост не відобразиться
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            flowViewModel.textSharedFlow.collectLatest {
                binding.tvSharedFlow.text = it
                //Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun subscribeToFlow() {
        lifecycleScope.launch {
            flowViewModel.triggerFlow().collectLatest {
                binding.tvFlow.text = it
            }
        }
    }
}