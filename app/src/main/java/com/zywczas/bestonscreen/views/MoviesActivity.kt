package com.zywczas.bestonscreen.views

import android.content.Intent
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
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.utilities.EXTRA_MOVIE
import com.zywczas.bestonscreen.utilities.logD
import com.zywczas.bestonscreen.viewmodels.factories.GenericSavedStateViewModelFactory
import com.zywczas.bestonscreen.viewmodels.MoviesVM
import com.zywczas.bestonscreen.viewmodels.factories.MoviesVMFactory
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.nav_movies.*
import javax.inject.Inject

/**
 * Second activity for displaying movies. This activity focuses only on movies downloaded from API.
 * It is separated from DBMoviesActivity to make Live data of movies from local data base easier
 * to manage.
 */
class MoviesActivity : AppCompatActivity() {

    @Inject lateinit var factory: MoviesVMFactory
    private val moviesVM: MoviesVM by viewModels { GenericSavedStateViewModelFactory(factory,this) }
    private lateinit var movieAdapter: MovieAdapter
    @Inject lateinit var picasso: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        progressBarMovies.isVisible = false

        App.moviesComponent.inject(this)

        //setting up drawer layout and toggle button
        val toggleMovies = ActionBarDrawerToggle(this,drawer_layout_movies,moviesToolbar,R.string.nav_drawer_open,R.string.nav_drawer_closed)
        drawer_layout_movies.addDrawerListener(toggleMovies)
        toggleMovies.syncState()

//        moviesVM = ViewModelProvider(this, factory).get(MoviesVM::class.java)

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
        val layoutManager = GridLayoutManager(this, 2)
//        val layoutManager = LinearLayoutManager(this)
        moviesRecyclerView.layoutManager = layoutManager
    }

    private fun setupObserver() {
        moviesVM.getApiMovies(Category.TOP_RATED).observe(this, Observer {
                it.getContentIfNotHandled()?.let {movies -> logD("otrzymuje API liste w onCreate")
                    movieAdapter.submitList(movies.toMutableList())
                }
    })
    }

    //tags used to choose category of movie and to be passed to MovieRepository
    private fun setupTags() {
        upcomingTextView.tag = Category.UPCOMING
        topRatedTextView.tag = Category.TOP_RATED
        popularTextView.tag = Category.POPULAR
        toWatchListTextView.tag = Category.TO_WATCH
    }

    fun categoryClicked(view: View) {
        closeDrawer()
        progressBarMovies.isVisible = true

        val category = view.tag as Category

        moviesVM.getMovies(category).observe(this, Observer { movies ->
            logD("list adapter dostaje liste")
            movieAdapter.submitList(movies.toMutableList())
            progressBarMovies.isVisible = false
            moviesRecyclerView.smoothScrollToPosition(0)
//                moviesRecyclerView.smoothScrollToPosition(0)//to mozna do MovieAdapter wrzucic pozniej
        })

        moviesToolbar.title = "Movies: $category"
    }

    override fun onDestroy() {
        moviesVM.clear()
        super.onDestroy()
    }

    override fun onBackPressed() {
        closeDrawer()
        super.onBackPressed()
    }

    private fun closeDrawer() {
        if (drawer_layout_movies.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_movies.closeDrawer(GravityCompat.START)
        }
    }
}
