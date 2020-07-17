package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie


class ApiVM (private val repo: ApiRepository,
    //todo dodac jakies zastosowanie dla handle
                   //SavedStateHandle not used yet, just implemented for future expansion
             private val handle: SavedStateHandle
) : ViewModel() {

    private var isViewModelInit = true
    //todo sprobowac zamienic to empty na mutator live data
    private val moviesLd = repo.getMoviesFromApi(Category.EMPTY, 1)

    fun isViewModelInitialization() = isViewModelInit

    fun finishViewModelInitialization(){
        isViewModelInit = false
    }

    fun getLd() = moviesLd as LiveData<Triple<List<Movie>, Int, Category>>

    fun getApiMovies(category: Category, nextPage: Int) = repo.getMoviesFromApi(category, nextPage)

    fun clearDisposables() = repo.clearDisposables()
}