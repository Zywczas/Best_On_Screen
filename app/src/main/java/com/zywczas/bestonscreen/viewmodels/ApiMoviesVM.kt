package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.ApiMoviesRepo
import com.zywczas.bestonscreen.utilities.Event


class ApiMoviesVM (private val repo: ApiMoviesRepo,
                //not used yet but implemented for future expansion
                   private val handle: SavedStateHandle
) : ViewModel() {

//    fun getLd() = handle.getLiveData<List<Movie>>(KEY_MOVIE)
//    fun saveNewLd(movies: List<Movie>) = handle.set(KEY_MOVIE, movies)

    fun clearDisposables() = repo.clearDisposables()

//    fun getApiMovies(category: String) =
//        repo.getMoviesFromApi(category) as LiveData<Event<List<Movie>>>

    fun getApiMovies(category: String) =
        repo.getMoviesFromApi(category) as LiveData<List<Movie>>


}