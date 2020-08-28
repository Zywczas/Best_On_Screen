package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.viewmodels.ApiVM
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiVMFactory @Inject constructor(
    private val repo: ApiRepository,
    private val moviesMLD: MediatorLiveData<Triple<List<Movie>, Int, Category>>,
    private val movies: ArrayList<Movie>,
    private val errorMLD: MutableLiveData<Event<String>>
) : ViewModelAssistedFactory<ApiVM> {

    override fun create(handle: SavedStateHandle): ApiVM {
        return ApiVM(repo, moviesMLD, movies, errorMLD, handle)
    }

}