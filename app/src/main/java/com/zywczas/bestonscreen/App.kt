package com.zywczas.bestonscreen

import android.app.Application
import com.zywczas.bestonscreen.di.DaggerMoviesComponent
import com.zywczas.bestonscreen.di.MoviesComponent
import com.zywczas.bestonscreen.di.MoviesModule
import com.zywczas.bestonscreen.utilities.CheckNetwork

class App : Application() {

    private val network = CheckNetwork(this)

    companion object {
        lateinit var moviesComponent: MoviesComponent
    }


    override fun onCreate() {
        super.onCreate()
        moviesComponent = DaggerMoviesComponent.builder()
            .moviesModule(MoviesModule(this))
            .build()
        network.registerNetworkCallback()
    }
//todo usunac
//    override fun onCreate() {
//        super.onCreate()
//        moviesComponent = DaggerMoviesComponent.builder()
//            .app
//
//            .moviesModule(MoviesModule(this))
//
//            .build()
//        network.registerNetworkCallback()
//    }

}