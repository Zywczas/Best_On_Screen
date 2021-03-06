package com.zywczas.bestonscreen.views

import android.os.Looper.getMainLooper
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter.ViewHolder
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category.POPULAR
import com.zywczas.bestonscreen.model.Category.TOP_RATED
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.viewmodels.ApiVM
import com.zywczas.bestonscreen.viewmodels.ViewModelsProviderFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import io.reactivex.rxjava3.core.Flowable
import kotlinx.android.synthetic.main.fragment_api.*
import org.hamcrest.core.IsNot.not
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.LooperMode
import org.robolectric.shadows.ShadowToast

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ApiFragmentTest {


    private val repo = mockk<ApiRepository>()
    private val picasso = mockk<Picasso>(relaxed = true)
    private val networkCheck = mockk<NetworkCheck>()
    private val viewModelFactory = mockk<ViewModelsProviderFactory>()
    private val fragmentsFactory = mockk<MoviesFragmentsFactory>()
    private val recyclerView = onView(withId(R.id.recyclerViewApi))

    @Before
    fun init(){
        every { repo.getApiMovies(any(), any()) } returns Flowable.just(Resource.success(TestUtil.moviesList1_2))
        every { networkCheck.isConnected } returns true
        every { viewModelFactory.create(ApiVM::class.java) } returns ApiVM(repo, networkCheck)
        every { fragmentsFactory.instantiate(any(), any()) } returns ApiFragment(viewModelFactory, picasso)
    }

    @Test
    fun isFragmentInView() {
        @Suppress("UNUSED_VARIABLE")
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
    fun clickNotSelectedTab_isNewCategoryLoaded(){
        var actualSelectedTabIndex : Int? = null
        var actualItemsCountInRecyclerView : Int? = null
        every { repo.getApiMovies(any(), any()) } returns
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
        verifySequence {
            repo.getApiMovies(TOP_RATED, 1)
            repo.getApiMovies(POPULAR, 1)
        }
    }

    @Test
    fun clickTheSameTab_isNothingChanged(){
        var actualSelectedTabIndex : Int? = null
        var actualItemsCountInRecyclerView : Int? = null
        every { repo.getApiMovies(any(), any()) } returns
                Flowable.just(Resource.success(TestUtil.moviesList1_5))

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        onView(withText("Top Rated")).perform(click())
        scenario.onFragment {
            actualSelectedTabIndex = it.moviesCategoriesTabs.selectedTabPosition
            actualItemsCountInRecyclerView = it.recyclerViewApi.adapter?.itemCount
        }

        onView(withId(R.id.progressBarApi)).check(matches(not(isDisplayed())))
        assertEquals(0, actualSelectedTabIndex)
        assertEquals(5, actualItemsCountInRecyclerView)
        verify(exactly = 1) { repo.getApiMovies(TOP_RATED, 1)}
    }

    @Test
    fun getError_isToastDisplayed(){
        every { repo.getApiMovies(any(), any()) } returns
                Flowable.just(Resource.error("some error", TestUtil.moviesList1_2))

        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)

        shadowOf(getMainLooper()).idle()
        assertEquals("some error", ShadowToast.getTextOfLatestToast())
    }

    @Test
    fun scrollToBottom_isNextPageLoaded(){
        every { repo.getApiMovies(any(), any()) } returns
                Flowable.just(Resource.success(TestUtil.moviesList1_5)) andThen
                Flowable.just(Resource.success(TestUtil.moviesList1_10))

        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        recyclerView.perform(swipeUp())

        verify(exactly = 2) { repo.getApiMovies(TOP_RATED, any()) }
    }

    @Test
    fun getMovies_getError_destroyActivity_isStillTheSameDataDisplayedAfterRestoration(){
        var actualItemsCountInRecyclerView : Int? = null
        every { repo.getApiMovies(any(), any()) } returns
                Flowable.just(Resource.success(TestUtil.moviesList1_2)) andThen
                Flowable.just(Resource.error("some error", null))

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        recyclerView.perform(swipeUp())
        scenario.recreate()
        scenario.onFragment {
            actualItemsCountInRecyclerView = it.recyclerViewApi.adapter?.itemCount
        }

        assertEquals(2, actualItemsCountInRecyclerView)
        assertEquals("some error", ShadowToast.getTextOfLatestToast())
        verify(exactly = 2) { repo.getApiMovies(TOP_RATED, any()) }
    }

    @Test
    fun navigationToDetailsFragment() {
        val expectedArgument = ApiFragmentDirections.actionToDetails(TestUtil.moviesList1_2[0]).arguments["movie"] as Movie
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.main_nav_graph)
        navController.setCurrentDestination(R.id.destinationApi)

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        assertEquals(R.id.destinationDetails, navController.currentDestination?.id)
        assertEquals(expectedArgument, navController.backStack.last().arguments?.get("movie"))
    }

}

