package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.utilities.Status

class ApiVM(
    private val repo: ApiRepository,
    //todo sprawdzic czy nie pousuwac tego z konstuktora
    private val moviesMLD: MediatorLiveData<Resource<Pair<List<Movie>, Category>>>
) : ViewModel() {

    private val firstPageOfNewCategory = 1
    private val anyCategoryOnInit = Category.POPULAR
    private var page = firstPageOfNewCategory
    private var category = anyCategoryOnInit
    private val movies = mutableListOf<Movie>()

    val moviesLD = moviesMLD as LiveData<Resource<Pair<List<Movie>, Category>>>

    fun getNextMovies(nextCategory: Category = category) {
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
        moviesMLD.addSource(source) {repoResource ->
            when (repoResource.status) {
                Status.SUCCESS -> {
                    updateAndSendData(repoResource.data!!)
                }
                Status.ERROR -> {
                    sendError(repoResource.message!!)
                }
            }
            moviesMLD.removeSource(source)
        }
    }

    private fun updateAndSendData(data: List<Movie>) {
        movies.addAll(data)
        moviesMLD.postValue(Resource.success(Pair(movies.toList(), category)))
        page++
    }

    private fun sendError(message: String){
        moviesMLD.postValue(Resource.error(message, Pair(emptyList(), category)))
    }

}