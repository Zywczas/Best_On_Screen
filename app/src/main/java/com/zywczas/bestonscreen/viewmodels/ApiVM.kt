package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.CONNECTION_PROBLEM
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.utilities.Status
import javax.inject.Inject

class ApiVM @Inject constructor(
    private val repo: ApiRepository,
    private val networkCheck: NetworkCheck
) : ViewModel() {

    private var page = 1
    private var category = Category.UPCOMING
    private var firstMoviesRequested = false
    private val movies by lazy { mutableListOf<Movie>() }

    private val moviesAndCategoryMLD
            by lazy { MediatorLiveData<Resource<Pair<List<Movie>, Category>>>() }
    val moviesAndCategoryLD : LiveData<Resource<Pair<List<Movie>, Category>>>
            by lazy { moviesAndCategoryMLD }


    fun getFirstMovies(nextCategory: Category) {
        if (!firstMoviesRequested) {
            firstMoviesRequested = true
            category = nextCategory
            getNextMoviesIfConnected()
        }
    }

    fun getNextMoviesIfConnected(nextCategory: Category = category) {
        if (networkCheck.isNetworkConnected) {
            getNextMovies(nextCategory)
        } else {
            sendError(CONNECTION_PROBLEM)
        }
    }

    private fun getNextMovies(nextCategory: Category){
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
        moviesAndCategoryMLD.addSource(source) { repoResource ->
            when (repoResource.status) {
                Status.SUCCESS -> {
                    updateAndSendData(repoResource.data!!)
                }
                Status.ERROR -> {
                    sendError(repoResource.message!!)
                }
            }
            moviesAndCategoryMLD.removeSource(source)
        }
    }

    private fun updateAndSendData(data: List<Movie>) {
        movies.addAll(data)
        moviesAndCategoryMLD.postValue(Resource.success(Pair(movies.toList(), category)))
        page++
    }

    private fun sendError(message: String) {
        moviesAndCategoryMLD.postValue(Resource.error(message, Pair(movies.toList(), category)))
    }

}