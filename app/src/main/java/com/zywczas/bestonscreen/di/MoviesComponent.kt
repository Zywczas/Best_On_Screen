package com.zywczas.bestonscreen.di

import com.zywczas.bestonscreen.views.ApiMoviesActivity
import com.zywczas.bestonscreen.views.DBMoviesActivity
import com.zywczas.bestonscreen.views.MovieDetailsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = [MoviesModule::class])
    interface MoviesComponent {

    fun inject (app: DBMoviesActivity)
    fun inject (app: ApiMoviesActivity)
    fun inject (app: MovieDetailsActivity)
}