package com.zywczas.bestonscreen.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.zywczas.bestonscreen.utilities.Variables.isNetworkConnected

class CheckNetwork (private val context: Context) {

    private val activeNetworks: MutableList<Network> = mutableListOf()

    fun registerNetworkCallback() {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            connectivityManager.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    if (activeNetworks.none { activeNetwork -> activeNetwork.networkHandle == network.networkHandle }) {
                        activeNetworks.add(network)
                    }
                    isNetworkConnected = activeNetworks.isNotEmpty()
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    activeNetworks.removeAll { activeNetwork -> activeNetwork.networkHandle == network.networkHandle }
                    isNetworkConnected = activeNetworks.isNotEmpty()
                }

            })
    }

}