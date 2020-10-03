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
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.*
import com.zywczas.bestonscreen.viewmodels.DBVM
import com.zywczas.bestonscreen.viewmodels.factories.DBVMFactory
import kotlinx.android.synthetic.main.fragment_db.*
import javax.inject.Inject

class DBFragment @Inject constructor(
    private val viewModelFactory: DBVMFactory,
    private val picasso: Picasso,
    private val networkCheck: NetworkCheck
) : Fragment() {

    private val viewModel : DBVM by viewModels { viewModelFactory }
    private lateinit var adapter : MovieAdapter
    private val navController : NavController
            by lazyAndroid{ Navigation.findNavController(requireView()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_db, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startDbUISetupChain()
        checkInternetConnection()
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
        recyclerViewDB.adapter = adapter
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
        recyclerViewDB.layoutManager = layoutManager
        recyclerViewDB.setHasFixedSize(true)
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

}