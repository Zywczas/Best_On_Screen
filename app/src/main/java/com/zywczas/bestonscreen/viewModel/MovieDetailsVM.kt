package com.zywczas.bestonscreen.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.Repository

class MovieDetailsVM (private val repo: Repository) : ViewModel(){

    fun clear() = repo.clear()

    //this method is unnecessary for now
//    fun getMovieDetailsLiveData(context: Context, movieId: Int) =
//        repo.getMovieDetailsLiveData(context, movieId) as LiveData<Movie>
}