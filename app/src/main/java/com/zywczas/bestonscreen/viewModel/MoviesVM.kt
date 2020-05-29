package com.zywczas.bestonscreen.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Repository


class MoviesVM constructor (
    private val repo: Repository,
    //list for MovieActivity
    val movies: ArrayList<Movie> ) : ViewModel() {

    fun clear() = repo.clear()

    fun getMoviesLiveData(context: Context, category: Category) =
        repo.getMoviesLiveData(context, category) as LiveData<List<Movie>>


}