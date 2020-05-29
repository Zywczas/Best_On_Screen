package com.zywczas.bestonscreen.di

import android.app.Application
import androidx.lifecycle.ViewModelStoreOwner

class App : Application() {

    companion object {
        lateinit var moviesComponent: MoviesComponent
    }

    override fun onCreate() {
        super.onCreate()
        moviesComponent = DaggerMoviesComponent.builder()
            .build()
    }


}