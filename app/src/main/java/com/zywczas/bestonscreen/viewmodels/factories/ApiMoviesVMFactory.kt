package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.SavedStateHandle
import com.zywczas.bestonscreen.model.ApiMoviesRepo
import com.zywczas.bestonscreen.viewmodels.ApiMoviesVM
import javax.inject.Inject

/**
 * This class needs to implement ViewModelAssistedFactory so variables in constructor can
 * be injected by Dagger and everything can be used in GenericSavedStateViewModelFactory to
 * implement AbstractSavedStateViewModelFactory so we can use SavedStateHandle in this
 * particular ViewModel.
 */
class ApiMoviesVMFactory @Inject constructor (private val repo: ApiMoviesRepo)
    : ViewModelAssistedFactory<ApiMoviesVM> {

    override fun create(handle: SavedStateHandle): ApiMoviesVM {
        return ApiMoviesVM(repo, handle)
    }

}