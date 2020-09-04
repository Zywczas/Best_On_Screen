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


}