package com.podorozhniak.kotlinx.practice.view.workmanager

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentWorkManagerBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import com.podorozhniak.kotlinx.practice.workmanager.WorkManagerConstants.RESULT
import com.podorozhniak.kotlinx.practice.workmanager.WorkManagerConstants.TAG_WORK

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
                Log.d(TAG_WORK, "is empty")
                return@observe
            }

            // We only care about the one output status.
            // Every continuation has only one worker tagged TAG_OUTPUT
            val workInfo = listOfWorkInfo[0]

            if (workInfo.state.isFinished) {
                val result = workInfo.outputData.getString(RESULT)
                Log.d(TAG_WORK, "finished. result is $result")
            } else {
                Log.d(TAG_WORK, "in progress")
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

