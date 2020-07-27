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
import com.zywczas.bestonscreen.viewmodels.factories.GenericSavedStateViewModelFactory
import kotlinx.android.synthetic.main.activity_api_and_db.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.navigation.*
import javax.inject.Inject

class ApiActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ApiVMFactory
    private val viewModel: ApiVM by viewModels { GenericSavedStateViewModelFactory(factory,this) }
    private lateinit var adapter: MovieAdapter
    @Inject
    lateinit var picassoForAdapter: Picasso
    private var movieCategory = Category.POPULAR
    private var nextPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_and_db)
        startApiActivitySetupChain()
        setupDrawer()
        setupTags()
        setupOnScrollListener()
    }

    private fun startApiActivitySetupChain() {
        injectDependencies{success ->
            if (success) {
                setupAdapterAndLayoutManager {success2 ->
                    if (success2) {
                        setupMoviesObserver { success3 ->
                            if (success3) {
                                getMoviesOnViewModelInit()
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

    private fun setupAdapterAndLayoutManager(complete: (Boolean) -> Unit) {
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

    private fun setupMoviesObserver(complete: (Boolean) -> Unit) {
        viewModel.getLD().observe(this,
            Observer { trioMoviesPageCategory ->
                hideProgressBar()
                val incomingPage = trioMoviesPageCategory.second
                when (incomingPage) {
                    ERROR_FLAG -> { showToast("Problem with downloading movies.") }
                    NO_MORE_PAGES_FLAG -> { showToast("This is the last page in this category.") }
                    else -> {
                        val incomingMovies = trioMoviesPageCategory.first
                        val incomingCategory = trioMoviesPageCategory.third
                        updateDisplayedMovies(incomingMovies)
                        setupToolbarTitle(incomingCategory)
                        prepareDataForNextCall(incomingPage, incomingCategory)
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

    private fun setupToolbarTitle(category: Category){
        val titleCategory = when (category) {
            Category.POPULAR -> "Popular"
            Category.UPCOMING -> "Upcoming"
            Category.TOP_RATED -> "Top Rated"
        }
        toolbar.title = "Movies: $titleCategory"
    }

    private fun prepareDataForNextCall(incomingPage: Int, incomingCategory: Category) {
        nextPage = incomingPage + 1
        movieCategory = incomingCategory
    }

    private fun getMoviesOnViewModelInit(){
        if (viewModel.isViewModelInitialization()) {
            showProgressBar()
            val categoryFromIntent = intent.getStringExtra(EXTRA_CATEGORY)
            if (categoryFromIntent != null) {
                movieCategory = Category.valueOf(categoryFromIntent)
                viewModel.getApiMovies(movieCategory, nextPage)
                viewModel.finishViewModelInitialization()
            } else {
                showToast("Cannot access the category.")
                logD("Cannot get movie category from intent in ${this.localClassName}")
            }
        }
    }

    private fun showProgressBar(){
        progressBar.isVisible = true
    }

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

    private fun setupOnScrollListener() {
        moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val isRecyclerViewBottom = !recyclerView.canScrollVertically(1) &&
                        newState == RecyclerView.SCROLL_STATE_IDLE
                if (isRecyclerViewBottom) {
                    downloadNextPage()
                }
            }
        })
    }

    private fun downloadNextPage(){
        showProgressBar()
        viewModel.getApiMovies(movieCategory, nextPage)
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

    private fun switchToDBActivity(){
        val toWatchIntent = Intent(this, DBActivity::class.java)
        startActivity(toWatchIntent)
        finish()
    }

    fun categoryClicked(view: View) {
        closeDrawerOrMinimizeApp()
        val clickedCategory = view.tag as Category
        if (clickedCategory == movieCategory) {
            showToast("This is $clickedCategory.")
        } else {
            switchToNewMoviesCategory(clickedCategory)
        }
    }

    private fun switchToNewMoviesCategory(clickedCategory: Category){
        showProgressBar()
        val firstPageOfNewCategory = 1
        viewModel.getApiMovies(clickedCategory, firstPageOfNewCategory)
        moviesRecyclerView.scrollToPosition(0)
    }

    override fun onBackPressed() {
        closeDrawerOrMinimizeApp()
    }

    override fun onDestroy() {
        viewModel.clearDisposables()
        super.onDestroy()
    }
}
