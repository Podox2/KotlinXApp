package com.podorozhniak.kotlinx.practice.view.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ServiceBroadcastReceiver(private val listener: (Int, Int, Int) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val task = intent!!.getIntExtra(PARAM_TASK, 0)
        val status = intent.getIntExtra(PARAM_STATUS, 0)
        val result = intent.getIntExtra(PARAM_RESULT, 0)
        listener(task, status, result)
    }

}