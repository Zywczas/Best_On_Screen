package com.zywczas.bestonscreen.views

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.NestedScrollViewExtension
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.viewmodels.DetailsVM
import com.zywczas.bestonscreen.viewmodels.ViewModelsProviderFactory
import io.mockk.*
import org.hamcrest.core.IsNot.not
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Captor
import org.robolectric.annotation.LooperMode
import org.robolectric.shadows.ShadowToast

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class DetailsFragmentTest {

    private val picasso = mockk<Picasso>(relaxed = true)
    private val networkCheck = mockk<NetworkCheck>()
    private val viewModel = mockk<DetailsVM>()
    private val viewModelFactory = mockk<ViewModelsProviderFactory>()
    private val fragmentsFactory = mockk<MoviesFragmentsFactory>()
    private val directions = DBFragmentDirections.actionToDetails(TestUtil.movie1)
    private val isMovieInDbLD = MutableLiveData<Event<Boolean>>()
    private val messageLD = MutableLiveData<Event<String>>()

    @Before
    fun init(){
        every { networkCheck.isConnected } returns true
        every { viewModelFactory.create(DetailsVM::class.java) } returns viewModel
        every { fragmentsFactory.instantiate(any(), any()) } returns DetailsFragment(viewModelFactory, picasso, networkCheck)
        every { viewModel.isMovieInDbLD } returns isMovieInDbLD
        every { viewModel.messageLD } returns messageLD
        every { viewModel.checkIfIsInDb(any()) } just Runs
    }

    @Test
    fun isFragmentInView(){
        val scenario = launchFragmentInContainer<DetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )

        onView(withId(R.id.posterImageViewDetails)).check(matches(isDisplayed()))
        onView(withId(R.id.titleTextViewDetails)).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
        onView(withId(R.id.rateTextViewDetails)).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
        onView(withId(R.id.releaseDateTextViewDetails)).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
        onView(withId(R.id.addToMyListBtnDetails)).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
        onView(withId(R.id.genresTextViewDetails)).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
        onView(withId(R.id.overviewTextViewDetails)).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
    }

    @Test
    fun isDataDisplayed(){
        val scenario = launchFragmentInContainer<DetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )

//todo dac picasso check      onView(withId(R.id.posterImageViewDetails)).check(matches(isDisplayed()))
        onView(withId(R.id.titleTextViewDetails)).check(matches(withText("Hard Kill")))
        onView(withId(R.id.rateTextViewDetails)).check(matches(withText("Rate: 5.5")))
        onView(withId(R.id.releaseDateTextViewDetails)).check(matches(withText("Release date: 2020-08-25")))
        onView(withId(R.id.genresTextViewDetails)).check(matches(withText("Genres: Action, Thriller")))
        onView(withId(R.id.overviewTextViewDetails))
            .check(matches(withText("The work of billionaire tech CEO Donovan Chalmers " +
                "is so valuable that he hires mercenaries to protect it, and a terrorist group " +
                    "kidnaps his daughter just to get it.")))
    }

    @Test
    fun movieInDb_isAddToMyListBtnStateChecked(){
        isMovieInDbLD.value = Event(true)

        val scenario = launchFragmentInContainer<DetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )

        onView(withId(R.id.addToMyListBtnDetails)).check(matches(isChecked()))
    }

    @Test
    fun movieNotInDb_isAddToMyListBtnStateUnchecked(){
        isMovieInDbLD.value = Event(false)

        val scenario = launchFragmentInContainer<DetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )

        onView(withId(R.id.addToMyListBtnDetails)).check(matches(not(isChecked())))
    }

    @Test
    fun addingMovieToDatabase_isAddToMyListBtnChecked(){
        isMovieInDbLD.value = Event(false)
        every { viewModel.addOrDeleteMovie(any(), false) } answers {
            isMovieInDbLD.value = Event(true) }

        val scenario = launchFragmentInContainer<DetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )

        onView(withId(R.id.addToMyListBtnDetails)).perform(NestedScrollViewExtension())
            .perform(click()).check(matches(isChecked()))
    }

    @Test
    fun addingMovieToDatabase_isToastShown(){
        isMovieInDbLD.value = Event(false)
        every { viewModel.addOrDeleteMovie(any(), false) } answers {
            messageLD.value = Event("movie added to database") }

        val scenario = launchFragmentInContainer<DetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )
        onView(withId(R.id.addToMyListBtnDetails)).perform(NestedScrollViewExtension()).perform(click())

        assertEquals("movie added to database", ShadowToast.getTextOfLatestToast())
    }

    @Test
    fun deletingMovieFromDatabase_isAddToMyListBtnUnchecked(){
        isMovieInDbLD.value = Event(true)
        every { viewModel.addOrDeleteMovie(any(), true) } answers {
            isMovieInDbLD.value = Event(false)
        }

        val scenario = launchFragmentInContainer<DetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )

        onView(withId(R.id.addToMyListBtnDetails)).perform(NestedScrollViewExtension()).perform(click())
            .check(matches(not(isChecked())))
    }

    @Test
    fun deletingMovieFromDatabase_isToastShown(){
        isMovieInDbLD.value = Event(true)
        every { viewModel.addOrDeleteMovie(any(), true) } answers {
            messageLD.value = Event("movie deleted")
        }

        val scenario = launchFragmentInContainer<DetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )
        onView(withId(R.id.addToMyListBtnDetails)).perform(NestedScrollViewExtension()).perform(click())

        assertEquals("movie deleted", ShadowToast.getTextOfLatestToast())
    }

    @Test
    fun activityDestroyed_isInstanceStateSavedAndRestored() {
        isMovieInDbLD.value = Event(true)

        val scenario = launchFragmentInContainer<DetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )
        scenario.recreate()
//todo dac tu picasso
        onView(withId(R.id.posterImageViewDetails)).check(matches(isDisplayed()))
        onView(withText("Hard Kill")).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
        onView(withText("Rate: 5.5")).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
        onView(withText("Release date: 2020-08-25")).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
        onView(withText("Remove from my list")).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
        onView(withText("Genres: Action, Thriller")).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
        onView(withText("The work of billionaire tech CEO Donovan Chalmers " +
                "is so valuable that he hires mercenaries to protect it, and a terrorist group " +
                "kidnaps his daughter just to get it.")).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
    }

    @Test
    fun activityDestroyed_isToastNotShownAgainOnFragmentRecreated(){
        messageLD.value = Event("all good so far")

        val scenario = launchFragmentInContainer<DetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )
        scenario.recreate()

        assertEquals(1, ShadowToast.shownToastCount())
    }

    @Test
    fun noInternet_isToastShown(){
        every { networkCheck.isConnected } returns false

        val scenario = launchFragmentInContainer<DetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )

        assertEquals("Problem with internet. Check your connection and try again.", ShadowToast.getTextOfLatestToast())
    }

}
