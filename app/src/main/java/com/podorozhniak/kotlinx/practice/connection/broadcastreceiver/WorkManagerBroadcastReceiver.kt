package com.podorozhniak.kotlinx.practice.connection.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.RESULT

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