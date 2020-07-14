package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.SavedStateHandle
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.viewmodels.ApiVM
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ApiVMFactory @Inject constructor (private val repo: ApiRepository)
    : ViewModelAssistedFactory<ApiVM> {

    override fun create(handle: SavedStateHandle): ApiVM {
        return ApiVM(repo, handle)
    }

}