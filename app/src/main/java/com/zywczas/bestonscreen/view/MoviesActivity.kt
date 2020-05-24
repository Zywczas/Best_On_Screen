package com.zywczas.bestonscreen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.GridLayoutManager
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.InjectorUtils
import com.zywczas.bestonscreen.viewModel.MoviesViewModel
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.content_movies.*

class MoviesActivity : AppCompatActivity() {

    lateinit var moviesViewModel : MoviesViewModel
    lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        //setting up drawer layout and toggle button
        val toggleMovies = ActionBarDrawerToggle(this, drawer_layout_movies, moviesToolbar, R.string.nav_drawer_open, R.string.nav_drawer_closed)
        drawer_layout_movies.addDrawerListener(toggleMovies)
        toggleMovies.syncState()

        val factory = InjectorUtils.provideMoviesViewModelFactory()
        moviesViewModel = ViewModelProvider(this, factory).get(MoviesViewModel::class.java)

        movieAdapter = MovieAdapter(this, moviesViewModel.movies)
        moviesRecyclerView.adapter = movieAdapter
        //dac to do daggera
        val layoutManager = GridLayoutManager(this, 2)
        moviesRecyclerView.layoutManager = layoutManager
    }

    fun popularTextViewClicked(view : View) {
        closeDrawer()
        getPopularMovies()
    }

    private fun getPopularMovies(){
        moviesViewModel.getPopularMovies(this).observe(this,
            Observer<List<Movie>> { m ->
                if (m != null) {
                    moviesViewModel.movies.clear()
                    moviesViewModel.movies.addAll(m)
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

    fun closeDrawer() {
        if (drawer_layout_movies.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_movies.closeDrawer(GravityCompat.START)
        }
    }
}
