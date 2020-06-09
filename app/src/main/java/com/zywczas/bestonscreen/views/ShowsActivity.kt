package com.zywczas.bestonscreen.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.zywczas.bestonscreen.R
import kotlinx.android.synthetic.main.activity_shows.*
import kotlinx.android.synthetic.main.content_shows.*

class ShowsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows)

        val toggleShows = ActionBarDrawerToggle(this, drawer_layout_shows, showsToolbar, R.string.nav_drawer_open, R.string.nav_drawer_closed)
        drawer_layout_shows.addDrawerListener(toggleShows)
        toggleShows.syncState()

    }

    override fun onBackPressed() {
        if (drawer_layout_shows.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_shows.closeDrawer(GravityCompat.START)
        } else { super.onBackPressed() }
    }
}
