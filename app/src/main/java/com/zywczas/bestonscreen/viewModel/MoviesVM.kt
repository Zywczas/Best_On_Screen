package com.zywczas.bestonscreen.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.MovieRepository


class MoviesVM constructor (private val repo: MovieRepository,
                            val movies: ArrayList<Movie>
) : ViewModel() {

    fun clear() = repo.clear()

    fun getMovies(context: Context, category: Category) =
        repo.downloadMovies(context, category) as LiveData<List<Movie>>


}