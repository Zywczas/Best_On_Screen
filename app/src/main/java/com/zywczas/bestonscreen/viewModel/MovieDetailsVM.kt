package com.zywczas.bestonscreen.viewModel

import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.MovieRepository

class MovieDetailsVM (private val repo: MovieRepository) : ViewModel(){

    fun clear() = repo.clear()

    //this method is unnecessary for now
//    fun getMovieDetailsLiveData(context: Context, movieId: Int) =
//        repo.getMovieDetailsLiveData(context, movieId) as LiveData<MovieFromApi>
}