package com.zywczas.bestonscreen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.di.App
import com.zywczas.bestonscreen.model.MovieCategory
import com.zywczas.bestonscreen.viewModel.MoviesViewModel
import com.zywczas.bestonscreen.viewModel.MoviesViewModelFactory
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.nav_movies.*
import javax.inject.Inject

class MoviesActivity : AppCompatActivity() {

    @Inject lateinit var factory: MoviesViewModelFactory
    lateinit var moviesViewModel : MoviesViewModel
    lateinit var movieAdapter: MovieAdapter
    @Inject lateinit var picasso: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        App.moviesComponent.inject(this)

        //setting up drawer layout and toggle button
        val toggleMovies = ActionBarDrawerToggle(this, drawer_layout_movies, moviesToolbar, R.string.nav_drawer_open, R.string.nav_drawer_closed)
        drawer_layout_movies.addDrawerListener(toggleMovies)
        toggleMovies.syncState()

        moviesViewModel = ViewModelProvider(this, factory).get(MoviesViewModel::class.java)

        setupAdapter()
        setupTags()
    }

    private fun setupAdapter() {
        movieAdapter = MovieAdapter(this, moviesViewModel.movies, picasso)
        moviesRecyclerView.adapter = movieAdapter
        //dac to do daggera
        val layoutManager = GridLayoutManager(this, 2)
        moviesRecyclerView.layoutManager = layoutManager
    }

    //tags used to choose category of movie and passed to Repository
    private fun setupTags() {
        upcomingTextView.tag = MovieCategory.UPCOMING
        topRatedTextView.tag = MovieCategory.TOP_RATED
        popularTextView.tag = MovieCategory.POPULAR
    }

    fun movieCategoryClicked(view : View) {
        closeDrawer()
        getMovies(view.tag as MovieCategory)
    }

    private fun getMovies(movieCategory: MovieCategory){
        moviesViewModel.getMoviesLiveData(this, movieCategory).observe(this,
            Observer { movies ->
                if (movies != null) {
                    moviesViewModel.movies.clear()
                    moviesViewModel.movies.addAll(movies)
                    movieAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onDestroy() {
        moviesViewModel.clear()
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
