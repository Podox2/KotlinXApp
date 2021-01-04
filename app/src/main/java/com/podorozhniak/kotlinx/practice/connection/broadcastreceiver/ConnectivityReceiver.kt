package com.podorozhniak.kotlinx.practice.connection.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        connectivityReceiverListener?.onNetworkConnectionChanged()
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged()
    }

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
    }
}
