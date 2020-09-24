package com.zywczas.bestonscreen.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.viewmodels.factories.ApiVMFactory
import com.zywczas.bestonscreen.viewmodels.factories.DBVMFactory
import com.zywczas.bestonscreen.viewmodels.factories.DetailsVMFactory
import javax.inject.Inject
import javax.inject.Singleton

//todo porozdzielac pozniej na pojedyncze fabryki, zeby Dagger nie musial od razu tworzyc wszystkich View Models na raz
//todo dc pozniej chyba navhost fragment factory
@Singleton
class MoviesFragmentsFactory @Inject constructor(
    private val apiVMFactory: ApiVMFactory?,
    private val dbVMFactory: DBVMFactory?,
    private val detailsVMFactory: DetailsVMFactory?,
    private val picasso: Picasso?
) : FragmentFactory(){
//todo 1. tworzymy mape binds w FragmentBindingModule
    //todo 2. w fabryce wczytujemy ten fragment z mapy z konstruktora fabryki
    //todo 3. przy konstruktorze fragmentu dajemy inject a w nim to co potrzebujemy dla danego fragmentu
    //todo 4. i juz powinno dzialac :)
    //class OfferFragment
//@Inject constructor(
//    private val sharedPreferences: SharedPreferences,
//    private val textProducer: TextProducer,
//    private val someDep: SomeDep
//) : Fragment() {
//    // Fragment body
//}
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {

            ApiFragment::class.java.name -> {
                if (apiVMFactory != null && picasso != null) {
                    ApiFragment(apiVMFactory, picasso)
                } else {
                    super.instantiate(classLoader, className)
                }
            }

            DBFragment::class.java.name -> {
                if (dbVMFactory != null && picasso != null) {
                    DBFragment(dbVMFactory, picasso)
                } else {
                    super.instantiate(classLoader, className)
                }
            }

            DetailsFragment::class.java.name -> {
                if (detailsVMFactory != null && picasso != null) {
                    DetailsFragment(detailsVMFactory, picasso)
                } else {
                    super.instantiate(classLoader, className)
                }
            }

            else -> super.instantiate(classLoader, className)
        }
    }

}