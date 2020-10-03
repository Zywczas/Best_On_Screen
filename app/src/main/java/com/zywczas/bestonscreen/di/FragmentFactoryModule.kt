package com.zywczas.bestonscreen.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.viewmodels.ApiVM
import com.zywczas.bestonscreen.viewmodels.DBVM
import com.zywczas.bestonscreen.viewmodels.DetailsVM
import com.zywczas.bestonscreen.views.ApiFragment
import com.zywczas.bestonscreen.views.DBFragment
import com.zywczas.bestonscreen.views.DetailsFragment
import com.zywczas.bestonscreen.views.MoviesFragmentsFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FragmentFactoryModule {

    //podobno juz nie potrzebne
//    @Binds
//    abstract fun bindFragmentFactory(factory: MoviesFragmentsFactory) : FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(DBFragment::class)
    abstract fun bindDBFragment(dbFragment: DBFragment) : Fragment

    @Binds
    @IntoMap
    @FragmentKey(ApiFragment::class)
    abstract fun bindApiFragment(apiFragment: ApiFragment) : Fragment

    @Binds
    @IntoMap
    @FragmentKey(DetailsFragment::class)
    abstract fun bindDetailsFragment(detailsFragment: DetailsFragment) : Fragment

    //todo przeniesc
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