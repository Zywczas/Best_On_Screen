package com.zywczas.bestonscreen.views

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.*
import com.zywczas.bestonscreen.viewmodels.DBVM
import com.zywczas.bestonscreen.viewmodels.factories.DBVMFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.fragment_api_and_db.*
import kotlinx.android.synthetic.main.navigation_drawer.*
import javax.inject.Inject

class DBFragment @Inject constructor(
    private val viewModelFactory: DBVMFactory,
    private val picasso: Picasso,
    private val networkCheck: NetworkCheck
) : Fragment() {

    private val viewModel: DBVM by viewModels { viewModelFactory }
    private lateinit var adapter: MovieAdapter


    //todo on back pressed
    private val dispatcher by lazy {requireActivity().onBackPressed()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_api_and_db, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startDbUISetupChain()
        setupDrawer()
        checkInternetConnection()
        setupDrawerNavButtons()
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
    //todo sprawdzic o co chodzi z grafami z navigation i moze dodac je tutaj

    private fun goToDetailsFragment(movie: Movie) {
        activity?.run {
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_MOVIE, movie)
            //todo dodac transition animation do wszystkich fragmentow przy przechodzeniu do details
            supportFragmentManager.beginTransaction()
                .replace(R.id.navHostContainerView, DetailsFragment::class.java, bundle)
                .addToBackStack("DetailsFragment")
                .commit()
        }
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
        val toggle = ActionBarDrawerToggle(
            activity, drawer_layout, moviesToolbar,
            R.string.nav_drawer_open, R.string.nav_drawer_closed
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun checkInternetConnection() {
        if (!networkCheck.isNetworkConnected) {
            showToast(CONNECTION_PROBLEM)
        }
    }

    private fun setupDrawerNavButtons() {
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
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    private fun categoryClicked(category: Category) {
        if (networkCheck.isNetworkConnected) {
            switchToApiFragment(category)
        } else {
            showToast(CONNECTION_PROBLEM)
        }
    }

    private fun switchToApiFragment(category: Category) {
        activity?.run {
            val bundle = Bundle()
            bundle.putSerializable(EXTRA_CATEGORY, category)
            supportFragmentManager.beginTransaction()
                .replace(R.id.navHostContainerView, ApiFragment::class.java, bundle)
                .commit()
        }
    }

    //todo dodac onBack pressed bo nie zamyka szuflady tylko minimalizuje cala apke
    //todo pousuwac importy

//todo przejrzec kazda funkcje jeszcze raz i sprawdzi czjest czysto i poukladane

}