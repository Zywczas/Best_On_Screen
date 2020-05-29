package com.zywczas.bestonscreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.Repository
import javax.inject.Inject


class MoviesVMFactory @Inject constructor (private val repo: Repository, private val arrayList: ArrayList<Movie>)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesVM(repo, arrayList) as T
    }
}