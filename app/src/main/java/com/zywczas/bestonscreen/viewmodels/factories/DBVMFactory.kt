package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.zywczas.bestonscreen.model.DBRepository
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.viewmodels.DBVM
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DBVMFactory @Inject constructor( private val repo: DBRepository) : ViewModelAssistedFactory<DBVM> {

    override fun create(handle: SavedStateHandle): DBVM {
        return DBVM(repo, handle)
    }
}