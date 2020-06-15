package com.zywczas.bestonscreen.views

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.*
import com.zywczas.bestonscreen.viewmodels.factories.GenericSavedStateViewModelFactory
import com.zywczas.bestonscreen.viewmodels.ApiMoviesVM
import com.zywczas.bestonscreen.viewmodels.factories.ApiMoviesVMFactory
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.nav_movies.*
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Second activity for displaying movies. This activity focuses only on movies downloaded from API.
 * It is separated from DBMoviesActivity to make Live data easier to manage.
 */
class ApiMoviesActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ApiMoviesVMFactory
    private val viewModel: ApiMoviesVM by viewModels {
        GenericSavedStateViewModelFactory(
            factory,
            this
        )
    }
    private lateinit var adapter: MovieAdapter

    //backup list to pass to ViewModel if orientation changed
    @Inject
    lateinit var moviesList: ArrayList<Movie>

    @Inject
    lateinit var picasso: Picasso
    private var orientation by Delegates.notNull<Int>()
    private var movieCategory = POPULAR
    private var nextPage = 1
    private var wasScreenRotated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        progressBarMovies.isVisible = false
        emptyListTextView.isVisible = false
        orientation = resources.configuration.orientation

        App.moviesComponent.inject(this)

        //setting up drawer layout and toggle button
        val toggleMovies = ActionBarDrawerToggle(
            this,
            drawer_layout_movies,
            moviesToolbar,
            R.string.nav_drawer_open,
            R.string.nav_drawer_closed
        )
        drawer_layout_movies.addDrawerListener(toggleMovies)
        toggleMovies.syncState()

        //movieCategory either from intent or from SavedStateHandle
        intent.getStringExtra(EXTRA_CATEGORY)?.let { movieCategory = it }
        viewModel.getSavedCategory()?.let {
            movieCategory = it
            viewModel.clearSavedCategory()
        }
        viewModel.getMetaState()?.let { wasScreenRotated = it }

        setupAdapter()
        setupTags()
        setupObserver()
        getFirstData()
        setupOnScrollListener()

    }

    private fun setupAdapter() {
        adapter = MovieAdapter(this, picasso)
        //custom onClick method for recycler view
        { movie ->
            val movieDetailsActivity = Intent(this, MovieDetailsActivity::class.java)
            movieDetailsActivity.putExtra(EXTRA_MOVIE, movie)
            startActivity(movieDetailsActivity)
        }
        moviesRecyclerView.adapter = adapter
        var spanCount = 2
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4
        }
        val layoutManager = GridLayoutManager(this, spanCount)
        moviesRecyclerView.layoutManager = layoutManager

    }

    //tags used to choose category of movie and to be passed to ApiMoviesRepo
    private fun setupTags() {
        upcomingTextView.tag = UPCOMING
        topRatedTextView.tag = TOP_RATED
        popularTextView.tag = POPULAR
        toWatchListTextView.tag = TO_WATCH
    }

    private fun setupObserver() {
        viewModel.getLd().observe(this,
            Observer { pairMoviesInt ->
                logD("observer otrzymuje live data")
                //'0' working as a flag
                if (pairMoviesInt.second == 0) {
                    showToast("This is the last page in this category.")
                    progressBarMovies.isVisible = false
                } else {
                    //tu dodac rosniecie listy
                    adapter.submitList(pairMoviesInt.first.toMutableList())
                    progressBarMovies.isVisible = false
                    //prepare data for next call
                    nextPage = pairMoviesInt.second + 1
                    moviesList = pairMoviesInt.first
                }
            }
        )
        moviesToolbar.title = "Movies: $movieCategory"
    }

    private fun getFirstData() {
        progressBarMovies.isVisible = true
        if (wasScreenRotated) {
            logD("leci z saved state")
            viewModel.getSavedStateLd()
            wasScreenRotated = false
            viewModel
        } else {
            logD("leci z api")
            viewModel.getApiMovies(movieCategory, nextPage)
        }
    }

    private fun setupOnScrollListener() {
        moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    progressBarMovies.isVisible = true
                    viewModel.getApiMovies(movieCategory, nextPage)
                }
            }
        })
    }

    fun toWatchClicked(view: View) {
        closeDrawerOrMinimizeApp()
        val toWatchIntent = Intent(this, DBMoviesActivity::class.java)
        startActivity(toWatchIntent)
        finish()
    }

    /**
     * This method resets list of movies and category.
     */
    fun categoryClicked(view: View) {
        closeDrawerOrMinimizeApp()
        var clickedCategory = view.tag as String
        //to sprawdzic czy moze byc ==
        if (movieCategory.equals(clickedCategory)) {
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
            this.moveTaskToBack(true);
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        viewModel.saveCategory(SAVED_CATEGORY, movieCategory)
        //this method needs current list f movies and current page
        viewModel.saveLD(SAVED_LD, PairMoviesInt(moviesList, nextPage - 1))
        viewModel.saveMetaState(SAVED_STATE, true)
    }
}
