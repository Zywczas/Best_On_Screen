package com.zywczas.bestonscreen.views

import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.viewmodels.DBVM
import com.zywczas.bestonscreen.viewmodels.ViewModelsProviderFactory
import io.mockk.MockKSettings.relaxed
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class DBFragmentTest {
//todo espresso idling resource nie jest tu potrzebne bo robolectric dziala w main thread zawsze
    private val picasso = mockk<Picasso>(relaxed = true)
    private val networkCheck = mockk<NetworkCheck>()
    private val viewModel = mockk<DBVM>()
    private val moviesLD = MutableLiveData<List<Movie>>()
    private val viewModelFactory = mockk<ViewModelsProviderFactory>()
    private val fragmentsFactory = mockk<MoviesFragmentsFactory>()

    @Before
    fun init(){
        every { networkCheck.isConnected } returns true
        moviesLD.value = TestUtil.movies
        every { viewModel.moviesLD } returns moviesLD
        every { viewModelFactory.create(DBVM::class.java) } returns viewModel
        every { fragmentsFactory.instantiate(any(), any()) } returns DBFragment(viewModelFactory, picasso, networkCheck)
    }

    @Test
    fun isFragmentInView(){
        val scenario = launchFragmentInContainer<DBFragment>(factory = fragmentsFactory)

        onView(withId(R.id.recyclerViewDB)).check(matches(isDisplayed()))
    }

    //todo nav cotroller always set current destination, it needs to know where it is


}

//    @Rule
//    @JvmField
//    val activityRule = ActivityScenarioRule(DBActivity::class.java)
//
//    @Test
//    fun isActivityInView() {
//        onView(withId(R.id.drawer_layout))
//            .check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun visibility_toolbar_RecyclerView_progressbar_textView() {
//        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
//        onView(withId(R.id.moviesRecyclerView)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
//        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
//        onView(withId(R.id.emptyListTextView)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun isContentDisplayed() {
//        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Movies: My List"))))
//
//    }