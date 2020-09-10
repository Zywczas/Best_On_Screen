package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.viewmodels.ApiVM
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiVMFactory @Inject constructor(
    private val repo: ApiRepository,
    private val moviesMLD: MediatorLiveData<Resource<Pair<List<Movie>, Category>>>
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ApiVM(repo, moviesMLD) as T
    }

}