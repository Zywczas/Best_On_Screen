package com.zywczas.bestonscreen.views

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.*
import com.zywczas.bestonscreen.viewmodels.ApiVM
import com.zywczas.bestonscreen.viewmodels.factories.ApiVMFactory
import kotlinx.android.synthetic.main.fragment_api.*
import javax.inject.Inject

//todo ogarnac back stack, sprawdzic czy jak jest sie w Api i zminimaluzuje to po czasie wraca do tego czy od nowa wlacza aktivity i DB
//todo jak sie da Api i kliknie wstecz to monimalizuje aplikacje ale jak sie znowu wlaczy to od nowa wlacza Db, chyba trzeba dodawac do backstack jak sie przechodzi z Db do Api

class ApiFragment @Inject constructor(
    private val viewModelFactory: ApiVMFactory,
    private val picasso: Picasso
) : Fragment() {

    private val viewModel: ApiVM by viewModels { viewModelFactory }
    private lateinit var adapter: MovieAdapter
    private val navController : NavController
            by lazy{ Navigation.findNavController(requireView()) }
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

    //todo dodac toolbar z zakladkami i zeby observer tez ustawial ktory jest wcisniety, bo  jak nie
    // wybierzemy kategori a scrollujemy na dol to sie zalacza domyslna kategoria czyli popular
    //przy przechodzeniu z top rated do upcoming iz upcoming do popular recycler view nie leci na gore, spowrotem juz leci
    //moze dac wszystko w on view created

    private fun setupLayoutManager() {
        var spanCount = 2
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4
        }
        val layoutManager = GridLayoutManager(activity, spanCount)
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
        val isNewCategoryLoaded = data.second != displayedCategory
        if (isNewCategoryLoaded){
            recyclerViewApi.scrollToPosition(0)
        }
        displayedCategory = data.second
    }

    private fun updateDisplayedMovies(movies: List<Movie>) {
        adapter.submitList(movies.toMutableList())
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
        moviesCategoriesTabs.getTabAt(0)?.tag = Category.TOP_RATED
        moviesCategoriesTabs.getTabAt(1)?.tag = Category.POPULAR
        moviesCategoriesTabs.getTabAt(2)?.tag = Category.UPCOMING
        complete(true)
    }

    private fun setupOnClickListeners() {
        moviesCategoriesTabs.addOnTabSelectedListener(categoryClickListener)
    }
    //todo dac na start ladowanie pierwszej kategori
//todo przy obracaniu ekranu resetuje kliknieta zakladke -> dac to: moviesCategoriesTabs.getTabAt(2)?.select(), sprawdzic czy jak to daje to nie resetuje kategorii
    //todo jak przelaczam na popular to niby resetuje ale u gory jest wiecej lifmow i nie skroluje do nich
    //nie scroluje jak sie przelacza z top rated na upcoming i z upcoming na popular, w druga strone dziala...
    private val categoryClickListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let{
                val category = it.tag as Category
                downloadNewCategory(category)
            }
        }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {
            tab?.let {val category = it.tag as Category
                showToast("This is category $category") }
        }
    }

    private fun downloadNewCategory(category: Category) {
        showProgressBar(true)
        viewModel.getNextMoviesIfConnected(category)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(CONFIGURATION_CHANGE, displayedCategory)
    }

}