package com.zywczas.bestonscreen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.zywczas.bestonscreen.R
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.content_movies.*

class MoviesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        //setting up drawer layout and toggle button
        val toggleMovies = ActionBarDrawerToggle(this, drawer_layout_movies, moviesToolbar, R.string.nav_drawer_open, R.string.nav_drawer_closed)
        drawer_layout_movies.addDrawerListener(toggleMovies)
        toggleMovies.syncState()

    }

    override fun onBackPressed() {
        if (drawer_layout_movies.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_movies.closeDrawer(GravityCompat.START)
        } else { super.onBackPressed() }
    }
}
