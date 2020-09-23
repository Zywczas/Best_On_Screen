package com.zywczas.bestonscreen.views

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.*
import com.zywczas.bestonscreen.viewmodels.DBVM
import com.zywczas.bestonscreen.viewmodels.factories.DBVMFactory
import kotlinx.android.synthetic.main.activity_api_and_db.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.navigation_drawer.*
import javax.inject.Inject

class DBActivity : AppCompatActivity() {



//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_api_and_db)
//        startDBActivitySetupChain()
//        setupDrawer()
//        setupTags()
//        checkInternetConnection()
//    }

//    private fun startDBActivitySetupChain() {
//        injectDependencies { injectionFinished ->
//            if (injectionFinished) {
//                initViewModel()
//                setupRecyclerView { recyclerViewSetupFinished ->
//                    if (recyclerViewSetupFinished) {
//                        setupMoviesObserver()
//                    }
//                }
//            }
//        }
//    }


//    private fun initViewModel() {
//        viewModel = ViewModelProvider(this, factory).get(DBVM::class.java)
//    }

//    private fun setupRecyclerView(complete: (Boolean) -> Unit) {
//        setupAdapter()
//        setupLayoutManager()
//        complete(true)
//    }

//    private fun setupAdapter() {
//        adapter = MovieAdapter(this, picassoForAdapter) { movie ->
//            val detailsActivity = Intent(this, DetailsActivity::class.java)
//            detailsActivity.putExtra(EXTRA_MOVIE, movie)
//            startActivity(detailsActivity)
//        }
//        moviesRecyclerView.adapter = adapter
//    }
//
//    private fun setupLayoutManager() {
//        var spanCount = 2
//        val orientation = resources.configuration.orientation
//        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            spanCount = 4
//        }
//        val layoutManager = GridLayoutManager(this, spanCount)
//        moviesRecyclerView.layoutManager = layoutManager
//        moviesRecyclerView.setHasFixedSize(true)
//    }

//    private fun setupMoviesObserver() {
//        viewModel.moviesLD.observe(this) { movies ->
//            updateDisplayedMovies(movies)
//            if (movies.isEmpty()) {
//                showMessageAboutEmptyDB()
//            }
//        }
//    }

//    private fun updateDisplayedMovies(movies: List<Movie>) {
//        adapter.submitList(movies.toMutableList())
//    }
//
//    private fun showMessageAboutEmptyDB() {
//        emptyListTextView.isVisible = true
//    }

//    private fun setupDrawer() {
//        val toggle = ActionBarDrawerToggle(
//            this, drawer_layout, toolbar,
//            R.string.nav_drawer_open, R.string.nav_drawer_closed
//        )
//        drawer_layout.addDrawerListener(toggle)
//        toggle.syncState()
//    }
//
//    private fun setupTags() {
//        upcomingTextView.tag = Category.UPCOMING
//        topRatedTextView.tag = Category.TOP_RATED
//        popularTextView.tag = Category.POPULAR
//    }
//
//    private fun checkInternetConnection() {
//        if (!Variables.isNetworkConnected) {
//            showToast(CONNECTION_PROBLEM)
//        }
//    }

//    fun myToWatchListClicked(view: View) {
//        closeDrawerOrMinimizeApp()
//        showToast("This is your list.")
//    }
//
//    private fun closeDrawerOrMinimizeApp() {
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//        } else {
//            this.moveTaskToBack(true)
//        }
//    }
//
//    fun categoryClicked(view: View) {
//        closeDrawerOrMinimizeApp()
//        if (Variables.isNetworkConnected) {
//            val category = view.tag as Category
//            switchToApiActivity(category)
//        } else {
//            showToast(CONNECTION_PROBLEM)
//        }
//    }
//
//    private fun switchToApiActivity(category: Category) {
//        val apiActivityIntent = Intent(this, ApiActivity::class.java)
//        apiActivityIntent.putExtra(EXTRA_CATEGORY, category)
//        startActivity(apiActivityIntent)
//        finish()
//    }
//
//    override fun onBackPressed() {
//        closeDrawerOrMinimizeApp()
//    }

}
