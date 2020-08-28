package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.*
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.NO_MORE_PAGES_FLAG
import com.zywczas.bestonscreen.utilities.logD


class ApiVM(
    private val repo: ApiRepository,
    private val moviesMLD: MediatorLiveData<Triple<List<Movie>, Int, Category>>,
    private val movies: ArrayList<Movie>,
    private val errorMLD: MutableLiveData<Event<String>>,
    //SavedStateHandle not used yet, but implemented for future expansion
    private val handle: SavedStateHandle
) : ViewModel() {

    private var nextPage = 1
    private lateinit var category: Category
    private val firstPageOfNewCategory = 1
    private var currentPage = firstPageOfNewCategory
    private var lastPageOfCategory = firstPageOfNewCategory

    val moviesLD = moviesMLD as LiveData<Triple<List<Movie>, Int, Category>>
    val errorLD = errorMLD as LiveData<Event<String>>

    fun getApiMovies(nextCategory: Category, nextPage: Int) {
        this.category = nextCategory
        this.nextPage = nextPage
        return if (nextPage > lastPageOfCategory) {
            sendError("No more pages.")
        } else {
            downloadAndSendMovies()
        }
    }

    private fun sendError(message: String) {
        errorMLD.postValue(Event(message))
    }

    private fun downloadAndSendMovies() {
        if (nextPage == firstPageOfNewCategory) {
            resetDownloadedMovies()
        }
        val source = LiveDataReactiveStreams.fromPublisher(
            repo.getApiMovies(category, nextPage)
        )
        moviesMLD.addSource(source) {
            movies.addAll(it.first)
            currentPage = it.second
            lastPageOfCategory = it.third
            moviesMLD.postValue(Triple(movies.toList(), currentPage, category))
            moviesMLD.removeSource(source)
        }
    }

    private fun resetDownloadedMovies() = movies.clear()

//todo sprobowac ogarnac page i category wewnatrz view model, nie w repo albo aktywity
    //todo pozamieniac pozniej na Livedata od REsult
}