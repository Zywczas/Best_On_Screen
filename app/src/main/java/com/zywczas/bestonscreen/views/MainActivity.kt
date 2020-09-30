package com.zywczas.bestonscreen.views


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.utilities.showToast
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity()
//    , NavigationView.OnNavigationItemSelectedListener
{

    @Inject
    lateinit var moviesFragmentsFactory: MoviesFragmentsFactory
    private val navHostFragment: NavHostFragment by lazy{
        supportFragmentManager.findFragmentById(R.id.navHostFragmentView) as NavHostFragment }
    private val navController : NavController by lazy { navHostFragment.navController }
    private val appBarConfiguration : AppBarConfiguration by lazy {
        AppBarConfiguration(setOf(R.id.destDbFragment, R.id.destApiFragment), drawer_layout) }

        override fun onCreate(savedInstanceState: Bundle?) {
            AndroidInjection.inject(this)
            supportFragmentManager.fragmentFactory = moviesFragmentsFactory
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            setupActionBar()
            setupSideDrawer()

//        setupActionBarWithNavController(navController, appBarConfiguration)
            //        navDrawer.setNavigationItemSelectedListener(this)
        }

        private fun setupActionBar(){
            //todo zapisac sobie te rozne opcej paska do zeszytu
//        NavigationUI.setupActionBarWithNavController(this, navController) <- to daje sam pasek i strzalke wstecz jak jest do czego wrocic :), nie trzeba dawac szuflady


//        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        }

        private fun setupSideDrawer(){
            //to ustawia navController na guzikach w szufladzie, jak sa takie same id guzikow i destination,
            //to tam przechodzi, nie potrzebuje akcji zadnej,
            //mi ta funkcja nie jest potrzebna bo chce przechodzic akcjami
            NavigationUI.setupWithNavController(navDrawer, navController)

        }

        //wazna funkcja, ustawia kilka rzeczy, nie jest potrzebna jezeli mamy swoje wlasne toolbary,
        // tutaj chyba tez trzeba podac appBarConfigurations
        override fun onSupportNavigateUp(): Boolean {
            return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        }

        //to sie wlacza jak klikamy guziki z paska i z menu settings
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val shouldCloseDrawer = item.itemId == android.R.id.home &&
                    drawer_layout != null && drawer_layout.isDrawerOpen(GravityCompat.START)
            if (shouldCloseDrawer){
                drawer_layout.closeDrawer(GravityCompat.START)
                return true
            }
            return super.onOptionsItemSelected(item)
        }

        //ta funkcja ustawia klikanie na menu w szufladzie, ale musi byc interface NavigationView.OnNavigationItemSelectedListener
        //oraz navDrawer.setNavigationItemSelectedListener(this)
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.destDbFragment -> navController.navigate(R.id.destDbFragment)
//            R.id.destApiFragment -> navController.navigate(R.id.destApiFragment)
//            else -> showToast("Button not assigned")
//        }
//        drawer_layout.closeDrawer(GravityCompat.START)
//        return true
//    }

        //todo sprawdza czy dane dstination do ktorego chcemy isc jest juz w widoku, jezeli tak to nie idzie tam drugi raz, zeby backstack nie rosl bez sensu
//    private fun isValidDestination(destination: Int): Boolean {
//        return destination != Navigation.findNavController(
//            this,
//            R.id.nav_host_fragment
//        ).currentDestination!!
//            .id
//    }
//    }
}