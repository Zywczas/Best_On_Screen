package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.*
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.ApiMoviesRepo
import com.zywczas.bestonscreen.utilities.*


class ApiMoviesVM (private val repo: ApiMoviesRepo,
                   private val handle: SavedStateHandle
) : ViewModel() {


    //TODO pozniej to wrzucic w daggera
    private val mediatorLd = MediatorLiveData<PairMoviesInt>()
    private val handleLd = LiveEvent<PairMoviesInt>()

    init {
        mediatorLd.addSource(repo.getMoviesFromApi(EMPTY_CATEGORY, 0)) {mediatorLd.value = it}
        mediatorLd.addSource(handleLd) {mediatorLd.value = it}
    }

    fun getLd() = mediatorLd as LiveData<PairMoviesInt>

    fun getApiMovies(category: String, nextPage: Int) = repo.getMoviesFromApi(category, nextPage)
    fun getSavedStateLd() {
        val tempValue = handle.getLiveData<PairMoviesInt>(SAVED_LD).value
        handleLd.value = tempValue
        handle.remove<PairMoviesInt>(SAVED_LD)
    }

    /**
     * This fun needs to save current list and page number.
     */
    fun saveLD(key: String, moviesAndPage: PairMoviesInt) = handle.set(key, moviesAndPage)
    fun saveMetaState(key: String, isStateSaved: Boolean) = handle.set(key, isStateSaved)
    fun saveCategory(key: String, category: String) = handle.set(key, category)

    fun getSavedCategory() = handle.get<String>(SAVED_CATEGORY)
    fun clearSavedCategory() = handle.remove<String>(SAVED_CATEGORY)
    fun getMetaState() = handle.get<Boolean>(SAVED_STATE)

    fun clearDisposables() = repo.clearDisposables()
}