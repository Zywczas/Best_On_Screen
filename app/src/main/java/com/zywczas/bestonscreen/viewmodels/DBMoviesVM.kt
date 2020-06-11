package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.MovieRepository
import com.zywczas.bestonscreen.utilities.Event

class DBMoviesVM (private val repo: MovieRepository,
                  //not used yet but implemented for future expansion
                  private val handle: SavedStateHandle
) : ViewModel() {

    fun clear() = repo.clearMoviesDisposables()

    fun getDbMovies() = repo.getMoviesFromDB() as LiveData<Event<List<Movie>>>

}