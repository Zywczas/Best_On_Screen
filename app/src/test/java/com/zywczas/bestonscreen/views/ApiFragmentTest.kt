package com.zywczas.bestonscreen.views

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
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
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.android.synthetic.main.fragment_api.*
import org.hamcrest.core.IsNot.*
import org.junit.After
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

    @RelaxedMockK
    private lateinit var picasso : Picasso
    @MockK(relaxUnitFun = true)
    private lateinit var viewModel : ApiVM
    @MockK
    private lateinit var viewModelFactory : ViewModelsProviderFactory
    @MockK
    private lateinit var fragmentsFactory : MoviesFragmentsFactory
    private lateinit var moviesAndCategoryLD : MutableLiveData<Resource<Pair<List<Movie>, Category>>>
    private val recyclerView = onView(withId(R.id.recyclerViewApi))

    @Before
    fun init(){
        MockKAnnotations.init(this)
        moviesAndCategoryLD = MutableLiveData<Resource<Pair<List<Movie>, Category>>>()
        every { viewModelFactory.create(ApiVM::class.java) } returns viewModel
        every { fragmentsFactory.instantiate(any(), any()) } returns ApiFragment(viewModelFactory, picasso)
        every { viewModel.moviesAndCategoryLD } returns moviesAndCategoryLD
    }

    @After
    fun finish(){
        unmockkAll()
    }

    @Test
    fun isFragmentInView() {
        moviesAndCategoryLD.value = Resource.success(Pair(TestUtil.moviesListOf2, Category.TOP_RATED))

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)

        recyclerView.check(matches(isDisplayed()))
        onView(withId(R.id.progressBarApi)).check(matches(not(isDisplayed())))
        onView(withId(R.id.moviesCategoriesTabs)).check(matches(isDisplayed()))
    }

    @Test
    fun loadingMovies_isProgressBarDisplayed() {
        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)

        onView(withId(R.id.progressBarApi)).check(matches(isDisplayed()))
    }

    @Test
    fun isDataDisplayedOnViewModelInit() {
        moviesAndCategoryLD.value = Resource.success(Pair(TestUtil.moviesListOf2, Category.POPULAR))
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
        assertEquals(1, actualSelectedTabIndex)
        assertEquals("Top Rated", actual1stTabName)
        assertEquals("Popular", actual2ndTabName)
        assertEquals("Upcoming", actual3rdtTabName)
        verify {
            viewModel.getFirstMovies(Category.TOP_RATED)
            viewModel.moviesAndCategoryLD
        }
        confirmVerified(viewModel)
    }

    @Test
    fun changingCategory_isProgressBarDisplayed() {
        moviesAndCategoryLD.value = Resource.success(Pair(TestUtil.moviesListOf2, Category.UPCOMING))

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        onView(withText("Popular")).perform(click())

        onView(withId(R.id.progressBarApi)).check(matches(isDisplayed()))
    }

    @Test
    fun activityDestroyed_isInstanceStateSavedAndRestored() {
        moviesAndCategoryLD.value = Resource.success(Pair(TestUtil.moviesListOf10, Category.UPCOMING))
        var actualSelectedTabIndex : Int? = null
        var actualItemsDisplayed : Int? = null

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        scenario.recreate()
        scenario.onFragment {
            actualSelectedTabIndex = it.moviesCategoriesTabs.selectedTabPosition
            actualItemsDisplayed = it.recyclerViewApi.adapter?.itemCount
        }

        onView(withId(R.id.progressBarApi)).check(matches(not(isDisplayed())))
        assertEquals(2, actualSelectedTabIndex)
        assertEquals(10, actualItemsDisplayed)
    }

}


//todo czy po recreate wszystko tak samo poustawiane filmy o zakladki
//todo czy jak error to wszystko ok
//todo czy klikanie na zakladki dziala
//todo czy progress bar sie pokazuje, przy ladowaniu kolejnej strony
//todo czy progress bar znika
//todo czy toast sie pokazuje jak error
//todo czy nawigacja dziala
//todo czy recycler view sciaga nowe filmy na przewijanie ekranu
//todo czy jak success a pozniej error to dalej sa filmy i po obrocie tez