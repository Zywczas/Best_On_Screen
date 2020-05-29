package com.zywczas.bestonscreen.view

import android.content.Intent
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
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.utilities.EXTRA_MOVIE
import com.zywczas.bestonscreen.viewModel.MoviesVM
import com.zywczas.bestonscreen.viewModel.MoviesVMFactory
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.nav_movies.*
import javax.inject.Inject

class MoviesActivity : AppCompatActivity() {

    @Inject lateinit var factory: MoviesVMFactory
    lateinit var moviesVM : MoviesVM
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

        moviesVM = ViewModelProvider(this, factory).get(MoviesVM::class.java)

        setupAdapter()
        setupTags()
    }

    private fun setupAdapter() {
        movieAdapter = MovieAdapter(this, moviesVM.movies, picasso) {movie ->
            val movieDetailsActivity = Intent(this, MovieDetailsActivity::class.java)
            movieDetailsActivity.putExtra(EXTRA_MOVIE, movie)
            startActivity(movieDetailsActivity)
        }
        moviesRecyclerView.adapter = movieAdapter
        val layoutManager = GridLayoutManager(this, 2)
        moviesRecyclerView.layoutManager = layoutManager
    }

    //tags used to choose category of movie and to be passed to Repository
    private fun setupTags() {
        upcomingTextView.tag = Category.UPCOMING
        topRatedTextView.tag = Category.TOP_RATED
        popularTextView.tag = Category.POPULAR
    }

    fun categoryClicked(view : View) {
        closeDrawer()
        getMovies(view.tag as Category)
    }

    private fun getMovies(category: Category){
        moviesVM.getMoviesLiveData(this, category).observe(this,
            Observer { movies ->
                if (movies != null) {
                    moviesVM.movies.clear()
                    moviesVM.movies.addAll(movies)
                    movieAdapter.notifyDataSetChanged()
                }
            })
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
