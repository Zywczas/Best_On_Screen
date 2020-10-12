package com.zywczas.bestonscreen


import com.zywczas.bestonscreen.di.DaggerBestOnScreenComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class BestOnScreenApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerBestOnScreenComponent.factory().create(this)
    }

}