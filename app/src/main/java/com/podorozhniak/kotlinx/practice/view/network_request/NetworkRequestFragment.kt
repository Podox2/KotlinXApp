package com.podorozhniak.kotlinx.practice.view.network_request

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentNetworkRequestBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import com.podorozhniak.kotlinx.practice.extensions.onClick
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class NetworkRequestFragment : BaseFragment<FragmentNetworkRequestBinding>() {
    private val viewModel: NetworkRequestViewModel by viewModel()

    override val layoutId: Int
        get() = R.layout.fragment_network_request

    override fun onCreateViewBinding(view: View) = FragmentNetworkRequestBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnDoRequestSimple.onClick {
                viewModel.getInfoFromNetworkSimpleWay()
            }
            btnDoRequestExt.onClick {
                viewModel.getInfoFromNetworkCoroutineExtension()
            }
            btnDoRequestAdapter.onClick {
                viewModel.getInfoFromNetworkCallAdapter()
            }
            btnDoRequestRx.onClick {
                viewModel.getInfoFromNetworkRx()
            }
            btnDoRequestCall.onClick {
                viewModel.getInfoFromNetworkCall()
            }
        }

        viewModel.infoFromRequest.observe(viewLifecycleOwner) {
            binding.tvInfoManualRequests.text = "$it ${++viewModel.requestsCounter}"
        }

        viewModel.stringToStringTrans.observe(viewLifecycleOwner) {
            binding.tvInfoManualRequestsTransformation.text = "$it ${++viewModel.requestsCounter}"
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.infoFromRequestFlow.collect {
                binding.tvInfoManualRequests.text = "$it ${++viewModel.requestsCounter}"
            }
        }

        //flow and live data periodic
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.latestNews.collect {
                binding.tvInfoPeriodicRequestFlow.text = "received via flow. value = $it"
            }
        }

        viewModel.latestNewsLiveData.observe(viewLifecycleOwner) {
            binding.tvInfoPeriodicRequestLiveData.text = "received via live data. value = $it"
        }

    }
}

