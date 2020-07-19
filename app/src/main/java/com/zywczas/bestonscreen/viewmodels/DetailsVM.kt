package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.DetailsRepository
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.logD
import kotlin.system.exitProcess

class DetailsVM (private val repo: DetailsRepository) : ViewModel(){

    fun clearDisposables() = repo.clearDisposables()

    fun checkIfMovieIsInDb(movieId: Int) = repo.checkIfMovieIsInDB(movieId) as LiveData<Boolean>

    //TODO poprawic exitprocess na exception i buttonChecked dac na boolean, funkcja ta robi 2 rzeczy add i detele -> podzielic na 2 funkcje
    fun addDeleteMovie(movie: Movie, buttonIsChecked: String) =
        when(buttonIsChecked){
            "false" -> repo.addMovieToDB(movie)
            "true" -> repo.deleteMovieFromDB(movie)
            else -> {logD("incorrect tag on the button")
                exitProcess(0)}
        } as LiveData<Event<String>>

    fun getGenresDescription(movie: Movie) : String {
        return when (movie.genresAmount) {
            1 -> "Genre: ${movie.genre1}"
            2 -> "Genres: ${movie.genre1}, ${movie.genre2}"
            3 -> "Genres: ${movie.genre1}, ${movie.genre2}, ${movie.genre3}"
            4 -> "Genres: ${movie.genre1}, ${movie.genre2}, ${movie.genre3}, ${movie.genre4}"
            5 -> "Genres: ${movie.genre1}, ${movie.genre2}, ${movie.genre3}, ${movie.genre4}, ${movie.genre5}"
            else -> "no information about genres"
        }
    }


}