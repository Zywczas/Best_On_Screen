package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.MovieRepository
import com.zywczas.bestonscreen.utilities.logD
import kotlin.system.exitProcess

class MovieDetailsVM (private val repo: MovieRepository
//                      , private val errorEventStringLd: MutableLiveData<Event<String>>
//                      ,  private val stringLiveData: LiveData<String>
                      ) : ViewModel(){

    fun clear() = repo.clearMovieDetailsActivity()

//    fun addMovieToDb (movie: Movie) = repo.addMovieToDB(movie) as LiveData<Event<String>>

//    fun getMovie(movieId: Int, context: Context) = repo.getMovieFromDB(movieId, context) as LiveData<Movie>
    fun checkIfMovieIsInDb(movieId: Int) = repo.checkIfMovieIsInDB(movieId) as LiveData<Event<Boolean>>
//    fun checkIfMovieIsInDb(movieId: Int) = repo.checkIfMovieIsInDB(movieId) as LiveData<Boolean>

//    fun deleteMovieFromDb(movie: Movie) = repo.deleteMovieFromDB(movie) as LiveData<Event<String>>

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