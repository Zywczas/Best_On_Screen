package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.DBRepository
import com.zywczas.bestonscreen.model.Movie

class DBVM (private val repo: DBRepository,
    //todo dodac jakies zastosowanie dla handle
                  //not used yet but implemented for future expansion
            private val handle: SavedStateHandle
) : ViewModel() {

    fun clearDisposables() = repo.clearDisposables()

    fun listenToErrors() = repo.getErrorLiveEvent()

    fun getDbMovies() = repo.getMoviesFromDB() as LiveData<List<Movie>>

}