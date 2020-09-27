package com.zywczas.bestonscreen.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.utilities.CheckNetwork
import dagger.android.*

import javax.inject.Inject

class MainActivity : AppCompatActivity(){

    @Inject
    lateinit var moviesFragmentsFactory: MoviesFragmentsFactory

//    private val netwo rk = CheckNetwork(applicationContext)


    //todo dac cos co zapobiegnie resetowaniu sie fragmentow przy minimalizowaniu aplikacji

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = moviesFragmentsFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDBFragment()
    }

    private fun initDBFragment(){
        if (supportFragmentManager.fragments.size == 0){
            supportFragmentManager.beginTransaction()
                    //todo moze dac add?
                .replace(R.id.fragmentContainer, DBFragment::class.java, null)
                .commit()
        }
    }

}