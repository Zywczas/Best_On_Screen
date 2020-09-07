package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zywczas.bestonscreen.model.DetailsRepository
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.viewmodels.DetailsVM
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsVMFactory @Inject constructor(
    private val repo: DetailsRepository,
    private val isMovieInDbMLD: MediatorLiveData<Event<Resource<Boolean>>>,
    private val messageEventMLD: MediatorLiveData<Event<String>>
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsVM(repo, isMovieInDbMLD, messageEventMLD) as T
    }
}