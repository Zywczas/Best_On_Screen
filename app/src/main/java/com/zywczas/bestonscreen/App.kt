package com.zywczas.bestonscreen

import android.app.Application
import com.zywczas.bestonscreen.di.DaggerMoviesComponent
import com.zywczas.bestonscreen.di.MoviesComponent
import com.zywczas.bestonscreen.di.MoviesModule

class App : Application() {

    companion object {
        lateinit var moviesComponent: MoviesComponent
    }

    override fun onCreate() {
        super.onCreate()
        moviesComponent = DaggerMoviesComponent.builder()
            .moviesModule(MoviesModule(this))
            .build()
    }


}