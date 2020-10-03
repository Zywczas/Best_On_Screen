package com.zywczas.bestonscreen.di

import android.app.Application
import com.zywczas.bestonscreen.BestOnScreenApp
import com.zywczas.bestonscreen.utilities.NetworkCheck
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    BestOnScreenModule::class,
    AndroidInjectionModule::class,
    FragmentFactoryModule::class,
    ActivityBuilderModule::class
])
interface BestOnScreenComponent : AndroidInjector<BestOnScreenApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application) : BestOnScreenComponent
    }

}