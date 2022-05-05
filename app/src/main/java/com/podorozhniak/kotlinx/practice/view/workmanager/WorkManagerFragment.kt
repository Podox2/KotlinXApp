package com.podorozhniak.kotlinx.practice.view.workmanager

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentWorkManagerBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.TAG_NON_COROUTINE_WORK

class WorkManagerFragment : BaseFragment<FragmentWorkManagerBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_work_manager

    override fun onCreateViewBinding(view: View) = FragmentWorkManagerBinding.bind(view)

    private val workManagerViewModel: WorkManagerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        workManagerViewModel.outputWorkInfos.observe(viewLifecycleOwner) { listOfWorkInfo ->
            if (listOfWorkInfo.isEmpty()) {
                Log.d(TAG_NON_COROUTINE_WORK, "isEmpty")
                return@observe
            }

            // We only care about the one output status.
            // Every continuation has only one worker tagged TAG_OUTPUT
            val workInfo = listOfWorkInfo[0]

            if (workInfo.state.isFinished) {
                Log.d(TAG_NON_COROUTINE_WORK, "isFinished")
            } else {
                Log.d(TAG_NON_COROUTINE_WORK, "in progress")
            }
        }
    }

    private fun setClickListeners() {
        binding.apply {
            btnStartNonCoroutineWork.setOnClickListener {
                workManagerViewModel.startChainOfWorks()
            }
            btnStartSimpleWork.setOnClickListener {
                workManagerViewModel.startSimpleWork()
            }
            btnStopSimpleWork.setOnClickListener {
                workManagerViewModel.stopSimpleWork()
            }
            btnStartPeriodicWork.setOnClickListener {
                workManagerViewModel.startPeriodicWork()
            }
            btnStopPeriodicWork.setOnClickListener {
                workManagerViewModel.stopPeriodicWork()
            }
        }
    }
}

