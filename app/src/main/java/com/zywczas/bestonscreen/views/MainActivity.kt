package com.zywczas.bestonscreen.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.zywczas.bestonscreen.R
import dagger.android.*

import javax.inject.Inject

class MainActivity : AppCompatActivity(){

    @Inject
    lateinit var moviesFragmentsFactory: MoviesFragmentsFactory
//    private val navHostFragment: NavHostFragment by lazy{
//        supportFragmentManager.findFragmentById(R.id.navHostFragmentView) as NavHostFragment }
//    private val navController : NavController by lazy { navHostFragment.navController }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = moviesFragmentsFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

}