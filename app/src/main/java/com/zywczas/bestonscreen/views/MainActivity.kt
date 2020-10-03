package com.zywczas.bestonscreen.views

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.utilities.lazyAndroid
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var moviesFragmentsFactory: MoviesFragmentsFactory
    private val navHostFragment: NavHostFragment by lazyAndroid {
        supportFragmentManager.findFragmentById(R.id.navHostFragmentView) as NavHostFragment }
    private val navController: NavController by lazyAndroid { navHostFragment.navController }
    private val appBarConfiguration: AppBarConfiguration by lazyAndroid {
        AppBarConfiguration(setOf(R.id.destDbFragment, R.id.destApiFragment), drawerLayoutMain) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = moviesFragmentsFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navDrawer.setupWithNavController(navController)
        toolbarMovies.setupWithNavController(navController, appBarConfiguration)
        toolbarMovies.setNavigationOnClickListener(openCloseDrawerNavClick)
    }

    private val openCloseDrawerNavClick = View.OnClickListener {
        if (drawerLayoutMain.isDrawerOpen(GravityCompat.START)){
            drawerLayoutMain.closeDrawer(GravityCompat.START)
        } else {
            navController.navigateUp(appBarConfiguration)
        }
    }


}