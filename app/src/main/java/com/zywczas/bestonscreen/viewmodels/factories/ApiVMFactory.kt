package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.*
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.viewmodels.ApiVM
import com.zywczas.bestonscreen.viewmodels.DBVM
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiVMFactory @Inject constructor(
    private val repo: ApiRepository,
    private val moviesMLD: MediatorLiveData<Resource<List<Movie>>>,
    private val movies: ArrayList<Movie>
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ApiVM(repo, moviesMLD, movies) as T
    }

}