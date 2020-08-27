package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie


class ApiVM (private val repo: ApiRepository,
             private val moviesMLD : MutableLiveData<Triple<List<Movie>, Int, Category>>,
             private val isViewModelInit: MutableLiveData<Boolean>,
            //SavedStateHandle not used yet, but implemented for future expansion
             private val handle: SavedStateHandle
) : ViewModel() {

    init {
        isViewModelInit.postValue(true)
    }
    val isViewModelInitialization = isViewModelInit as LiveData<Boolean>

    fun finishViewModelInitialization(){
        isViewModelInit.postValue(false)
    }


    val moviesLD = repo.getLiveData()

    fun getApiMovies(category: Category, nextPage: Int) = repo.getMoviesFromApi(category, nextPage)

    fun clearDisposables() = repo.clearDisposables()
}