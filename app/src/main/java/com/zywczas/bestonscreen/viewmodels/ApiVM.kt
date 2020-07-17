package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.EMPTY_CATEGORY


class ApiVM (private val repo: ApiRepository,
    //todo dodac jakies zastosowanie dla handle
                   //SavedStateHandle not used yet, just implemented for future expansion
             private val handle: SavedStateHandle
) : ViewModel() {

    private var isActivityInit = true
    private val moviesLd = repo.getMoviesFromApi(EMPTY_CATEGORY, 1)

    fun isActivityInitialization() = isActivityInit

    fun finishActivityInitialization(){
        isActivityInit = false
    }

    fun getLd() = moviesLd as LiveData<Triple<List<Movie>, Int, String>>

    fun getApiMovies(category: String, nextPage: Int) = repo.getMoviesFromApi(category, nextPage)

    fun clearDisposables() = repo.clearDisposables()
}