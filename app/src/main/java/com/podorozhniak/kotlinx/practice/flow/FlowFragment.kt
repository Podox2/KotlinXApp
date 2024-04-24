package com.podorozhniak.kotlinx.practice.flow

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentFlowBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
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
                flowViewModel.liveData1.value = "ld1"
            }
            btnEvent.setOnClickListener {
                flowViewModel.updateEventLiveData()
                flowViewModel.liveData2.value = "ld2"
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
                flowViewModel.setEffect("string")
            }
        }
    }

    private fun subscribeToObservables() {
        flowViewModel.mediatorLiveData.observe(viewLifecycleOwner) {
            Log.d("MEDIATOR_TAG", it)
        }

        //при повороті екрану обробить значення і відобразиться тост
        flowViewModel.mappedTextLiveData.observe(viewLifecycleOwner) {
            binding.tvLiveData.text = it
            //Snackbar.make(binding.root, "$it (LiveData)", Snackbar.LENGTH_LONG).show()
        }

        //при повороті екрану значення вже буде оброблене до цього і тому тост не відобразиться
        flowViewModel.textEventLiveData.observe(viewLifecycleOwner) {
            binding.tvEvent.text = it.peekContent()
            it.getContentIfNotHandled()?.let { text ->
                lifecycleScope.launch {
                    Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()
                }
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
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
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

        /*Note: In Fragments, always use viewLifecycleOwner.lifecycleScope.
        That’s because the Fragment’s view lifecycle can be different from the lifecycle
        of the Fragment itself.*/
        // поганий варіант - launch. flow створює айтеми навіть, якщо згорнути аплікуху.
        // а фрагмент досі живий і обробляє їх.
        /*viewLifecycleOwner.lifecycleScope.launch {
            flowViewModel.endlessFLow().collectLatest {
                println("FLOW: value is: $it")
            }
        }*/

        // правильніший варіант - launchWhenStarted. фрагмент не обробляє дані, але флоу досі їхтворює
        // This isn’t a desired behavior because it can waste CPU resources for something that might never appear on the screen
        /*viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            flowViewModel.endlessFLow().collectLatest {
                println("FLOW: value is: $it")
            }
        }*/

        // правильний варіант - repeatOnLifecycle
        /*First, you need to launch a new coroutine because repeatOnLifecycle is a suspend
        function. It also takes a Lifecycle.State as a parameter. This parameter
        automatically runs the code block in the coroutine when the lifecycle reaches the
        specified state. The coroutine will be canceled when the ON_STOP event happens and
        will restart its execution if the lifecycle receives the ON_START event again. You can
        remove flowCollectorJob?.cancel() from onStop because you don’t need it
        anymore.
        Note: It’s recommended to call repeatOnX methods from an activity’s
        onCreate or fragments onViewCreated methods to avoid unexpected
        behavior.*/
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flowViewModel.endlessFLow().collectLatest {
                    println("FLOW: value is: $it")
                }
            }
        }

        // інший правильний варіант - flowWithLifecycle
        /*A good recommendation is to use flowWithLifecycle when you need to collect only one flow
        and use repeatOnLifecycle when you have multiple flows*/
        /*viewLifecycleOwner.lifecycleScope.launch {
            flowViewModel.endlessFLow().flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    println("FLOW: value is: $it")
                }
        }*/

        lifecycleScope.launchWhenStarted {
            flowViewModel.channel.collectLatest {
                binding.tvChannel.text = it
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
            flowViewModel.triggerFlow()
                .map { "$it is mapped" }
                .flowOn(Dispatchers.Main)
                .catch { it.printStackTrace() }
                .collectLatest {
                    binding.tvFlow.text = it
                }
        }
    }
}