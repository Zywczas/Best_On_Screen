package com.zywczas.bestonscreen.views

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.utilities.*
import com.zywczas.bestonscreen.viewmodels.DBVM
import com.zywczas.bestonscreen.viewmodels.factories.DBVMFactory
import com.zywczas.bestonscreen.viewmodels.factories.GenericSavedStateViewModelFactory
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.nav_movies.*
import javax.inject.Inject
import kotlin.properties.Delegates

class DBActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: DBVMFactory
    private val viewModel: DBVM by viewModels { GenericSavedStateViewModelFactory(factory,this) }
    private lateinit var adapter: MovieAdapter
    @Inject lateinit var picasso: Picasso
    private var orientation by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        progressBarMovies.isVisible = false
        moviesToolbar.title = "Movies: My List"
        orientation = resources.configuration.orientation

        App.moviesComponent.inject(this)

        val toggle = ActionBarDrawerToggle(this,drawer_layout_movies,moviesToolbar,
            R.string.nav_drawer_open,R.string.nav_drawer_closed)
        drawer_layout_movies.addDrawerListener(toggle)
        toggle.syncState()

        setupAdapter()
        setupTags()
        setupObserver()

    }

    private fun setupAdapter() {
        adapter = MovieAdapter(this, picasso) { movie ->
            val movieDetailsActivity = Intent(this, DetailsActivity::class.java)
            movieDetailsActivity.putExtra(EXTRA_MOVIE, movie)
            startActivity(movieDetailsActivity)
        }
        moviesRecyclerView.setHasFixedSize(true)
        moviesRecyclerView.adapter = adapter
        var spanCount = 2
        if (orientation ==  Configuration.ORIENTATION_LANDSCAPE){
            spanCount = 4
        }
        val layoutManager = GridLayoutManager(this, spanCount)
        moviesRecyclerView.layoutManager = layoutManager
    }

    private fun setupTags() {
        upcomingTextView.tag = UPCOMING
        topRatedTextView.tag = TOP_RATED
        popularTextView.tag = POPULAR
        toWatchListTextView.tag = TO_WATCH
    }

    private fun setupObserver() {
        viewModel.getDbMovies().observe(this, Observer { movies ->
                adapter.submitList(movies.toMutableList())
            if (movies.isEmpty()) {
                emptyListTextView.isVisible = true
            }
        })
    }



    fun toWatchClicked (view: View) {
        closeDrawerOrMinimizeApp()
        showToast("This is your list.")
    }

    fun categoryClicked(view: View) {
        closeDrawerOrMinimizeApp()
        val category = view.tag as String

        val moviesIntent = Intent(this, ApiActivity::class.java)
        moviesIntent.putExtra(EXTRA_CATEGORY, category)
        startActivity(moviesIntent)
        finish()
    }

    override fun onDestroy() {
        viewModel.clearDisposables()
        super.onDestroy()
    }

    override fun onBackPressed() {
        closeDrawerOrMinimizeApp()
    }
//todo powturzona funkcja, sprawdzic czy nie mozna 1 takiej dac gdzies
    private fun closeDrawerOrMinimizeApp() {
        if (drawer_layout_movies.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_movies.closeDrawer(GravityCompat.START)
        } else {
            this.moveTaskToBack(true)
        }
    }

}
