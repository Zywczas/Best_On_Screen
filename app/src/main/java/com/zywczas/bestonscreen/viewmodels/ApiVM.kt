package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.*
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.Event


class ApiVM(
    private val repo: ApiRepository,
    private val moviesMLD: MediatorLiveData<Pair<List<Movie>, Category>>,
    private val movies: ArrayList<Movie>,
    private val errorMLD: MutableLiveData<Event<String>>,
    //SavedStateHandle not used yet, but implemented for future expansion
    private val handle: SavedStateHandle
) : ViewModel() {

    private var nextPage = 1
    private  var currentCategory = Category.POPULAR
    private val firstPageOfNewCategory = 1
    private var lastPageOfCategory = firstPageOfNewCategory

    val moviesLD = moviesMLD as LiveData<Pair<List<Movie>, Category>>
    val errorLD = errorMLD as LiveData<Event<String>>

    fun getApiMovies(nextCategory: Category) {
        val isNewCategory = nextCategory != currentCategory
        if (isNewCategory) {
            resetData()
            this.currentCategory = nextCategory
        }
        return if (nextPage > lastPageOfCategory) {
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
        errorMLD.postValue(Event(message))
    }

    private fun downloadAndSendMovies() {
        val source = LiveDataReactiveStreams.fromPublisher(
            repo.getApiMovies(currentCategory, nextPage)
        )
        moviesMLD.addSource(source) {
            movies.addAll(it.first)
            lastPageOfCategory = it.second
            moviesMLD.postValue(Pair(movies.toList(), currentCategory))
            nextPage++
            moviesMLD.removeSource(source)
        }
    }



//todo sprobowac ogarnac page i category wewnatrz view model, nie w repo albo aktywity
    //todo pozamieniac pozniej na Livedata od REsult
}