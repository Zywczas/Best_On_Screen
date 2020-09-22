package com.zywczas.bestonscreen.di

import com.zywczas.bestonscreen.views.ApiActivity
import com.zywczas.bestonscreen.views.DBActivity
import com.zywczas.bestonscreen.views.DetailsActivity
import com.zywczas.bestonscreen.views.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MoviesModule::class])
interface MoviesComponent {
//todo pousuwac nieuzywane
    fun inject(app: DBActivity)
    fun inject(app: ApiActivity)
    fun inject(app: DetailsActivity)
    fun inject(app: MainActivity)
}