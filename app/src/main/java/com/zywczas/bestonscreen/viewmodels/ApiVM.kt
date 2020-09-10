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
    private val moviesMLD: MediatorLiveData<Resource<List<Movie>>>
) : ViewModel() {

    private val firstPageOfNewCategory = 1
    private val anyCategoryOnInit = Category.POPULAR
    private var nextPage = firstPageOfNewCategory
    private var nextCategory = anyCategoryOnInit
    private val movies = mutableListOf<Movie>()

    val moviesLD = moviesMLD as LiveData<Resource<List<Movie>>>

    fun getApiMovies(category: Category) {
        val isNewCategory = category != this.nextCategory
        if (isNewCategory) {
            resetData()
            this.nextCategory = category
        }
        downloadAndSendMovies()
    }

    private fun resetData() {
        movies.clear()
        nextPage = 1
    }

    private fun downloadAndSendMovies() {
        val source = LiveDataReactiveStreams.fromPublisher(
            repo.getApiMovies(nextCategory, nextPage)
        )
        moviesMLD.addSource(source) {repoResource ->
            when (repoResource.status) {
                Status.SUCCESS -> {
                    updateAndSendData(repoResource.data!!)
                }
                Status.ERROR -> {
                    moviesMLD.postValue(repoResource)
                }
            }
            moviesMLD.removeSource(source)
        }
    }

    private fun updateAndSendData(data: List<Movie>) {
        movies.addAll(data)
        moviesMLD.postValue(Resource.success(movies.toList()))
        nextPage++
    }

}