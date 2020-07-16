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
import com.zywczas.bestonscreen.utilities.*
import com.zywczas.bestonscreen.viewmodels.ApiVM
import com.zywczas.bestonscreen.viewmodels.factories.ApiVMFactory
import com.zywczas.bestonscreen.viewmodels.factories.GenericSavedStateViewModelFactory
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.navigation.*
import javax.inject.Inject
import kotlin.properties.Delegates

class ApiActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ApiVMFactory
    private val viewModel: ApiVM by viewModels { GenericSavedStateViewModelFactory(factory,this) }
    private lateinit var adapter: MovieAdapter

    @Inject
    lateinit var picasso: Picasso
    private var orientation by Delegates.notNull<Int>()
    //todo sprawdzicz czy to empty category to dobry pomysl
    private var movieCategory = EMPTY_CATEGORY
    private var nextPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        progressBarMovies.isVisible = false
        orientation = resources.configuration.orientation

        App.moviesComponent.inject(this)

        //setting up drawer layout and toggle button
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout_movies,
            moviesToolbar,
            R.string.nav_drawer_open,
            R.string.nav_drawer_closed
        )
        drawer_layout_movies.addDrawerListener(toggle)
        toggle.syncState()

        setupAdapter()
        setupTags()
        setupObserver()
        checkIfFirstStart()
        setupOnScrollListener()
    }

    private fun setupAdapter() {
        adapter = MovieAdapter(this, picasso) { movie ->
            val movieDetailsActivity = Intent(this, DetailsActivity::class.java)
            movieDetailsActivity.putExtra(EXTRA_MOVIE, movie)
            startActivity(movieDetailsActivity)
        }
        moviesRecyclerView.setHasFixedSize(true)
        moviesRecyclerView.adapter = adapter
        var spanCount = 2
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4
        }
        val layoutManager = GridLayoutManager(this, spanCount)
        moviesRecyclerView.layoutManager = layoutManager

    }

    private fun setupTags() {
        upcomingTextView.tag = UPCOMING
        topRatedTextView.tag = TOP_RATED
        popularTextView.tag = POPULAR
        toWatchListTextView.tag = TO_WATCH
    }
//todo poprawic
    private fun setupObserver() {
        viewModel.getLd().observe(this,
            Observer { tripleMoviesPageCategory ->
                //'0' working as a flag
                if (tripleMoviesPageCategory.second == 0) {
                    showToast("This is the last page in this category.")
                    progressBarMovies.isVisible = false

                } else {
                    adapter.submitList(tripleMoviesPageCategory.first.toMutableList())
                    progressBarMovies.isVisible = false
                    moviesToolbar.title = "Movies: ${tripleMoviesPageCategory.third}"

                    //prepare data for next call
                    nextPage = tripleMoviesPageCategory.second + 1
                    movieCategory = tripleMoviesPageCategory.third
                }
            }
        )
    }
//todo zmienic nawe na poprawniejsza
    private fun checkIfFirstStart(){
        //check if first start to prevent reloading data on orientation changed
        if (viewModel.activityFirstStart) {
            progressBarMovies.isVisible = true
            intent.getStringExtra(EXTRA_CATEGORY)?.let { movieCategory = it }
            viewModel.getApiMovies(movieCategory, nextPage)
            viewModel.activityFirstStart = false
        }
    }

    private fun setupOnScrollListener() {
        moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
//todo moze poprawic wykrzyknik - warunek ten zamienic na nowa funkcje typu 'shouldGetMoreMovies', moze dodac tez sprawdzenie czy nie est to ostatnia strona
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    progressBarMovies.isVisible = true
                    viewModel.getApiMovies(movieCategory, nextPage)
                }
            }
        })
    }
//todo sprawdzic nazwe
    fun toWatchClicked(view: View) {
        closeDrawerOrMinimizeApp()
        val toWatchIntent = Intent(this, DBActivity::class.java)
        startActivity(toWatchIntent)
        finish()
    }

    //todo poprawic
    fun categoryClicked(view: View) {
        closeDrawerOrMinimizeApp()
        val clickedCategory = view.tag as String
        if (movieCategory == clickedCategory) {
            showToast("This is $clickedCategory.")
        } else {
            progressBarMovies.isVisible = true
            viewModel.getApiMovies(clickedCategory, 1)
            moviesRecyclerView.scrollToPosition(0)
            moviesToolbar.title = "Movies: $clickedCategory"
            movieCategory = clickedCategory
        }
    }

    override fun onDestroy() {
        viewModel.clearDisposables()
        super.onDestroy()
    }

    override fun onBackPressed() {
        closeDrawerOrMinimizeApp()
    }

    private fun closeDrawerOrMinimizeApp() {
        if (drawer_layout_movies.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_movies.closeDrawer(GravityCompat.START)
        } else {
            this.moveTaskToBack(true)
        }
    }
}
