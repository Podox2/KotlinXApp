package com.podorozhniak.kotlinx.practice.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.podorozhniak.kotlinx.practice.workmanager.WorkManagerConstants.RESULT

// бродкасти надсилає SimpleWorker
class WorkManagerBroadcastReceiver : BroadcastReceiver() {

    var broadcastHandler: WorkManagerBroadcastHandler? = null

    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.getBundleExtra(RESULT)
        val response = bundle?.getString(RESULT) ?: "null"
        broadcastHandler?.handleBroadcast(response)
    }

    interface WorkManagerBroadcastHandler {
        fun handleBroadcast(response: String)
    }
}