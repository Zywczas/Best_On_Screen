package com.zywczas.bestonscreen.di

import android.app.Application
import com.zywczas.bestonscreen.BestOnScreenApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppProvidesModule::class,
    AndroidInjectionModule::class,
    FragmentFactoryModule::class,
    ViewModelFactoryModule::class,
    ActivityBuilderModule::class
])
interface BestOnScreenComponent : AndroidInjector<BestOnScreenApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application) : BestOnScreenComponent
    }
}
