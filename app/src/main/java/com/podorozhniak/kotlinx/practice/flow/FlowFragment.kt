package com.podorozhniak.kotlinx.practice.flow

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
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
            btnNavigate.setOnClickListener {
                findNavController().navigate(FlowFragmentDirections.openEpamFragment())
            }
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
                subscribeToObservables()
                //subscribeToFlow()
            }
            btnChannel.setOnClickListener {
                flowViewModel.updateChannel()
            }
        }
    }

    private fun subscribeToObservables() {
        //при повороті екрану обробить значення і відобразиться тост
        flowViewModel.mappedTextLiveData.observe(viewLifecycleOwner) {
            binding.tvLiveData.text = it
            //Snackbar.make(binding.root, "$it (LiveData)", Snackbar.LENGTH_LONG).show()
        }

        //при повороті екрану значення вже буде оброблене до цього і тому тост не відобразиться
        flowViewModel.textEventLiveData.observe(viewLifecycleOwner) {
            binding.tvEvent.text = it.getContentIfNotHandled()
            it.getContentIfNotHandled()?.let { text ->
                //Snackbar.make(binding.root, "$text (EventLivaData)", Snackbar.LENGTH_LONG).show()
            }
        }

        //потрібно юзати launchWhenStarted
        //можна робити всякі операції з flow (н-д, map)
        //при повороті екрану flow заемітить значення, воно обробиться і відобразиться тост
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            flowViewModel.textStateFlow
                .map { text -> "$text was mapped (StateFlow)" }
                .collectLatest {
                    binding.tvStateFlow.text = it
                    //Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                }
        }

        //при повороті екрану тост не відобразиться
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            flowViewModel.textSharedFlow
                .map { text -> "$text was mapped (SharedFlow)" }
                .collectLatest {
                    Log.d("Flows_TAG", "$it (SharedFlow)")
                    binding.tvSharedFlow.text = it
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                flowViewModel.channel.collect {
                    Log.d("Flows_TAG", "$it (Channel)" )
                    binding.tvChannel.text = it
                    Snackbar.make(binding.root, "$it (Channel)", Snackbar.LENGTH_LONG).show()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                flowViewModel.channel.collect {
                    Log.d("Flows_TAG", "$it (Channel2)" )
                    binding.tvChannel.text = it
                    Snackbar.make(binding.root, "$it (Channel2)", Snackbar.LENGTH_LONG).show()
                }
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