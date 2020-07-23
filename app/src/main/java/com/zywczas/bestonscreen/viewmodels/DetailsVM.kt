package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.DetailsRepository
import com.zywczas.bestonscreen.utilities.Event

class DetailsVM (private val repo: DetailsRepository) : ViewModel(){

    fun clearDisposables() = repo.clearDisposables()

    fun isMovieInDb(movieId: Int) = repo.checkIfMovieIsInDB(movieId) as LiveData<Boolean>

    fun addOrDeleteMovie(movie: Movie, isButtonChecked: Boolean) =
        when(isButtonChecked){
            false -> repo.addMovieToDB(movie)
            true -> repo.deleteMovieFromDB(movie)
        } as LiveData<Event<String>>

    fun getGenresDescription(movie: Movie) : String {
        return when (movie.assignedGenresAmount) {
            1 -> "Genre: ${movie.genre1}"
            2 -> "Genres: ${movie.genre1}, ${movie.genre2}"
            3 -> "Genres: ${movie.genre1}, ${movie.genre2}, ${movie.genre3}"
            4 -> "Genres: ${movie.genre1}, ${movie.genre2}, ${movie.genre3}, ${movie.genre4}"
            5 -> "Genres: ${movie.genre1}, ${movie.genre2}, ${movie.genre3}, ${movie.genre4}, ${movie.genre5}"
            else -> "no information about genres"
        }
    }


}