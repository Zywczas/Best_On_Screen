package com.zywczas.bestonscreen.views

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.*
import com.zywczas.bestonscreen.viewmodels.DBVM
import com.zywczas.bestonscreen.viewmodels.factories.DBVMFactory
import kotlinx.android.synthetic.main.fragment_api_and_db.*
import kotlinx.android.synthetic.main.navigation_drawer.*
import kotlinx.android.synthetic.main.navigation_drawer.view.*
import javax.inject.Inject

class DBFragment @Inject constructor(
    private val viewModelFactory: DBVMFactory,
    private val picasso: Picasso,
    private val networkCheck: NetworkCheck
) : Fragment(), View.OnClickListener {

    private val viewModel : DBVM by viewModels { viewModelFactory }
    private lateinit var adapter : MovieAdapter
    private val navController : NavController
            by lazy{ Navigation.findNavController(requireView()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_api_and_db, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startDbUISetupChain()
        checkInternetConnection()
        setupDrawerOnClickListeners()
    }

    private fun startDbUISetupChain() {
        activity?.run {
            setupRecyclerView { recyclerViewSetupFinished ->
                if (recyclerViewSetupFinished) {
                    setupMoviesObserver()
                }
            }
        }
    }

    private fun setupRecyclerView(complete: (Boolean) -> Unit) {
        setupAdapter()
        setupLayoutManager()
        complete(true)
    }

    private fun setupAdapter() {
        adapter = MovieAdapter(requireContext(), picasso) { movie ->
            goToDetailsFragment(movie)
        }
        moviesRecyclerView.adapter = adapter
    }

    private fun goToDetailsFragment(movie: Movie) {
        val directions = DBFragmentDirections.actionToDetails(movie)
        navController.navigate(directions)
    }

    private fun setupLayoutManager() {
        var spanCount = 2
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4
        }
        val layoutManager = GridLayoutManager(activity, spanCount)
        moviesRecyclerView.layoutManager = layoutManager
        moviesRecyclerView.setHasFixedSize(true)
    }

    private fun setupMoviesObserver() {
        viewModel.moviesLD.observe(viewLifecycleOwner) { movies ->
            updateDisplayedMovies(movies)
            if (movies.isEmpty()) {
                showMessageAboutEmptyDB()
            }
        }
    }

    private fun updateDisplayedMovies(movies: List<Movie>) {
        adapter.submitList(movies.toMutableList())
    }

    private fun showMessageAboutEmptyDB() {
        emptyListTextView.isVisible = true
    }

    private fun checkInternetConnection() {
        if (!networkCheck.isNetworkConnected) {
            showToast(CONNECTION_PROBLEM)
        }
    }

    private fun setupDrawerOnClickListeners() {
        setupTags { isFinished ->
            if (isFinished) {
                setupOnClickListeners()
            }
        }
    }

    private fun setupTags(complete: (Boolean) -> Unit) {
        upcomingTextView?.let { it.tag = Category.UPCOMING }
        topRatedTextView?.let { it.tag = Category.TOP_RATED }
        popularTextView?.let { it.tag = Category.POPULAR }
        complete(true)
    }


    private fun setupOnClickListeners() {
//        myToWatchListTextView.setOnClickListener(this)
//        popularTextView?.let { it.setOnClickListener(apiCategoryListener) }
//        upcomingTextView?.let { it.setOnClickListener(apiCategoryListener) }
//        topRatedTextView?.let { it.setOnClickListener(apiCategoryListener) }
    }

    private val apiCategoryListener = View.OnClickListener { view ->
        closeDrawer()
        if (networkCheck.isNetworkConnected) {
            val category = view.tag as Category
            switchToApiFragment(category)
        } else {
            showToast(CONNECTION_PROBLEM)
        }
    }

    //todo dac close drawer or minimize app bo teraz szuflada sie nie zamyka tylko aplikacja znika jak sie da onBackPressed
    private fun closeDrawer() {
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//        }
    }

    private fun switchToApiFragment(category: Category) {
        val destination = DBFragmentDirections.actionToApi(category)
        navController.navigate(destination)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if (v.id == myToWatchListTextView.id) {
                showToast("dziala!!")
            }

        }
    }

    //todo dodac onBack pressed bo nie zamyka szuflady tylko minimalizuje cala apke


}