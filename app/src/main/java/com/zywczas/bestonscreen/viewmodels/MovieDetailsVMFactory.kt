package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zywczas.bestonscreen.model.MovieRepository
import com.zywczas.bestonscreen.utilities.Event
import javax.inject.Inject

class MovieDetailsVMFactory
@Inject constructor(private val repo: MovieRepository
//                    , private val errorEventStringLd: MutableLiveData<Event<String>>
) :
    ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailsVM(repo) as T
    }
}