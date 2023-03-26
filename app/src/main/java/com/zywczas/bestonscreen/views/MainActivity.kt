package com.zywczas.bestonscreen.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.zywczas.bestonscreen.R
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var moviesFragmentsFactory: MoviesFragmentsFactory
    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.navHostFragmentView) as NavHostFragment }
    private val navController by lazy { navHostFragment.navController }
    private val appBarConfiguration by lazy { AppBarConfiguration(setOf(R.id.destinationLocalMovies, R.id.destinationNetworkMovies), drawerLayoutMain) }

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
        if (drawerLayoutMain.isOpen) {
            drawerLayoutMain.close()
        } else {
            navController.navigateUp(appBarConfiguration)
        }
    }

}
