package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.*
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.DetailsRepository
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.LiveEvent

class DetailsVM(
    private val repo: DetailsRepository,
    //todo poprawic nazwe
    private val isMovieInDbMLD: MediatorLiveData<Event<Boolean>>,
    private val messageEventMLD: MediatorLiveData<Event<String>>
) : ViewModel() {
//todo jak zmienie na resource to sprawdzic czy dalej wysyla podwojne live data za krotryms razem po obrotach
    val isMovieInDbLD = isMovieInDbMLD as LiveData<Event<Boolean>>
    val messageLD = messageEventMLD as LiveData<Event<String>>

    @Throws(Exception::class)
    fun checkIfIsInDb(movieId: Int) {
        val source = LiveDataReactiveStreams.fromPublisher(repo.checkIfMovieIsInDB(movieId))
        isMovieInDbMLD.addSource(source) {
            isMovieInDbMLD.postValue(it)
            isMovieInDbMLD.removeSource(source)
        }
    }

    fun addOrDeleteMovie(movie: Movie, isButtonChecked: Boolean) {
        when (isButtonChecked) {
            false -> addMovieToDB(movie)
            true -> deleteMovieFromDB(movie)
        }
    }

    @Throws(Exception::class)
    private fun addMovieToDB(movie: Movie) {
        val source = LiveDataReactiveStreams.fromPublisher(
            repo.addMovieToDB(movie))
        messageEventMLD.addSource(source) { event ->
            messageEventMLD.postValue(event)
            messageEventMLD.removeSource(source)
        }
    }

    @Throws(Exception::class)
    private fun deleteMovieFromDB(movie: Movie) {
        val source = LiveDataReactiveStreams.fromPublisher(
            repo.deleteMovieFromDB(movie))
        messageEventMLD.addSource(source) { event ->
            messageEventMLD.postValue(event)
            messageEventMLD.removeSource(source)
        }
    }


}