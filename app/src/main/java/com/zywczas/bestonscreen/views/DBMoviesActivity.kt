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
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.utilities.*
import com.zywczas.bestonscreen.viewmodels.DBMoviesVM
import com.zywczas.bestonscreen.viewmodels.factories.DBMoviesVMFactory
import com.zywczas.bestonscreen.viewmodels.factories.GenericSavedStateViewModelFactory
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.nav_movies.*
import javax.inject.Inject
import kotlin.properties.Delegates

class DBMoviesActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: DBMoviesVMFactory
    private val dBMoviesVM: DBMoviesVM by viewModels { GenericSavedStateViewModelFactory(factory,this) }
    private lateinit var movieAdapter: MovieAdapter
    @Inject lateinit var picasso: Picasso
    var orientation by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        progressBarMovies.isVisible = false
        moviesToolbar.title = "Movies: To Watch List"
        orientation = resources.configuration.orientation

        App.moviesComponent.inject(this)

        //setting up drawer layout and toggle button
        val toggleMovies = ActionBarDrawerToggle(this,drawer_layout_movies,moviesToolbar,R.string.nav_drawer_open,R.string.nav_drawer_closed)
        drawer_layout_movies.addDrawerListener(toggleMovies)
        toggleMovies.syncState()

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
        if (orientation ==  Configuration.ORIENTATION_LANDSCAPE){
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

    override fun onResume() {
        super.onResume()

    }

    private fun setupObserver() {
        dBMoviesVM.getDbMovies().observe(this, Observer {
            it.getContentIfNotHandled()?.let {movies -> logD("otrzymuje DB liste w onCreate")
                movieAdapter.submitList(movies.toMutableList())
            }
        })
    }

    fun toWatchClicked (view: View) {
        closeDrawerOrMinimizeApp()
        showToast("This is your list.")
    }

    fun categoryClicked(view: View) {
        closeDrawerOrMinimizeApp()
        dBMoviesVM.clearDisposables()
        val category = view.tag as String

        val moviesIntent = Intent(this, ApiMoviesActivity::class.java)
        moviesIntent.putExtra(EXTRA_CATEGORY, category)
        startActivity(moviesIntent)
        finish()
    }

    override fun onDestroy() {
        dBMoviesVM.clearDisposables()
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
