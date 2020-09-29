package com.zywczas.bestonscreen.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.zywczas.bestonscreen.R
import dagger.android.*

import javax.inject.Inject

class MainActivity : AppCompatActivity(){

    @Inject
    lateinit var moviesFragmentsFactory: MoviesFragmentsFactory

    //todo dac cos co zapobiegnie resetowaniu sie fragmentow przy minimalizowaniu aplikacji

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = moviesFragmentsFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //todo ustawic toolbar - na ten z androida

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentView) as NavHostFragment
        val navController = navHostFragment.navController

//        initDBFragment()


    }

//    private fun initDBFragment(){
//        if (supportFragmentManager.fragments.size == 0){
//            supportFragmentManager.beginTransaction()
//
//                .replace(R.id.navHostContainerView, DBFragment::class.java, null)
//                .commit()
//        }
//    }

}