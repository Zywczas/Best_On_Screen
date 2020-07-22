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
//todo pomyslec nad daniem nazwy na koniec AndConfirmFinish
        val areDependenciesInjected = injectDependencies()

        if (areDependenciesInjected) {
            setupChain()
        }
        setupDrawer()
        setupTags()
        setupOnScrollListener()
    }

    private fun injectDependencies() : Boolean {
        App.moviesComponent.inject(this)
        return true
    }

    private fun setupChain() {
        val isRecyclerViewSetup = setupAdapterAndLayoutManager()

        if(isRecyclerViewSetup) {
            setupObserverAndGetMoviesOnViewModelInit()
        }
    }

    private fun setupAdapterAndLayoutManager() : Boolean {
        setupAdapter()
        setupLayoutManager()
        return true
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

    private fun setupObserverAndGetMoviesOnViewModelInit(){
        val isObserverSetup = setupObserver()

        if(isObserverSetup) {
            getMoviesOnViewModelInit()
        }
    }

    private fun setupObserver() : Boolean {
        viewModel.getLD().observe(this,
            Observer { tripleMoviesPageCategory ->
                hideProgressBar()
                val incomingPage = tripleMoviesPageCategory.second
                when (incomingPage) {
                    ERROR_FLAG -> { showToast("Problem with downloading movies.") }
                    NO_MORE_PAGES_FLAG -> { showToast("This is the last page in this category.") }
                    else -> {
                        val incomingMovies = tripleMoviesPageCategory.first
                        val incomingCategory = tripleMoviesPageCategory.third

                        adapter.submitList(incomingMovies.toMutableList())
                        setupToolbarTitle(incomingCategory)
                        prepareDataForNextCall(incomingPage, incomingCategory)
                    }
                }
            }
        )
        return true
    }

    private fun hideProgressBar() {
        progressBar.isVisible = false
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
            //todo sprobowc te nulle wszystkie pousuwac
            intent.getStringExtra(EXTRA_CATEGORY)?.let { movieCategory = Category.valueOf(it) }
            viewModel.getApiMovies(movieCategory, nextPage)
            viewModel.finishViewModelInitialization()
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
