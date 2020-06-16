package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.DBMoviesRepo
import com.zywczas.bestonscreen.model.Movie

class DBMoviesVM (private val repo: DBMoviesRepo,
                  //not used yet but implemented for future expansion
                  private val handle: SavedStateHandle
) : ViewModel() {

    fun clearDisposables() = repo.clearDisposables()

    fun getDbMovies() = repo.getMoviesFromDB() as LiveData<List<Movie>>

}