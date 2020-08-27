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
            //SavedStateHandle not used yet, but implemented for future expansion
             private val handle: SavedStateHandle
) : ViewModel() {

    val moviesLD = repo.getLiveData()

    fun getApiMovies(category: Category, nextPage: Int) = repo.getMoviesFromApi(category, nextPage)

    fun clearDisposables() = repo.clearDisposables()
}