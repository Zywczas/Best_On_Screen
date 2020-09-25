package com.zywczas.bestonscreen.di

import com.zywczas.bestonscreen.views.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

//todo to moze usunac, zmienic nazwy na activity builder i fragment builder
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity() : MainActivity


}