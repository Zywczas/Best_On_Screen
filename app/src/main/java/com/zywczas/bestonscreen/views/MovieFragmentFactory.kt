package com.zywczas.bestonscreen.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.viewmodels.factories.ApiVMFactory
import com.zywczas.bestonscreen.viewmodels.factories.DBVMFactory
import com.zywczas.bestonscreen.viewmodels.factories.DetailsVMFactory
import javax.inject.Inject
import javax.inject.Singleton

//todo porozdzielac pozniej na pojedyncze fabryki, zeby Dagger nie musial od razu tworzyc wszystkich View Models na raz
@Singleton
class MovieFragmentFactory @Inject constructor(
    private val apiVMFactory: ApiVMFactory,
    private val dbVMFactory: DBVMFactory,
    private val detailsVMFactory: DetailsVMFactory,
    private val picasso: Picasso
) : FragmentFactory(){

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            ApiFragment::class.java.name -> ApiFragment(apiVMFactory, picasso)
            DBFragment::class.java.name -> DBFragment(dbVMFactory, picasso)
            DetailsFragment::class.java.name -> DetailsFragment(detailsVMFactory, picasso)
            else -> super.instantiate(classLoader, className)
        }
    }

}