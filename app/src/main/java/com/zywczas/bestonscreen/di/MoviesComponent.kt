package com.zywczas.bestonscreen.di

import com.zywczas.bestonscreen.view.MovieDetailsActivity
import com.zywczas.bestonscreen.view.MoviesActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = [MoviesModule::class])
    interface MoviesComponent {

    fun inject (app: MoviesActivity)
    fun inject (app: MovieDetailsActivity)
}