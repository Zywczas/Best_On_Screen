package com.zywczas.bestonscreen.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import com.zywczas.bestonscreen.utilities.Variables.isNetworkConnected

class CheckNetwork(private val context: Context) {

    fun registerNetworkCallback() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isNetworkConnected = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isNetworkConnected = false
            }

        })
    }

}