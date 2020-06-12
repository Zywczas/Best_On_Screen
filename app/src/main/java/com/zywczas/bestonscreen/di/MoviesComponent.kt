package com.zywczas.bestonscreen.di

import com.zywczas.bestonscreen.views.DBMoviesActivity
import com.zywczas.bestonscreen.views.MovieDetailsActivity
import com.zywczas.bestonscreen.views.MoviesActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = [MoviesModule::class])
    interface MoviesComponent {

    fun inject (app: DBMoviesActivity)
    fun inject (app: MoviesActivity)
    fun inject (app: MovieDetailsActivity)
}