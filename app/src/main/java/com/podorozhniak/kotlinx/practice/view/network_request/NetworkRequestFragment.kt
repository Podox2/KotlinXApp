package com.podorozhniak.kotlinx.practice.view.network_request

import android.os.Bundle
import android.view.View
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentNetworkRequestBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import com.podorozhniak.kotlinx.practice.extensions.onClick
import org.koin.androidx.viewmodel.ext.android.viewModel

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
            binding.tvInfo.text = "$it ${++viewModel.requestsCounter}"
        }
    }
}

