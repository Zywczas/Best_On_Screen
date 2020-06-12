package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.MovieRepository


class MoviesVM (private val repo: MovieRepository,
                //not used yet but implemented for future expansion
                private val handle: SavedStateHandle
) : ViewModel() {

//    fun getLd() = handle.getLiveData<List<Movie>>(KEY_MOVIE)
//    fun saveNewLd(movies: List<Movie>) = handle.set(KEY_MOVIE, movies)

    fun clearDisposables() = repo.clearMoviesDisposables()



    fun getApiMovies(category: String) =
        repo.getMoviesFromApi(category) as LiveData<List<Movie>>



//    fun getMovies(category: Category) : LiveData<List<Movie>> {
//        return when(category) {
//            Category.TO_WATCH -> repo.getMoviesFromDB()
//            else -> repo.getMoviesFromApi(category)
//        }
//    }
    



}