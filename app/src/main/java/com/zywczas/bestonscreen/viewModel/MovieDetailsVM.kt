package com.zywczas.bestonscreen.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.MovieRepository

class MovieDetailsVM (private val repo: MovieRepository) : ViewModel(){

    fun clear() = repo.clearMovieDetailsActivity()

    fun addMovieToDb (movie: Movie) = repo.addMovieToDB(movie) as LiveData<Event<String>>

//    fun getMovie(movieId: Int, context: Context) = repo.getMovieFromDB(movieId, context) as LiveData<Movie>
    fun checkIfMovieIsInDb(movieId: Int) = repo.checkIfMovieIsInDB(movieId) as LiveData<Event<Int>>

    fun deleteMovieFromDb(movie: Movie) = repo.deleteMovieFromDB(movie) as LiveData<Event<String>>


}