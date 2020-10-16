package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.DetailsRepository
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.Event
import javax.inject.Inject

open class DetailsVM @Inject constructor(private val repo: DetailsRepository) : ViewModel() {

    private val messageMLD by lazy { MediatorLiveData<Event<String>>() }
    val messageLD : LiveData<Event<String>> by lazy { messageMLD }
    lateinit var isMovieInDbLD : LiveData<Boolean>
    lateinit var movie: Movie

    fun getMovieAndInitIsInDbLD(movieFromFragment: Movie) {
        movie = movieFromFragment
        isMovieInDbLD = LiveDataReactiveStreams.fromPublisher(repo.checkIfMovieIsInDB(movie.id))
    }

    fun addOrDeleteMovie() {
        when (isMovieInDbLD.value) {
            false -> addMovieToDB()
            true -> deleteMovieFromDB()
        }
    }

    private fun addMovieToDB() {
        val source = LiveDataReactiveStreams.fromPublisher(
            repo.addMovieToDB(movie)
        )
        messageMLD.addSource(source) { event ->
            messageMLD.postValue(event)
            messageMLD.removeSource(source)
        }
    }

    private fun deleteMovieFromDB() {
        val source = LiveDataReactiveStreams.fromPublisher(
            repo.deleteMovieFromDB(movie)
        )
        messageMLD.addSource(source) { event ->
            messageMLD.postValue(event)
            messageMLD.removeSource(source)
        }
    }


}