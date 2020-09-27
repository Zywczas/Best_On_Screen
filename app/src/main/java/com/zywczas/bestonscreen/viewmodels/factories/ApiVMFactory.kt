package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.viewmodels.ApiVM
import javax.inject.Inject
import javax.inject.Singleton
//todo sprobowac jeszcze raz dac fabryke z daggera ale tym razme bez inject constructor
class ApiVMFactory @Inject constructor(
    private val repo: ApiRepository,
    private val networkCheck: NetworkCheck
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ApiVM(repo, networkCheck) as T
    }

}