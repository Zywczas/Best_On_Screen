package com.zywczas.bestonscreen.views

import android.content.Context
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import javax.inject.Inject

class InjectingNavHostFragment @Inject constructor(
    private val moviesFragmentsFactory: MoviesFragmentsFactory
) : NavHostFragment() {

    //todo sprawdzic gdzie to ma byc
//    override fun onCreate(savedInstanceState: Bundle?) {
//        childFragmentManager.fragmentFactory = moviesFragmentsFactory
//        super.onCreate(savedInstanceState)
//    }

    override fun onAttach(context: Context) {
        childFragmentManager.fragmentFactory = moviesFragmentsFactory
        super.onAttach(context)
    }

}