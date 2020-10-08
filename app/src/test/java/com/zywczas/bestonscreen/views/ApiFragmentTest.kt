package com.zywczas.bestonscreen.views

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.viewmodels.ApiVM
import com.zywczas.bestonscreen.viewmodels.ViewModelsProviderFactory
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.android.synthetic.main.fragment_api.*
import org.hamcrest.core.IsNot.*
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode
import kotlin.math.exp

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ApiFragmentTest {

    private val picasso = mockk<Picasso>(relaxed = true)
    private val viewModel = mockk<ApiVM>()
    private val viewModelFactory = mockk<ViewModelsProviderFactory>()
    private val fragmentsFactory = mockk<MoviesFragmentsFactory>()
    private val moviesAndCategoryLD = MutableLiveData<Resource<Pair<List<Movie>, Category>>>()
    private val recyclerView = onView(withId(R.id.recyclerViewApi))

    @Before
    fun init(){
        every { viewModelFactory.create(ApiVM::class.java) } returns viewModel
        every { fragmentsFactory.instantiate(any(), any()) } returns ApiFragment(viewModelFactory, picasso)
        every { viewModel.moviesAndCategoryLD } returns moviesAndCategoryLD
        every { viewModel.getFirstMovies(any()) } answers {
            moviesAndCategoryLD.value = Resource.success(Pair(TestUtil.moviesListOf2, Category.TOP_RATED)) }
        every { viewModel.getNextMoviesIfConnected(any()) } just Runs
    }

    @Test
    fun isFragmentInView() {
        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)

        recyclerView.check(matches(isDisplayed()))
        onView(withId(R.id.progressBarApi)).check(matches(not(isDisplayed())))
        onView(withId(R.id.moviesCategoriesTabs)).check(matches(isDisplayed()))
    }

    @Test
    fun loadingMovies_isProgressBarDisplayed() {
        every { viewModel.getFirstMovies(any()) } just Runs

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)

        onView(withId(R.id.progressBarApi)).check(matches(isDisplayed()))
    }

    @Test
    fun isDataDisplayed() {
        var actualSelectedTabIndex : Int? = null
        var actual1stTabName : String? = null
        var actual2ndTabName : String? = null
        var actual3rdtTabName : String? = null

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        scenario.onFragment {
            actualSelectedTabIndex = it.moviesCategoriesTabs.selectedTabPosition
            actual1stTabName = it.moviesCategoriesTabs.getTabAt(0)?.text.toString()
            actual2ndTabName = it.moviesCategoriesTabs.getTabAt(1)?.text.toString()
            actual3rdtTabName = it.moviesCategoriesTabs.getTabAt(2)?.text.toString()
        }

        recyclerView.perform(scrollToPosition<MovieAdapter.ViewHolder>(1))
            .check(matches(hasDescendant(withText("Unknown Origins"))))
            .check(matches(hasDescendant(withText("6.2"))))
            .check(matches(hasDescendant(withId(R.id.posterImageViewListItem))))
        assertEquals(0, actualSelectedTabIndex)
        assertEquals("Top Rated", actual1stTabName)
        assertEquals("Popular", actual2ndTabName)
        assertEquals("Upcoming", actual3rdtTabName)
    }

}

//todo ac test sprawdzajacy czy toast sie nie powtarza po recreate
//todo czy wczytuja sie dane
//todo czy sa filmy odpowiednie od razu pokazane
//todo czy po recreate wszystko tak samo poustawiane filmy o zakladki
//todo czy jak sukces to wszytko dobrze wczytane
//todo czy jak error to wszystko ok
//todo czy klikanie na zakladki dziala
//todo czy progress bar sie pokazuje
//todo czy progress bar znika
//todo czy toast sie pokazuje jak error
//todo czy nawigacja dziala
//todo czy recycler view sciaga nowe filmy na przewijanie ekranu