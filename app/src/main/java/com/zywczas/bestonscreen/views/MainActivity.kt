package com.zywczas.bestonscreen.views


import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.zywczas.bestonscreen.R
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    //todo dodac collapsing bar, i osobne bary do kazdego fragmentu
    @Inject
    lateinit var moviesFragmentsFactory: MoviesFragmentsFactory
    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.navHostFragmentView) as NavHostFragment
    }
    private val navController: NavController by lazy { navHostFragment.navController }
    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(setOf(R.id.destDbFragment, R.id.destApiFragment), drawerLayout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = moviesFragmentsFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navDrawer.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }


//        NavigationUI.setupActionBarWithNavController(this, navController) <- to daje sam pasek i strzalke wstecz jak jest do czego wrocic :), nie trzeba dawac szuflady


    //wazna funkcja, ustawia kilka rzeczy, nie jest potrzebna jezeli mamy swoje wlasne toolbary,
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val shouldCloseDrawer = item.itemId == android.R.id.home &&
                drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)
        if (shouldCloseDrawer) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}