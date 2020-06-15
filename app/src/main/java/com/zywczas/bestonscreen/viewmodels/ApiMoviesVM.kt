package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.*
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.ApiMoviesRepo
import com.zywczas.bestonscreen.utilities.*


class ApiMoviesVM (private val repo: ApiMoviesRepo,
                   private val handle: SavedStateHandle
) : ViewModel() {

    //pozniej to wrzucic w daggera
    private val mediatorLd = MediatorLiveData<PairMoviesInt>()
    private val handleLd = LiveEvent<PairMoviesInt>()

    fun getLd() = mediatorLd as LiveData<PairMoviesInt>

    init {
        mediatorLd.addSource(repo.getMoviesFromApi(EMPTY_CATEGORY, 0)) {mediatorLd.value = it}
        mediatorLd.addSource(handleLd) {mediatorLd.value = it}
    }

    fun getApiMovies(category: String, nextPage: Int) = repo.getMoviesFromApi(category, nextPage)
    fun getSavedStateLd() {
        val tempValue = handle.getLiveData<PairMoviesInt>(SAVED_LD).value
        handleLd.value = tempValue
    }

    fun getiMovies(category: String, nextPage: Int) : LiveData<PairMoviesInt> {
        //if orientation changed take LiveData from SavedStateHandle
        val isStateSaved = handle.get<Boolean>(SAVED_STATE)
        return if (isStateSaved != null && isStateSaved == true) {

            logD("saved state: $isStateSaved")
            //reset status so in the next step it can continue with API repository
            handle.remove<Boolean>(SAVED_STATE)
            handle.getLiveData(SAVED_LD)
        } else {
            logD("filmy z api w MV")

            repo.getMoviesFromApi(category, nextPage)
        }
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