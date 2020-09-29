package com.zywczas.bestonscreen.views

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import javax.inject.Inject

class DBFragment @Inject constructor(
    private val viewModelFactory: DBVMFactory,
    private val picasso: Picasso,
    private val networkCheck: NetworkCheck
) : Fragment() {

    private val viewModel : DBVM by viewModels { viewModelFactory }
    private lateinit var adapter : MovieAdapter
    private val navController : NavController
            by lazy{ Navigation.findNavController(requireView()) }

    //todo on back pressed
    private val dispatcher by lazy { requireActivity().onBackPressed() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_api_and_db, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startDbUISetupChain()
//        setupDrawer()
        checkInternetConnection()
//        setupDrawerOnClickListeners()
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

    private fun setupDrawer() {
//        val toggle = ActionBarDrawerToggle(
//            activity, drawer_layout, moviesToolbar,
//            R.string.nav_drawer_open, R.string.nav_drawer_closed
//        )
//        drawer_layout.addDrawerListener(toggle)
//        toggle.syncState()
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
        upcomingTextView.tag = Category.UPCOMING
        topRatedTextView.tag = Category.TOP_RATED
        popularTextView.tag = Category.POPULAR
        complete(true)
    }

    private fun setupOnClickListeners() {
        myToWatchListTextView.setOnClickListener(onClickListener)
        popularTextView.setOnClickListener(onClickListener)
        upcomingTextView.setOnClickListener(onClickListener)
        topRatedTextView.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        closeDrawer()
        if (view.id == R.id.myToWatchListTextView) {
            showToast("This is your list.")
        } else {
            val category = view.tag as Category
            categoryClicked(category)
        }
    }

    //todo dac close drawer or minimize app bo teraz szuflada sie nie zamyka tylko aplikacja znika jak sie da onBackPressed
    private fun closeDrawer() {
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//        }
    }

    private fun categoryClicked(category: Category) {
        if (networkCheck.isNetworkConnected) {
            switchToApiFragment(category)
        } else {
            showToast(CONNECTION_PROBLEM)
        }
    }

    private fun switchToApiFragment(category: Category) {
        val destination = DBFragmentDirections.actionToApi(category)
        navController.navigate(destination)
    }

    //todo dodac onBack pressed bo nie zamyka szuflady tylko minimalizuje cala apke


}