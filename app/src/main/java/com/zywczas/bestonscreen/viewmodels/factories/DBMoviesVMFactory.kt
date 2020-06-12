package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.SavedStateHandle
import com.zywczas.bestonscreen.model.ApiMoviesRepo
import com.zywczas.bestonscreen.model.DBMoviesRepo
import com.zywczas.bestonscreen.viewmodels.DBMoviesVM
import javax.inject.Inject

/**
 * This class needs to implement ViewModelAssistedFactory so variables in constructor can
 * be injected by Dagger and everything can be used in GenericSavedStateViewModelFactory to
 * implement AbstractSavedStateViewModelFactory so we can use SavedStateHandle in this
 * particular ViewModel.
 */
class DBMoviesVMFactory @Inject constructor (private val repo: DBMoviesRepo)
    : ViewModelAssistedFactory<DBMoviesVM> {
    override fun create(handle: SavedStateHandle): DBMoviesVM {
        return DBMoviesVM(repo, handle)
    }
}