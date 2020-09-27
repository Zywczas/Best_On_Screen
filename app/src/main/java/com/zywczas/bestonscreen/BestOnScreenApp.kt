package com.zywczas.bestonscreen


import com.zywczas.bestonscreen.di.DaggerBestOnScreenComponent
import com.zywczas.bestonscreen.utilities.CheckNetwork
import com.zywczas.bestonscreen.utilities.Variables
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class BestOnScreenApp : DaggerApplication() {

    private val network = CheckNetwork(this)

    override fun onCreate() {
        super.onCreate()
        network.registerNetworkCallback()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerBestOnScreenComponent.factory().create(this)
    }

}