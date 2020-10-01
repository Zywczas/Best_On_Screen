package com.zywczas.bestonscreen.views


import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.zywczas.bestonscreen.R
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var moviesFragmentsFactory: MoviesFragmentsFactory
    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.navHostFragmentView) as NavHostFragment
    }
    private val navController: NavController by lazy { navHostFragment.navController }
    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(setOf(R.id.destDbFragment, R.id.destApiFragment), drawerLayoutMain)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = moviesFragmentsFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navDrawer.setupWithNavController(navController)
        toolbarMovies.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val shouldCloseDrawer = item.itemId == android.R.id.home &&
                drawerLayoutMain != null && drawerLayoutMain.isDrawerOpen(GravityCompat.START)
        if (shouldCloseDrawer) {
            drawerLayoutMain.closeDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}