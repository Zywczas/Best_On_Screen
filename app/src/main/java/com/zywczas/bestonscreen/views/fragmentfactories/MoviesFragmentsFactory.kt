package com.zywczas.bestonscreen.views.fragmentfactories

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.viewmodels.factories.ApiVMFactory
import com.zywczas.bestonscreen.viewmodels.factories.DBVMFactory
import com.zywczas.bestonscreen.viewmodels.factories.DetailsVMFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesFragmentsFactory @Inject constructor(
    private val apiVMFactory: ApiVMFactory,
    private val dbvmFactory: DBVMFactory,
    private val detailsVMFactory: DetailsVMFactory,
    private val picasso: Picasso
) : FragmentFactory(){

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {




        }
    }



}