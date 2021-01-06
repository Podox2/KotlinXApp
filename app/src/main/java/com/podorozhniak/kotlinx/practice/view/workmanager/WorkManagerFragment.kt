package com.podorozhniak.kotlinx.practice.view.workmanager

import android.os.Bundle
import android.view.View
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentWorkManagerBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import com.podorozhniak.kotlinx.practice.connection.worker.MyWorkManager

class WorkManagerFragment : BaseFragment<FragmentWorkManagerBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_work_manager

    override fun onCreateViewBinding(view: View) = FragmentWorkManagerBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnStartSimpleWork.setOnClickListener {
                MyWorkManager.startSimpleWork()
            }
            btnStopSimpleWork.setOnClickListener {
                MyWorkManager.stopSimpleWork()
            }

            btnStartPeriodicWork.setOnClickListener {
                MyWorkManager.startPeriodicWork()
            }
            btnStopPeriodicWork.setOnClickListener {
                MyWorkManager.stopPeriodicWork()
            }
        }
    }
}

