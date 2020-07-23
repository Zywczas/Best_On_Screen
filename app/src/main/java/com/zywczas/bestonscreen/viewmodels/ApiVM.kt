package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category


class ApiVM (private val repo: ApiRepository,
            //SavedStateHandle not used yet, but implemented for future expansion
             private val handle: SavedStateHandle
) : ViewModel() {

    private var isViewModelInit = true

    fun isViewModelInitialization() = isViewModelInit

    fun finishViewModelInitialization(){
        isViewModelInit = false
    }

    fun getLD() = repo.getLiveData()

    fun getApiMovies(category: Category, nextPage: Int) = repo.getMoviesFromApi(category, nextPage)

    fun clearDisposables() = repo.clearDisposables()
}