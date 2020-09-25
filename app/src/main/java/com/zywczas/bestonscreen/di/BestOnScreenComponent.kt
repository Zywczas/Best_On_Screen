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
    //todo AndroidInjectionModule binds your app.Fragment to dagger. But If you want to use injection
    // in v4.fragment then you should add AndroidSupportInjectionModule.class
    AndroidSupportInjectionModule::class,
    FragmentModule::class,
    ActivityModule::class
])
interface BestOnScreenComponent : AndroidInjector<BestOnScreenApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application) : BestOnScreenComponent
    }

//todo to chyba nie jest potrzebne
    override fun inject(instance: BestOnScreenApp)

}