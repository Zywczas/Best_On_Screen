package com.zywczas.bestonscreen.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.MovieCategory
import com.zywczas.bestonscreen.model.Repository
import java.util.ArrayList
import javax.inject.Inject


class MoviesViewModel @Inject constructor (
    private val repo: Repository,
    //list for activity
    //sprawdzic czy mozna zamienic na kotlin.collections
    val movies: ArrayList<Movie>
) : ViewModel() {



    fun clear() = repo.clear()

    fun getMoviesLiveData(context: Context, movieCategory: MovieCategory) =
        repo.getMoviesLiveData(context, movieCategory) as LiveData<List<Movie>>


}