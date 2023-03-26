package com.zywczas.bestonscreen.di

import androidx.fragment.app.Fragment
import com.zywczas.bestonscreen.views.NetworkMoviesFragment
import com.zywczas.bestonscreen.views.LocalMoviesFragment
import com.zywczas.bestonscreen.views.MovieDetailsFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FragmentFactoryModule {

    @Binds
    @IntoMap
    @FragmentKey(LocalMoviesFragment::class)
    abstract fun bindDBFragment(fragment: LocalMoviesFragment) : Fragment

    @Binds
    @IntoMap
    @FragmentKey(NetworkMoviesFragment::class)
    abstract fun bindApiFragment(fragment: NetworkMoviesFragment) : Fragment

    @Binds
    @IntoMap
    @FragmentKey(MovieDetailsFragment::class)
    abstract fun bindDetailsFragment(fragment: MovieDetailsFragment) : Fragment

}
