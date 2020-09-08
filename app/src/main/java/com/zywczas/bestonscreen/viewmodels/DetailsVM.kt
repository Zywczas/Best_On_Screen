package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.*
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.DetailsRepository
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.Resource

class DetailsVM(
    private val repo: DetailsRepository,
    private val isMovieInDbEventMLD: MediatorLiveData<Event<Resource<Boolean>>>,
    private val messageEventMLD: MediatorLiveData<Event<String>>
) : ViewModel() {

    val isMovieInDbLD = isMovieInDbEventMLD as LiveData<Event<Resource<Boolean>>>
    val messageLD = messageEventMLD as LiveData<Event<String>>

    fun checkIfIsInDb(movieId: Int) {
        val source = LiveDataReactiveStreams.fromPublisher(repo.checkIfMovieIsInDB(movieId))
        isMovieInDbEventMLD.addSource(source) {
            isMovieInDbEventMLD.postValue(it)
            isMovieInDbEventMLD.removeSource(source)
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
            repo.addMovieToDB(movie))
        messageEventMLD.addSource(source) { event ->
            messageEventMLD.postValue(event)
            messageEventMLD.removeSource(source)
        }
    }

    private fun deleteMovieFromDB(movie: Movie) {
        val source = LiveDataReactiveStreams.fromPublisher(
            repo.deleteMovieFromDB(movie))
        messageEventMLD.addSource(source) { event ->
            messageEventMLD.postValue(event)
            messageEventMLD.removeSource(source)
        }
    }


}