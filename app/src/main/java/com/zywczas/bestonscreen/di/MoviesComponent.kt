package com.zywczas.bestonscreen.di

import android.app.Application
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.views.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton
//todo usunac
//@Singleton
//@Component(modules = [
//    MoviesModule::class,
//    AndroidInjectionModule::class
//])
//interface MoviesComponent {
//
//    @Component.Builder
//    interface Builder {
//        @BindsInstance
//        fun application(app : Application) : Builder
//        fun build() : MoviesComponent
//    }
//
//    fun inject(app: App)
//}


@Singleton
@Component(modules = [MoviesModule::class])
interface MoviesComponent {

    fun inject(app: MainActivity)
}