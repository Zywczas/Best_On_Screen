package com.zywczas.bestonscreen.viewmodels

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.MovieRepository
import javax.inject.Inject

/**
 * This class needs to implement ViewModelAssistedFactory so variables in constructor can
 * be injected by Dagger and everything can be used in GenericSavedStateViewModelFactory to
 * implement AbstractSavedStateViewModelFactory so we can use SavedStateHandle in this
 * particular ViewModel.
 */
class MoviesVMFactory
@Inject
constructor (private val repo: MovieRepository
//                                           owner: SavedStateRegistryOwner,
//                                           defaultArgs: Bundle? = null
) : ViewModelAssistedFactory<MoviesVM> {
    override fun create(handle: SavedStateHandle): MoviesVM {
        return MoviesVM(repo, handle)
    }


//    : ViewModelProvider.NewInstanceFactory() {
//    : AbstractSavedStateViewModelFactory(owner, defaultArgs){
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel?> create(
//        key: String,
//        modelClass: Class<T>,
//        handle: SavedStateHandle
//    ): T {
//        return MoviesVM(repo, handle) as T
//    }

//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return MoviesVM(repo, arrayList) as T
//    }
}