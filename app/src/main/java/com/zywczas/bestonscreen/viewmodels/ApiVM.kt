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

    private val firstPageOfNewCategory = 1
    private var nextPage = firstPageOfNewCategory
    private var lastPageOfCategory = firstPageOfNewCategory
    private val anyCategoryOnInit = Category.POPULAR
    private var nextCategory = anyCategoryOnInit

    val moviesLD = moviesMLD as LiveData<Pair<List<Movie>, Category>>
    val errorLD = errorMLD as LiveData<Event<String>>

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
        errorMLD.postValue(Event(message))
    }

    private fun downloadAndSendMovies() {
        val source = LiveDataReactiveStreams.fromPublisher(
            repo.getApiMovies(nextCategory, nextPage)
        )
        moviesMLD.addSource(source) {
            movies.addAll(it.first)
            lastPageOfCategory = it.second
            moviesMLD.postValue(Pair(movies.toList(), nextCategory))
            moviesMLD.removeSource(source)
        }
    }

    //todo pozamieniac pozniej na Livedata od REsult
}