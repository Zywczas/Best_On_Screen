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
        onView(withText("Top Rated")).check(matches(isDisplayed()))
        onView(withText("Popular")).check(matches(isDisplayed()))
        onView(withText("Upcoming")).check(matches(isDisplayed()))
    }

    @Test
    fun isDataDisplayedOnViewModelInit() {
        var actualSelectedTabIndex : Int? = null
        var actualItemsCountInRecyclerView : Int? = null

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        scenario.onFragment {
            actualSelectedTabIndex = it.moviesCategoriesTabs.selectedTabPosition
            actualItemsCountInRecyclerView = it.recyclerViewApi.adapter?.itemCount
        }

        recyclerView.perform(scrollToPosition<ViewHolder>(1))
            .check(matches(hasDescendant(withText("Unknown Origins"))))
            .check(matches(hasDescendant(withText("6.2"))))
            .check(matches(hasDescendant(withId(R.id.posterImageViewListItem))))
        assertEquals(0, actualSelectedTabIndex)
        assertEquals(2, actualItemsCountInRecyclerView)
    }

    @Test
    fun loadingMovies_isProgressBarDisplayed() {
        val viewModelLocal = mockk<ApiVM>(relaxed = true)
        val picassoLocal = mockk<Picasso>(relaxed = true)
        val viewModelFactoryLocal = mockk<ViewModelsProviderFactory>()
        val fragmentsFactoryLocal = mockk<MoviesFragmentsFactory>()
        every { viewModelFactoryLocal.create(ApiVM::class.java) } returns viewModelLocal
        every { fragmentsFactoryLocal.instantiate(any(), any()) } returns ApiFragment(viewModelFactoryLocal, picassoLocal)

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactoryLocal)

        onView(withId(R.id.progressBarApi)).check(matches(isDisplayed()))
    }

    @Test
    fun changingCategory_isProgressBarDisplayed() {
        val viewModelLocal = mockk<ApiVM>(relaxed = true)
        val picassoLocal = mockk<Picasso>(relaxed = true)
        val viewModelFactoryLocal = mockk<ViewModelsProviderFactory>()
        val fragmentsFactoryLocal = mockk<MoviesFragmentsFactory>()
        val moviesAndCategoryLDLocal = MutableLiveData<Resource<Pair<List<Movie>, Category>>>()
        moviesAndCategoryLDLocal.value = Resource.success(Pair(TestUtil.moviesList1_2, UPCOMING))
        val categorySlot = slot<Category>()
        every { viewModelFactoryLocal.create(ApiVM::class.java) } returns viewModelLocal
        every { fragmentsFactoryLocal.instantiate(any(), any()) } returns
                ApiFragment(viewModelFactoryLocal, picassoLocal)
        every { viewModelLocal.moviesAndCategoryLD } returns moviesAndCategoryLDLocal
        every { viewModelLocal.getFirstMovies(capture(categorySlot)) } answers
                { moviesAndCategoryLDLocal.value = Resource.success(Pair(TestUtil.moviesList1_2, categorySlot.captured)) }

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactoryLocal)
        onView(withText("Popular")).perform(click())

        onView(withId(R.id.progressBarApi)).check(matches(isDisplayed()))
    }

    @Test
    fun activityDestroyed_isInstanceStateSavedAndRestored() {
        var actualSelectedTabIndex : Int? = null
        var actualItemsCountInRecyclerView : Int? = null
        every { repo.getApiMovies(any(), any()) } returns
                Flowable.just(Resource.success(TestUtil.moviesList1_2)) andThen
                Flowable.just(Resource.success(TestUtil.moviesList1_10))

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        onView(withText("Upcoming")).perform(click())
        recyclerView.perform(scrollToPosition<ViewHolder>(8))
        scenario.recreate()
        scenario.onFragment {
            actualSelectedTabIndex = it.moviesCategoriesTabs.selectedTabPosition
            actualItemsCountInRecyclerView = it.recyclerViewApi.adapter?.itemCount
        }

        onView(withId(R.id.progressBarApi)).check(matches(not(isDisplayed())))
        recyclerView.check(matches(isDisplayed()))
        recyclerView.check(matches(hasDescendant(withText("The Empire Strikes Back"))))
        assertEquals(2, actualSelectedTabIndex)
        assertEquals(10, actualItemsCountInRecyclerView)
        verify(exactly = 2) { repo.getApiMovies(any(), any())}
    }

    @Test
    fun isClickedTabLoadingNewCategory(){
        val actualCategories = mutableListOf<Category>()
        val actualPages = mutableListOf<Int>()
        var actualSelectedTabIndex : Int? = null
        var actualItemsCountInRecyclerView : Int? = null
        every { repo.getApiMovies(capture(actualCategories), capture(actualPages)) } returns
                Flowable.just(Resource.success(TestUtil.moviesList1_5)) andThen
                Flowable.just(Resource.success(TestUtil.moviesList6_8))

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        onView(withText("Popular")).perform(click())
        scenario.onFragment {
            actualSelectedTabIndex = it.moviesCategoriesTabs.selectedTabPosition
            actualItemsCountInRecyclerView = it.recyclerViewApi.adapter?.itemCount
        }

        onView(withId(R.id.progressBarApi)).check(matches(not(isDisplayed())))
        assertEquals(1, actualSelectedTabIndex)
        assertEquals(3, actualItemsCountInRecyclerView)
        verify(exactly = 2) { repo.getApiMovies(any(), any())}
        assertEquals(mutableListOf(TOP_RATED, POPULAR), actualCategories)
        assertEquals(mutableListOf(1, 1), actualPages)
    }

    @Test
    fun clickingTheSameTab_isNothingChanged(){
        val actualCategories = mutableListOf<Category>()
        val actualPages = mutableListOf<Int>()
        var actualSelectedTabIndex : Int? = null
        var actualItemsCountInRecyclerView : Int? = null
        every { repo.getApiMovies(capture(actualCategories), capture(actualPages)) } returns
                Flowable.just(Resource.success(TestUtil.moviesList1_5)) andThen
                Flowable.just(Resource.success(TestUtil.moviesList6_8))

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        onView(withText("Top Rated")).perform(click())
        scenario.onFragment {
            actualSelectedTabIndex = it.moviesCategoriesTabs.selectedTabPosition
            actualItemsCountInRecyclerView = it.recyclerViewApi.adapter?.itemCount
        }

        onView(withId(R.id.progressBarApi)).check(matches(not(isDisplayed())))
        assertEquals(0, actualSelectedTabIndex)
        assertEquals(5, actualItemsCountInRecyclerView)
        verify(exactly = 1) { repo.getApiMovies(any(), any())}
        assertEquals(mutableListOf(TOP_RATED), actualCategories)
        assertEquals(mutableListOf(1), actualPages)
    }




}



//todo czy jak error to wszystko ok
//todo czy progress bar sie pokazuje, przy ladowaniu kolejnej strony
//todo czy toast sie pokazuje jak error
//todo czy nawigacja dziala
//todo czy recycler view sciaga nowe filmy na przewijanie ekranu
//todo czy jak success a pozniej error to dalej sa filmy i po obrocie tez
