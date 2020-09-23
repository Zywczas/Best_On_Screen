package com.zywczas.bestonscreen.di

import com.zywczas.bestonscreen.views.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MoviesModule::class])
interface MoviesComponent {

    fun inject(app: MainActivity)
}