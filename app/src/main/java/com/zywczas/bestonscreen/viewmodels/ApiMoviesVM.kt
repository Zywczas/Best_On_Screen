package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.*
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.ApiMoviesRepo
import com.zywczas.bestonscreen.utilities.*


class ApiMoviesVM (private val repo: ApiMoviesRepo,
                   //SavedStateHandle not used yet, just implemented for future expansion
                   private val handle: SavedStateHandle
) : ViewModel() {

    private val moviesLd = repo.getMoviesFromApi(EMPTY_CATEGORY, 1)
    private var wasScreenRotated = false

    fun getLd() = moviesLd as LiveData<Triple<List<Movie>, Int, String>>

    fun wasScreenRotated() = wasScreenRotated
    fun setScreenNotRotated() { wasScreenRotated = false }
    fun setScreenRotated() { wasScreenRotated = true }

    fun getApiMovies(category: String, nextPage: Int) = repo.getMoviesFromApi(category, nextPage)


    fun clearDisposables() = repo.clearDisposables()
}