package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.*
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.NO_MORE_PAGES_FLAG


class ApiVM(
    private val repo: ApiRepository,
    private val moviesMLD: MutableLiveData<Triple<List<Movie>, Int, Category>>,
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
        LiveDataReactiveStreams.fromPublisher(
            repo.getApiMovies(category, nextPage)
                .doOnEach {
                    movies.addAll(it.value.first)
                    currentPage = it.value.second
                    lastPageOfCategory = it.value.third
                    moviesMLD.postValue(Triple(movies.toList(), currentPage, category))
                }
                .doOnError { sendError("Problem with downloading more movies.") }
        )
    }

    private fun resetDownloadedMovies() = movies.clear()

}