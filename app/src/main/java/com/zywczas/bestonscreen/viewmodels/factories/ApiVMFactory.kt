package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.viewmodels.ApiVM
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ApiVMFactory @Inject constructor(
    private val repo: ApiRepository,
    private val moviesLD: MutableLiveData<Triple<List<Movie>, Int, Category>>,
    private val isViewModelInit: MutableLiveData<Boolean>
) : ViewModelAssistedFactory<ApiVM> {

    override fun create(handle: SavedStateHandle): ApiVM {
        return ApiVM(repo, moviesLD, isViewModelInit, handle)
    }

}