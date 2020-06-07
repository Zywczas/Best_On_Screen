package com.zywczas.bestonscreen.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Event
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.MovieRepository
import com.zywczas.bestonscreen.model.localstore.MovieFromDB


class MoviesVM constructor (private val repo: MovieRepository,
                            //list to store movies for Recycler view in Movie Activity
                            val movies: ArrayList<Movie>
) : ViewModel() {

    fun clear() = repo.clear()

    fun getApiMovies(context: Context, category: Category) =
        repo.getMoviesFromApi(context, category) as LiveData<Event<List<Movie>>>

    fun getDbMovies(context: Context, category: Category) =
        repo.getMoviesFromDB(context, category) as LiveData<List<Movie>>



}