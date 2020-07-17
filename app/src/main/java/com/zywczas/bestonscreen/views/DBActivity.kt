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
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.*
import com.zywczas.bestonscreen.viewmodels.DBVM
import com.zywczas.bestonscreen.viewmodels.factories.DBVMFactory
import com.zywczas.bestonscreen.viewmodels.factories.GenericSavedStateViewModelFactory
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.navigation.*
import javax.inject.Inject

class DBActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: DBVMFactory
    private val viewModel: DBVM by viewModels { GenericSavedStateViewModelFactory(factory,this) }
    private lateinit var adapter: MovieAdapter
    @Inject lateinit var picasso: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        initializeDagger()
        setupDrawer()
        setupAdapter()
        setupTags()
        displayMoviesOrMessage()
    }

    private fun initializeDagger() {
        App.moviesComponent.inject(this)
    }

    private fun setupDrawer(){
        val toggle = ActionBarDrawerToggle(this,drawer_layout_movies,moviesToolbar,
            R.string.nav_drawer_open,R.string.nav_drawer_closed)
        drawer_layout_movies.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setupAdapter() {
        adapter = MovieAdapter(this, picasso) { movie ->
            val movieDetailsActivity = Intent(this, DetailsActivity::class.java)
            movieDetailsActivity.putExtra(EXTRA_MOVIE, movie)
            startActivity(movieDetailsActivity)
        }
        moviesRecyclerView.adapter = adapter
        var spanCount = 2
        val orientation = resources.configuration.orientation
        if (orientation ==  Configuration.ORIENTATION_LANDSCAPE){
            spanCount = 4
        }
        val layoutManager = GridLayoutManager(this, spanCount)
        moviesRecyclerView.layoutManager = layoutManager
        moviesRecyclerView.setHasFixedSize(true)
    }
//todo sprawdzic czy sie nie da zmienic na enum
    private fun setupTags() {
        upcomingTextView.tag = UPCOMING
        topRatedTextView.tag = TOP_RATED
        popularTextView.tag = POPULAR
        MyToWatchListTextView.tag = TO_WATCH
    }

    private fun displayMoviesOrMessage() {
        viewModel.getDbMovies().observe(this, Observer { movies ->
            updateDisplayedMovies(movies)
            if (movies.isEmpty()){
                showMessageAboutEmptyDB()
            }
        })
    }

    private fun updateDisplayedMovies(movies: List<Movie>){
        adapter.submitList(movies.toMutableList())
    }

    private fun showMessageAboutEmptyDB(){
        emptyListTextView.isVisible = true
    }

    fun myToWatchListClicked (view: View) {
        closeDrawerOrMinimizeApp()
        showToast("This is your list.")
    }

    fun categoryClicked(view: View) {
        closeDrawerOrMinimizeApp()
        val category = view.tag as String
        switchToApiActivity(category)
    }

    private fun switchToApiActivity(category: String){
        val moviesIntent = Intent(this, ApiActivity::class.java)
        moviesIntent.putExtra(EXTRA_CATEGORY, category)
        startActivity(moviesIntent)
        finish()
    }

    override fun onBackPressed() {
        closeDrawerOrMinimizeApp()
    }

    private fun closeDrawerOrMinimizeApp() {
        if (drawer_layout_movies.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_movies.closeDrawer(GravityCompat.START)
        } else {
            this.moveTaskToBack(true)
        }
    }

    override fun onDestroy() {
        viewModel.clearDisposables()
        super.onDestroy()
    }

}
