package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.*
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.DetailsRepository
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.LiveEvent

class DetailsVM(
    private val repo: DetailsRepository,
    private val isMovieInDbLE: LiveEvent<Boolean>
) : ViewModel() {

    val isMovieInDbLD = isMovieInDbLE as LiveData<Boolean>

    fun checkIfIsInDb(movieId: Int) {
        val source = LiveDataReactiveStreams.fromPublisher(repo.checkIfMovieIsInDB(movieId))
        isMovieInDbLE.addSource(source) { isMovieInDbLE.postValue(it) }
    }

    fun addOrDeleteMovie(movie: Movie, isButtonChecked: Boolean) =
        when (isButtonChecked) {
            false -> repo.addMovieToDB(movie)
            true -> repo.deleteMovieFromDB(movie)
        } as LiveData<Event<String>>


    private fun updateBooleanLiveEvent(idCount: Int) {
        when (idCount) {
            0 -> isMovieInDbLE.postValue(false)
            else -> isMovieInDbLE.postValue(true)
        }
    }
}