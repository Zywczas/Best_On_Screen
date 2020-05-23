package com.zywczas.bestonscreen.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.Repository

class MoviesViewModel (private val repo: Repository) : ViewModel() {

    fun clear() = repo.clear()

    fun getPopularMovies(context: Context) = repo.getPopularMoviesLiveData(context) as LiveData<List<Movie>>


}