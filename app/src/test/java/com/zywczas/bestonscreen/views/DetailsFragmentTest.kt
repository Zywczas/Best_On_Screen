package com.zywczas.bestonscreen.views

import android.util.Log
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.viewpager.widget.ViewPager
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.util.LiveDataTestUtil
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.NestedScrollViewExtension
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.utilities.ToastMatcher
import com.zywczas.bestonscreen.viewmodels.DetailsVM
import com.zywczas.bestonscreen.viewmodels.ViewModelsProviderFactory
import io.mockk.*
import kotlinx.android.synthetic.main.fragment_details.*
import org.hamcrest.Matchers
import org.hamcrest.core.IsNot.not
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Captor
import org.robolectric.annotation.LooperMode

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
    @Captor

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
            isMovieInDbLD.value = Event(true)
        }

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
            messageLD.value = Event("movie added to database")
        }

        val scenario = launchFragmentInContainer<DetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )


//todo dodac toast

        onView(withId(R.id.addToMyListBtnDetails)).perform(NestedScrollViewExtension()).perform(click())
        onView(withText("movie added to database")).inRoot(ToastMatcher()).check(matches(isDisplayed()))
//        onView(withId(R.id.addToMyListBtnDetails)).check(matches(isChecked()))
//        verify { viewModel.addOrDeleteMovie(any(), false) }


//        lateinit var decorView : View
//        scenario.onFragment { decorView = it.requireActivity().window.decorView }
//        onView(withText(R.string.toast)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()))

    }

    @Test
    fun deletingMovieFromDatabase_isAddToMyListBtnUnchecked(){
        isMovieInDbLD.value = Event(true)
        every { viewModel.addOrDeleteMovie(any(), true) } answers {
            messageLD.value = Event("movie deleted from database")
            isMovieInDbLD.value = Event(false)
        }

        val scenario = launchFragmentInContainer<DetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )
//todo dodac toast
        onView(withId(R.id.addToMyListBtnDetails)).perform(NestedScrollViewExtension()).perform(click())
        onView(withId(R.id.addToMyListBtnDetails)).check(matches(not(isChecked())))
    }

    @Test
    fun deletingMovieFromDatabase_isToastShown(){
        isMovieInDbLD.value = Event(true)
        every { viewModel.addOrDeleteMovie(any(), true) } answers {
            messageLD.value = Event("movie deleted from database")
            isMovieInDbLD.value = Event(false)
        }

        val scenario = launchFragmentInContainer<DetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )
//todo dodac toast
        onView(withId(R.id.addToMyListBtnDetails)).perform(NestedScrollViewExtension()).perform(click())
        onView(withId(R.id.addToMyListBtnDetails)).check(matches(not(isChecked())))
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

}
