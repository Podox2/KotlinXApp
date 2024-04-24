package com.podorozhniak.kotlinx

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ConnectivityObserver(
    private val context: Context
) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isNetworkConnected = isNetworkCapabilitiesValid(
        networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    )

    private fun isNetworkCapabilitiesValid(networkCapabilities: NetworkCapabilities?): Boolean {
        networkCapabilities.let {
            return when {
                it == null -> false
                it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                        (it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                it.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ||
                                it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) -> true

                else -> false
            }
        }
    }

    suspend fun observe(): Flow<ConnectivityStatus> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    trySend(ConnectivityStatus.Available)
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    if (isNetworkConnected.not()) {
                        trySend(ConnectivityStatus.Lost)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(ConnectivityStatus.Lost)
                }
            }

            // it checks connection at launch
            if (connectivityManager.activeNetwork == null) {
                if (isNetworkConnected.not()) {
                    trySend(ConnectivityStatus.Lost)
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }
    }
}