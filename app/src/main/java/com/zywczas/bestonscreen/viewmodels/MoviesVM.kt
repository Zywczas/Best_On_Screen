package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.MovieRepository
import com.zywczas.bestonscreen.utilities.KEY_MOVIE


class MoviesVM (private val repo: MovieRepository,
                //not used yet but implemented for future expansion
                private val handle: SavedStateHandle
) : ViewModel() {

//    fun getLd() = handle.getLiveData<List<Movie>>(KEY_MOVIE)
//    fun saveNewLd(movies: List<Movie>) = handle.set(KEY_MOVIE, movies)

    fun clearDisposables() = repo.clearMoviesDisposables()



    fun getApiMovies(category: Category) =
        repo.getMoviesFromApi(category) as LiveData<Event<List<Movie>>>



//    fun getMovies(category: Category) : LiveData<List<Movie>> {
//        return when(category) {
//            Category.TO_WATCH -> repo.getMoviesFromDB()
//            else -> repo.getMoviesFromApi(category)
//        }
//    }
    



}