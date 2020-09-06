package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.*
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.DetailsRepository
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.LiveEvent
import org.reactivestreams.Publisher

class DetailsVM(
    private val repo: DetailsRepository,
    private val isMovieInDbLE: LiveEvent<Boolean>,
    private val messageEventMLD: MediatorLiveData<Event<String>>
) : ViewModel() {
//todo jak zmienie na resource to sprawdzic czy dalej wysyla podwojne live data za krotryms razem po obrotach
    val isMovieInDbLD = isMovieInDbLE as LiveData<Boolean>
    val messageLD = messageEventMLD as LiveData<Event<String>>

    fun checkIfIsInDb(movieId: Int) {
        val source = LiveDataReactiveStreams.fromPublisher(repo.checkIfMovieIsInDB(movieId))
        isMovieInDbLE.addSource(source) { isMovieInDbLE.postValue(it) }
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