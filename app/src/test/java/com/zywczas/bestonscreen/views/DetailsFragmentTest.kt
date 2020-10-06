package com.zywczas.bestonscreen.views

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.viewmodels.DetailsVM
import com.zywczas.bestonscreen.viewmodels.ViewModelsProviderFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class DetailsFragmentTest {

    private val directions = DBFragmentDirections.actionToDetails(TestUtil.movie1)
    private val picasso = mockk<Picasso>(relaxed = true)
    private val networkCheck = mockk<NetworkCheck>()
    //todo albo dac normalny mock albo jakis inny, ten teraz nie dziala
    private val viewModel = spyk<DetailsVM>()
    private val viewModelFactory = mockk<ViewModelsProviderFactory>()
    private val fragmentsFactory = mockk<MoviesFragmentsFactory>()

    @Before
    fun init(){
        every { networkCheck.isConnected } returns true
        every { viewModelFactory.create(DetailsVM::class.java) } returns viewModel
        every { fragmentsFactory.instantiate(any(), any()) } returns DetailsFragment(viewModelFactory, picasso, networkCheck)
    }

    @Test
    fun isFragmentInView(){
        val isMovieInDbLD = MutableLiveData<Event<Boolean>>()
        isMovieInDbLD.value = Event(true)
        val messageLD = MutableLiveData<Event<String>>()
        messageLD.value = Event("Movie added to your list")
        //todo dodac answers { code }
        every { viewModel.isMovieInDbLD } returns isMovieInDbLD
        every { viewModel.messageLD } returns messageLD

        val scenario = launchFragmentInContainer<DetailsFragment>(
            factory = fragmentsFactory,
            fragmentArgs = directions.arguments
        )


        onView(withId(R.id.posterImageViewDetails)).check(matches(isDisplayed()))
        onView(withId(R.id.titleTextViewDetails)).check(matches(isDisplayed()))
        onView(withId(R.id.rateTextViewDetails)).check(matches(isDisplayed()))
        onView(withId(R.id.releaseDateTextViewDetails)).check(matches(isDisplayed()))
        onView(withId(R.id.addToListBtnDetails)).check(matches(isDisplayed()))
        onView(withId(R.id.genresTextViewDetails)).check(matches(isDisplayed()))
        onView(withId(R.id.overviewTextViewDetails)).check(matches(isDisplayed()))
    }

    //czy laduje film

    //czy dodawanie filmu do bazy dziala - czy klikanie guzika dziala i zostawia checked lub unchecked

    //czy wczytuje sie dobry stan guzika checked przy dwoch opcjach bycia w bazie

    //czy pokazuje sie wiadomosc dodania lub usuniecia


}
