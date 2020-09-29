package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.*
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.CONNECTION_PROBLEM
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.utilities.Status

class ApiVM(
    private val repo: ApiRepository,
    private val networkCheck: NetworkCheck
) : ViewModel() {

    //todo pozamieniac wszystkie val w klasach na lazy
    private var firstMoviesRequested = false
    private val firstPageOfNewCategory = 1
    private val anyCategoryOnInit = Category.POPULAR
    private var page = firstPageOfNewCategory
    private var category = anyCategoryOnInit
    private val movies = mutableListOf<Movie>()

    private val moviesAndCategoryMLD = MediatorLiveData<Resource<Pair<List<Movie>, Category>>>()
    val moviesAndCategoryLD =
        moviesAndCategoryMLD as LiveData<Resource<Pair<List<Movie>, Category>>>

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