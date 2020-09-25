package com.zywczas.bestonscreen.di

import androidx.fragment.app.Fragment
import dagger.MapKey
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class FragmentKey (val value: KClass<out Fragment>)