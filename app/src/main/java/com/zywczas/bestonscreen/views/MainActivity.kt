package com.zywczas.bestonscreen.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.zywczas.bestonscreen.R
import dagger.android.*
import kotlinx.android.synthetic.main.activity_main.*

import javax.inject.Inject

class MainActivity : AppCompatActivity(){

    @Inject
    lateinit var moviesFragmentsFactory: MoviesFragmentsFactory

//    private val netwo rk = NetworkCheck(applicationContext)


    //todo dac cos co zapobiegnie resetowaniu sie fragmentow przy minimalizowaniu aplikacji

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = moviesFragmentsFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        initDBFragment()
        //todo ustawic toolbar
        setSupportActionBar(moviesToolbar)
    }

    private fun initDBFragment(){
        if (supportFragmentManager.fragments.size == 0){
            supportFragmentManager.beginTransaction()
                    //todo moze dac add?
                .replace(R.id.navHostContainerView, DBFragment::class.java, null)
                .commit()
        }
    }

}