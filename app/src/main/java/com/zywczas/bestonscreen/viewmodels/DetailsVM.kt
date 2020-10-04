package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.DetailsRepository
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.Event
import javax.inject.Inject

class DetailsVM @Inject constructor(private val repo: DetailsRepository) : ViewModel() {

    private val isMovieInDbMLD by lazy { MediatorLiveData<Event<Boolean>>() }
    private val messageMLD by lazy { MediatorLiveData<Event<String>>() }
    val isMovieInDbLD : LiveData<Event<Boolean>> by lazy { isMovieInDbMLD }
    val messageLD : LiveData<Event<String>> by lazy { messageMLD }

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

    private fun addMovieToDB(movie: Movie) {
        val source = LiveDataReactiveStreams.fromPublisher(
            repo.addMovieToDB(movie)
        )
        messageMLD.addSource(source) { event ->
            messageMLD.postValue(event)
            messageMLD.removeSource(source)
        }
    }

    private fun deleteMovieFromDB(movie: Movie) {
        val source = LiveDataReactiveStreams.fromPublisher(
            repo.deleteMovieFromDB(movie)
        )
        messageMLD.addSource(source) { event ->
            messageMLD.postValue(event)
            messageMLD.removeSource(source)
        }
    }


}