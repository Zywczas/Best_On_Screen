package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.repositories.MovieDetailsRepository
import com.zywczas.bestonscreen.utilities.Event
import javax.inject.Inject

open class MovieDetailsViewModel @Inject constructor(private val repo: MovieDetailsRepository) : ViewModel() {

    private val _isMovieInDb = MediatorLiveData<Event<Boolean>>()
    private val _message = MediatorLiveData<Event<String>>()
    val isMovieInDb: LiveData<Event<Boolean>> = _isMovieInDb
    val message: LiveData<Event<String>> = _message

    fun checkIfIsInDb(movieId: Int) {
        val source = LiveDataReactiveStreams.fromPublisher(repo.checkIfMovieIsInDB(movieId))
        _isMovieInDb.addSource(source) {
            _isMovieInDb.postValue(it)
            _isMovieInDb.removeSource(source)
        }
    }

    fun addOrDeleteMovie(movie: Movie, isButtonChecked: Boolean) {
        when (isButtonChecked) {
            false -> addMovieToDB(movie)
            true -> deleteMovieFromDB(movie)
        }
    }

    private fun addMovieToDB(movie: Movie) {
        val source = LiveDataReactiveStreams.fromPublisher(
            repo.addMovieToDB(movie)
        )
        _message.addSource(source) { event ->
            _message.postValue(event)
            _message.removeSource(source)
        }
    }

    private fun deleteMovieFromDB(movie: Movie) {
        val source = LiveDataReactiveStreams.fromPublisher(
            repo.deleteMovieFromDB(movie)
        )
        _message.addSource(source) { event ->
            _message.postValue(event)
            _message.removeSource(source)
        }
    }

}
