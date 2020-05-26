package com.zywczas.bestonscreen.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.Repository
import javax.inject.Inject


class MoviesViewModel @Inject constructor (private val repo: Repository) : ViewModel() {

    val movies = ArrayList<Movie>()

    fun clear() = repo.clear()

    fun getPopularMovies(context: Context) = repo.getPopularMoviesLiveData(context) as LiveData<List<Movie>>

}