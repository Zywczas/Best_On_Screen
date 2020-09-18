package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zywczas.bestonscreen.model.DetailsRepository
import com.zywczas.bestonscreen.viewmodels.DetailsVM
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsVMFactory @Inject constructor(private val repo: DetailsRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsVM(repo) as T
    }
}
