package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.SavedStateHandle
import com.zywczas.bestonscreen.model.ApiMoviesRepo
import com.zywczas.bestonscreen.viewmodels.ApiMoviesVM
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ApiMoviesVMFactory @Inject constructor (private val repo: ApiMoviesRepo)
    : ViewModelAssistedFactory<ApiMoviesVM> {

    override fun create(handle: SavedStateHandle): ApiMoviesVM {
        return ApiMoviesVM(repo, handle)
    }

}