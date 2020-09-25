package com.zywczas.bestonscreen.views

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.*
import com.zywczas.bestonscreen.viewmodels.ApiVM
import com.zywczas.bestonscreen.viewmodels.factories.ApiVMFactory
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.fragment_api_and_db.*
import kotlinx.android.synthetic.main.navigation_drawer.*
import javax.inject.Inject

//todo ogarnac back stack, sprawdzic czy jak jest sie w Api i zminimaluzuje to po czasie wraca do tego czy od nowa wlacza aktivity i DB
//todo jak sie da Api i kliknie wstecz to monimalizuje aplikacje ale jak sie znowu wlaczy to od nowa wlacza Db, chyba trzeba dodawac do backstack jak sie przechodzi z Db do Api

class ApiFragment @Inject constructor(
    private val viewModelFactory : ApiVMFactory,
    private val picasso : Picasso
) : Fragment() {

    private val viewModel : ApiVM by viewModels { viewModelFactory }
    private lateinit var adapter: MovieAdapter
//    private var wasConfigurationChanged : Boolean? = null
//    private var areFirstMoviesInitialized = false
    private var displayedCategory: Category? = null


    //todo on back pressed
    private val dispatcher by lazy {requireActivity().onBackPressedDispatcher}
    private lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        wasConfigurationChanged = savedInstanceState?.getBoolean(CONFIGURATION_CHANGE)
        callback = dispatcher.addCallback(this) {
            closeDrawerOrGoBack()
        }
    }
//todo jak wchodze w details a pozniej cofam to resetuje sie Api na kategorie ktora byla zainicjowana z bundle, ale jak sie obroci ekran to juz nie
    private fun closeDrawerOrGoBack() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            callback.isEnabled = false
            dispatcher.onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_api_and_db, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startApiUISetupChain()
        setupDrawer()
        setupDrawerNavButtons()
    }

    private fun startApiUISetupChain() {
        setupRecyclerView { recyclerViewSetupFinished ->
            if (recyclerViewSetupFinished) {
                setupMoviesObserver { observerSetupFinished ->
                    if (observerSetupFinished) {
                        getMoviesOnViewModelInitIfConnected()
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
        adapter = MovieAdapter(requireActivity(), picasso) { movie ->
            goToDetailsFragment(movie)
        }
        moviesRecyclerView.adapter = adapter
    }

    private fun goToDetailsFragment(movie: Movie){
        activity?.run {
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_MOVIE, movie)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DetailsFragment::class.java, bundle)
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

    private fun setupMoviesObserver(complete: (Boolean) -> Unit) {
        viewModel.moviesAndCategoryLD.observe(viewLifecycleOwner) { resource ->
            hideProgressBar()
            when (resource.status) {
                Status.SUCCESS -> { updateContent(resource.data!!) }
                Status.ERROR -> {
                    showToast(resource.message!!)
                    resource.data?.let { updateContent(it) }
                }
            }
        }
        complete(true)
    }

    private fun hideProgressBar() {
        progressBar.isVisible = false
    }

    private fun updateContent(data: Pair<List<Movie>, Category>) {
        updateDisplayedMovies(data.first)
        updateToolbarTitle(data.second)
        displayedCategory = data.second
    }

    private fun updateDisplayedMovies(movies: List<Movie>) {
        adapter.submitList(movies.toMutableList())
    }

    private fun updateToolbarTitle(category: Category) {
        toolbar.title = "Movies: $category"
    }

    private fun getMoviesOnViewModelInitIfConnected() {
//    @Suppress("SimplifyBooleanWithConstants")
//    val shouldGetFirstMovies = wasConfigurationChanged != true && areFirstMoviesInitialized != true
//    if (shouldGetFirstMovies) {
//        areFirstMoviesInitialized = true
        if (Variables.isNetworkConnected) {
            showProgressBar()
            val categoryFromBundle = arguments?.getSerializable(EXTRA_CATEGORY) as Category
            viewModel.getFirstMovies(categoryFromBundle)
//            viewModel.getNextMovies(categoryFromBundle)
        } else {
            showToast(CONNECTION_PROBLEM)
        }
    }


    private fun showProgressBar() {
        progressBar.isVisible = true
    }

    private fun setupOnScrollListener() {
        moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val isRecyclerViewBottom = !recyclerView.canScrollVertically(1) &&
                        newState == RecyclerView.SCROLL_STATE_IDLE
                if (isRecyclerViewBottom) {
                    downloadNextPageIfConnected()
                }
            }
        })
    }

    private fun downloadNextPageIfConnected() {
        if (Variables.isNetworkConnected) {
            showProgressBar()
            viewModel.getNextMovies()
        } else {
            showToast(CONNECTION_PROBLEM)
        }
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(
            activity, drawer_layout, toolbar,
            R.string.nav_drawer_open, R.string.nav_drawer_closed
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
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
        closeDrawerOrGoBack()
        if (view.id == R.id.myToWatchListTextView) {
            switchToDBFragment()
        } else {
            val category = view.tag as Category
            categoryClicked(category)
        }
    }

    private fun switchToDBFragment() {
        activity?.run {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DBFragment::class.java, null)
                .commit()
        }
    }

    private fun categoryClicked(category: Category) {
        if (category == displayedCategory) {
            showToast("This is category $category.")
        } else {
            downloadNewCategoryIfConnected(category)
        }
    }

    private fun downloadNewCategoryIfConnected(category: Category) {
        if (Variables.isNetworkConnected) {
            showProgressBar()
            viewModel.getNextMovies(category)
            moviesRecyclerView.scrollToPosition(0)
        } else {
            showToast(CONNECTION_PROBLEM)
        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putBoolean(CONFIGURATION_CHANGE, true)
//    }

}