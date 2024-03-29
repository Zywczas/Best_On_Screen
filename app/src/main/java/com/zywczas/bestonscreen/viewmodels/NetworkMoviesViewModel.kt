package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.MovieCategory
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.repositories.NetworkMoviesRepository
import com.zywczas.bestonscreen.utilities.CONNECTION_PROBLEM
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.utilities.Status
import javax.inject.Inject

class NetworkMoviesViewModel @Inject constructor(
    private val repo: NetworkMoviesRepository,
    private val networkCheck: NetworkCheck
) : ViewModel() {

    private var page = 1
    private var category = MovieCategory.UPCOMING
    private var firstMoviesRequested = false
    private val movies = mutableListOf<Movie>()

    private val _moviesAndCategory = MediatorLiveData<Resource<Pair<List<Movie>, MovieCategory>>>()
    val moviesAndCategory: LiveData<Resource<Pair<List<Movie>, MovieCategory>>> = _moviesAndCategory

    fun getFirstMovies(nextCategory: MovieCategory) {
        if (firstMoviesRequested.not()) {
            firstMoviesRequested = true
            category = nextCategory
            getNextMoviesIfConnected()
        }
    }

    fun getNextMoviesIfConnected(nextCategory: MovieCategory = category) {
        if (networkCheck.isConnected) {
            getNextMovies(nextCategory)
        } else {
            sendError(CONNECTION_PROBLEM)
        }
    }

    private fun getNextMovies(nextCategory: MovieCategory) {
        val isNewCategory = nextCategory != category
        if (isNewCategory) {
            resetData()
            category = nextCategory
        }
        downloadAndSendMovies()
    }

    private fun resetData() {
        movies.clear()
        page = 1
    }

    private fun downloadAndSendMovies() {
        val source = LiveDataReactiveStreams.fromPublisher(
            repo.getApiMovies(category, page)
        )
        _moviesAndCategory.addSource(source) { repoResource ->
            when (repoResource.status) {
                Status.SUCCESS -> {
                    updateAndSendData(repoResource.data!!)
                }
                Status.ERROR -> {
                    repoResource.message!!.getContentIfNotHandled()?.let { sendError(it) }
                }
            }
            _moviesAndCategory.removeSource(source)
        }
    }

    private fun updateAndSendData(data: List<Movie>) {
        movies.addAll(data)
        _moviesAndCategory.postValue(Resource.success(Pair(movies.toList(), category)))
        page++
    }

    private fun sendError(message: String) {
        _moviesAndCategory.postValue(Resource.error(message, Pair(movies.toList(), category)))
    }

}
