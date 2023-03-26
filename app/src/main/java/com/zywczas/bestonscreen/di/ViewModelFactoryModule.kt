package com.zywczas.bestonscreen.di

import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.viewmodels.NetworkMoviesViewModel
import com.zywczas.bestonscreen.viewmodels.LocalMoviesViewModel
import com.zywczas.bestonscreen.viewmodels.MovieDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun bindDetailsVM(vm: MovieDetailsViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NetworkMoviesViewModel::class)
    abstract fun bindApiVM(vm: NetworkMoviesViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocalMoviesViewModel::class)
    abstract fun bindDbVM(vm: LocalMoviesViewModel) : ViewModel
}
