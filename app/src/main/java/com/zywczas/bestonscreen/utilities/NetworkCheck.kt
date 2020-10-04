package com.zywczas.bestonscreen.utilities

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class NetworkCheck @Inject constructor(private val app: Application) {

    private var isNetworkConnected = false

    init {
        registerNetworkCallback()
    }
    //todo dac test sprawdzajacy czy to ten sam obiekt jest podawany
    private fun registerNetworkCallback() {
        val connectivityManager =
            app.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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

    open fun isConnected() = isNetworkConnected

}