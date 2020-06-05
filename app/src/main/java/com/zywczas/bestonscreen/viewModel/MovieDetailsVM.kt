package com.zywczas.bestonscreen.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.MovieRepository

class MovieDetailsVM (private val repo: MovieRepository) : ViewModel(){

    fun clear() = repo.clear()

    fun addMovieToWatchList (movie: Movie, context: Context) = repo.addMovieToDB(movie, context)

//    fun getMovie(movieId: Int, context: Context) = repo.getMovieFromDB(movieId, context) as LiveData<Movie>
    fun checkIfMovieInToWatchList(movieId: Int) = repo.checkIfMovieInDB(movieId) as LiveData<Boolean>
}