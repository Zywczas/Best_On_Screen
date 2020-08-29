package com.zywczas.bestonscreen.views

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.model.*
import com.zywczas.bestonscreen.utilities.*
import com.zywczas.bestonscreen.viewmodels.DBVM
import com.zywczas.bestonscreen.viewmodels.factories.DBVMFactory
import com.zywczas.bestonscreen.viewmodels.factories.GenericSavedStateViewModelFactory
import kotlinx.android.synthetic.main.activity_api_and_db.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.navigation.*
import javax.inject.Inject

class DBActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: DBVMFactory
    private val viewModel: DBVM by viewModels { GenericSavedStateViewModelFactory(factory,this) }
    private lateinit var adapter: MovieAdapter
    @Inject lateinit var picassoForAdapter: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_and_db)
        startDBActivitySetupChain()
        setupDrawer()
        setupTags()




    }

    //todo logika zawarta w repozytorium powinna znajdować się we viewmodelu. Przy takim podejściu jak widoczne w zadaniu, viewModel tak naprawde nie pełni żadnej funkcji.

    //todo dane z viewModelu powinny być przekazywane do widoku (activity, fragment) poprzez livedata lub observable. Dzięki temu możemy zabezpieczyć się przed wyciekami pamięci.

    //todo  repozytorium nie powinno zwracać LiveData

    //todo  funkcje viewmodelu nie powinny zwracac zadnej wartości. Wszystkie dane powinny być wystawiane w postaci publicznych zmiennych typu LiveData.

    //todo nie powinno się używać metod typu deprecated ( funkcja isInternetConnected() )

    private fun startDBActivitySetupChain() {
        injectDependencies { injectionFinished ->
            if (injectionFinished) {
                setupErrorListener()
                setupRecyclerView{ recyclerViewSetupFinished ->
                    if (recyclerViewSetupFinished){
                        setupMoviesObserver{isObserverSetup ->
                            if (isObserverSetup){
//                                getMovies()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun injectDependencies(complete: (Boolean) -> Unit) {
        App.moviesComponent.inject(this)
        complete(true)
    }

    private fun setupErrorListener(){
        viewModel.errorLD.observe(this, Observer {it.getContentIfNotHandled()?.let {
            message -> showToast(message)
        }

        })
    }

    private fun setupRecyclerView(complete: (Boolean) -> Unit) {
        setupAdapter()
        setupLayoutManager()
        complete(true)
    }

    private fun setupAdapter(){
        adapter = MovieAdapter(this, picassoForAdapter) { movie ->
            val detailsActivity = Intent(this, DetailsActivity::class.java)
            detailsActivity.putExtra(EXTRA_MOVIE, movie)
            startActivity(detailsActivity)
        }
        moviesRecyclerView.adapter = adapter
    }

    private fun setupLayoutManager(){
        var spanCount = 2
        val orientation = resources.configuration.orientation
        if (orientation ==  Configuration.ORIENTATION_LANDSCAPE){
            spanCount = 4
        }
        val layoutManager = GridLayoutManager(this, spanCount)
        moviesRecyclerView.layoutManager = layoutManager
        moviesRecyclerView.setHasFixedSize(true)
    }

    //todo dac osobno sluchanie live data i osobno getMovies on init

    private fun setupMoviesObserver(complete: (Boolean) -> Unit) {
        viewModel.moviesLD.observe(this, Observer { movies ->
            if (movies != null) {
                updateDisplayedMovies(movies)
                if (movies.isEmpty()){
                    showMessageAboutEmptyDB()
                }
            }

        })
        complete(true)
    }

    private fun updateDisplayedMovies(movies: List<Movie>){
        adapter.submitList(movies.toMutableList())
    }

    private fun showMessageAboutEmptyDB(){
        emptyListTextView.isVisible = true
    }

//    private fun getMovies(){
//        viewModel.getDbMovies()
//    }

    private fun setupDrawer(){
        val toggle = ActionBarDrawerToggle(this,drawer_layout,toolbar,
            R.string.nav_drawer_open,R.string.nav_drawer_closed)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setupTags() {
        upcomingTextView.tag = Category.UPCOMING
        topRatedTextView.tag = Category.TOP_RATED
        popularTextView.tag = Category.POPULAR
    }

    fun myToWatchListClicked (view: View) {
        closeDrawerOrMinimizeApp()
        showToast("This is your list.")
    }

    private fun closeDrawerOrMinimizeApp() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            this.moveTaskToBack(true)
        }
    }

    fun categoryClicked(view: View) {
        closeDrawerOrMinimizeApp()
        val category = view.tag as Category
        switchToApiActivity(category)
    }

    private fun switchToApiActivity(category: Category){
        val apiIntent = Intent(this, ApiActivity::class.java)
        apiIntent.putExtra(EXTRA_CATEGORY, category)
        startActivity(apiIntent)
        finish()
    }

    override fun onBackPressed() {
        closeDrawerOrMinimizeApp()
    }
//todo dodac obracNIE EKRANU
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putBoolean(CONFIGURATION_CHANGE, true)
    }

}
