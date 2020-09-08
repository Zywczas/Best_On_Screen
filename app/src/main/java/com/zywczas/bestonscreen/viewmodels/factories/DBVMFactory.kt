package com.zywczas.bestonscreen.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zywczas.bestonscreen.model.DBRepository
import com.zywczas.bestonscreen.viewmodels.DBVM
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DBVMFactory @Inject constructor( private val repo: DBRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DBVM(repo) as T
    }
}