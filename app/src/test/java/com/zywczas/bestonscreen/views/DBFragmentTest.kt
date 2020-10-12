package com.zywczas.bestonscreen.views

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter.ViewHolder
import com.zywczas.bestonscreen.model.DBRepository
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.viewmodels.DBVM
import com.zywczas.bestonscreen.viewmodels.ViewModelsProviderFactory
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Flowable
import org.hamcrest.core.IsNot.not
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode
import org.robolectric.shadows.ShadowToast

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class DBFragmentTest {

    private val picasso = mockk<Picasso>(relaxed = true)
    private val networkCheck = mockk<NetworkCheck>()
    private val repo = mockk<DBRepository>()
    private val viewModelFactory = mockk<ViewModelsProviderFactory>()
    private val fragmentsFactory = mockk<MoviesFragmentsFactory>()
    private val recyclerView = onView(withId(R.id.recyclerViewDB))

    @Before
    fun init() {
        every { networkCheck.isConnected } returns true
        every { repo.getMoviesFromDB() } returns Flowable.just(TestUtil.moviesList1_10)
        every { viewModelFactory.create(DBVM::class.java) } returns DBVM(repo)
        every { fragmentsFactory.instantiate(any(), any()) } returns
                DBFragment(viewModelFactory, picasso, networkCheck)
    }

    @Test
    fun isFragmentInView() {
        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<DBFragment>(factory = fragmentsFactory)

        recyclerView.check(matches(isDisplayed()))
        onView(withId(R.id.emptyListTextView)).check(matches(not(isDisplayed())))
    }

    @Test
    fun noMovies_isEmptyListMessageInView() {
        every { repo.getMoviesFromDB() } returns Flowable.just(emptyList())

        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<DBFragment>(factory = fragmentsFactory)

        onView(withId(R.id.emptyListTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun isDataDisplayedOnFragmentInit() {
        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<DBFragment>(factory = fragmentsFactory)

        recyclerView.perform(scrollToPosition<ViewHolder>(9))
            .check(matches(hasDescendant(withText("The Pianist"))))
            .check(matches(hasDescendant(withText("8.4"))))
            .check(matches(hasDescendant(withId(R.id.posterImageViewListItem))))
    }

    @Test
    fun navigationToDetailsFragment() {
        val expectedArgument = DBFragmentDirections.actionToDetails(TestUtil.moviesList1_10[7]).arguments["movie"] as Movie
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.main_nav_graph)
        navController.setCurrentDestination(R.id.destinationDb)

        val scenario = launchFragmentInContainer<DBFragment>(factory = fragmentsFactory)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(7, click()))

        assertEquals(R.id.destinationDetails, navController.currentDestination?.id)
        assertEquals(expectedArgument, navController.backStack.last().arguments?.get("movie"))
    }

    @Test
    fun activityDestroyed_isInstanceStateSavedAndRestored() {
        val scenario = launchFragmentInContainer<DBFragment>(factory = fragmentsFactory)
        recyclerView.perform(scrollToPosition<ViewHolder>(9))
        scenario.recreate()

        onView(withText("The Pianist")).check(matches(isDisplayed()))
    }

    @Test
    fun noInternet_isToastShown(){
        every { networkCheck.isConnected } returns false

        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<DBFragment>(factory = fragmentsFactory)

        assertEquals("Problem with internet. Check your connection and try again.", ShadowToast.getTextOfLatestToast())
    }


}
