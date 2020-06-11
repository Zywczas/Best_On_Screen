package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zywczas.bestonscreen.model.MovieRepository
import com.zywczas.bestonscreen.viewmodels.MovieDetailsVM
import javax.inject.Inject

class MovieDetailsVMFactory
@Inject constructor(private val repo: MovieRepository) :
    ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailsVM(repo) as T
    }
}