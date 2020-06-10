package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.MovieRepository


class MoviesVM constructor (private val repo: MovieRepository,
                            //list to store movies for Recycler view in Movie Activity
                            val movies: ArrayList<Movie>
) : ViewModel() {

    fun clear() = repo.clearMoviesDisposables()

//    fun getApiMovies(category: Category) =
//        repo.getMoviesFromApi(category) as LiveData<Event<List<Movie>>>
//
//    fun getDbMovies() =
//        repo.getMoviesFromDB() as LiveData<Event<List<Movie>>>

    fun getMovies(category: Category) : LiveData<List<Movie>> {
        return when(category) {
            Category.TO_WATCH -> repo.getMoviesFromDB()
            else -> repo.getMoviesFromApi(category)
        }
    }

//    fun getMovies(category: Category) : LiveData<Event<List<Movie>>> {
//        return when(category) {
//            Category.TO_WATCH -> repo.getMoviesFromDB()
//            else -> repo.getMoviesFromApi(category)
//        }
//    }



}