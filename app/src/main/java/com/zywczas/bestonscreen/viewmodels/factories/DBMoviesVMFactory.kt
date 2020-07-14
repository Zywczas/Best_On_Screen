package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.SavedStateHandle
import com.zywczas.bestonscreen.model.DBMoviesRepo
import com.zywczas.bestonscreen.viewmodels.DBMoviesVM
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DBMoviesVMFactory @Inject constructor (private val repo: DBMoviesRepo)
    : ViewModelAssistedFactory<DBMoviesVM> {
    override fun create(handle: SavedStateHandle): DBMoviesVM {
        return DBMoviesVM(repo, handle)
    }
}