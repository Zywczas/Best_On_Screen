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
import com.zywczas.bestonscreen.adapter.MovieAdapter.*
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Category.*
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.viewmodels.ApiVM
import com.zywczas.bestonscreen.viewmodels.ViewModelsProviderFactory
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.reactivex.rxjava3.core.Flowable
import kotlinx.android.synthetic.main.fragment_api.*
import org.hamcrest.core.IsNot.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ApiFragmentTest {

    @MockK
    private lateinit var repo : ApiRepository
    @RelaxedMockK
    private lateinit var picasso : Picasso
    @MockK
    private lateinit var networkCheck: NetworkCheck
    @MockK
    private lateinit var viewModelFactory : ViewModelsProviderFactory
    @MockK
    private lateinit var fragmentsFactory : MoviesFragmentsFactory
    private val recyclerView = onView(withId(R.id.recyclerViewApi))

    @Before
    fun init(){
        MockKAnnotations.init(this)
        every { repo.getApiMovies(any(), any()) } returns Flowable.just(Resource.success(TestUtil.moviesList1_2))
        every { networkCheck.isConnected } returns true
        every { viewModelFactory.create(ApiVM::class.java) } returns ApiVM(repo, networkCheck)
        every { fragmentsFactory.instantiate(any(), any()) } returns ApiFragment(viewModelFactory, picasso)
    }

    @After
    fun finish(){
        unmockkAll()
    }

    @Test
    fun isFragmentInView() {
        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)

        recyclerView.check(matches(isDisplayed()))
        onView(withId(R.id.progressBarApi)).check(matches(not(isDisplayed())))
        onView(withId(R.id.moviesCategoriesTabs)).check(matches(isDisplayed()))
    }

    @Test
    fun isDataDisplayedOnViewModelInit() {
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

        recyclerView.perform(scrollToPosition<ViewHolder>(1))
            .check(matches(hasDescendant(withText("Unknown Origins"))))
            .check(matches(hasDescendant(withText("6.2"))))
            .check(matches(hasDescendant(withId(R.id.posterImageViewListItem))))
        assertEquals(0, actualSelectedTabIndex)
        assertEquals("Top Rated", actual1stTabName)
        assertEquals("Popular", actual2ndTabName)
        assertEquals("Upcoming", actual3rdtTabName)
    }

    @Test
    fun loadingMovies_isProgressBarDisplayed() {
        val viewModel = mockk<ApiVM>(relaxed = true)
        val picasso = mockk<Picasso>(relaxed = true)
        val viewModelFactory = mockk<ViewModelsProviderFactory>()
        val fragmentsFactory = mockk<MoviesFragmentsFactory>()
        every { viewModelFactory.create(ApiVM::class.java) } returns viewModel
        every { fragmentsFactory.instantiate(any(), any()) } returns ApiFragment(viewModelFactory, picasso)

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)

        onView(withId(R.id.progressBarApi)).check(matches(isDisplayed()))
    }

    @Test
    fun changingCategory_isProgressBarDisplayed() {
        val viewModel = mockk<ApiVM>(relaxed = true)
        val picasso = mockk<Picasso>(relaxed = true)
        val viewModelFactory = mockk<ViewModelsProviderFactory>()
        val fragmentsFactory = mockk<MoviesFragmentsFactory>()
        val moviesAndCategoryLD = MutableLiveData<Resource<Pair<List<Movie>, Category>>>()
        moviesAndCategoryLD.value = Resource.success(Pair(TestUtil.moviesList1_2, UPCOMING))
        val categorySlot = slot<Category>()
        every { viewModelFactory.create(ApiVM::class.java) } returns viewModel
        every { fragmentsFactory.instantiate(any(), any()) } returns ApiFragment(viewModelFactory, picasso)
        every { viewModel.moviesAndCategoryLD } returns moviesAndCategoryLD
        every { viewModel.getFirstMovies(capture(categorySlot)) } answers {
            moviesAndCategoryLD.value = Resource.success(Pair(TestUtil.moviesList1_2, categorySlot.captured)) }

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        onView(withText("Popular")).perform(click())

        onView(withId(R.id.progressBarApi)).check(matches(isDisplayed()))
    }

    @Test
    fun activityDestroyed_isInstanceStateSavedAndRestored() {
        var actualSelectedTabIndex : Int? = null
        var actualItemsInRecyclerView : Int? = null
        every { repo.getApiMovies(any(), any()) } returns
                Flowable.just(Resource.success(TestUtil.moviesList1_2)) andThen
                Flowable.just(Resource.success(TestUtil.moviesList1_10))

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        onView(withText("Upcoming")).perform(click())
        recyclerView.perform(scrollToPosition<ViewHolder>(8))
        scenario.recreate()
        scenario.onFragment {
            actualSelectedTabIndex = it.moviesCategoriesTabs.selectedTabPosition
            actualItemsInRecyclerView = it.recyclerViewApi.adapter?.itemCount
        }

        onView(withId(R.id.progressBarApi)).check(matches(not(isDisplayed())))
        recyclerView.check(matches(isDisplayed()))
        recyclerView.check(matches(hasDescendant(withText("The Empire Strikes Back"))))
        assertEquals(2, actualSelectedTabIndex)
        assertEquals(10, actualItemsInRecyclerView)
        verify(exactly = 2) { repo.getApiMovies(any(), any())}
    }

    @Test
    fun isClickedTabLoadingNewCategory(){
        val actualCategories = mutableListOf<Category>()
        val actualPages = mutableListOf<Int>()
        var actualSelectedTabIndex : Int? = null
        var actualItemsInRecyclerView : Int? = null
        every { repo.getApiMovies(capture(actualCategories), capture(actualPages)) } returns
                Flowable.just(Resource.success(TestUtil.moviesList1_5)) andThen
                Flowable.just(Resource.success(TestUtil.moviesList6_8))

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        onView(withText("Popular")).perform(click())
        scenario.onFragment {
            actualSelectedTabIndex = it.moviesCategoriesTabs.selectedTabPosition
            actualItemsInRecyclerView = it.recyclerViewApi.adapter?.itemCount
        }

        onView(withId(R.id.progressBarApi)).check(matches(not(isDisplayed())))
        assertEquals(1, actualSelectedTabIndex)
        assertEquals(3, actualItemsInRecyclerView)
        verify(exactly = 2) { repo.getApiMovies(any(), any())}
        assertEquals(mutableListOf(TOP_RATED, POPULAR), actualCategories)
        assertEquals(mutableListOf(1, 1), actualPages)
    }


}



//todo czy jak error to wszystko ok
//todo czy progress bar sie pokazuje, przy ladowaniu kolejnej strony
//todo czy toast sie pokazuje jak error
//todo czy nawigacja dziala
//todo czy recycler view sciaga nowe filmy na przewijanie ekranu
//todo czy jak success a pozniej error to dalej sa filmy i po obrocie tez
//todo klikanie tej same kategori - nic sie nie dzieje