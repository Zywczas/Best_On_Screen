package com.zywczas.bestonscreen.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.zywczas.bestonscreen.views.ApiFragment
import com.zywczas.bestonscreen.views.DBFragment
import com.zywczas.bestonscreen.views.DetailsFragment
import com.zywczas.bestonscreen.views.MoviesFragmentsFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FragmentBindingModule {

    //todo podobno juz nie potrzebne
    @Binds
    abstract fun bindFragmentFactory(factory: MoviesFragmentsFactory) : FragmentFactory

//todo gdzies chyba trzeba dodac jeszcze main activity
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

}