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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.utilities.*
import com.zywczas.bestonscreen.viewmodels.factories.GenericSavedStateViewModelFactory
import com.zywczas.bestonscreen.viewmodels.MoviesVM
import com.zywczas.bestonscreen.viewmodels.factories.MoviesVMFactory
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.nav_movies.*
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Extension function allowing to observe Live Data once and remove Observer straight away to
 * not allow user to create multiple observers by clicking the same button few times or by changing
 * categories.
 */
fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, object : Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer(value)
        }
    })
}

/**
 * Second activity for displaying movies. This activity focuses only on movies downloaded from API.
 * It is separated from DBMoviesActivity to make Live data easier to manage.
 */
class MoviesActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: MoviesVMFactory
    private val moviesVM: MoviesVM by viewModels {
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

    //tags used to choose category of movie and to be passed to MovieRepository
    private fun setupTags() {
        upcomingTextView.tag = UPCOMING
        topRatedTextView.tag = TOP_RATED
        popularTextView.tag = POPULAR
        toWatchListTextView.tag = TO_WATCH
    }

    private fun setupObserver() {
        if (categoryFromIntent != null) {
            moviesVM.getApiMovies(categoryFromIntent!!).observe(this, Observer { movies ->
                logD("otrzymuje API liste w onCreate")
                movieAdapter.submitList(movies.toMutableList())
            })
        } else {
            showToast("Cannot download movies")
        }
        moviesToolbar.title = "Movies: $categoryFromIntent"
    }

    fun toWatchClicked(view: View) {
        closeDrawerOrMinimizeApp()
        val toWatchIntent = Intent(this, DBMoviesActivity::class.java)
        startActivity(toWatchIntent)
        finish()
    }

    fun categoryClicked(view: View) {
        closeDrawerOrMinimizeApp()
//        progressBarMovies.isVisible = true
//
//        val category = view.tag as Category
//
//        moviesVM.getApiMovies(category).observe(this, Observer {
//                it.getContentIfNotHandled()?.let { movies ->
//                    logD("list adapter dostaje liste")
//                    movieAdapter.submitList(movies.toMutableList())
//                    moviesRecyclerView.scrollToPosition(0)
//                    progressBarMovies.isVisible = false
//                }
//        })
//
//        moviesToolbar.title = "Movies: $category"
    }

    override fun onDestroy() {
        moviesVM.clearDisposables()
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
