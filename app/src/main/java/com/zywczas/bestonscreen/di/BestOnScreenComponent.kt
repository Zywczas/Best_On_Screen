package com.zywczas.bestonscreen.di

import android.app.Application
import com.zywczas.bestonscreen.BestOnScreenApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    BestOnScreenModule::class,
    AndroidSupportInjectionModule::class,
    FragmentBindingModule::class,
    ActivityModule::class
])
interface BestOnScreenComponent : AndroidInjector<BestOnScreenApp> {

    override fun inject(instance: BestOnScreenApp)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application) : BestOnScreenComponent
    }

}