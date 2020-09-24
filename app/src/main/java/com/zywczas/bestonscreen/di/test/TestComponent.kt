package com.zywczas.bestonscreen.di.test

import android.app.Application
import androidx.fragment.app.FragmentFactory
import com.zywczas.bestonscreen.di.ActivityModule
import com.zywczas.bestonscreen.di.BestOnScreenModule
import com.zywczas.bestonscreen.di.FragmentBindingModule
import dagger.BindsInstance
import dagger.Component

////todo mozliwe ze do usuniecie, sprawdzic przy testach
//@Component(
//    modules = [
//        BestOnScreenModule::class,
//        FragmentBindingModule::class,
//        //todo nie wiem czy to potrzebne
//        ActivityModule::class
//    ]
//)
//interface TestComponent {
//
//    @Component.Factory
//    interface TestFactory {
//        fun create(@BindsInstance app: Application): TestComponent
//    }
//
//    fun fragmentFactory() : FragmentFactory
//
//}