package com.zywczas.bestonscreen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.InjectorUtils
import com.zywczas.bestonscreen.viewModel.MoviesViewModel
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.content_movies.*

class MoviesActivity : AppCompatActivity() {

//    val application = Application()
    val movies = ArrayList<Movie>()

    lateinit var moviesViewModel : MoviesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        //setting up drawer layout and toggle button
        val toggleMovies = ActionBarDrawerToggle(this, drawer_layout_movies, moviesToolbar, R.string.nav_drawer_open, R.string.nav_drawer_closed)
        drawer_layout_movies.addDrawerListener(toggleMovies)
        toggleMovies.syncState()

        val factory = InjectorUtils.provideMoviesViewModelFactory()
        //chyba dobrze... ale inaczej niz w filmiku, spawdzic jak wyszlo
        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)

    }

    override fun onBackPressed() {
        if (drawer_layout_movies.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_movies.closeDrawer(GravityCompat.START)
        } else { super.onBackPressed() }
    }

    fun popularTextViewClicked(view : View) {
        getPopularMovies()
    }

    fun getPopularMovies(){
        moviesViewModel.getPopularMovies(this).observe(this,
            Observer<List<Movie>> { m ->
                if (m != null) {
                    movies.clear()
                    movies.addAll(m)
                }
            })
        textView2.text = movies[2].originalTitle
    }

    override fun onDestroy() {
        moviesViewModel.clear()
        super.onDestroy()
    }
}
