package com.zywczas.bestonscreen.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Event
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.MovieRepository

class MovieDetailsVM (private val repo: MovieRepository) : ViewModel(){

    fun clear() = repo.clear()

    fun addMovieToWatchList (movie: Movie) = repo.addMovieToDB(movie)
//            as LiveData<Event<String>>

//    fun getMovie(movieId: Int, context: Context) = repo.getMovieFromDB(movieId, context) as LiveData<Movie>
    fun checkIfMovieInToWatchList(movieId: Int) = repo.checkIfMovieInDB(movieId) as LiveData<Event<Int>>

    fun deleteMovieFromToWatchList(movie: Movie) = repo.deleteMovieFromDB(movie)

}