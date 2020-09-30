package com.zywczas.bestonscreen.views

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.utilities.showToast
import dagger.android.*
import kotlinx.android.synthetic.main.activity_main.*


import javax.inject.Inject

class MainActivity : AppCompatActivity(){

    @Inject
    lateinit var moviesFragmentsFactory: MoviesFragmentsFactory
    private val navHostFragment: NavHostFragment by lazy{
        supportFragmentManager.findFragmentById(R.id.navHostFragmentView) as NavHostFragment }
    private val navController : NavController by lazy { navHostFragment.navController }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = moviesFragmentsFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBar()
//        setupSideDrawer()
    }

    private fun setupActionBar(){
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
    }

    private fun setupSideDrawer(){
        //to ustawia navController na guzikach w szufladzie, jak sa takie same id guzikow i destination,
        //to tam przechodzi, nie potrzebuje akcji zadnej,
        //mi ta funkcja nie jest potrzebna bo chce przechodzic akcjami
//        NavigationUI.setupWithNavController(navDrawer, navController)
        navDrawer.setNavigationItemSelectedListener(this)
    }

    //wazna funkcja, ustawia kilka rzeczy
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer_layout)
    }

    //to sie wlacza jak klikamy guziki z paska
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val shouldCloseDrawer = item.itemId == android.R.id.home &&
                drawer_layout != null && drawer_layout.isDrawerOpen(GravityCompat.START)
        if (shouldCloseDrawer){
            drawer_layout.closeDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }



}