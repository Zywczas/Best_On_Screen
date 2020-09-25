package com.zywczas.bestonscreen.di

import com.zywczas.bestonscreen.views.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity() : MainActivity

}