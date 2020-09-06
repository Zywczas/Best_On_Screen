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
import com.zywczas.bestonscreen.model.*
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
    private val viewModel: ApiVM by viewModels { GenericSavedStateViewModelFactory(factory, this) }
    private lateinit var adapter: MovieAdapter
    @Inject
    lateinit var picassoForAdapter: Picasso
    private val anyCategoryOnInit = Category.POPULAR
    private var currentCategory = anyCategoryOnInit
    private var wasOrientationChanged: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_and_db)
        wasOrientationChanged = savedInstanceState?.getBoolean(CONFIGURATION_CHANGE)
        startApiActivitySetupChain()
        setupDrawer()
        setupTags()
    }

    private fun startApiActivitySetupChain() {
        injectDependencies { injectionFinished ->
            if (injectionFinished) {
                setupRecyclerView { recyclerViewSetupFinished ->
                    if (recyclerViewSetupFinished) {
                        setupMoviesObserver { observerSetupFinished ->
                            if (observerSetupFinished) {
                                getMoviesOnViewModelInit()
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
        viewModel.moviesLD.observe(this,
            Observer { vmResource ->
                //todo moze podzielic na mniejsze
                hideProgressBar()
                when (vmResource.status) {
                    Status.SUCCESS -> {
                        val incomingMovies = vmResource.data!!.first
                        val incomingCategory = vmResource.data.second
                        updateDisplayedMovies(incomingMovies)
                        setupToolbarTitle(incomingCategory)
                        prepareDataForNextCall(incomingCategory)
                    }
                    Status.ERROR -> {
                        showToast(vmResource.message!!)
                    }
                    Status.LOADING -> {
                        //todo dodac internet handling
                        showToast("still loading")
                    }
                }
            }
        )
        complete(true)
    }

    //todo pousuwac logi

    private fun hideProgressBar() {
        progressBar.isVisible = false
    }

    private fun updateDisplayedMovies(movies: List<Movie>) {
        adapter.submitList(movies.toMutableList())
    }

    private fun setupToolbarTitle(incomingCategory: Category) {
        toolbar.title = "Movies: $incomingCategory"
    }

    private fun prepareDataForNextCall(incomingCategory: Category) {
        currentCategory = incomingCategory
    }

    private fun getMoviesOnViewModelInit() {
        if (wasOrientationChanged == null) {
            showProgressBar()
            val intentCategory = intent.getSerializableExtra(EXTRA_CATEGORY) as Category
            viewModel.getApiMovies(intentCategory)
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
                    downloadNextPage()
                }
            }
        })
    }

    private fun downloadNextPage() {
        showProgressBar()
        viewModel.getApiMovies(currentCategory)
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
        val toWatchIntent = Intent(this, DBActivity::class.java)
        startActivity(toWatchIntent)
        finish()
    }

    fun categoryClicked(view: View) {
        closeDrawerOrMinimizeApp()
        val clickedCategory = view.tag as Category
        if (clickedCategory == currentCategory) {
            showToast("This is $clickedCategory.")
        } else {
            switchToNewMoviesCategory(clickedCategory)
        }
    }

    private fun switchToNewMoviesCategory(clickedCategory: Category) {
        showProgressBar()
        viewModel.getApiMovies(clickedCategory)
        moviesRecyclerView.scrollToPosition(0)
    }

    override fun onBackPressed() {
        closeDrawerOrMinimizeApp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(CONFIGURATION_CHANGE, true)
    }

    //todo dodac internet error handling
}
