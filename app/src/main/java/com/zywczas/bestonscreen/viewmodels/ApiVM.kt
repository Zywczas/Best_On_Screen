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
    private val moviesMLD: MediatorLiveData<Resource<List<Movie>>>,
    private val movies: ArrayList<Movie>
) : ViewModel() {

    private val firstPageOfNewCategory = 1
    private val anyCategoryOnInit = Category.POPULAR
    private var nextPage = firstPageOfNewCategory
    private var lastPageOfCategory = firstPageOfNewCategory
    private var nextCategory = anyCategoryOnInit

    val moviesLD = moviesMLD as LiveData<Resource<List<Movie>>>

    fun getApiMovies(category: Category) {
        val isNewCategory = category != this.nextCategory
        if (isNewCategory) {
            resetData()
            this.nextCategory = category
        }
        if (nextPage > lastPageOfCategory) {
            sendError("No more pages.")
        } else {
            downloadAndSendMovies()
        }
    }

    private fun resetData() {
        movies.clear()
        nextPage = 1
        lastPageOfCategory = 1
    }

    private fun sendError(message: String) {
        moviesMLD.postValue(Resource.error(message, null))
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
                    sendError(repoResource.message!!)
                }
            }
            moviesMLD.removeSource(source)
        }
    }

    private fun updateAndSendData(data: Pair<List<Movie>, Int>) {
        movies.addAll(data.first)
        lastPageOfCategory = data.second
        moviesMLD.postValue(Resource.success(movies.toList()))
        nextPage++
    }

}