package com.zywczas.bestonscreen.views

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.swipeDown
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
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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
    private val messageLD = MutableLiveData<Event<String>>()

    @Before
    fun init(){
        every { networkCheck.isConnected } returns true
        every { viewModelFactory.create(DetailsVM::class.java) } returns viewModel
        every { fragmentsFactory.instantiate(any(), any()) } returns DetailsFragment(viewModelFactory, picasso, networkCheck)
    }

    @Test
    fun isFragmentInView(){

//        messageLD.value = Event("Movie added to your list")
        //todo dodac answers { code }
        //todo sprobowac wniesc do Before
        every { viewModel.isMovieInDbLD } returns isMovieInDbLD
        every { viewModel.messageLD } returns messageLD
        every { viewModel.checkIfIsInDb(any()) } just runs

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
        onView(withId(R.id.addToListBtnDetails)).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
        onView(withId(R.id.genresTextViewDetails)).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
        onView(withId(R.id.overviewTextViewDetails)).perform(NestedScrollViewExtension())
            .check(matches(isDisplayed()))
    }

    @Test
    fun isDataDisplayed(){
        every { viewModel.isMovieInDbLD } returns isMovieInDbLD
        every { viewModel.messageLD } returns messageLD
        every { viewModel.checkIfIsInDb(any()) } just runs

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

    //czy dodawanie filmu do bazy dziala - czy klikanie guzika dziala i zostawia checked lub unchecked

    //czy wczytuje sie dobry stan guzika checked przy dwoch opcjach bycia w bazie

    //czy pokazuje sie wiadomosc dodania lub usuniecia


}
