package com.zywczas.bestonscreen.views

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Category.*
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.CONFIGURATION_CHANGE
import com.zywczas.bestonscreen.utilities.Status
import com.zywczas.bestonscreen.utilities.lazyAndroid
import com.zywczas.bestonscreen.utilities.showToast
import com.zywczas.bestonscreen.viewmodels.ApiVM
import com.zywczas.bestonscreen.viewmodels.ViewModelsProviderFactory
import kotlinx.android.synthetic.main.fragment_api.*
import javax.inject.Inject

class ApiFragment @Inject constructor(
    private val viewModelFactory: ViewModelsProviderFactory,
    private val picasso: Picasso
) : Fragment() {

    private val viewModel: ApiVM by viewModels { viewModelFactory }
    private lateinit var adapter: MovieAdapter
    private val navController by lazyAndroid { Navigation.findNavController(requireView()) }
    private var displayedCategory: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.getSerializable(CONFIGURATION_CHANGE)?.let { displayedCategory = it as Category }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_api, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startApiUISetupChain()
        setupCategoryButtons()
    }

    private fun startApiUISetupChain() {
        setupRecyclerView { recyclerViewSetupFinished ->
            if (recyclerViewSetupFinished) {
                setupMoviesObserver { observerSetupFinished ->
                    if (observerSetupFinished) {
                        viewModel.getFirstMovies(TOP_RATED)
                        setupOnScrollListener()
                    }
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
        recyclerViewApi.adapter = adapter
    }

    private fun goToDetailsFragment(movie: Movie) {
        val destination = ApiFragmentDirections.actionToDetails(movie)
        navController.navigate(destination)
    }

    private fun setupLayoutManager() {
        var spanCount = 2
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4
        }
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        recyclerViewApi.layoutManager = layoutManager
        recyclerViewApi.setHasFixedSize(true)
    }

    private fun setupMoviesObserver(complete: (Boolean) -> Unit) {
        viewModel.moviesAndCategoryLD.observe(viewLifecycleOwner) { resource ->
            showProgressBar(false)
            when (resource.status) {
                Status.SUCCESS -> {
                    updateContent(resource.data!!)
                }
                Status.ERROR -> {
                    showToast(resource.message!!)
                    resource.data?.let { updateContent(it) }
                }
            }
        }
        complete(true)
    }

    private fun showProgressBar(visible: Boolean) {
        progressBarApi.isVisible = visible
    }

    private fun updateContent(data: Pair<List<Movie>, Category>) {
        updateDisplayedMovies(data.first)
        displayedCategory = data.second
        updateSelectedTab(displayedCategory!!)
    }

    private fun updateDisplayedMovies(movies: List<Movie>) {
        adapter.submitList(movies.toMutableList())
    }

    private fun updateSelectedTab(category: Category){
        val index = when (category) {
            TOP_RATED -> 0
            POPULAR -> 1
            UPCOMING -> 2
        }
        moviesCategoriesTabs.getTabAt(index)?.select()
    }

    private fun setupOnScrollListener() {
        recyclerViewApi.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val isRecyclerViewBottom = !recyclerView.canScrollVertically(1) &&
                        newState == RecyclerView.SCROLL_STATE_IDLE
                if (isRecyclerViewBottom) {
                    downloadNextPage()
                }
            }
        })
    }

    private fun downloadNextPage() {
        showProgressBar(true)
        viewModel.getNextMoviesIfConnected()
    }

    private fun setupCategoryButtons() {
        setupTags { isFinished ->
            if (isFinished) {
                setupOnClickListeners()
            }
        }
    }

    private fun setupTags(complete: (Boolean) -> Unit) {
        moviesCategoriesTabs.getTabAt(0)?.tag = TOP_RATED
        moviesCategoriesTabs.getTabAt(1)?.tag = POPULAR
        moviesCategoriesTabs.getTabAt(2)?.tag = UPCOMING
        complete(true)
    }

    private fun setupOnClickListeners() {
        moviesCategoriesTabs.addOnTabSelectedListener(categoryClickListener)
    }

    private val categoryClickListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let {
                val category = it.tag as Category
                if (category != displayedCategory){
                    downloadNewCategory(category)
                }

            }
        }
        override fun onTabUnselected(tab: TabLayout.Tab?) {}
        override fun onTabReselected(tab: TabLayout.Tab?) {}
    }

    private fun downloadNewCategory(category: Category) {
        showProgressBar(true)
        adapter.submitList(emptyList())
        recyclerViewApi.scrollToPosition(0)
        viewModel.getNextMoviesIfConnected(category)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(CONFIGURATION_CHANGE, displayedCategory)
    }



}