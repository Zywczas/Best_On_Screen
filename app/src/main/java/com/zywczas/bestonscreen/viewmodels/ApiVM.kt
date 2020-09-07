package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.*
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.utilities.Status.*


class ApiVM(
    private val repo: ApiRepository,
    private val moviesMLD: MediatorLiveData<Resource<Pair<List<Movie>, Category>>>,
    private val movies: ArrayList<Movie>
) : ViewModel() {

    private val firstPageOfNewCategory = 1
    private var nextPage = firstPageOfNewCategory
    private var lastPageOfCategory = firstPageOfNewCategory
    private val anyCategoryOnInit = Category.POPULAR
    private var nextCategory = anyCategoryOnInit

    val moviesLD = moviesMLD as LiveData<Resource<Pair<List<Movie>, Category>>>

    fun getApiMovies(nextCategory: Category) {
        val isNewCategory = nextCategory != this.nextCategory
        if (isNewCategory) {
            resetData()
            this.nextCategory = nextCategory
        }
        if (nextPage > lastPageOfCategory) {
            sendError("No more pages.")
        } else {
            downloadAndSendMovies()
            nextPage++
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
                SUCCESS -> {
                    updateAndSendData(repoResource.data!!)
                }
                ERROR -> {
                    sendError(repoResource.message!!)
                }
//todo sprawdzic jak z ladowaniem internetu, moze pominac ladowanie tutaj
                LOADING -> {
                    moviesMLD.postValue(Resource.loading("still loading", null))
                }
            }
            moviesMLD.removeSource(source)
        }
    }

    private fun updateAndSendData(data: Pair<List<Movie>, Int>) {
        movies.addAll(data.first)
        lastPageOfCategory = data.second
        moviesMLD.postValue(Resource.success(Pair(movies.toList(), nextCategory)))
    }

    //todo pozamieniac pozniej na Livedata od REsult
}