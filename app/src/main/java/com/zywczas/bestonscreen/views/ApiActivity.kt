package com.zywczas.bestonscreen.views

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.*
import com.zywczas.bestonscreen.viewmodels.ApiVM
import com.zywczas.bestonscreen.viewmodels.factories.ApiVMFactory
import kotlinx.android.synthetic.main.activity_api_and_db.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.navigation.*
import javax.inject.Inject

class ApiActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ApiVMFactory
    @Inject
    lateinit var picassoForAdapter: Picasso
    private lateinit var viewModel: ApiVM
    private lateinit var adapter: MovieAdapter
    private var wasConfigurationChanged: Boolean? = null
    private var displayedCategory : Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_and_db)
        wasConfigurationChanged = savedInstanceState?.getBoolean(CONFIGURATION_CHANGE)
        startApiActivitySetupChain()
        setupDrawer()
        setupTags()
    }

    private fun startApiActivitySetupChain() {
        injectDependencies { injectionFinished ->
            if (injectionFinished) {
                initViewModel()
                setupRecyclerView { recyclerViewSetupFinished ->
                    if (recyclerViewSetupFinished) {
                        setupMoviesObserver { observerSetupFinished ->
                            if (observerSetupFinished) {
                                getMoviesOnViewModelInitIfConnected()
                                setupOnScrollListener()
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

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, factory).get(ApiVM::class.java)
    }

    private fun setupRecyclerView(complete: (Boolean) -> Unit) {
        setupAdapter()
        setupLayoutManager()
        complete(true)
    }

    private fun setupAdapter() {
        adapter = MovieAdapter(this, picassoForAdapter) { movie ->
            val detailsActivity = Intent(this, DetailsActivity::class.java)
            detailsActivity.putExtra(EXTRA_MOVIE, movie)
            startActivity(detailsActivity)
        }
        moviesRecyclerView.adapter = adapter
    }

    private fun setupLayoutManager() {
        var spanCount = 2
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4
        }
        val layoutManager = GridLayoutManager(this, spanCount)
        moviesRecyclerView.layoutManager = layoutManager
        moviesRecyclerView.setHasFixedSize(true)
    }

    private fun setupMoviesObserver(complete: (Boolean) -> Unit) {
        viewModel.moviesAndCategoryLD.observe(this,
            Observer { resource ->
                hideProgressBar()
                when (resource.status) {
                    Status.SUCCESS -> {
                        val incomingMovies = resource.data!!.first
                        val incomingCategory = resource.data.second
                        updateDisplayedMovies(incomingMovies)
                        updateToolbarTitle(incomingCategory)
                        displayedCategory = incomingCategory
                    }
                    Status.ERROR -> {
                        showToast(resource.message!!)
                    }
                }
            }
        )
        complete(true)
    }

    private fun hideProgressBar() {
        progressBar.isVisible = false
    }

    private fun updateDisplayedMovies(movies: List<Movie>) {
        adapter.submitList(movies.toMutableList())
    }

    private fun updateToolbarTitle(category: Category) {
        toolbar.title = "Movies: $category"
    }

    private fun getMoviesOnViewModelInitIfConnected() {
        if (wasConfigurationChanged == null) {
            if (Variables.isNetworkConnected) {
                val categoryFromIntent = intent.getSerializableExtra(EXTRA_CATEGORY) as Category
                showProgressBar()
                viewModel.getNextMovies(categoryFromIntent)
            } else {
                showToast(CONNECTION_PROBLEM)
            }
        }
    }

    private fun showProgressBar() {
        progressBar.isVisible = true
    }

    private fun setupOnScrollListener() {
        moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val isRecyclerViewBottom = !recyclerView.canScrollVertically(1) &&
                        newState == RecyclerView.SCROLL_STATE_IDLE
                if (isRecyclerViewBottom) {
                    downloadNextPageIfConnected()
                }
            }
        })
    }

    private fun downloadNextPageIfConnected() {
        if (Variables.isNetworkConnected) {
            showProgressBar()
            viewModel.getNextMovies()
        } else {
            showToast(CONNECTION_PROBLEM)
        }
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.nav_drawer_open, R.string.nav_drawer_closed
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setupTags() {
        upcomingTextView.tag = Category.UPCOMING
        topRatedTextView.tag = Category.TOP_RATED
        popularTextView.tag = Category.POPULAR
    }

    fun myToWatchListClicked(view: View) {
        closeDrawerOrMinimizeApp()
        switchToDBActivity()
    }

    private fun closeDrawerOrMinimizeApp() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            this.moveTaskToBack(true)
        }
    }

    private fun switchToDBActivity() {
        val databaseActivityIntent = Intent(this, DBActivity::class.java)
        startActivity(databaseActivityIntent)
        finish()
    }

    fun categoryClicked(view: View) {
        closeDrawerOrMinimizeApp()
        val clickedCategory = view.tag as Category
        if (clickedCategory == displayedCategory) {
            showToast("This is $clickedCategory.")
        } else {
            downloadNewCategoryIfConnected(clickedCategory)
        }
    }

    private fun downloadNewCategoryIfConnected(category: Category){
        if (Variables.isNetworkConnected){
            showProgressBar()
            viewModel.getNextMovies(category)
            moviesRecyclerView.scrollToPosition(0)
        } else {
            showToast(CONNECTION_PROBLEM)
        }
    }

    override fun onBackPressed() {
        closeDrawerOrMinimizeApp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(CONFIGURATION_CHANGE, true)
    }


}
