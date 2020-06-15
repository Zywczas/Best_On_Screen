package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zywczas.bestonscreen.model.ApiMoviesRepo
import com.zywczas.bestonscreen.model.MovieDetailsRepo
import com.zywczas.bestonscreen.viewmodels.MovieDetailsVM
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDetailsVMFactory @Inject constructor(private val repo: MovieDetailsRepo) :
    ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailsVM(repo) as T
    }
}