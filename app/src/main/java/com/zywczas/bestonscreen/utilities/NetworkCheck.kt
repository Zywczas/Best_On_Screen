package com.zywczas.bestonscreen.utilities

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkCheck @Inject constructor(private val app: Application) {

    var isNetworkConnected = false
        private set

    init {
        registerNetworkCallback()
    }

    private fun registerNetworkCallback() {
        val connectivityManager =
            app.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isNetworkConnected = true
                //todo przetestowac jak przeniose do view model i wtedy usunac te logi
                Log.d("networkcheck", "onAvailable: $isNetworkConnected")
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isNetworkConnected = false
                Log.d("networkcheck", "onLost: $isNetworkConnected")
            }

        })
    }

}