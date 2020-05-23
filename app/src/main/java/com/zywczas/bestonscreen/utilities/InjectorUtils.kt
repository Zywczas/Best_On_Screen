package com.zywczas.bestonscreen.utilities

import com.zywczas.bestonscreen.model.Repository
import com.zywczas.bestonscreen.viewModel.MoviesViewModelFactory

object InjectorUtils {

    fun provideMoviesViewModelFactory () : MoviesViewModelFactory {
        val repository = Repository.getInstance()
        return MoviesViewModelFactory(repository)
    }
}