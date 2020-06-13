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
    private val apiMoviesVM: ApiMoviesVM by viewModels {
        GenericSavedStateViewModelFactory(
            factory,
            this
        )
    }
    private lateinit var movieAdapter: MovieAdapter
    @Inject
    lateinit var picasso: Picasso
    var orientation by Delegates.notNull<Int>()
    var categoryFromIntent: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        progressBarMovies.isVisible = false
        orientation = resources.configuration.orientation

        App.moviesComponent.inject(this)

        //setting up drawer layout and toggle button
        val toggleMovies = ActionBarDrawerToggle(this, drawer_layout_movies,moviesToolbar,R.string.nav_drawer_open,R.string.nav_drawer_closed        )
        drawer_layout_movies.addDrawerListener(toggleMovies)
        toggleMovies.syncState()

        categoryFromIntent = intent.getStringExtra(EXTRA_CATEGORY)

        setupAdapter()
        setupTags()
        setupObserver()
        setupOnScrollListener()
    }

    private fun setupAdapter() {
        movieAdapter = MovieAdapter(this, picasso)
        //custom onClick method for recycler view
        { movie ->
            val movieDetailsActivity = Intent(this, MovieDetailsActivity::class.java)
            movieDetailsActivity.putExtra(EXTRA_MOVIE, movie)
            startActivity(movieDetailsActivity)
        }
        moviesRecyclerView.adapter = movieAdapter
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



//    private fun setupObserver() {
//        progressBarMovies.isVisible = true
//
//        if (categoryFromIntent != null) {
//            apiMoviesVM.getApiMovies(categoryFromIntent!!).observe(this, object : Observer<Event<List<Movie>>>{
//                override fun onChanged(t: Event<List<Movie>>?) {
//                    logD("onChanged rozpoczety")
//                    if (t != null) {
//                        logD("odbiera event")
//                        val movies = t.getContentIfNotHandled()
//                        if (movies != null) {
//                            logD("adapter odbiera liste")
//                            movieAdapter.submitList(movies.toMutableList())
//                            progressBarMovies.isVisible = false
////                            apiMoviesVM.getApiMovies(categoryFromIntent!!).removeObserver(this)
////                            logD("observer usuniety")
//                        }
//                    }
//                }
//            })
//
//        } else {
//            logD("pusty intent")
//        }
//
//        moviesToolbar.title = "Movies: $categoryFromIntent"
//    }

    private fun setupObserver() {
        progressBarMovies.isVisible = true
        if (categoryFromIntent != null) {
            apiMoviesVM.getApiMovies(categoryFromIntent!!).observe(this, object : Observer<List<Movie>>{
                override fun onChanged(t: List<Movie>?) {
                    if (t != null) {
                        logD("adapter otrzymuje liste w onCreate")
                        movieAdapter.submitList(t.toMutableList())
                        progressBarMovies.isVisible = false
                        apiMoviesVM.getApiMovies("any String here").removeObservers(this@ApiMoviesActivity)
                        logD("observery usuniete")
                    }
                }
            })

        }

        moviesToolbar.title = "Movies: $categoryFromIntent"
    }

    private fun setupOnScrollListener() {
        moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && newState ==RecyclerView.SCROLL_STATE_IDLE){
                    logD("this is the end")
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

//    fun categoryClicked(view: View) {
//        closeDrawerOrMinimizeApp()
//        progressBarMovies.isVisible = true
//        val category = view.tag as String
//        apiMoviesVM.getApiMovies(category).removeObservers(this)
//
//        apiMoviesVM.getApiMovies(category).observe(this, Observer {
//                it.getContentIfNotHandled()?.let { movies ->
//                    logD("category clicked list adapter dostaje liste")
//                    moviesRecyclerView.scrollToPosition(0)
//                    movieAdapter.submitList(movies.toMutableList())
//                    progressBarMovies.isVisible = false
//                }
//        })
//
//        moviesToolbar.title = "Movies: $category"
//    }

    fun categoryClicked(view: View) {
        closeDrawerOrMinimizeApp()
        progressBarMovies.isVisible = true
        val category = view.tag as String

        apiMoviesVM.getApiMovies("jakis napis").removeObservers(this)

        apiMoviesVM.getApiMovies(category).observe(this, Observer { movies ->
                logD("category clicked list adapter dostaje liste")
                moviesRecyclerView.scrollToPosition(0)
                movieAdapter.submitList(movies.toMutableList())
                progressBarMovies.isVisible = false

        })

        moviesToolbar.title = "Movies: $category"
    }

    override fun onDestroy() {
        apiMoviesVM.clearDisposables()
        super.onDestroy()
    }

    override fun onBackPressed() {
        closeDrawerOrMinimizeApp()
//        super.onBackPressed()
    }

    private fun closeDrawerOrMinimizeApp() {
        if (drawer_layout_movies.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_movies.closeDrawer(GravityCompat.START)
        } else {
            this.moveTaskToBack(true);
        }
    }
}
