package com.zywczas.bestonscreen.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network

class CheckNetwork(private val context: Context) {

    fun registerNetworkCallback() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Variables.isNetworkConnected = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Variables.isNetworkConnected = false
            }

        })
    }

}