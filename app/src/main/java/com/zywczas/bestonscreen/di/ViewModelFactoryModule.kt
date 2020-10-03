package com.zywczas.bestonscreen.di

import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.viewmodels.ApiVM
import com.zywczas.bestonscreen.viewmodels.DBVM
import com.zywczas.bestonscreen.viewmodels.DetailsVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(DetailsVM::class)
    abstract fun bindDetailsVM(detailsVM: DetailsVM) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ApiVM::class)
    abstract fun bindApiVM(apiVM: ApiVM) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DBVM::class)
    abstract fun bindDbVM(dbVM: DBVM) : ViewModel
}