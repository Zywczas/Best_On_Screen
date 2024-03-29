package com.zywczas.bestonscreen.views

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.model.repositories.MovieDetailsRepository
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.NestedScrollViewExtension
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.viewmodels.MovieDetailsViewModel
import com.zywczas.bestonscreen.viewmodels.ViewModelsProviderFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
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
class MovieDetailsFragmentTest {

    private val picasso = mockk<Picasso>(relaxed = true)
    private val networkCheck = mockk<NetworkCheck>()
    private val repo = mockk<MovieDetailsRepository>()
    private val viewModelFactory = mockk<ViewModelsProviderFactory>()
    private val fragmentsFactory = mockk<MoviesFragmentsFactory>()
    private val directions = LocalMoviesFragmentDirections.actionToDetails(TestUtil.movie1)

    @Before
    fun init() {
        every { networkCheck.isConnected } returns true
        every { viewModelFactory.create(MovieDetailsViewModel::class.java) } returns MovieDetailsViewModel(repo)
        every { fragmentsFactory.instantiate(any(), any()) } returns MovieDetailsFragment(viewModelFactory, picasso, networkCheck)
    }

    @Test
    fun isFragmentInView() {
        every { repo.checkIfMovieIsInDB(any()) } returns Flowable.just(Event(true))

        launchFragmentInContainer<MovieDetailsFragment>(
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
    fun isDataFromDirectionsDisplayed() {
        every { repo.checkIfMovieIsInDB(any()) } returns Flowable.just(Event(true))

        launchFragmentInContainer<MovieDetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )

        onView(withId(R.id.titleTextViewDetails)).check(matches(withText("Hard Kill")))
        onView(withId(R.id.rateTextViewDetails)).check(matches(withText("Rate: 5.5")))
        onView(withId(R.id.releaseDateTextViewDetails)).check(matches(withText("Release date: 2020-08-25")))
        onView(withId(R.id.genresTextViewDetails)).check(matches(withText("Genres: Action, Thriller")))
        onView(withId(R.id.overviewTextViewDetails)).check(
            matches(
                withText(
                    "The work of " +
                            "billionaire tech CEO Donovan Chalmers is so valuable that he hires mercenaries to " +
                            "protect it, and a terrorist group kidnaps his daughter just to get it."
                )
            )
        )
    }

    @Test
    fun movieInDb_isAddToMyListBtnStateChecked() {
        every { repo.checkIfMovieIsInDB(any()) } returns Flowable.just(Event(true))

        launchFragmentInContainer<MovieDetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )

        onView(withId(R.id.addToMyListBtnDetails)).check(matches(isChecked()))
    }

    @Test
    fun movieNotInDb_isAddToMyListBtnStateUnchecked() {
        every { repo.checkIfMovieIsInDB(any()) } returns Flowable.just(Event(false))

        launchFragmentInContainer<MovieDetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )

        onView(withId(R.id.addToMyListBtnDetails)).check(matches(not(isChecked())))
    }

    @Test
    fun addingMovieToDatabase_isAddToMyListBtnCheckedAndToastShown() {
        every { repo.checkIfMovieIsInDB(any()) } returns
                Flowable.just(Event(false)) andThen
                Flowable.just(Event(true))
        every { repo.addMovieToDB(any()) } returns Flowable.just(Event("movie added to database"))

        launchFragmentInContainer<MovieDetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )

        onView(withId(R.id.addToMyListBtnDetails)).perform(NestedScrollViewExtension())
            .perform(click()).check(matches(isChecked()))
        assertEquals("movie added to database", ShadowToast.getTextOfLatestToast())
        verifySequence {
            repo.checkIfMovieIsInDB(any())
            repo.addMovieToDB(any())
            repo.checkIfMovieIsInDB(any())
        }
    }

    @Test
    fun deletingMovieFromDatabase_isAddToMyListBtnUncheckedAndToastShown() {
        every { repo.checkIfMovieIsInDB(any()) } returns
                Flowable.just(Event(true)) andThen
                Flowable.just(Event(false))
        every { repo.deleteMovieFromDB(any()) } returns Flowable.just(Event("movie deleted"))

        launchFragmentInContainer<MovieDetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )

        onView(withId(R.id.addToMyListBtnDetails)).perform(NestedScrollViewExtension()).perform(click())
            .check(matches(not(isChecked())))
        assertEquals("movie deleted", ShadowToast.getTextOfLatestToast())
        verifySequence {
            repo.checkIfMovieIsInDB(any())
            repo.deleteMovieFromDB(any())
            repo.checkIfMovieIsInDB(any())
        }
    }

    @Test
    fun activityDestroyed_isInstanceStateSavedAndRestored() {
        every { repo.checkIfMovieIsInDB(any()) } returns Flowable.just(Event(true))

        val scenario = launchFragmentInContainer<MovieDetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )
        scenario.recreate()

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
        onView(
            withText(
                "The work of billionaire tech CEO Donovan Chalmers " +
                        "is so valuable that he hires mercenaries to protect it, and a terrorist group " +
                        "kidnaps his daughter just to get it."
            )
        ).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
        verify(exactly = 2) { repo.checkIfMovieIsInDB(any()) }
    }

    @Test
    fun deleteMovie_activityDestroyed_isToastNotShownAgainOnFragmentRecreated() {
        every { repo.checkIfMovieIsInDB(any()) } returns
                Flowable.just(Event(true)) andThen
                Flowable.just(Event(false))
        every { repo.deleteMovieFromDB(any()) } returns Flowable.just(Event("movie deleted"))

        val scenario = launchFragmentInContainer<MovieDetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )
        onView(withId(R.id.addToMyListBtnDetails)).perform(NestedScrollViewExtension()).perform(click())
        scenario.recreate()

        assertEquals(1, ShadowToast.shownToastCount())
    }

    @Test
    fun noInternet_isToastShown() {
        every { repo.checkIfMovieIsInDB(any()) } returns Flowable.just(Event(false))
        every { networkCheck.isConnected } returns false

        launchFragmentInContainer<MovieDetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )

        assertEquals("Problem with internet. Check your connection and try again.", ShadowToast.getTextOfLatestToast())
    }
}
